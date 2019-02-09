package application;

/* Class: Movie 
* Description: It is the movie class that is responsible for calculating any fees that apply when it is being borrowed or returned.
* Author: Md Abir Ishtiaque - s3677701
*/

public class Movie extends Item {
	private boolean isNewRelease;
	private final static double NEW_RELEASE_SURCHARGE = 2.0;
	private final static double BASE_FEE = 3.0;

	public Movie(String id, String title, String genre, String description, boolean isNewRelease) {

		super("M_" + id, title, genre, description, isNewRelease ? BASE_FEE + NEW_RELEASE_SURCHARGE : BASE_FEE);
		fee = isNewRelease ? BASE_FEE + NEW_RELEASE_SURCHARGE : BASE_FEE;
		this.isNewRelease = isNewRelease;
	}

	public boolean getIsNewRelease() {
		return isNewRelease;
	}

	/*Algorithm 
	 * BEGIN 
	 * Calculate difference between return and borrow date by using
	 * diffDays method in DateTime class. If diffDays is less than 0, then throw exception. 
	 * Calculate the lateFee if diffDays is greater than 2 or 7 for a new or weekly movie respectively.
	 *  LateFee is half of rentalFee for everyday a movie is late. 
	 *  Pass returnDate and lateFee on to the super class.
	 *   Return lateFee.
	 */
	public double returnItem(DateTime returnDate) throws BorrowException {
		double lateFee = 0;

		int diffDays = DateTime.diffDays(returnDate, getCurrentlyBorrowed().getBorrowDate());
		if (diffDays < 0) {
			throw new BorrowException("INVALID RETURN DATE!");
		}

		if (isNewRelease == true) {
			if (diffDays > 2) {
				lateFee = (getCurrentlyBorrowed().getRentalFee() / 2.0) * (diffDays - 2);
			}
			setCurrentlyBorrowed(null);
			getRecord().setReturnDate(returnDate, lateFee);

		} else {
			if (diffDays > 7) {
				lateFee = (getCurrentlyBorrowed().getRentalFee() / 2.0) * (diffDays - 7);
			}
			super.returnItem(returnDate, lateFee);
		}

		return lateFee;

	}

	public String getDetails() {

		String onL;
		int rentalPeriod = 0;
		if (getCurrentlyBorrowed() != null) {
			onL = "Yes";
		} else {
			onL = "No";
		}

		if (fee == 5) {
			rentalPeriod = 2;
		} else if (fee == BASE_FEE) {
			rentalPeriod = 7;
		}

		String str = super.getFirstDetails();

		str += String.format("\n%-25s %s\n%-25s %s\n%-25s %d days\n\n", "On Loan", onL, "Movie Type:",
				fee == BASE_FEE ? "Weekly" : "New Release", "Rental Period", rentalPeriod);

		str += super.getDetails();
		return str + "\n-----------------------------------------------------------------\n";

	}

	public String toString() {
		String loanStatus;

		loanStatus = getCurrentlyBorrowed() != null ? "Y" : "N";
		String str = super.toString() + ":" + (fee == BASE_FEE ? "WK" : "NR") + ":" + loanStatus;
		return str;
	}

}