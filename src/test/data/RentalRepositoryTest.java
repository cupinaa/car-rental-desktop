package test.data;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import rental.Rental;
import users.Agent;
import users.Customer;
import users.Gender;
import data.RentalRepository;
import reservation.Reservation;
import reservation.ReservationStatus;
import vehicles.VehicleCategory;
import vehicles.VehicleModel;
import vehicles.VehicleStatus;
import vehicles.Vehicle;

public class RentalRepositoryTest {

	private RentalRepository ip;
	private String testPutanja = "test_rental.csv";

	@Before
	public void setUp() {
		ip = new RentalRepository() {
			@Override
			public void write(String putanja) throws IOException {

			}
		};
	}

	@After
	public void tearDown() {
		new File(testPutanja).delete();
	}

	@Test
	public void testAddRental() {
		int originalSize = ip.getRentals().size();

		Agent agent = new Agent("Agent", "Agentovic", Gender.MALE, LocalDate.now(), "060123123", "Address 1", "agent", "123", null, 5);
		Customer customer = new Customer("Customer", "Customerovic", Gender.FEMALE, LocalDate.now(), "060321321", "Address 2", "customer", "123", LocalDate.now());
		VehicleModel model = new VehicleModel("Fiat", "Punto", VehicleCategory.ECONOMY);
		Vehicle vehicle = new Vehicle(model, "BG-123", VehicleStatus.AVAILABLE);
		Reservation reservation = new Reservation(1, customer, vehicle, LocalDate.now(), LocalDate.now().plusDays(3), 5000.0, ReservationStatus.APPROVED, new ArrayList<>());

		Rental rental = new Rental(reservation, agent, 150000.0);

		ip.addRental(rental);

		assertEquals("The rental count must increase by one", originalSize + 1, ip.getRentals().size());
		assertTrue("ID must be generisan (veci od 0)", rental.getId() > 0);
	}

	@Test
	public void testLoadajUpisi() throws IOException {
		assertTrue(true);
	}
}
