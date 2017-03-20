package me.ricotiongson.elegantsms.framework;

/**
 * Exception class for dispatch errors.
 */
public class SmsPatternMismatchError extends Error {

    public SmsPatternMismatchError() {
        super();
    }

    public SmsPatternMismatchError(String message) {
        super(message);
    }

}
