package application;

/* Class: IdException 
* Description: This custom exception class allows an error message to be specified when the exception is created.
* Author: Md Abir Ishtiaque - s3677701
*/

public class IdException extends Exception {


	private static final long serialVersionUID = -8996432077521933914L;

	public IdException(String msg)
	{
		super(msg);
	}
	
}
