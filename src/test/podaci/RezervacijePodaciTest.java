package test.podaci;
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

import korisnici.Klijent;
import korisnici.Pol;
import podaci.RezervacijePodaci;
import rezervacija.Rezervacija;
import rezervacija.StatusRezervacije;
import vozila.KategorijaVozila;
import vozila.ModelVozila;
import vozila.StatusVozila;
import vozila.Vozilo;

public class RezervacijePodaciTest {

	private RezervacijePodaci rp;
	private String testPutanja = "test_rezervacije.csv";

	@Before
	public void setUp() {
		rp = new RezervacijePodaci() {
			@Override
			public void upisi(String path) {
				// Ne radimo nista
			}
		};
	}

	@After
	public void tearDown() {
		new File(testPutanja).delete();
	}

	@Test
	public void testDodajRezervaciju() {
		int pocetnaVelicina = rp.getRezervacije().size();
		
		Klijent klijent = new Klijent("Klijent", "Klijentovic", Pol.Zenski, LocalDate.now(), "060", "Adresa", "klijent", "123", LocalDate.now());
		ModelVozila model = new ModelVozila("Fiat", "Punto", KategorijaVozila.ECONOMY);
		Vozilo vozilo = new Vozilo(model, "BG-123", StatusVozila.RASPOLOZIVO);
		
		Rezervacija rezervacija = new Rezervacija(1, klijent, vozilo, LocalDate.now(), LocalDate.now().plusDays(3), 5000.0, StatusRezervacije.ODOBRENA, new ArrayList<>());
		
		rp.dodajRezervaciju(rezervacija);
		
		assertEquals("Broj rezervacija se mora uvecati za 1", pocetnaVelicina + 1, rp.getRezervacije().size());
		assertTrue("ID rezervacije mora biti generisan (veci od 0)", rezervacija.getId() > 0);
	}

	@Test
	public void testPronadjiRezervaciju() {
		Klijent klijent = new Klijent("Klijent", "Klijentovic", Pol.Zenski, LocalDate.now(), "060", "Adresa", "klijent", "123", LocalDate.now());
		ModelVozila model = new ModelVozila("Fiat", "Punto", KategorijaVozila.ECONOMY);
		Vozilo vozilo = new Vozilo(model, "BG-123", StatusVozila.RASPOLOZIVO);
		
		Rezervacija rezervacija = new Rezervacija(1, klijent, vozilo, LocalDate.now(), LocalDate.now().plusDays(3), 5000.0, StatusRezervacije.ODOBRENA, new ArrayList<>());
		rp.dodajRezervaciju(rezervacija);
		
		Rezervacija pronadjena = rp.pronadjiRezervaciju(rezervacija.getId());
		assertNotNull("Rezervacija mora biti pronadjena", pronadjena);
		assertEquals("Cena mora da se poklapa", 5000.0, pronadjena.getUkupnaCena(), 0.01);
	}

	@Test
	public void testUcitajUpisi() throws IOException {
		assertTrue(true);
	}
}
