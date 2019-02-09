package application;

/* Class: HiringRecord 
* Description: The class represents a single hiring record for  
*  any type of item that can be hired. A HiringRecord is created at the time of the hire transaction.
* Author: Md Abir Ishtiaque - s3677701
*/

public class HiringRecord {
	private String id;
	private double rentalFee;
	private double lateFee;
	private DateTime borrowDate;
	private DateTime returnDate;

	public HiringRecord(String itemId, String memberId, DateTime borrowDate, double rentalFee) {
		this.id = itemId + "_" + memberId + "_" + borrowDate.getEightDigitDate();
		this.rentalFee = rentalFee;
		this.borrowDate = borrowDate;
	}

	protected void setReturnDate(DateTime returnDate, double lateFee) {
		this.returnDate = returnDate;
		if (lateFee >= 0) {
			this.lateFee = lateFee;
		}
	}

	protected DateTime getBorrowDate() {
		return borrowDate;
	}

	protected double getRentalFee() {
		return rentalFee;
	}

	/*
	 * ALGORITHM BEGIN Append id and borrowDate to String sbf. If returnDate is not
	 * null, then append all other details for HiringRecord. Return String sbf. END
	 */
	protected String getDetails() {
		StringBuffer sbf = new StringBuffer();
		sbf.append(String.format("   \t%-25s%s\n", "Hire Id:", id));
		sbf.append(String.format("   \t%-25s%s\n", "Borrow Date:", borrowDate.getFormattedDate()));

		if (returnDate != null) {

			sbf.append(String.format("   \t%-25s%s\n", "Return Date:", returnDate.getFormattedDate()));
			sbf.append(String.format("   \t%-25s%s\n", "Fee:", "$" + rentalFee));
			sbf.append(String.format("   \t%-25s%s\n", "Late Fee:", "$" + lateFee));
			sbf.append(String.format("   \t%-25s%s\n", "Total Fees:", "$" + (lateFee + rentalFee)));
			sbf.append("-------------------------------------------------------------\n");
		}
		return sbf.toString();
	} // End of getDetails

	public String toString() {
		if (returnDate == null) {
			return id + ":" + borrowDate.getEightDigitDate() + ":none:none:none";
		}
		return id + ":" + borrowDate.getEightDigitDate() + ":" + returnDate.getEightDigitDate() + ":" + rentalFee + ":"
				+ lateFee;
	}

}
