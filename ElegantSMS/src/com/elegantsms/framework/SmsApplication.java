package com.elegantsms.framework;

import com.elegantsms.annotations.SmsQuery;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

import com.elegantsms.util.TypeConverter;
import com.elegantsms.util.TypeConverterFactory;
import com.elegantsms.util.TypeConverterMap;

/**
 * Prepares SMS Module dispatching.
 */
public class SmsApplication implements SmsModule {

    // collection of dispatchers for application
    private static Objenesis objenesis = new ObjenesisStd();
    private List<DispatchMethod> dispatchers = new ArrayList<>();
    private Map<Class<? extends SmsModule>, SmsModule> moduleMap = new HashMap<>();
    private TypeConverterMap converterMap = TypeConverterFactory.createDefaultConverterMap();

    // Empty constructor
    protected SmsApplication() {
    }

    /**
     * Scans a package for modules and loads them dynamically into
     *
     * @param packageName
     * @return
     */
    public static SmsApplication loadPackage(String packageName) {
        SmsApplication app = new SmsApplication();
        app.addModulesFromPackage(packageName);
        return app;
    }

    /**
     * Creates an SMS Application from a given list of module classes.
     *
     * @param modules
     * @return
     */
    @SafeVarargs
    public static SmsApplication loadModules(Class<? extends SmsModule>... modules) {
        SmsApplication app = new SmsApplication();
        Arrays.stream(modules).forEach(app::addModule);
        return app;
    }

    public <T> void registerTypeConverter(Class<T> type, TypeConverter<T> typeConverter) {
        converterMap.put(type, typeConverter);
    }

    public <T> void unregisterTypeConverter(Class<T> type) {
        converterMap.remove(type);
    }

    public <T> TypeConverter<T> getTypeConverter(Class<T> type) {
        return (TypeConverter<T>) converterMap.get(type);
    }

    /**
     * Adds a module instances to this application. In contrast to adding a module by class, this
     * allows the binding of module objects from another SMS application.
     *
     * @param module the SmsModule to add, that has @SmsQuery methods
     */
    public void addModule(SmsModule module) {
        // replace module if it exists
        removeModule(module.getClass());
        moduleMap.put(module.getClass(), module);
        for (Method method : module.getClass().getDeclaredMethods()) {
            method.setAccessible(true);
            if (method.isAnnotationPresent(SmsQuery.class))
                dispatchers.add(new DispatchMethod(module, method, converterMap));
        }
        // inefficient because resorting, but sufficient for finitely small number of modules
        Collections.sort(dispatchers);
    }

    /**
     * Adds a module classes to this application. This creates a new module through Reflection
     * based on the module class specified. If the module class has no accessible empty
     * constructor, this method creates an empty object using Objenesis library.
     *
     * @param module the class to add, that has @SmsQuery methods
     */
    public <T extends SmsModule> void addModule(Class<T> module) {
        T instance;
        try {
            // try instantiating first via reflection
            Constructor<T> emptyConstructor = module.getDeclaredConstructor();
            emptyConstructor.setAccessible(true); // for private constructors
            instance = emptyConstructor.newInstance();
        } catch (Exception e) {
            // instantiate a default object using objenesis
            instance = objenesis.newInstance(module);
        }
        addModule(instance);
    }

    /**
     * Removes a specific module from this application based on its class.
     *
     * @param module
     */
    public void removeModule(Class<? extends SmsModule> module) {
        if (moduleMap.containsKey(module)) {
            moduleMap.remove(module);
            dispatchers.removeIf(method -> method.getModuleClass().equals(module));
        }
    }

    /**
     * Finds the module object bound to the module class used by this application.
     * Returns null if class not found.
     */
    public <T extends SmsModule> T findModuleByClass(Class<T> cls) {
        return (T) moduleMap.get(cls);
    }

    /**
     * Load modules from package
     *
     * @param packageName
     */
    public void addModulesFromPackage(String packageName) {
        new FastClasspathScanner(packageName)
            .matchClassesImplementing(SmsModule.class, this::addModule)
            .scan();
    }

    /**
     * Clears list of modules.
     */
    public void clear() {
        // remove all modules and dispatchers
        dispatchers.clear();
        moduleMap.clear();
    }

    /**
     * Gets a reply from the given message. Navigates through the format and runs the first method
     * that matches the given message. Formats with the higher priority will be processed first.
     *
     * @param message the message to send
     * @return the reply as a String
     * @throws SmsPatternMismatchException if there is no format in the app modules matches the message
     */
    public String getReply(String message) throws SmsPatternMismatchException {
        return getReply(message, String.class);
    }

    /**
     * Gets a reply from the given message with a specified return type. Navigates through the
     * format and runs the first method that matches the given message. Formats with the higher
     * priority will be processed first.
     *
     * @param returnType the class of the return type
     * @param message    the message to send
     * @param <T>        template argument for the return type
     * @return the reply cast to T
     * @throws SmsPatternMismatchException if there is no format in the app modules matches the message
     */
    public <T> T getReply(String message, Class<T> returnType) throws SmsPatternMismatchException {
        for (DispatchMethod method : dispatchers)
            if (method.matches(message)) {
                Object value = method.dispatch(message);
                if (value == null)
                    return null;
                if (returnType.equals(String.class))
                    return (T) value.toString();
                else
                    return (T) value;
            }
        throw new SmsPatternMismatchException("no pattern found");
    }

    /**
     * Gets an array of all replies that match the format of a given message. Module methods with
     * the higher priority will be processed first.
     *
     * @param message
     * @return an array of replies
     */
    public Object[] getAllReplies(String message) {
        ArrayList<Object> replies = new ArrayList<>();
        for (DispatchMethod method : dispatchers) {
            if (method.matches(message)) {
                try {
                    replies.add(method.dispatch(message));
                } catch (SmsPatternMismatchException ignore) {
                }
            }
        }
        return replies.toArray(new Object[0]);
    }

    /**
     * Gets a reply from the given message. Navigates through the format and runs the first method
     * that matches the given message. Module methods with the higher priority will be processed
     * first.
     *
     * @param message the message to send
     * @return a reply or null if no pattern matches
     */
    public String getReplyNoThrow(String message) {
        try {
            return getReply(message);
        } catch (SmsPatternMismatchException e) {
            return null;
        }
    }

    /**
     * Gets a reply from the given message. Navigates through the format and runs the first method
     * that matches the given message. Module methods with the higher priority will be processed
     * first.
     *
     * @param returnType the class of the return type
     * @param message    the message to send
     * @param <T>        template argument for the return type
     * @return a reply or null if no pattern matches
     */
    public <T> T getReplyNoThrow(String message, Class<T> returnType) {
        try {
            return getReply(message, returnType);
        } catch (SmsPatternMismatchException e) {
            return null;
        }
    }


}
