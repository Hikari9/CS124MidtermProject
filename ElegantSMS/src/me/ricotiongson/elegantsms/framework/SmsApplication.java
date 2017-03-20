package me.ricotiongson.elegantsms.framework;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListSet;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

/**
 * Prepares SMS Module dispatching.
 */
public class SmsApplication {

    // collection of dispatchers for application
    private static Objenesis objenesis = new ObjenesisStd();
    private Set<DispatchMethod> dispatchers = new TreeSet<>();

    /**
     * Creates an SMS Application from specific modules.
     * @param modules
     */
    public SmsApplication(SmsModule... modules) {
        for (SmsModule module : modules)
            addModule(module);
    }

    /**
     * Adds a module to this application.
     * @param module the SmsModule to add, with @SmsQuery methods
     */
    public void addModule(SmsModule module) {
        for (Method method : module.getClass().getDeclaredMethods()) {
            method.setAccessible(true);
            dispatchers.add(new DispatchMethod(module, method));
        }
    }

    /**
     * Scans a package for modules and loads them dynamically into
     * @param packageName
     * @return
     */
    public static SmsApplication fromPackage(String packageName) {
        SmsApplication app = new SmsApplication();
        // perform a scan on the package, and process via callback
        new FastClasspathScanner(packageName)
            // .verbose()
            .matchClassesImplementing(SmsModule.class, module -> {
                SmsModule instance = objenesis.newInstance(module);
                app.addModule(instance);
            })
            .scan();
        return app;
    }

    public String dispatch(String message) throws SmsPatternMismatchError {
        for (DispatchMethod method : dispatchers) {
            if (method.matches(message)) {
                return method.dispatch(message);
            }
        }
        throw new SmsPatternMismatchError("no pattern found");
    }

    public String dispatchNoThrow(String message) {
        for (DispatchMethod method : dispatchers) {
            if (method.matches(message)) {
                return method.dispatch(message);
            }
        }
        return null;
    }

    public String[] dispatchAll(String message) {
        ArrayList<String> replies = new ArrayList<>();
        for (DispatchMethod method : dispatchers) {
            if (method.matches(message)) {
                replies.add(method.dispatch(message));
            }
        }
        return replies.toArray(new String[0]);
    }


}
