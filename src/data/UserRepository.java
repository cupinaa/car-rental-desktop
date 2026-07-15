package data;
import java.time.LocalDateTime;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import users.Administrator;
import users.Agent;
import users.CustomerCategory;
import users.Customer;
import users.User;
import users.Gender;
import users.EducationLevel;
import users.Employee;


public class UserRepository {

	protected ArrayList<User> users = new ArrayList<>();

	public void load(String putanja) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(putanja));
		String linija;
		while ((linija = br.readLine()) != null) {
			String[] delovi = linija.split("\\|");
			String tip = delovi[0];
			int id = Integer.parseInt(delovi[1]);
			String firstName = delovi[2];
			String lastName = delovi[3];
			Gender gender = Gender.valueOf(delovi[4].trim());
			LocalDate dateOfBirth = LocalDate.parse(delovi[5]);
			String phone = delovi[6];
			String address = delovi[7];
			String username = delovi[8];
			String password = delovi[9];
			if (tip.equals("ADMINISTRATOR") || tip.equals("AGENT")) {
				String sprema = delovi[10];
				EducationLevel educationLevel = EducationLevel.valueOf(sprema);
				int yearsOfService = Integer.parseInt(delovi[11]);
				double plata = Double.parseDouble(delovi[12]);
				if (tip.equals("ADMINISTRATOR")) {
					Administrator admin = new Administrator(id, firstName, lastName, gender, dateOfBirth, phone, address,
							username, password, educationLevel, yearsOfService, plata);
					users.add(admin);
				} else {
					Agent agent = new Agent(id, firstName, lastName, gender, dateOfBirth, phone, address, username,
							password, educationLevel, yearsOfService, plata);
					users.add(agent);
				}

			} else if (tip.equals("CUSTOMER")) {
				LocalDate licenseIssueDate = LocalDate.parse(delovi[10]);
				Customer customer = new Customer(id, firstName, lastName, gender, dateOfBirth, phone, address, username, password, licenseIssueDate);

				if (delovi.length > 11 && !delovi[11].isEmpty() && !delovi[11].equals("null")) {
					try {
						CustomerCategory customerCategory = CustomerCategory.valueOf(delovi[11]);
						customer.setCustomerCategory(customerCategory);
					} catch (Exception e) {}
				}

				if (delovi.length > 12 && !delovi[12].isEmpty() && !delovi[12].equals("null")) {
					customer.setBookingBlockedUntil(LocalDateTime.parse(delovi[12]));
				}

				if (delovi.length > 13 && !delovi[13].isEmpty() && !delovi[13].equals("null")) {
					customer.setLateReturnCount(Integer.parseInt(delovi[13]));
				}

				users.add(customer);
			}
		}
		br.close();
	}

	public void write(String putanja) throws IOException {
		PrintWriter pw = new PrintWriter(new FileWriter(putanja));
		try {
			for (User k : users) {
				String commonFields = k.getId() + "|" + k.getFirstName() + "|" + k.getLastName() + "|" + k.getGender() + "|"
						+ k.getDateOfBirth() + "|" + k.getPhone() + "|" + k.getAddress() + "|" + k.getUsername()
						+ "|" + k.getPassword();

				if (k instanceof Administrator) {
					Administrator admin = (Administrator) k;
					pw.println("ADMINISTRATOR|" + commonFields + "|" + admin.getEducationLevel() + "|"
							+ admin.getYearsOfService() + "|" + admin.getSalary());
				} else if (k instanceof Agent) {
					Agent agent = (Agent) k;
					pw.println("AGENT|" + commonFields + "|" + agent.getEducationLevel() + "|" + agent.getYearsOfService()
							+ "|" + agent.getSalary());
				} else if (k instanceof Customer) {
					Customer customer = (Customer) k;
					String blockedUntilText = (customer.getBookingBlockedUntil() != null) ? customer.getBookingBlockedUntil().toString() : "null";

					if (customer.getCustomerCategory() != null) {
				        pw.println("CUSTOMER|" + commonFields + "|" + customer.getLicenseIssueDate() + "|" + customer.getCustomerCategory().toString() + "|" + blockedUntilText + "|" + customer.getLateReturnCount());
				    } else {
				        pw.println("CUSTOMER|" + commonFields + "|" + customer.getLicenseIssueDate() + "|null|" + blockedUntilText + "|" + customer.getLateReturnCount());
				    }
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());

		}
		pw.close();
	}

	public Agent findAgent(int id) {
	    for (User k : users) {
	        if (k instanceof Agent && k.getId() == id) {
			return (Agent) k;
	        }
	    }
	    return null;
	}


	public Customer findCustomer(int id) {
	    for (User k : users) {
	        if (k instanceof Customer && k.getId() == id) {
	            return (Customer) k;
	        }
	    }
	    return null;
	}

	public User login(String username, String password) {
		for(User k : users) {
			if(k.getUsername().equals(username) && k.getPassword().equals(password)) {
				return k;
			}
		}
		return null;
	}

	public void addUsera(User k) {
		k.setId(generateNewId());
		users.add(k);
		saveChanges();
	}

    private int generateNewId() {
        int maxId = 0;
        for (User k : users) {
            if (k.getId() > maxId) {
                maxId = k.getId();
            }
        }
        return maxId + 1;
    }

	public void deleteUsera(User k) {
		users.remove(k);
		saveChanges();
	}

	public void saveChanges() {
		try {
			write("users.csv");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public double calculateRashode(LocalDate odDatea, LocalDate doDatea) {
		double totalExpenses = 0;

		long numberOfDays = ChronoUnit.DAYS.between(odDatea, doDatea);
		if (numberOfDays <= 0) {
			numberOfDays = 1;
		}

		for (User k : users) {
			if (k instanceof Employee) {
				double plataZaposlenog = ((Employee) k).getSalary();
				double dnevnica = plataZaposlenog / 30.0;

				totalExpenses += (dnevnica * numberOfDays);
			}
		}
		return totalExpenses;
	}

	public ArrayList<User> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}
}
