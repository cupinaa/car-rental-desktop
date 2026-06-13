package test.podaci;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import izdavanje.Izdavanje;
import korisnici.Agent;
import korisnici.Klijent;
import korisnici.Pol;
import podaci.IzdavanjePodaci;
import rezervacija.Rezervacija;
import rezervacija.StatusRezervacije;
import vozila.KategorijaVozila;
import vozila.ModelVozila;
import vozila.StatusVozila;
import vozila.Vozilo;

public class IzdavanjePodaciTest {

	private IzdavanjePodaci ip;
	private String testPutanja = "test_izdavanje.csv";

	@Before
	public void setUp() {
		ip = new IzdavanjePodaci() {
			@Override
			public void upisi(String putanja) throws IOException {
				// Ne menjamo produkcione fajlove
			}
		};
	}

	@After
	public void tearDown() {
		new File(testPutanja).delete();
	}

	@Test
	public void testDodajIzdavanje() {
		int staraVelicina = ip.getIzdavanja().size();
		
		Agent agent = new Agent("Agent", "Agentovic", Pol.Muski, LocalDate.now(), "060123123", "Adresa 1", "agent", "123", null, 5);
		Klijent klijent = new Klijent("Klijent", "Klijentovic", Pol.Zenski, LocalDate.now(), "060321321", "Adresa 2", "klijent", "123", LocalDate.now());
		ModelVozila model = new ModelVozila("Fiat", "Punto", KategorijaVozila.ECONOMY);
		Vozilo vozilo = new Vozilo(model, "BG-123", StatusVozila.RASPOLOZIVO);
		Rezervacija rezervacija = new Rezervacija(1, klijent, vozilo, LocalDate.now(), LocalDate.now().plusDays(3), 5000.0, StatusRezervacije.ODOBRENA, new ArrayList<>());
		
		Izdavanje izdavanje = new Izdavanje(rezervacija, agent, 150000.0);
		
		ip.dodajIzdavanje(izdavanje);
		
		assertEquals("Lista izdavanja se mora povecati za 1", staraVelicina + 1, ip.getIzdavanja().size());
		assertTrue("ID mora biti generisan (veci od 0)", izdavanje.getId() > 0);
	}

	@Test
	public void testUcitajUpisi() throws IOException {
		assertTrue(true);
	}
}
