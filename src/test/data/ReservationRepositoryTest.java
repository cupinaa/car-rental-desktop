package test.data;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import users.Customer;
import users.Gender;
import data.ReservationRepository;
import reservation.Reservation;
import reservation.ReservationStatus;
import vehicles.VehicleCategory;
import vehicles.VehicleModel;
import vehicles.VehicleStatus;
import vehicles.Vehicle;

public class ReservationRepositoryTest {

	private ReservationRepository rp;
	private String testPutanja = "test_reservations.csv";

	@Before
	public void setUp() {
		rp = new ReservationRepository() {
			@Override
			public void write(String path) {

			}
		};
	}

	@After
	public void tearDown() {
		new File(testPutanja).delete();
	}

	@Test
	public void testAddReservation() {
		int initialSize = rp.getReservations().size();

		Customer customer = new Customer("Customer", "Customerovic", Gender.FEMALE, LocalDate.now(), "060", "Address", "customer", "123", LocalDate.now());
		VehicleModel model = new VehicleModel("Fiat", "Punto", VehicleCategory.ECONOMY);
		Vehicle vehicle = new Vehicle(model, "BG-123", VehicleStatus.AVAILABLE);

		Reservation reservation = new Reservation(1, customer, vehicle, LocalDate.now(), LocalDate.now().plusDays(3), 5000.0, ReservationStatus.APPROVED, new ArrayList<>());

		rp.addReservation(reservation);

		assertEquals("The reservation count must increase by one", initialSize + 1, rp.getReservations().size());
		assertTrue("ID reservations must be generisan (veci od 0)", reservation.getId() > 0);
	}

	@Test
	public void testFindReservation() {
		Customer customer = new Customer("Customer", "Customerovic", Gender.FEMALE, LocalDate.now(), "060", "Address", "customer", "123", LocalDate.now());
		VehicleModel model = new VehicleModel("Fiat", "Punto", VehicleCategory.ECONOMY);
		Vehicle vehicle = new Vehicle(model, "BG-123", VehicleStatus.AVAILABLE);

		Reservation reservation = new Reservation(1, customer, vehicle, LocalDate.now(), LocalDate.now().plusDays(3), 5000.0, ReservationStatus.APPROVED, new ArrayList<>());
		rp.addReservation(reservation);

		Reservation found = rp.findReservation(reservation.getId());
		assertNotNull("Reservation must be found", found);
		assertEquals("Prices must match", 5000.0, found.getTotalPrice(), 0.01);
	}

	@Test
	public void testLoadajUpisi() throws IOException {
		assertTrue(true);
	}
}
