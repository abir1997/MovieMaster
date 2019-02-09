package application;

/* Class: Item 
* Description: It is the superclass for movie and game.
* It should not be possible to instantiate this class as every item will have a different implementation of returning an item and some additional behaviors. 
* Author: Md Abir Ishtiaque - s3677701
*/
public abstract class Item {
	private String id;
	private String title;
	private String description;
	private String genre;
	protected double fee;
	private HiringRecord currentlyBorrowed;
	private HiringRecord[] hireHistory;
	private int hireCounter = 0; // need counter for keeping track of hiringRecords

	public Item(String id, String title, String genre, String description, double fee) {
		this.id = id;
		this.title = title;
		this.genre = genre;
		this.description = description;
		hireHistory = new HiringRecord[10]; // holds most recent 10
	}

	protected String getId() {
		return id;
	}

	protected String getTitle() {
		return title;
	}

	protected HiringRecord getCurrentlyBorrowed() {
		return currentlyBorrowed;
	}

	protected void addRecord(HiringRecord currentlyBorrowed) {
		hireHistory[hireCounter] = currentlyBorrowed;
		hireCounter += 1; // incrementing counter as a new hiringRecord has been added
	}

	protected void setCurrentlyBorrowed(HiringRecord currentlyBorrowed) {
		this.currentlyBorrowed = currentlyBorrowed;
	}

	protected HiringRecord getRecord() {
		return hireHistory[hireCounter - 1]; // subtract 1 as in addRecord we add 1.
	}

	protected HiringRecord[] getHireHistory() {
		return hireHistory;
	}

	/*
	 * ALGORITHM BEGIN Check if memberId is valid and item can be borrowed,if it is
	 * not then throw exception. Else adjust the hireHistory array if needed by
	 * checking if maximum hiring records have been reached and remove the oldest
	 * one to make room. Create a HiringRecord and add it to the array,also setting
	 * currentlyBorrowed to it. Return the rental fee. END
	 */

	protected double borrow(String memberId) throws IdException, BorrowException {
		if (memberId == null || memberId.length() != 3 || memberId.trim().isEmpty()) {
			throw new IdException("INVALID ID! Please try again!");
		} else if (currentlyBorrowed != null) {
			throw new BorrowException("The item " + getId() + " is currently on loan");
		} else {

			adjustHireArray();

			DateTime borrowDate = new DateTime();
			HiringRecord hr = new HiringRecord(id, memberId, borrowDate, fee);

			setCurrentlyBorrowed(hr); // currentlyBorrowed=new HiringRecord(...)
			addRecord(hr); // hireHistory[i]=currentlyBorrowed

			return fee;
		}
	}

	protected abstract double returnItem(DateTime returnDate) throws BorrowException;

	protected double returnItem(DateTime returnDate, double lateFee) throws BorrowException {

		if (returnDate == null || getCurrentlyBorrowed() == null) {
			throw new BorrowException("Item is not on loan!");
		}
		setCurrentlyBorrowed(null);
		getRecord().setReturnDate(returnDate, lateFee);
		return lateFee;

	}

	private void adjustHireArray() {
		// Check if max number of Hiring Records have been reached and remove the oldest
		// one.
		int c = hireCounter;
		if (c == 10) {
			for (int i = 0; i < hireHistory.length; i++) {
				hireHistory[i] = hireHistory[i + 1];
			}
			hireCounter--;
		}
	}

	protected String getFirstDetails() {
		String str = String.format("%-25s %s\n%-25s %s\n%-25s %s\n%-25s %s" + "\n%-25s $%.1f\n", "ID:", id, "Title:",
				title, "Genre:", genre, "Description", description, "Standard fee", fee);
		return str;
	}

	protected String getDetails() {

		String str = "   \tBorrowing Record:\n";
		if (hireCounter > 0) {
			for (HiringRecord hr : hireHistory) {
				if (hr != null) {
					str += "-------------------------------------------------------------\n";
					str += hr.getDetails();
				}
			}
		} else {
			str += "   \tNONE\n";
		}
		return str;
	}// End of getDetails

	public String toString() {

		return id + ":" + title + ":" + genre + ":" + description + ":" + fee;
	}
}
