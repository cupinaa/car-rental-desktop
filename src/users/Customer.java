package users;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Customer extends User {

	protected LocalDate licenseIssueDate;
	protected CustomerCategory customerCategory;
	protected int lateReturnCount = 0;

	public Customer(int id, String firstName, String lastName, Gender gender, LocalDate dateOfBirth, String phone, String address,
			String username, String password, LocalDate licenseIssueDate,
			CustomerCategory customerCategory) {
		super(id, firstName, lastName, gender, dateOfBirth, phone, address, username, password);
		this.licenseIssueDate = licenseIssueDate;
		this.customerCategory = customerCategory;
	}

	public Customer(int id, String firstName, String lastName, Gender gender, LocalDate dateOfBirth, String phone, String address,
			String username, String password, LocalDate licenseIssueDate) {
		super(id, firstName, lastName, gender, dateOfBirth, phone, address, username, password);
		this.licenseIssueDate = licenseIssueDate;
	}

	public Customer(String firstName, String lastName, Gender gender, LocalDate dateOfBirth, String phone, String address,
			String username, String password, LocalDate licenseIssueDate,
			CustomerCategory customerCategory) {
		super(firstName, lastName, gender, dateOfBirth, phone, address, username, password);
		this.licenseIssueDate = licenseIssueDate;
		this.customerCategory = customerCategory;
	}

	public Customer(String firstName, String lastName, Gender gender, LocalDate dateOfBirth, String phone, String address,
			String username, String password, LocalDate licenseIssueDate) {
		super(firstName, lastName, gender, dateOfBirth, phone, address, username, password);
		this.licenseIssueDate = licenseIssueDate;
	}

	public boolean isEligibleToBook() {
		LocalDate dayss = LocalDate.now();
		LocalDate preDveYears = dayss.minusYears(2);

		return preDveYears.isAfter(this.licenseIssueDate) || preDveYears.isEqual(this.licenseIssueDate);
	}

	public boolean podZabranom() {
		if (bookingBlockedUntil == null) {
			return false;
		}
		return LocalDateTime.now().isBefore(bookingBlockedUntil);
	}

	public LocalDate getLicenseIssueDate() {
		return licenseIssueDate;
	}

	public void setLicenseIssueDate(LocalDate licenseIssueDate) {
		this.licenseIssueDate = licenseIssueDate;
	}

	public CustomerCategory getCustomerCategory() {
		return customerCategory;
	}

	public void setCustomerCategory(CustomerCategory customerCategory) {
		this.customerCategory = customerCategory;
	}

	protected LocalDateTime bookingBlockedUntil;

	public LocalDateTime getBookingBlockedUntil() {
		return bookingBlockedUntil;
	}

	public void setBookingBlockedUntil(LocalDateTime bookingBlockedUntil) {
		this.bookingBlockedUntil = bookingBlockedUntil;
	}

	public int getLateReturnCount() {
		return lateReturnCount;
	}

	public void setLateReturnCount(int lateReturnCount) {
		this.lateReturnCount = lateReturnCount;
	}

}
