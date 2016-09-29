package com.apprenda.rectangles;

/**
 * Thrown to indicate a data type overflow.
 * 
 * @author Cody McCain
 *
 */
public class OverflowException extends RuntimeException {

	private static final long serialVersionUID = 7683912284591836386L;

	public OverflowException() {
		super();
	}

	public OverflowException(String message) {
		super(message);
	}

	public OverflowException(Throwable cause) {
		super(cause);
	}

	public OverflowException(String message, Throwable cause) {
		super(message, cause);
	}

}
