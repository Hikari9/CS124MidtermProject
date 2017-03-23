package me.ricotiongson.elegantsms.util;

/**
 * Exception class for getReply errors.
 */
public class SmsPatternMismatchException extends Exception {

    public SmsPatternMismatchException() {
        super();
    }

    public SmsPatternMismatchException(String message) {
        super(message);
    }

    public SmsPatternMismatchException(String message, Throwable e) {
        super(message, e);
    }

    public SmsPatternMismatchException(Throwable e) {
        super(e);
    }
}
