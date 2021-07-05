package org.tfa.mtld.service.exception;

@SuppressWarnings("all")
public class TFAInvalidCohortException extends Exception {
	private static final long serialVersionUID = 1L;

	private String message = null;

	public TFAInvalidCohortException() {
		super();
	}

	public TFAInvalidCohortException(String message) {
		super(message);
		this.message = message;
	}

	public TFAInvalidCohortException(Throwable cause) {
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

