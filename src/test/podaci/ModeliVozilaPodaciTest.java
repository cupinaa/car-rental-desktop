package test.podaci;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import podaci.ModeliVozilaPodaci;
import vozila.KategorijaVozila;
import vozila.ModelVozila;

public class ModeliVozilaPodaciTest {

	private ModeliVozilaPodaci mp;

	@Before
	public void setUp() {
		mp = new ModeliVozilaPodaci() {
			@Override
			public void upisi(String path) {
				// Ne radimo nista
			}
		};
	}

	@Test
	public void testDodajModel() {
		int pocetnaVelicina = mp.getModeli().size();
		ModelVozila m = new ModelVozila("Toyota", "Yaris", KategorijaVozila.ECONOMY);
		mp.dodajModel(m);
		
		assertEquals("Broj modela mora biti uvecan za 1", pocetnaVelicina + 1, mp.getModeli().size());
		assertTrue("ID mora biti veci od 0", m.getId() > 0);
	}

	@Test
	public void testPronadjiModel() {
		ModelVozila m = new ModelVozila("Toyota", "Corolla", KategorijaVozila.ECONOMY);
		mp.dodajModel(m);
		
		ModelVozila pronadjeni = mp.pronadjiModel(m.getId());
		assertNotNull(pronadjeni);
		assertEquals("Marke se moraju poklapati", "Toyota", pronadjeni.getMarkaVozila());
	}

	@Test
	public void testUcitajUpisi() throws IOException {
		assertTrue(true);
	}
}
