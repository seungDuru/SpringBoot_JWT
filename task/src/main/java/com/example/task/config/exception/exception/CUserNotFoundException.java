package com.example.task.config.exception.exception;

public class CUserNotFoundException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5289675773357472167L;

	public CUserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CUserNotFoundException(String message) {
        super(message);
    }

    public CUserNotFoundException() {
        super();
    }
}
