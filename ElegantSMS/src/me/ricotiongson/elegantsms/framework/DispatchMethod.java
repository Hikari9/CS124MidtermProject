package me.ricotiongson.elegantsms.framework;

import java.lang.annotation.AnnotationFormatError;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.ricotiongson.elegantsms.annotations.ArrayDelim;
import me.ricotiongson.elegantsms.annotations.CaseSensitive;
import me.ricotiongson.elegantsms.annotations.DispatchPriority;
import me.ricotiongson.elegantsms.annotations.SmsQuery;

/**
 * Holder class for dispatching methods (package-private)
 */
class DispatchMethod implements Comparable<DispatchMethod> {

    // module props
    protected SmsModule module;
    protected Method method;
    protected Class<? extends SmsModule> moduleClass;

    // priority props
    protected int classPriority = Priority.DEFAULT;
    protected int methodPriority = Priority.DEFAULT;

    // identifier for each token
    protected Pattern pattern;
    protected String[] identifiers; // array of identifier names in the @SmsQuery pattern
    protected boolean[] identifierIsArray;
    protected Parameter[] identifierParams; // array referencing to the actual parameters


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


    // holder class for pre-compiled patterns
//    private static final String literal = "(?:[^<>]|\\\\.)+";
    private static final String literal = "[^<>]+";
    private static final String variable = "<([^<>]+)(\\.\\.\\.)?>".replace("(", "(?:");
    private static final Pattern queryTokenizer = Pattern.compile(String.format("(%s|%s)", literal, variable));

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

        DispatchPriority classDispatchPriority
            = moduleClass.getDeclaredAnnotation(DispatchPriority.class);
        if (classDispatchPriority != null)
            classPriority = classDispatchPriority.value();
        DispatchPriority methodDispatchPriority
            = method.getDeclaredAnnotation(DispatchPriority.class);
        if (methodDispatchPriority != null)
            methodPriority = methodDispatchPriority.value();

        String queryFormat = method.getDeclaredAnnotation(SmsQuery.class).value();

        Matcher tokenizer = queryTokenizer.matcher(queryFormat);
        ArrayList<String> tokenList = new ArrayList<>();
        int lastMatchPos = 0;
        while (tokenizer.find()) {
            tokenList.add(tokenizer.group(1));
            lastMatchPos = tokenizer.end();
        }

        if (lastMatchPos != queryFormat.length())
            throwAnnotationError(method, "invalid query format");

        // collect all tokens
        String[] tokens = new String[tokenList.size()];
        String[] identifiers = new String[tokens.length];
        boolean[] identifierIsArray = new boolean[tokens.length];
        int idents = 0, size = 0;
        for (int i = 0; i < tokens.length; ++i) {
            String token = tokenList.get(i);
            if (token == null || !token.matches("\\s+")) {
                tokens[size++] = token;
                if (token.charAt(0) == '<') {
                    if (token.endsWith("...>")) {
                        identifiers[idents] = token.substring(1, token.length() - 4);
                        identifierIsArray[idents++] = true;
                    } else {
                        identifiers[idents] = token.substring(1, token.length() - 1);
                        identifierIsArray[idents++] = false;
                    }
                }
            }
        }

        tokens = Arrays.copyOf(tokens, size);
        this.identifiers = Arrays.copyOf(identifiers, idents);
        this.identifierIsArray = Arrays.copyOf(identifierIsArray, idents);
        this.identifierParams = this.method.getParameters();

        // setup pattern here
        StringBuilder patternBuilder = new StringBuilder("^\\s*");
        for (int i = 0; i < tokens.length; ++i) {
            if (tokens[i].charAt(0) != '<') {
                // split token into spaces
                String[] split = tokens[i].trim().split("\\s+");
                // join
                StringBuilder join = new StringBuilder();
                for (String subliteral : split) {
                    if (join.length() > 0) join.append("\\s+");
                    join.append(Pattern.quote(subliteral));
                }
                patternBuilder.append(join);
                String last = split[split.length - 1];
                if (i + 1 < tokens.length
                    && Character.isLetterOrDigit(last.charAt(last.length() - 1))
                    && Character.isWhitespace(tokens[i].charAt(tokens[i].length() - 1))) {
                    // guaranteed to have a variable next, delimit it with whitespace
                    patternBuilder.append("\\s+");
                }
            } else {
                // get next non-space token
                if (i + 1 < tokens.length) {
                    String next = tokens[i + 1];
                    if (next.charAt(0) == '<')
                        patternBuilder.append("(\\S+)\\s+"); // everything that is not whitespace
                    else {
                        // get first non-space character
                        for (int j = 0; j < next.length(); ++j) {
                            char ch = next.charAt(j);
                            if (ch != ' ') {
                                if (Character.isLetterOrDigit(ch))
                                    patternBuilder.append("(\\S+)\\s+");
                                else {
                                    if (ch == '\\' || ch == ']')
                                        patternBuilder.append("([^\\").append(ch).append("]+)");
                                    else
                                        patternBuilder.append("([^").append(ch).append("]+)");
                                }
                            }
                        }
                    }
                }
                else if (identifierIsArray[identifierIsArray.length - 1]
                    || (Character.isWhitespace(queryFormat.charAt(queryFormat.length() - 1))
                    && queryFormat.charAt(queryFormat.length() - 1) != ' '))
                    patternBuilder.append("(.*)");
                else
                    patternBuilder.append("(\\S+)");
            }
            patternBuilder.append("\\s*");
        }
        patternBuilder.append("$");
        if (method.isAnnotationPresent(CaseSensitive.class))
            this.pattern = Pattern.compile(patternBuilder.toString());
        else
            this.pattern = Pattern.compile(patternBuilder.toString(), Pattern.CASE_INSENSITIVE);

//        System.out.println(pattern);
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
     * Dispatches a message that matches a pattern in one of the modules of this application.
     * If there exists more than one pattern that matches the message, the method with the higher
     * priority (class first then method) will be dispatched.
     *
     * @param message the message to be processed by this dispatcher
     * @throws SmsPatternMismatchError when dispatch method for message cannot be found
     * @return the reply of the dispatcher
     */
    public String dispatch(String message) throws SmsPatternMismatchError {

        // run through the pattern, otherwise throw an error if Pattern does not match
        Matcher matcher = pattern.matcher(message);
        if (!matcher.matches())
            throw new SmsPatternMismatchError("message does not match Pattern");

        // dynamically construct arguments
        Object[] args = new Object[identifierParams.length];
        Arrays.fill(args, null);

        // iterate order
        int size = Math.min(args.length, matcher.groupCount());
        for (int i = 0; i < size; ++i) {
            if (!identifierIsArray[i]) {
                args[i] = typeConversion(matcher.group(i + 1).trim(), identifierParams[i]);

            } else {
                ArrayDelim delim = identifierParams[i].getDeclaredAnnotation(ArrayDelim.class);
                String delimRegex = delim == null ? "\\s+" : delim.value();
                args[i] = typeConversion(matcher.group(i + 1).trim().split(delimRegex), identifierParams[i]);
            }
        }

        // invoke method
        try {
            return (String) method.invoke(module, args);
        } catch (Exception e) {
            throw new SmsPatternMismatchError(e.getMessage());
        }

    }

    private Object typeConversion(String arg, Parameter param) {
        return typeConversion(arg, param.getType());
    }

    private Object typeConversion(String arg, Class<?> type) {
        if (type.equals(String.class))      return arg;
        if (type.equals(int.class))         return (int) Integer.parseInt(arg);
        if (type.equals(Integer.class))     return Integer.parseInt(arg);
        if (type.equals(long.class))        return (long) Long.parseLong(arg);
        if (type.equals(Long.class))        return Long.parseLong(arg);
        if (type.equals(boolean.class))     return (boolean) Boolean.parseBoolean(arg);
        if (type.equals(Boolean.class))     return Boolean.parseBoolean(arg);
        if (type.equals(short.class))       return (short) Short.parseShort(arg);
        if (type.equals(Short.class))       return Short.parseShort(arg);
        if (type.equals(byte.class))        return (short) Byte.parseByte(arg);
        if (type.equals(Byte.class))        return Byte.parseByte(arg);
        if (type.equals(char.class))        return arg.charAt(0);
        if (type.equals(Character.class))   return (Character) arg.charAt(0);
        return arg;
    }

    private Object typeConversion(String[] arg, Parameter param) {
        Class<?> type = param.getType();
        if (type.equals(String[].class))
            return arg;
        if (type.equals(int[].class)) {
            int[] ans = new int[arg.length];
            for (int i = 0; i < arg.length; ++i)
                ans[i] = (int) Integer.parseInt(arg[i]);
            return ans;
        }
        if (type.equals(Integer[].class)) {
            Integer[] ans = new Integer[arg.length];
            for (int i = 0; i < arg.length; ++i)
                ans[i] = Integer.parseInt(arg[i]);
            return ans;
        }

        if (type.equals(long[].class)){
            long[] ans = new long[arg.length];
            for (int i = 0; i < arg.length; ++i)
                ans[i] = (long) Long.parseLong(arg[i]);
            return ans;
        }

        if (type.equals(Long[].class)) {
            Long[] ans = new Long[arg.length];
            for (int i = 0; i < arg.length; ++i)
                ans[i] = Long.parseLong(arg[i]);
            return ans;
        }

        if (type.equals(Boolean[].class)) {
            Boolean[] ans = new Boolean[arg.length];
            for (int i = 0; i < arg.length; ++i)
                ans[i] = Boolean.parseBoolean(arg[i]);
            return ans;
        }

        if (type.equals(boolean[].class))         {
            boolean[] ans = new boolean[arg.length];
            for (int i = 0; i < arg.length; ++i)
                ans[i] = (boolean) Boolean.parseBoolean(arg[i]);
            return ans;
        }

        if (type.equals(Short[].class)) {
            Short[] ans = new Short[arg.length];
            for (int i = 0; i < arg.length; ++i)
                ans[i] = Short.parseShort(arg[i]);
            return ans;
        }

        if (type.equals(short[].class))         {
            short[] ans = new short[arg.length];
            for (int i = 0; i < arg.length; ++i)
                ans[i] = (short) Short.parseShort(arg[i]);
            return ans;
        }

        if (type.equals(Byte[].class))         {
            short[] ans = new short[arg.length];
            for (int i = 0; i < arg.length; ++i)
                ans[i] = (byte) Byte.parseByte(arg[i]);
            return ans;
        }

        if (type.equals(byte[].class))         {
            short[] ans = new short[arg.length];
            for (int i = 0; i < arg.length; ++i)
                ans[i] = (byte) Byte.parseByte(arg[i]);
            return ans;
        }

        return arg;
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