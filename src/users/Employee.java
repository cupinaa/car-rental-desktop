package users;

import java.time.LocalDate;

public abstract class Employee extends User {

	protected EducationLevel educationLevel;
	protected int yearsOfService;
	protected double plata;

	public Employee(int id, String firstName, String lastName, Gender gender, LocalDate dateOfBirth, String phone,
			String address, String username, String password, EducationLevel educationLevel, int yearsOfService,
			double plata) {
		super(id, firstName, lastName, gender, dateOfBirth, phone, address, username, password);
		this.educationLevel = educationLevel;
		this.yearsOfService = yearsOfService;
		this.plata = plata;

	}

	public Employee(String firstName, String lastName, Gender gender, LocalDate dateOfBirth, String phone, String address,
			String username, String password, EducationLevel educationLevel, int yearsOfService) {
		super(firstName, lastName, gender, dateOfBirth, phone, address, username, password);
		this.educationLevel = educationLevel;
		this.yearsOfService = yearsOfService;


	}

	public void racunanjePlate(double osnew) {
		this.plata = osnew * (educationLevel.getSalaryMultiplier() + 0.004 * yearsOfService);
	}

	public EducationLevel getEducationLevel() {
		return educationLevel;
	}

	public void setEducationLevel(EducationLevel educationLevel) {
		this.educationLevel = educationLevel;
	}

	public int getYearsOfService() {
		return yearsOfService;
	}

	public void setYearsOfService(int yearsOfService) {
		this.yearsOfService = yearsOfService;
	}

	public double getSalary() {
		return plata;
	}

	public void setSalary(double plata) {
		this.plata = plata;
	}
}
