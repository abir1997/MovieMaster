package application;

import java.io.*;

import java.util.Scanner;
/* Class: MovieMaster
* Description: This application class holds an array of Movie and Game references called 'items' which is used to store and manage movies
* 		    and games that are added to the system by the user. This class adds all the functionality.
* Author: Md Abir Ishtiaque - s3677701
*/

public class MovieMaster {
	Scanner keyboard = new Scanner(System.in);
	private String input, itemId, title, genre, description, newRel;
	private String memberId;
	private double rentalFee;
	private int advBorrowDays, returnDays;
	private boolean isNewRelease;
	Item[] items = new Item[100];

	public MovieMaster() {
		readFile();
		menu();
	}

	private void menu() {
		do {
			System.out.printf(
					"*** Movie Master System Menu ***\n"
							+ "%-25s %s\n%-25s %s \n%-25s %s\n%-25s %s\n%-25s %s\n%-25s %s\n",
					"Add item", "A", "Borrow item", "B", "Return item", "C", "Display details", "D", "Seed date", "E",
					"Exit program", "X");

			System.out.println("Enter selection: ");
			input = keyboard.nextLine();
			switch (input.toUpperCase()) {
			case "A":
				if (items[99] == null) { // Cannot add beyond 100 items.
					try {
						addItem();
					} catch (IdException e) {
						System.out.println(e.getMessage());
					}
				} else {
					System.out.println("Array is full!");
				}
				break;
			case "B":
				try {
					borrowProcess();
				} catch (IdException e) {
					System.out.println(e.getMessage());
				} catch (BorrowException e) {
					System.out.println(e.getMessage());
				}
				break;
			case "C":
				try {
					returnProcess();
				} catch (BorrowException e) {
					System.out.println(e.getMessage());
				}
				break;
			case "E":
				if (items[0] == null) { // Can only seedData if no items exist in array.
					try {
						seedData();
					} catch (BorrowException e) {
						System.out.println(e.getMessage());
					} catch (IdException e) {
						System.out.println(e.getMessage());
					}
				} else {
					System.out.println("Error - items already exist!");
				}
				break;
			case "D":
				if (items[0] != null) {
					getDetail();
				} else {
					System.out.println("No items exist!");
				}
				break;
			case "X":
				write();
				System.exit(0);
				break;
			default:
				System.out.println("Invalid user input! Please try again.");
				break;
			}// End of switch

		} while (!input.equalsIgnoreCase("X"));

	}// End of menu.

	private void seedData() throws IdException, BorrowException {
		items[0] = new Movie("MOS", "MAN OF STEEL", "ACTION", "Superman flies bla..", false); // movie that has not been
																								// borrowed
		items[1] = new Movie("POC", "PIRATES OF THE CAR", "ACTION", "Made up movie dont't...", false); // movie that has
																										// been borrowed
																										// but not
																										// returned
		items[1].borrow("RIC");
		items[2] = new Movie("AAU", "Avengers Age of Ultron", "ACTION", "Description of avengers...", false);// has been
																												// borrowed
																												// and
																												// returned
																												// in 5
																												// days
		items[2].borrow("SHM");
		items[2].returnItem(new DateTime(5));
		items[3] = new Movie("PPN", "Peter Pan", "FANTASY", "Epic adventures await Peter...", false);// has been
																										// borrowed and
																										// returned
																										// after 10 days
		items[3].borrow("JIC");
		items[3].returnItem(new DateTime(10));
		items[4] = new Movie("SSQ", "Suicide squad", "ACTION", "Suicide is an option...", false);// has been
																									// borrowed,returned
																									// after 10 days and
																									// borrowed again
																									// but not returned.
		items[4].borrow("JIN");
		items[4].returnItem(new DateTime(10));
		items[4].borrow("SHA");
		items[5] = new Movie("GOT", "Game of Thrones", "ACTION", "Need marks to pass...", true);// has not been borrowed
		items[6] = new Movie("NAR", "Naruto", "ANIME", "Please give me marks and Naruto...", true);// has been borrowed
																									// but not returned
		items[6].borrow("FLN");
		items[7] = new Movie("GOZ", "Godzilla", "ACTION", "Godzilla cant save me...", true);// borrowed and returned in
																							// 1 day
		items[7].borrow("VLI");
		items[7].returnItem(new DateTime(1));
		items[8] = new Movie("BAT", "Batman", "ACTION", "Batman programs in Java...", true);// borrowed and returned in
																							// 3 days
		items[8].borrow("RIC");
		items[8].returnItem(new DateTime(3));
		items[9] = new Movie("MNC", "Monsters Inc", "CARTOON", "Java is a monster...", true);// has been
																								// borrowed,returned and
																								// borrowed
		items[9].borrow("SHA");
		items[9].returnItem(new DateTime(3));
		items[9].borrow("SHM");

		items[10] = new Game("IGA", "Injustice Gods Among Us", "Fighting", "What if our heroes...", "Xbox 360,PS4");// has
																													// not
																													// been
																													// borrowed
		items[11] = new Game("SKY", "Skyrim", "Role-Playing", "Go on an epic adventure...", "Xbox 360,PC,PS4");// has
																												// been
																												// borrowed
																												// but
																												// not
																												// returned
		items[11].borrow("RIC");
		items[12] = new Game("ESO", "Elder Scrolls Online", "MMORPG", "Epic questing bha..", "Xbox 360,PS4,PC");// borrowed
																												// and
																												// returned
		items[12].borrow("PIC");
		items[12].returnItem(new DateTime(19));
		items[13] = new Game("WIT", "Witcher", "Action-RPG", "Hunt monsters with swords...", "Xbox 360,PS4,PC");// has
																												// been
																												// borrowed
																												// and
																												// returned
		items[13].borrow("ABI");
		items[13].returnItem(new DateTime(32));

	} // End of seedData

	private void getDetail() {
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) // Can't print empty elements.
			{
				System.out.println(items[i].getDetails());
			}
		}
	} // End of getDetail

	/*
	 * Algorithm BEGIN Prompt user for 3 letter id,if id is not valid then throw
	 * exception. If id is valid, loop through item array and check if same id
	 * already exists in array,if it exists print out error message. Else prompt
	 * user for details of item. Create the new item in the array. END
	 */
	private void addItem() throws IdException {
		String mOrG;
		System.out.print("Enter id: ");
		itemId = keyboard.nextLine();
		// Check if entered movieId already exists in the system
		if (itemId.length() != 3) {
			throw new IdException("The id " + itemId + " is invalid. Please enter a 3 digit id.");
		}
		for (int i = 0; i < items.length; i++) // iterating through the movie array.
		{
			if (items[i] != null) // Excluding empty arrays
			{
				if (items[i].getId().substring(2).equals(itemId)) // checking to see if any id matches any existing id
				{
					if (items[i] instanceof Movie) {
						System.out.println("Error - Id for M_" + itemId + " already exists in the system!");
					} else {
						System.out.println("Error - Id for G_" + itemId + " already exists in the system");
					}
					break;
				}

			} else { // if id is unique then prompt for details
				System.out.print("Enter title: ");
				title = keyboard.nextLine();
				System.out.print("Enter genre: ");
				genre = keyboard.nextLine();
				System.out.print("Enter description: ");
				description = keyboard.nextLine();

				System.out.println("Movie or Game (M/G)?");
				mOrG = keyboard.nextLine();
				while (!mOrG.equalsIgnoreCase("M") && !mOrG.equalsIgnoreCase("G")) {
					System.out.println("Invalid input! Please enter M or G.");
					mOrG = keyboard.nextLine();
				}
				if (mOrG.equalsIgnoreCase("M")) {
					System.out.print("Enter new release (Y/N): ");
					newRel = keyboard.nextLine();
					while (!newRel.equalsIgnoreCase("Y") && !newRel.equalsIgnoreCase("N")) {
						System.out.println("Invalid input! Please enter Y or N.");
						newRel = keyboard.nextLine();
					}

					if (newRel.equalsIgnoreCase("Y")) {
						isNewRelease = true;
					} else if (newRel.equalsIgnoreCase("N")) {
						isNewRelease = false;
					}

					items[i] = new Movie(itemId, title, genre, description, isNewRelease);
					System.out.println("New movie succesfully added for id :M_" + itemId);
					break;
				} else if (mOrG.equalsIgnoreCase("G")) {
					System.out.print("Enter Game platforms: ");
					String platforms = keyboard.nextLine();

					System.out.println("New game successfully added for id: G_" + itemId);
					items[i] = new Game(itemId, title, genre, description, platforms);
					break;
				}
			}

		} // End of for loop

	}// End of addItem.

	private int findItemIndex() {
		// Find which position of the array movie belongs to.
		int i = 0;
		while (items[i] != null && i < items.length && !items[i].getId().substring(2).equals(itemId)) {
			if (items[i] != null) {
				i++;
			}
		}
		return i;
	}

	private void borrowProcess() throws IdException, BorrowException {
		System.out.print("Enter id: ");
		itemId = keyboard.nextLine();

		int i = findItemIndex();
		if (items[i] != null && (items[i].getId().substring(2)).equals(itemId)) {
			// Check if it is on loan
			if (items[i].getCurrentlyBorrowed() != null) {
				throw new BorrowException("The item with id " + items[i].getId() + " is currently on loan!");
			} else {
				System.out.print("Member id: ");
				memberId = keyboard.nextLine();
				double fee = items[i].borrow(memberId);
				System.out.print("Advance borrow (days): ");
				advBorrowDays = keyboard.nextInt();
				keyboard.nextLine();
				DateTime borrowDate = new DateTime(advBorrowDays);

				// Check if its a Movie or a Game.
				if (items[i] instanceof Movie) {
					DateTime dueDate = new DateTime(borrowDate, ((Movie) items[i]).getIsNewRelease() ? 2 : 7);
					System.out.println("The item " + items[i].getTitle() + " costs $" + fee + " and is due on "
							+ dueDate.getFormattedDate());
				} else {
					System.out.print("Enable Extended hire? (Y/N)?  ");
					String eHire = keyboard.nextLine();
					if (eHire.equalsIgnoreCase("Y")) {
						((Game) items[i]).setExtended(true);
					} else {
						((Game) items[i]).setExtended(false);
					}
					DateTime dueDate = new DateTime(borrowDate, 22);
					System.out.println("The item " + items[i].getTitle() + " costs $" + 20 + " and is due on "
							+ dueDate.getFormattedDate());
				}
			}

		} else {
			System.out.println("Error - The item with id number: " + itemId + ", not found.");

		}

	}// End of borrowP

	private void returnProcess() throws BorrowException {
		System.out.println("Enter id: ");
		itemId = keyboard.nextLine();

		int i = findItemIndex();
		if (items[i] != null && items[i].getId().substring(2).equals(itemId)
				&& items[i].getCurrentlyBorrowed() != null) { // Item has been found and is on loan

			System.out.print("Enter number of days on loan: ");
			returnDays = keyboard.nextInt();
			keyboard.nextLine();
			rentalFee = items[i].returnItem(new DateTime(returnDays));
			System.out.println("The total fee payable is $" + rentalFee);
		} else if (items[i] != null && !items[i].getId().substring(2).equals(itemId) || items[i] == null) { // Invalid
																											// itemId

			throw new BorrowException("Error - The item with id : " + itemId + ", not found.");
		} else { // Item found but it is not on loan

			throw new BorrowException("The item with the id " + items[i].getId() + " is not currently on loan");
		}

	}// End of returnP

	private void write() {
		String str = null;
		PrintWriter pw;
		try {
			pw = new PrintWriter("ItemStatus.txt", "UTF-8");
			for (int i = 0; i < items.length; i++) {
				if (items[i] != null) {
					str = items[i].toString() + "\n";
					for (HiringRecord hr : items[i].getHireHistory()) {
						if (hr != null) {
							str += hr.toString() + "\n";
						}
					}
					str += "----------------------------------------------------------------------------";

					pw.println(str);
				}

			} // End of loop
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		copy("ItemStatus.txt", "ItemStatus_backup.txt");

	}// End of write

	private void copy(String file, String backupFile) {
		try {
			Scanner sc = new Scanner(new File(file));
			PrintWriter pw = new PrintWriter(new File(backupFile));

			while (sc.hasNext()) {
				pw.println(sc.nextLine());
			}
			pw.close();
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}// End of copy

	private DateTime parseDate(String date) {
		if (date.equalsIgnoreCase("none"))
			return null;
		// Else convert to int
		int day = Integer.parseInt(date.substring(0, 2));
		int month = Integer.parseInt(date.substring(2, 4));
		int year = Integer.parseInt(date.substring(4, 8));
		return new DateTime(day, month, year);
	}

	private void read(File file) {

		int counter = 0;
		String brk = "----------------------------------------------------------------------------";
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(file));
			Item item = null;
			String scanLine = null;
			while ((scanLine = br.readLine()) != null) {
				if (!scanLine.trim().isEmpty()) {
					String[] split = scanLine.split(":");
					String id = split[0];
					if (id.startsWith("M_")) {
						String title = split[1];
						String description = split[3];
						String genre = split[2];
						String isNewRelease = split[5];
						String loanStatus = split[6];
						item = new Movie(id.substring(2, 5), title, genre, description,
								"NR".equalsIgnoreCase(isNewRelease) ? true : false);

						String borrowingRecord = null;
						int index = 0;
						while (!brk.equals(borrowingRecord = br.readLine())) {

							if (borrowingRecord.trim().isEmpty() == false) {
								// Split into different Strings using ':' as delimiter.
								String[] recSplit = borrowingRecord.split(":");
								String hireId = recSplit[0];
								String borrowDate = recSplit[1];
								String returnDate = recSplit[2];
								String rentalFee = recSplit[3];
								String lateFee = recSplit[4];
								rentalFee = "none".equals(rentalFee) ? "0.0" : rentalFee;
								lateFee = "none".equals(lateFee) ? "0.0" : lateFee;

								HiringRecord hr = new HiringRecord("M_" + id.substring(2, 5), hireId.substring(6, 9),
										parseDate(borrowDate), Double.valueOf(rentalFee));
								item.addRecord(hr);
								index++;
								item.getRecord().setReturnDate(parseDate(returnDate), Double.valueOf(lateFee));
							}
						}
						if (loanStatus.equalsIgnoreCase("Y")) {
							item.setCurrentlyBorrowed(item.getHireHistory()[index - 1]);
						} else {
							item.setCurrentlyBorrowed(null);
						}
						items[counter] = item;
						counter++;

					} else if (id.startsWith("G_")) {

						String title = split[1];
						String description = split[3];
						String genre = split[2];
						String platforms = split[5];
						String loanStatus = split[6];

						item = new Game(id.substring(2), title, genre, description, platforms);

						String borrowingRecord = null;
						int index = 0;
						while (!brk.equals(borrowingRecord = br.readLine())) {
							if (borrowingRecord.trim().isEmpty() == false) {
								String[] recSplit = borrowingRecord.split(":");
								String hireId = recSplit[0];
								String borrowDate = recSplit[1];
								String returnDate = recSplit[2];
								String rentalFee = recSplit[3];
								String lateFee = recSplit[4];
								rentalFee = "none".equals(rentalFee) ? "0.0" : rentalFee;
								lateFee = "none".equals(lateFee) ? "0.0" : lateFee;

								HiringRecord hr = new HiringRecord("G_" + id.substring(2, 5), hireId.substring(6, 9),
										parseDate(borrowDate), Double.valueOf(rentalFee));
								item.addRecord(hr);
								index++;
								item.getRecord().setReturnDate(parseDate(returnDate), Double.valueOf(lateFee));
							}
						}

						if (loanStatus.equals("Y")) {
							item.setCurrentlyBorrowed(item.getHireHistory()[index - 1]);
						} else if (loanStatus.equals("N")) {
							item.setCurrentlyBorrowed(null);
						} else if (loanStatus.equals("E")) {
							((Game) item).setExtended(true);
							item.setCurrentlyBorrowed(item.getHireHistory()[index - 1]);
						}

						items[counter] = item;
						counter++;

					} // e of g
				}
			} // End of Master loop

		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}// End of read

	private void readFile() {
		// Select file to read from
		File file = new File("ItemStatus.txt");
		if (file.exists()) {
			read(file);
		} else {
			file = new File("ItemStatus_backup.txt");
			if (file.exists()) {
				read(file);
				System.out.println("Data was read from a backup file");
			} else {
				System.out.println("No item data was loaded!");
			}
		}
	}

}
