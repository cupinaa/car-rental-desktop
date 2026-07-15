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

import data.ExtraServiceRepository;
import reservation.ExtraService;

public class ExtraServiceRepositoryTest {

	private ExtraServiceRepository dup;
	private String testPutanja = "test_extra-services.csv";

	@Before
	public void setUp() {
		dup = new ExtraServiceRepository() {
			@Override
			public void write(String p) throws IOException {

			}
		};
	}

	@After
	public void tearDown() {
		new File(testPutanja).delete();
	}

	@Test
	public void testAddService() {
		int originalSize = dup.getServices().size();
		ExtraService du = new ExtraService("Krovni kofer");
		dup.addService(du);

		assertEquals("The service count must increase by one", originalSize + 1, dup.getServices().size());
		assertTrue("ID must be generisan (veci od 0)", du.getId() > 0);
	}

	@Test
	public void testDeleteService() {
		ExtraService du = new ExtraService("GPS Navigacija");
		dup.addService(du);
		int id = du.getId();

		assertNotNull("The service must exist before deletion", dup.findService(id));

		dup.deleteService(du);
		assertNull("Service must be null nakon brisanja", dup.findService(id));
	}

	@Test
	public void testFindService() {
		ExtraService du = new ExtraService("Child seat");
		dup.addService(du);

		ExtraService found = dup.findService(du.getId());
		assertNotNull(found);
		assertEquals("Service names must match", "Child seat", found.getServiceName());
	}

	@Test
	public void testLoadajUpisi() throws IOException {
		ExtraServiceRepository productionRepository = new ExtraServiceRepository();
		productionRepository.getServices().add(new ExtraService(1, "Winter tires"));
		productionRepository.getServices().add(new ExtraService(2, "Snow chains"));

		productionRepository.write(testPutanja);

		ExtraServiceRepository loadedRepository = new ExtraServiceRepository();
		loadedRepository.load(testPutanja);

		assertEquals("Two services must be loaded", 2, loadedRepository.getServices().size());
		assertEquals("The first service must be Winter tires", "Winter tires", loadedRepository.getServices().get(0).getServiceName());
	}
}
