package me.ricotiongson.elegantsms.framework;

import java.lang.reflect.Method;
import java.util.TreeSet;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import me.ricotiongson.elegantsms.dispatch.DispatchMethod;

/**
 * Prepares SMS Module dispatching.
 */
public class SmsApplication {

    private TreeSet<DispatchMethod> dispatchers = new TreeSet<>();

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
            dispatchers.add(DispatchMethod.create(module, method));
        }
    }

    /**
     * Scans a package for modules and loads them dynamically into
     * @param packageName
     * @return
     */
    public static SmsApplication loadModulesInPackage(String packageName) {
        SmsApplication app = new SmsApplication();
        // perform a scan on the package, and process via callback
        new FastClasspathScanner(packageName)
            .matchSubclassesOf(SmsModule.class, module -> {
                SmsModule instance = null;
                // try instantiating the module with its default constructor
                try {
                    instance = module.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (instance != null)
                    app.addModule(instance);
            })
            .scan();
        return app;
    }

    public String dispatch(String message) {
        // run through the services
        return null;
    }

    public String[] dispatchAll(String message) {
        // collect all replies
        return null;
    }


}
