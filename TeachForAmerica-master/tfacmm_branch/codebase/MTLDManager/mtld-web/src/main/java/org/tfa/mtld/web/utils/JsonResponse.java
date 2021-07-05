package org.tfa.mtld.web.utils;

/**
 * 
 */

public class JsonResponse {
	private Object data;
	private String status;
	private String message;

	public static enum JsonResponseStatus {
		SUCCESS("success"), FAIL("fail"), NOT_FOUND("not_found");
		private final String status;

		private JsonResponseStatus(final String s) {
			status = s;
		}

		@Override
		public String toString() {
			return status;
		}
	}

	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(final Object data) {
		this.data = data;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(final String status) {
		this.status = status;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(final String message) {
		this.message = message;
	}

}
