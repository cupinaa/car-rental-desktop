package test.data;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import users.Customer;
import users.Gender;
import data.UserRepository;

public class UserRepositoryTest {

	private UserRepository kp;
	private String testPutanja = "test_users.csv";

	@Before
	public void setUp() {
		kp = new UserRepository() {
			@Override
			public void saveChanges() {

			}
		};
	}

	@After
	public void tearDown() {
		new File(testPutanja).delete();
	}

	@Test
	public void testAddUsera() {
		int initialSize = kp.getUsers().size();

		Customer k = new Customer("Pat", "Tester", Gender.MALE, LocalDate.now(), "060", "Address", "pera123", "pass", LocalDate.now());
		kp.addUsera(k);

		assertEquals("The user count must increase by one", initialSize + 1, kp.getUsers().size());
		assertTrue("The user must receive a positive ID", k.getId() > 0);
	}

	@Test
	public void testDeleteUsera() {
		Customer k = new Customer("Pat", "Tester", Gender.MALE, LocalDate.now(), "060", "Address", "pera123", "pass", LocalDate.now());
		kp.addUsera(k);
		int id = k.getId();

		assertNotNull("The user must exist before deletion", kp.findCustomer(id));

		kp.deleteUsera(k);
		assertNull("User must be null nakon brisanja", kp.findCustomer(id));
	}

	@Test
	public void testFindUseraPoId() {
		Customer k = new Customer("Mike", "Tester", Gender.MALE, LocalDate.now(), "060", "Address", "mika123", "pass", LocalDate.now());
		kp.addUsera(k);

		Customer found = kp.findCustomer(k.getId());
		assertNotNull(found);
		assertEquals("Names must match", "Mike", found.getFirstName());
	}

	@Test
	public void testLogin() {
		Customer k = new Customer("Ann", "Tester", Gender.FEMALE, LocalDate.now(), "060", "Address", "ana123", "test-password", LocalDate.now());
		kp.addUsera(k);

		assertNotNull("A valid login must return a user", kp.login("ana123", "test-password"));
		assertNull("An incorrect password must return null", kp.login("ana123", "incorrect"));
		assertNull("An incorrect username must return null", kp.login("missing", "test-password"));
	}

	@Test
	public void testLoadajUpisi() throws IOException {
		UserRepository productionRepository = new UserRepository();
		productionRepository.getUsers().add(new Customer(1, "FirstName", "LastName", Gender.MALE, LocalDate.now(), "060", "Address", "username", "pass", LocalDate.now()));
		productionRepository.write(testPutanja);

		UserRepository loadedRepository = new UserRepository();
		loadedRepository.load(testPutanja);

		assertEquals("One user must be loaded", 1, loadedRepository.getUsers().size());
		assertEquals("Username must be tacan", "username", loadedRepository.getUsers().get(0).getUsername());
	}
}
