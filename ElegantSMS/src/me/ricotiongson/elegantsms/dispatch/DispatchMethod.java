package me.ricotiongson.elegantsms.dispatch;

import java.lang.annotation.Annotation;
import java.lang.annotation.AnnotationFormatError;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import me.ricotiongson.elegantsms.annotations.DispatchPriority;
import me.ricotiongson.elegantsms.annotations.SmsQuery;
import me.ricotiongson.elegantsms.framework.SmsModule;

/**
 * Holder class for dispatching methods
 */
public abstract class DispatchMethod implements Comparable<DispatchMethod> {

    // holder class for pre-compiled patterns
    private static final class Patterns {
        static final Pattern nameIterator = Pattern.compile("<\\s*([^<>\\s]*)\\s*>");
        static final Pattern bracketIterator = Pattern.compile("(<\\s*[^<>\\s]+\\s*>)");
        static final Pattern queryTokenizer = Pattern.compile("([^<>()]*|<\\s*[^<>\\s]+\\s*>|\\((?:[^()|]|\\\\(?:\\)|\\(|\\\\|\\|))+(\\|(?:[^()|]|\\\\(?:\\)|\\(|\\\\|\\|)))*\\))*");
    }

    /**
     * Dispatches a message that matches a pattern in one of the modules of this application.
     * If there exists more than one pattern that matches the message, the method with the higher
     * priority (class first then method) will be dispatched.
     *
     * @param message the message to be processed by this dispatcher
     * @throws SmsPatternMismatchError when dispatch method for message cannot be found
     * @return the reply of the dispatcher
     */
    public abstract String dispatch(String message) throws SmsPatternMismatchError;

    /**
     * Dispatches message and doesn't throw exception on pattern mismatch
     * @param message the message to be processed by this dispatcher
     * @return the reply of the dispatcher, null on pattern mismatch
     */
    public final String dispatchNoThrow(String message) {
        try {
            return dispatch(message);
        } catch (SmsPatternMismatchError e) {
            return null;
        }
    }

    // module props
    protected SmsModule module;
    protected Method method;
    protected Class<? extends SmsModule> moduleClass;

    // priority props
    protected int classPriority;
    protected int methodPriority;

    // identifier for each token
    protected Pattern pattern;
    protected String[] identifiers; // array of identifier names in the @SmsQuery pattern
    protected Parameter[] identifierParams; // array referencing to the actual parameters

    private static Map<Class<? extends Annotation>, Class<? extends DispatchMethod>> dispatchMap
        = new HashMap<>();
    private static Class<? extends Annotation> defaultAnnotation;

    /**
     * Register a new dispatch method with specified annotation.
     *
     * @param annotation
     * @param dispatchMethod
     */
    public static void registerDispatcher(Class<? extends Annotation> annotation,
                                          Class<? extends DispatchMethod> dispatchMethod) {
        dispatchMap.put(annotation, dispatchMethod);
    }

    /**
     * Unregisters a dispatch method with specified annotation.
     *
     * @param annotation
     */
    public static void unregisterDispatcher(Class<? extends Annotation> annotation) {
        dispatchMap.remove(annotation);
    }

    /**
     * Throws an annotation error if annotations have bad pattern.
     *
     * @param message
     */
    protected static void throwAnnotationError(Method method, String message) {
        throw new AnnotationFormatError("Method "
            + method.getDeclaringClass().getCanonicalName()
            + "::"
            + method.getName()
            + " "
            + message);
    }

    /**
     * Throws an annotation error if annotations have bad pattern.
     *
     * @param message
     */
    protected static void throwAnnotationError(Class<?> cls, String message) {
        throw new AnnotationFormatError("Class "
            + cls.getCanonicalName()
            + " "
            + message);
    }

    /**
     * Factory function to create a DispatchMethod.
     *
     * @param method
     * @return
     */
    public static DispatchMethod create(SmsModule module, Method method) throws AnnotationFormatError {

        // get a declared annotation that is registered
        Annotation found = null;
        for (Annotation annotation : method.getDeclaredAnnotations()) {
            if (dispatchMap.containsKey(annotation)) {
                if (found != null) {
                    // duplicate annotation found, throw error
                    throwAnnotationError(method,
                        String.format("cannot have @%s and @%s annotations at the same time",
                            found.annotationType().getName(),
                            annotation.annotationType().getName()));
                }
                found = annotation;
            }
        }

        if (found == null) {
            // annotation not found, try crawling classes
            for (Class<?> cls = module.getClass(); cls != null; cls = cls.getSuperclass()) {
                for (Annotation annotation : cls.getDeclaredAnnotations()) {
                    if (dispatchMap.containsKey(annotation)) {
                        if (found != null) {
                            // duplicate annotation found, throw error
                            throwAnnotationError(cls,
                                String.format("cannot have @%s and @%s annotations at the same time",
                                    found.annotationType().getName(),
                                    annotation.annotationType().getName()));
                        }
                        found = annotation;
                    }
                }
            }
        }

        if (found == null)
            // no annotation found, notify user that it is required
            throwAnnotationError(method, "required dispatch method " + dispatchMap.keySet());

        // create dispatch method via reflection
        Class<? extends DispatchMethod> dispatchMethod = dispatchMap.get(found);
        try {
            Constructor<?> inst = dispatchMethod.getConstructor(SmsModule.class, Method.class);
            inst.setAccessible(true);
            return (DispatchMethod) inst.newInstance(module, method);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throwAnnotationError(dispatchMethod, e.getMessage());
        }

        return null;

    }

    /**
     * Creates a DispatchMethod object based on module and method
     *
     * @param module
     * @param method
     */
    public DispatchMethod(SmsModule module, Method method) {

        // setup model
        this.module = module;
        this.method = method;
        this.moduleClass = module.getClass();

        SmsQuery smsQuery = method.getDeclaredAnnotation(SmsQuery.class);
        if (smsQuery == null)
            throwAnnotationError(method, "@SmsQuery is required");

        setupPriorities();
        setupPattern(method.getDeclaredAnnotation(SmsQuery.class).value());

    }

    protected void setupPattern(String queryFormat) {
        this.pattern =Pattern.compile(queryFormat);
        if(pattern == null)
            throwAnnotationError(method, "invalid @SmsQuery pattern");
    }

    protected void setupPriorities() {
        DispatchPriority classDispatchPriority
            = moduleClass.getDeclaredAnnotation(DispatchPriority.class);
        if (classDispatchPriority != null)
            classPriority = classDispatchPriority.value();
        DispatchPriority methodDispatchPriority
            = method.getDeclaredAnnotation(DispatchPriority.class);
        if (methodDispatchPriority != null)
            methodPriority = methodDispatchPriority.value();
    }

    /**
     * Checks if this dispatch method matches the pattern
     * @param message
     * @return true if message matches the pattern contained by this dispatcher method
     */
    public boolean matches(String message) {
        // check if message matches method pattern
        return pattern.matcher(message).matches();
    }

    /**
     * Sort dispatch method by class priority then method priority.
     * @param other the other DispatchMethod to compare with
     * @return an integer value of the comparison, negative if less, zero if equal, positive otherwise
     */
    @Override
    public int compareTo(DispatchMethod other) {
        int c = Integer.compare(other.classPriority, classPriority);
        if (c == 0) c = Integer.compare(other.methodPriority, methodPriority);
        if (c == 0) c = Integer.compare(hashCode(), other.hashCode());
        return c;
    }
}