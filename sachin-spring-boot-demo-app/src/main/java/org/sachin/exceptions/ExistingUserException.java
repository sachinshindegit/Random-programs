package org.sachin.exceptions;

public class ExistingUserException extends Exception{
	public ExistingUserException(String message) {
        super(message);
    }
	public ExistingUserException() {
        super();
    }
}
