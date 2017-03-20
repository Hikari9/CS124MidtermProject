package me.ricotiongson.elegantsms.framework;

import java.lang.annotation.Annotation;
import java.lang.annotation.AnnotationFormatError;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.ricotiongson.elegantsms.annotations.BindByName;
import me.ricotiongson.elegantsms.annotations.BindByOrder;
import me.ricotiongson.elegantsms.annotations.DispatchPriority;
import me.ricotiongson.elegantsms.annotations.SMSQuery;

class DispatchMethod implements Comparable<DispatchMethod> {

    private static class Patterns {
        private static Pattern nameIterator = Pattern.compile("<\\s*([^<>\\s]*)\\s*>");
        private static Pattern bracketIterator = Pattern.compile("(<\\s*[^<>\\s]+\\s*>)");
        private static Pattern queryTokenizer = Pattern.compile("([^<>()]*|<\\s*[^<>\\s]+\\s*>|\\((?:[^()|]|\\\\(?:\\)|\\(|\\\\|\\|))+(\\|(?:[^()|]|\\\\(?:\\)|\\(|\\\\|\\|)))*\\))*");
    }

    private Object instance;
    private Method method;
    private int classPriority;
    private int methodPriority;
    private Pattern pattern;
    private String[] identifiers;
    private static final List<Class<? extends Annotation>> bindables
        = Arrays.asList(BindByOrder.class, BindByName.class);
    private Class<? extends Annotation> bindType = bindables.get(0);

    DispatchMethod(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
        Class<?> cls = instance.getClass();
        String query = method.getDeclaredAnnotation(SMSQuery.class).value();

        // setup pattern matcher
        pattern = Pattern.compile(query);



        if (pattern == null)
            throw new AnnotationFormatError("Method "
                +method.getDeclaringClass().getCanonicalName()
                + "::"
                + method.getName()
                + " has invalid @SMSQuery pattern");
        // setup priorities
        DispatchPriority classDispatchPriority = cls.getDeclaredAnnotation(DispatchPriority.class);
        if (classDispatchPriority != null)
            classPriority = classDispatchPriority.value();
        DispatchPriority methodDispatchPriority = method.getDeclaredAnnotation(DispatchPriority.class);
        if (methodDispatchPriority != null)
            methodPriority = methodDispatchPriority.value();
        // bind order
        for (Class<? extends Annotation> bindClass : bindables)
            if (cls.isAnnotationPresent(bindClass))
                bindType = bindClass;
    }

    public boolean matches(String message) {
        // check if message matches method pattern
        return pattern.matcher(message).matches();
    }

    /*
    public String dispatch(String message) {
        // iterate through message
        Matcher matcher = pattern.matcher(message);
        for (int i = 1; i <= matcher.groupCount(); ++i) {
            if ()
        }
    }
    */

    @Override
    public int compareTo(DispatchMethod other) {
        int c = Integer.compare(classPriority, other.classPriority);
        if (c == 0)
            c = Integer.compare(methodPriority, other.methodPriority);
        return c;
    }
}