package com.example.task.config.exception.exception;

public class CSignupFailedException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8162075092924392979L;

	public CSignupFailedException() {
        super();
    }

    public CSignupFailedException(String message) {
        super(message);
    }

    public CSignupFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
