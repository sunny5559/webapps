package org.tfa.mtld.web.exception;

public class TFAUserException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String message = null;

	public TFAUserException() {
		super();
	}

	public TFAUserException(String message) {
		super(message);
		this.message = message;
	}

	public TFAUserException(Throwable cause) {
		super(cause);
	}

	@Override
	public String toString() {
		return message;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
