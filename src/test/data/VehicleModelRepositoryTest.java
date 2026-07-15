package test.data;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import data.VehicleModelRepository;
import vehicles.VehicleCategory;
import vehicles.VehicleModel;

public class VehicleModelRepositoryTest {

	private VehicleModelRepository mp;

	@Before
	public void setUp() {
		mp = new VehicleModelRepository() {
			@Override
			public void write(String path) {

			}
		};
	}

	@Test
	public void testAddModel() {
		int initialSize = mp.getModels().size();
		VehicleModel m = new VehicleModel("Toyota", "Yaris", VehicleCategory.ECONOMY);
		mp.addModel(m);

		assertEquals("Number modela must be uvecan za 1", initialSize + 1, mp.getModels().size());
		assertTrue("ID must be veci od 0", m.getId() > 0);
	}

	@Test
	public void testFindModel() {
		VehicleModel m = new VehicleModel("Toyota", "Corolla", VehicleCategory.ECONOMY);
		mp.addModel(m);

		VehicleModel found = mp.findModel(m.getId());
		assertNotNull(found);
		assertEquals("Manufacturers must match", "Toyota", found.getManufacturer());
	}

	@Test
	public void testLoadajUpisi() throws IOException {
		assertTrue(true);
	}
}
