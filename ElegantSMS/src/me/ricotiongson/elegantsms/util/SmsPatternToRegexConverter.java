package me.ricotiongson.elegantsms.util;

import java.lang.annotation.AnnotationFormatError;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsPatternToRegexConverter {

    private static final String literal = "(?:\\\\<|[^<>])+";
    private static final String variable = "<([^<>]+)(\\.\\.\\.)?>".replace("(", "(?:");
    private static final Pattern queryTokenizer = Pattern.compile(String.format("(%s|%s)", literal, variable));

    /**
     * Splits an SMS query pattern into tokens of literals and identifiers.
     * @param pattern the SMS query pattern String to convert
     * @return an array of tokens
     * @throws AnnotationFormatError when the pattern does not match the format
     */
    public static String[] getTokens(String pattern) throws AnnotationFormatError {

        // collect all tokens
        Matcher tokenizer = queryTokenizer.matcher(pattern);
        ArrayList<String> tokenList = new ArrayList<>();

        int lastMatchPos = 0;
        while (tokenizer.find()) {
            String token = tokenizer.group(1);
            if (token == null || !token.matches("\\s+"))
                tokenList.add(token);
            lastMatchPos = tokenizer.end();
        }

        if (lastMatchPos != pattern.length())
            throw new AnnotationFormatError("invalid @SmsQuery format");

        // collect all tokens
        return tokenList.toArray(new String[0]);

    }

    /**
     * Convert an SMS query pattern to a Regex string such that identifiers will be assigned
     * to a specific group.
     * @param pattern the SMS query pattern String to convert
     * @return a regex String that represents the pattern
     * @throws AnnotationFormatError when the pattern does not match the format
     */
    public static String patternToRegex(String pattern) throws AnnotationFormatError {

        String[] tokens = getTokens(pattern);
        StringBuilder builder = new StringBuilder("^\\s*");

        boolean emitArray = false;
        for (int i = 0; i < tokens.length; ++i) {
            if (tokens[i].charAt(0) != '<') {
                // split token into spaces
                String[] split = tokens[i].trim().split("\\s+");
                int joinLength = builder.length();
                for (String subliteral : split) {
                    if (builder.length() > joinLength)
                        builder.append("\\s+");
                    builder.append(Pattern.quote(subliteral));
                }
                String last = split[split.length - 1];
                if (i + 1 < tokens.length
                    && Character.isLetterOrDigit(last.charAt(last.length() - 1))
                    && Character.isWhitespace(tokens[i].charAt(tokens[i].length() - 1))) {
                    if (tokens[i + 1].endsWith("...>")) {
                        // next token is an array
                        builder.append("(\\s*|\\s+");
                        emitArray = true;
                    } else
                        builder.append("\\s+");
                }
            } else {
                // get next non-space token
                if (i + 1 < tokens.length) {
                    String next = tokens[i + 1];
                    if (next.charAt(0) == '<') {
                        if (emitArray) {
                            builder.append("\\S*)");
                            emitArray = false;
                        } else
                            builder.append("(\\S+)");
                        if (tokens[i + 1].endsWith("...>")) {
                            builder.append("(\\s*|\\s+");
                            emitArray = true;
                        } else
                            builder.append("\\s+");
                    } else {
                        // get first non-space character
                        for (int j = 0; j < next.length(); ++j) {
                            char ch = next.charAt(j);
                            if (ch != ' ') {
                                if (Character.isLetterOrDigit(ch)) {
                                    if (emitArray) {
                                        builder.append("\\S*)\\s+");
                                        emitArray = false;
                                    } else
                                        builder.append("(\\S+)\\s+");
                                } else {
                                    if (!emitArray)
                                        builder.append("(");
                                    if (ch == '\\' || ch == ']')
                                        builder.append("[^\\").append(ch)
                                            .append("]");
                                    else
                                        builder.append("[^").append(ch).append("]");
                                    if (emitArray) {
                                        builder.append("*)");
                                        emitArray = false;
                                    } else
                                        builder.append("+)");
                                }
                            }
                        }
                    }
                } else {
                    if (!emitArray)
                        builder.append("(");
                    if (tokens[i].endsWith("...>")
                        || (Character.isWhitespace(pattern.charAt(pattern.length() - 1))
                        && pattern.charAt(pattern.length() - 1) != ' '))
                        builder.append(".*)");
                    else
                        builder.append("\\S+)");
                }
            }
            if (!emitArray)
                builder.append("\\s*");
        }
        builder.append("$");
        return builder.toString();
    }

}
