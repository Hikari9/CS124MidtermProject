package me.ricotiongson.elegantsms.framework;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.lang.reflect.Method;
import java.util.TreeSet;

import me.ricotiongson.elegantsms.annotations.SMSQuery;

public class SMSApplication {


    private static Objenesis objenesis = new ObjenesisStd();
    private TreeSet<DispatchMethod> dispatchers = new TreeSet<>();

    private SMSApplication() {
    }

    public void addService(Class<? extends SMSService> cls) {
        Object instance = objenesis.newInstance(cls);
        for (Method method : cls.getDeclaredMethods()) {
            if (method.isAnnotationPresent(SMSQuery.class)) {
                new DispatchMethod(instance, method);
            }
        }
    }

    public static SMSApplication loadServices(Class<? extends SMSService>... dispatcherClasses) {
        SMSApplication app = new SMSApplication();
        for (Class<? extends SMSService> cls : dispatcherClasses) {
            app.addService(cls);
        }
        return app;
    }

    /*
    public static SMSApplication loadServicesFromPackage(String packageName) {
        // TODO: add classpath loader
    }
    */

    public String dispatch(String message) {
        // run through the services
        return null;
    }

    public String[] dispatchAll(String message) {
        // collect all replies
        return null;
    }


}
