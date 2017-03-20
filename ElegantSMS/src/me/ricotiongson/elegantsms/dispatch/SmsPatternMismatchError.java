package me.ricotiongson.elegantsms.dispatch;

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
