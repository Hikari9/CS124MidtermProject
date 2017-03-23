package me.ricotiongson.elegantsms.framework;

import java.lang.annotation.AnnotationFormatError;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.ricotiongson.elegantsms.annotations.ArrayDelim;
import me.ricotiongson.elegantsms.annotations.CaseSensitive;
import me.ricotiongson.elegantsms.annotations.DispatchPriority;
import me.ricotiongson.elegantsms.annotations.RegexDebug;
import me.ricotiongson.elegantsms.annotations.SmsQuery;
import me.ricotiongson.elegantsms.util.SmsPatternMismatchException;
import me.ricotiongson.elegantsms.util.SmsPatternToRegexConverter;
import me.ricotiongson.elegantsms.util.TypeConverter;

/**
 * Internal holder class for dispatching methods (package-private)
 */
class Dispatcher implements Comparable<Dispatcher> {

    // holder class for pre-compiled patterns

    // module props
    private SmsModule module;
    private Method method;
    private Class<? extends SmsModule> moduleClass;
    // priority props
    private int classPriority = Priority.DEFAULT;
    private int methodPriority = Priority.DEFAULT;
    // identifier for each token
    private Pattern pattern;

    /**
     * Creates a Dispatcher object based on module and method
     *
     * @param module
     * @param method
     */
    public Dispatcher(SmsModule module, Method method) {

        // setup model
        this.module = module;
        this.method = method;
        this.moduleClass = module.getClass();

        SmsQuery smsQuery = method.getDeclaredAnnotation(SmsQuery.class);
        if (smsQuery == null) {
            throwAnnotationError(method, "@SmsQuery is required");
        }

        // build regex string
        String regexPattern = null;
        try {
            regexPattern = SmsPatternToRegexConverter.patternToRegex(smsQuery.value());
        } catch (AnnotationFormatError e) {
            throwAnnotationError(method, e.getMessage());
        }

        // obtain pattern
        this.pattern = method.isAnnotationPresent(CaseSensitive.class)
            ? Pattern.compile(regexPattern)
            : Pattern.compile(regexPattern, Pattern.CASE_INSENSITIVE);

        // assign priority
        DispatchPriority classDispatchPriority
            = moduleClass.getDeclaredAnnotation(DispatchPriority.class);
        if (classDispatchPriority != null)
            classPriority = classDispatchPriority.value();
        DispatchPriority methodDispatchPriority
            = method.getDeclaredAnnotation(DispatchPriority.class);
        if (methodDispatchPriority != null)
            methodPriority = methodDispatchPriority.value();

        // debug if annotated
        RegexDebug classDebug = moduleClass.getDeclaredAnnotation(RegexDebug.class);
        RegexDebug methodDebug = method.getDeclaredAnnotation(RegexDebug.class);
        if (classDebug != null || methodDebug != null) {
            System.out.println("@RegexDebug:"
                + "\n\tMethod:  " + moduleClass.getCanonicalName() + "#" + method.getName()
                + "\n\tPattern: " + pattern);
        }
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
     * Getter to the module class.
     *
     * @return
     */
    public Class<? extends SmsModule> getModuleClass() {
        return moduleClass;
    }

    /**
     * Checks if this getReply method matches the pattern
     *
     * @param message
     * @return true if message matches the pattern contained by this dispatcher method
     */
    public boolean matches(String message) {
        // check if message matches method pattern
        return pattern.matcher(message).matches();
    }

    /**
     * Dispatches a message that matches a pattern in one of the modules of this application.
     * If there exists more than one pattern that matches the message, the method with the higher
     * priority (class first then method) will be dispatched.
     *
     * @param message the message to be processed by this dispatcher
     * @return the reply of the dispatcher
     * @throws SmsPatternMismatchException when getReply method for message cannot be found
     */
    public Object dispatch(String message) throws SmsPatternMismatchException {

        // run through the pattern, otherwise throw an error if Pattern does not match
        Matcher matcher = pattern.matcher(message);
        if (!matcher.matches())
            throw new SmsPatternMismatchException("message does not match Pattern");

        // dynamically construct arguments
        Parameter[] params = method.getParameters();
        Object[] args = new Object[params.length];
        Arrays.fill(args, null);

        // iterate order
        int size = Math.min(args.length, matcher.groupCount());
        for (int i = 0; i < size; ++i) {
            if (!params[i].getType().isArray()) {
                try {
                    args[i] = TypeConverter.convertParameter(matcher.group(i + 1).trim(), params[i]);
                } catch (Throwable e) {
                    throw new SmsPatternMismatchException("cannot convert method parameter", e);
                }
            } else {
                ArrayDelim delim = params[i].getDeclaredAnnotation(ArrayDelim.class);
                String delimRegex = delim == null ? "\\s+" : delim.value();
                String text = matcher.group(i + 1).trim();
                if (text.length() == 0)
                    args[i] = new String[0];
                else {
                    try {
                        args[i] = TypeConverter.convertParameter(text.split(delimRegex), params[i]);
                    } catch (Throwable e) {
                        throw new SmsPatternMismatchException("cannot convert method parameter", e);
                    }
                }
            }
        }

        // invoke method
        try {
            return method.invoke(module, args);
        } catch (Throwable e) {
            throw new SmsPatternMismatchException(e.getMessage(), e);
        }

    }

    /**
     * Sort getReply method by class priority then method priority.
     *
     * @param other the other Dispatcher to compare with
     * @return an integer value of the comparison, negative if less, zero if equal, positive otherwise
     */
    @Override
    public int compareTo(Dispatcher other) {
        int c = Integer.compare(other.classPriority, classPriority);
        if (c == 0) c = Integer.compare(other.methodPriority, methodPriority);
        if (c == 0) c = Integer.compare(hashCode(), other.hashCode());
        return c;
    }
}