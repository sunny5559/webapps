/**
 * 
 */
package org.tfa.mtld.web.exception;

/**
 * @author arun.rathore
 *
 */
public class TFAException extends Exception{
	private static final long serialVersionUID = 1L;
	 
	private String errCode;
	private String errMsg;
	
	public TFAException() {
		super();
	}

	public TFAException(String message) {
		super(message);
		this.errMsg = message;
	}

	public TFAException(Throwable cause) {
		super(cause);
	}
	
	public String getErrCode() {
		return errCode;
	}
 
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
 
	public String getErrMsg() {
		return errMsg;
	}
 
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
 
	public TFAException(String errCode, String errMsg) {
		this.errCode = errCode;
		this.errMsg = errMsg;
	}
}
