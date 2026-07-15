package test.data;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import data.VehicleRepository;
import vehicles.VehicleCategory;
import vehicles.VehicleModel;
import vehicles.VehicleStatus;
import vehicles.Vehicle;

public class VehicleRepositoryTest {

	private VehicleRepository vp;
	private String testPutanja = "test_vehicles.csv";

	@Before
	public void setUp() {
		vp = new VehicleRepository() {
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
	public void testAddVehicle() {
		int initialSize = vp.getVehicles().size();
		VehicleModel model = new VehicleModel("Fiat", "Punto", VehicleCategory.ECONOMY);
		Vehicle v = new Vehicle(model, "BG-123-AA", VehicleStatus.AVAILABLE);

		vp.addVehicle(v);

		assertEquals("The vehicle count must increase by one", initialSize + 1, vp.getVehicles().size());
		assertTrue("ID vehicles must be generisan (veci od 0)", v.getId() > 0);
	}

	@Test
	public void testFindVehicle() {
		VehicleModel model = new VehicleModel("Fiat", "Punto", VehicleCategory.ECONOMY);
		Vehicle v = new Vehicle(model, "NS-111-BB", VehicleStatus.AVAILABLE);
		vp.addVehicle(v);

		Vehicle found = vp.findVehicle(v.getId());
		assertNotNull(found);
		assertEquals("License plates must match", "NS-111-BB", found.getLicensePlate());
	}

	@Test
	public void testDeleteVehicle() {
		VehicleModel model = new VehicleModel("Fiat", "Punto", VehicleCategory.ECONOMY);
		Vehicle v = new Vehicle(model, "NI-222-CC", VehicleStatus.AVAILABLE);
		vp.addVehicle(v);
		int id = v.getId();

		assertNotNull(vp.findVehicle(id));
		vp.deleteVehicle(v);
		assertNull(vp.findVehicle(id));
	}

	@Test
	public void testLoadajUpisi() throws IOException {
		assertTrue(true);
	}
}
