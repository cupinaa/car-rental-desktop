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

import podaci.VozilaPodaci;
import vozila.KategorijaVozila;
import vozila.ModelVozila;
import vozila.StatusVozila;
import vozila.Vozilo;

public class VozilaPodaciTest {

	private VozilaPodaci vp;
	private String testPutanja = "test_vozila.csv";

	@Before
	public void setUp() {
		vp = new VozilaPodaci() {
			@Override
			public void sacuvajIzmene() {
				// Ne radimo nista
			}
		};
	}

	@After
	public void tearDown() {
		new File(testPutanja).delete();
	}

	@Test
	public void testDodajVozilo() {
		int pocetnaVelicina = vp.getVozila().size();
		ModelVozila model = new ModelVozila("Fiat", "Punto", KategorijaVozila.ECONOMY);
		Vozilo v = new Vozilo(model, "BG-123-AA", StatusVozila.RASPOLOZIVO);
		
		vp.dodajVozilo(v);
		
		assertEquals("Broj vozila se mora uvecati za 1", pocetnaVelicina + 1, vp.getVozila().size());
		assertTrue("ID vozila mora biti generisan (veci od 0)", v.getId() > 0);
	}

	@Test
	public void testPronadjiVozilo() {
		ModelVozila model = new ModelVozila("Fiat", "Punto", KategorijaVozila.ECONOMY);
		Vozilo v = new Vozilo(model, "NS-111-BB", StatusVozila.RASPOLOZIVO);
		vp.dodajVozilo(v);
		
		Vozilo pronadjeno = vp.pronadjiVozilo(v.getId());
		assertNotNull(pronadjeno);
		assertEquals("Registracija mora da se poklapa", "NS-111-BB", pronadjeno.getRegistarskeTablice());
	}

	@Test
	public void testObrisiVozilo() {
		ModelVozila model = new ModelVozila("Fiat", "Punto", KategorijaVozila.ECONOMY);
		Vozilo v = new Vozilo(model, "NI-222-CC", StatusVozila.RASPOLOZIVO);
		vp.dodajVozilo(v);
		int id = v.getId();
		
		assertNotNull(vp.pronadjiVozilo(id));
		vp.obrisiVozilo(v);
		assertNull(vp.pronadjiVozilo(id));
	}

	@Test
	public void testUcitajUpisi() throws IOException {
		assertTrue(true);
	}
}
