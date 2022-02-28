package com.example.task.config.exception.exception;

public class CUserExistException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = -6923182120154818492L;

	public CUserExistException() {
        super();
    }

    public CUserExistException(String message) {
        super(message);
    }

    public CUserExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
