package org.tfa.mtld.service.exception;

@SuppressWarnings("all")
public class TFAUserServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String message = null;

	public TFAUserServiceException() {
		super();
	}

	public TFAUserServiceException(String message) {
		super(message);
		this.message = message;
	}

	public TFAUserServiceException(Throwable cause) {
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
