package test.podaci;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import podaci.DodatneUslugePodaci;
import rezervacija.DodatnaUsluga;

public class DodatneUslugePodaciTest {

	private DodatneUslugePodaci dup;
	private String testPutanja = "test_dodatne_usluge.csv";

	@Before
	public void setUp() {
		dup = new DodatneUslugePodaci() {
			@Override
			public void upisi(String p) throws IOException {
				// Ne menjamo fajlove u memorijskim testovima
			}
		};
	}

	@After
	public void tearDown() {
		new File(testPutanja).delete();
	}

	@Test
	public void testDodajUslugu() {
		int staraVelicina = dup.getUsluge().size();
		DodatnaUsluga du = new DodatnaUsluga("Krovni kofer");
		dup.dodajUslugu(du);
		
		assertEquals("Lista usluga se mora povecati za 1", staraVelicina + 1, dup.getUsluge().size());
		assertTrue("ID mora biti generisan (veci od 0)", du.getId() > 0);
	}

	@Test
	public void testObrisiUslugu() {
		DodatnaUsluga du = new DodatnaUsluga("GPS Navigacija");
		dup.dodajUslugu(du);
		int id = du.getId();
		
		assertNotNull("Usluga mora postojati pre brisanja", dup.pronadjiUslugu(id));
		
		dup.obrisiUslugu(du);
		assertNull("Usluga mora biti null nakon brisanja", dup.pronadjiUslugu(id));
	}

	@Test
	public void testPronadjiUslugu() {
		DodatnaUsluga du = new DodatnaUsluga("Decije sediste");
		dup.dodajUslugu(du);
		
		DodatnaUsluga pronadjena = dup.pronadjiUslugu(du.getId());
		assertNotNull(pronadjena);
		assertEquals("Naziv usluge se mora poklapati", "Decije sediste", pronadjena.getDodatnaUsluga());
	}

	@Test
	public void testUcitajUpisi() throws IOException {
		DodatneUslugePodaci produkcioniDup = new DodatneUslugePodaci();
		produkcioniDup.getUsluge().add(new DodatnaUsluga(1, "Zimske gume"));
		produkcioniDup.getUsluge().add(new DodatnaUsluga(2, "Lanac za sneg"));
		
		produkcioniDup.upisi(testPutanja);
		
		DodatneUslugePodaci ucitaniDup = new DodatneUslugePodaci();
		ucitaniDup.ucitaj(testPutanja);
		
		assertEquals("Mora biti ucitano 2 usluge", 2, ucitaniDup.getUsluge().size());
		assertEquals("Prva usluga mora biti Zimske gume", "Zimske gume", ucitaniDup.getUsluge().get(0).getDodatnaUsluga());
	}
}
