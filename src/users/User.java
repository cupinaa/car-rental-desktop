package users;

import java.time.LocalDate;

public abstract class User {

	int id;
	protected String firstName;
	protected String lastName;
	protected Gender gender;
	protected LocalDate dateOfBirth;
	protected String phone;
	protected String address;
	protected String username;
	protected String password;
	protected boolean prijavaljen = false;

	public User(int id, String firstName, String lastName, Gender gender, LocalDate dateOfBirth, String phone,
			String address, String username, String password) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.phone = phone;
		this.address = address;
		this.username = username;
		this.password = password;
	}

	public User(String firstName, String lastName, Gender gender, LocalDate dateOfBirth, String phone, String address,
			String username, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.phone = phone;
		this.address = address;
		this.username = username;
		this.password = password;
	}

	@Override
	public String toString() {
		return "User [ID: " + id + " | FirstName i lastName: " + firstName + " " + lastName +
		           " | Username firstName: " + username +
		           " | Telefon: " + phone +
		           " | Address: " + address +
		           " | Date birth: " + dateOfBirth + "]";
	}

	public void prijava() {
		this.prijavaljen = true;
	}

	public void odjava() {
		this.prijavaljen = false;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isPrijavaljen() {
		return prijavaljen;
	}

	public void setPrijavaljen(boolean prijavaljen) {
		this.prijavaljen = prijavaljen;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
