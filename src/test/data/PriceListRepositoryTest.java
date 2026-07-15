package test.data;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pricing.PriceList;
import pricing.PriceListItem;
import data.PriceListRepository;
import vehicles.VehicleCategory;

public class PriceListRepositoryTest {

	private PriceListRepository cp;
	private String testPutanjaPriceLista = "test_price-lists.csv";
	private String testPutanjaStavki = "test_stavke.csv";
	private String testPutanjaService = "test_services.csv";

	@Before
	public void setUp() {
		cp = new PriceListRepository() {
			@Override
			public void saveChanges() {

			}
		};
	}

	@After
	public void tearDown() {
		new File(testPutanjaPriceLista).delete();
		new File(testPutanjaStavki).delete();
		new File(testPutanjaService).delete();
	}

	@Test
	public void testAddPriceList() {
		ArrayList<PriceListItem> stavke = new ArrayList<>();
		stavke.add(new PriceListItem(VehicleCategory.ECONOMY, 2000.0));
		PriceList c = new PriceList(LocalDate.now(), LocalDate.now().plusMonths(1), stavke, 10000.0, 0.1, 0.15, 0.2, 500.0, new HashMap<>());

		int originalSize = cp.getPriceLists().size();
		cp.addPriceList(c);

		assertEquals("Number pricinga bi trebao da se uveca za 1", originalSize + 1, cp.getPriceLists().size());
		assertNotNull("The price list must receive an ID", c.getId());
	}

	@Test
	public void testFindPriceList() {
		ArrayList<PriceListItem> stavke = new ArrayList<>();
		PriceList c = new PriceList(LocalDate.now(), LocalDate.now().plusMonths(1), stavke, 10000.0, 0.1, 0.15, 0.2, 500.0, new HashMap<>());
		cp.addPriceList(c);

		PriceList found = cp.findPriceList(c.getId());
		assertNotNull("PriceList must be found", found);
		assertEquals("The found ID must match", c.getId(), found.getId());

		PriceList missing = cp.findPriceList(-999);
		assertNull("Trazenje nepostojeceg ID-ja treba returnti null", missing);
	}

	@Test
	public void testFindVazeciPriceList() {
		ArrayList<PriceListItem> stavke = new ArrayList<>();

		PriceList activePriceList = new PriceList(LocalDate.now().minusDays(10), LocalDate.now().plusDays(10), stavke, 1000.0, 0.0, 0.0, 0.0, 100.0, new HashMap<>());
		PriceList futurePriceList = new PriceList(LocalDate.now().plusDays(20), LocalDate.now().plusDays(30), stavke, 2000.0, 0.0, 0.0, 0.0, 100.0, new HashMap<>());

		cp.addPriceList(activePriceList);
		cp.addPriceList(futurePriceList);

		PriceList effectiveToday = cp.findVazeciPriceList(LocalDate.now());
		assertNotNull("A price list must be effective today", effectiveToday);
		assertEquals("The first price list must be effective today", activePriceList.getId(), effectiveToday.getId());

		PriceList effectiveLater = cp.findVazeciPriceList(LocalDate.now().plusDays(25));
		assertNotNull("A future price list must exist", effectiveLater);
		assertEquals("The future price list must be found", futurePriceList.getId(), effectiveLater.getId());

		PriceList missingDate = cp.findVazeciPriceList(LocalDate.now().plusDays(50));
		assertNull("There is no pricing za dati date", missingDate);
	}

	@Test
	public void testLoadajUpisi() throws IOException {
		PriceListRepository productionRepository = new PriceListRepository();

		ArrayList<PriceListItem> stavke = new ArrayList<>();
		PriceListItem st = new PriceListItem(1, VehicleCategory.ECONOMY, 3000.0);
		stavke.add(st);

		Map<Integer, Double> ceneService = new HashMap<>();
		ceneService.put(1, 500.0);

		PriceList c = new PriceList(1, LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31), stavke, 20000.0, 0.1, 0.2, 0.3, 1500.0, ceneService);
		productionRepository.getPriceLists().add(c);

		productionRepository.write(testPutanjaPriceLista, testPutanjaStavki, testPutanjaService);

		PriceListRepository loadedRepository = new PriceListRepository();
		loadedRepository.load(testPutanjaPriceLista, testPutanjaStavki, testPutanjaService);

		assertEquals("The correct number of price lists must be loaded", 1, loadedRepository.getPriceLists().size());
		PriceList loaded = loadedRepository.getPriceLists().get(0);
		assertEquals("Subscription must be ista", 20000.0, loaded.getSubscriptionPrice(), 0.01);
		assertEquals("Number stavki must be isti", 1, loaded.getStavkePriceLista().size());
		assertEquals("Price economy vehicles must be ista", 3000.0, loaded.getStavkePriceLista().get(0).getDailyPrice(), 0.01);
		assertEquals("Price dodatne services must be loaded", 500.0, loaded.getExtraServicePrices().get(1), 0.01);
	}
}
