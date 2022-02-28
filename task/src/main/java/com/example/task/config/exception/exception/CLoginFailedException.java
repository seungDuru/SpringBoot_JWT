package com.example.task.config.exception.exception;

public class CLoginFailedException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5206925868591242091L;

	public CLoginFailedException() {
        super();
    }

    public CLoginFailedException(String message) {
        super(message);
    }

    public CLoginFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
