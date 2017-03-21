package me.ricotiongson.elegantsms.framework;

/**
 * Exception class for dispatch errors.
 */
public class SmsPatternMismatchException extends Exception {

    public SmsPatternMismatchException() {
        super();
    }

    public SmsPatternMismatchException(String message) {
        super(message);
    }

}
