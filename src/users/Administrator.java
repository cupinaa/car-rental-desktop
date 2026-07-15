package users;
import java.time.LocalDate;

public class Administrator extends Employee {

	public Administrator(String firstName, String lastName, Gender gender, LocalDate dateOfBirth, String phone,
			String address, String username, String password, EducationLevel educationLevel, int yearsOfService) {
		super(firstName, lastName, gender, dateOfBirth, phone, address, username, password, educationLevel,
				yearsOfService);
	}



	public Administrator(int id, String firstName, String lastName, Gender gender, LocalDate dateOfBirth, String phone,
			String address, String username, String password, EducationLevel educationLevel, int yearsOfService,
			double plata) {
		super(id, firstName, lastName, gender, dateOfBirth, phone, address, username, password, educationLevel, yearsOfService,
				plata);
	}



}
