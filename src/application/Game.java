package application;

/* Class: Game 
* Description: It is the game class that is responsible for calculating any fees that apply when it is being borrowed or returned.
* Author: Md Abir Ishtiaque - s3677701
*/

public class Game extends Item {
	private String platforms[];
	private boolean extended;
	private final int RENTAL_PERIOD = 21;

	public Game(String id, String title, String genre, String description, String platforms) {
		super("G_" + id, title, genre, description, 20.0);
		this.platforms = platforms.split(",");
		fee = 20.0;
	}

	protected void setExtended(boolean extended) {
		this.extended = extended;
	}

	protected boolean isExtended() {
		return extended;
	}

	/*
	 * ALGORITHM BEGIN Check if returnDate or currentlyBorrowed is null and if it
	 * is, then throw exception. Calculate difference in days using diffDays method
	 * from DateTime. Calculate how many weeks in those days. If difference is less
	 * than 0, then throw exception. Else calculate lateFee which is a flat $1.00
	 * for every day past the due date with an additional late fee of $5.00 for
	 * every 7 days past the due date. Take 50% off if the item had been enabled for
	 * extended hire. Return lateFee. END
	 */
	protected double returnItem(DateTime returnDate) throws BorrowException {

		double lateFee = 0;

		int diffDays = DateTime.diffDays(returnDate, getCurrentlyBorrowed().getBorrowDate());
		double weeks = (1.0 / 7.0) * diffDays; // diffDays in weeks
		int roundWeeks = (int) Math.ceil(weeks);
		if (diffDays < 0) {
			throw new BorrowException("INVALID RETURN DATE!");
		}

		if (diffDays > RENTAL_PERIOD) {
			if (diffDays - RENTAL_PERIOD < 7) {
				lateFee = diffDays - RENTAL_PERIOD;
			} else {
				lateFee = diffDays - RENTAL_PERIOD;
				lateFee += (roundWeeks - 3) * 5;
			}

			if (extended) {
				lateFee /= 2; // applying 50% discount if item had been enable for extended hire.
			}

		}

		super.returnItem(returnDate, lateFee);
		return lateFee;
	}// End of returnI

	private String getPlatforms() {
		String p = "";
		for (int i = 0; i < platforms.length; i++) {
			p += platforms[i];
			if (i < platforms.length - 1)
				p += ",";
		}
		return p;
	}

	protected String getDetails() {

		String onL;
		if (getCurrentlyBorrowed() != null && extended == false) {
			onL = "Yes";
		} else if (extended) {
			onL = "Extended";
		} else {
			onL = "No";
		}

		String str = super.getFirstDetails();
		str += String.format("%-25s %d days\n%-25s %s\n%-25s %s\n\n", "Rental Period", RENTAL_PERIOD, "Platforms",
				getPlatforms(), "On loan", onL);
		str += super.getDetails();
		return str + "\n-----------------------------------------------------------------\n";

	}// End of getD

	public String toString() {
		String loanStatus;
		if (getCurrentlyBorrowed() != null && extended) {
			loanStatus = "E";
		} else if (getCurrentlyBorrowed() != null) {
			loanStatus = "Y";
		} else {
			loanStatus = "N";
		}
		return super.toString() + ":" + getPlatforms() + ":" + loanStatus;
	}

}
