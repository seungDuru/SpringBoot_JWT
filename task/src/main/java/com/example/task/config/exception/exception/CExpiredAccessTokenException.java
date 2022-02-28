package com.example.task.config.exception.exception;

public class CExpiredAccessTokenException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6680135087366359293L;

	public CExpiredAccessTokenException() {
        super();
    }

    public CExpiredAccessTokenException(String message) {
        super(message);
    }

    public CExpiredAccessTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
