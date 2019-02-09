package application;

/* Class: BorrowException 
* Description: This custom exception class allows an error message to be specified when the exception
* 			   related to borrowing an item is created.
* Author: Md Abir Ishtiaque - s3677701
*/

public class BorrowException extends Exception {

	
	private static final long serialVersionUID = 1L;
	
	public BorrowException(String msg)
	{
		super(msg);
	}
	
	

}
