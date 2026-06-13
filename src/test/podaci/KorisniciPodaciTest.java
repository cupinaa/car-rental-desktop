package test.podaci;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import korisnici.Klijent;
import korisnici.Pol;
import podaci.KorisniciPodaci;

public class KorisniciPodaciTest {

	private KorisniciPodaci kp;
	private String testPutanja = "test_korisnici.csv";

	@Before
	public void setUp() {
		kp = new KorisniciPodaci() {
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
	public void testDodajKorisnika() {
		int pocetnaVelicina = kp.getKorisnici().size();
		
		Klijent k = new Klijent("Pera", "Peric", Pol.Muski, LocalDate.now(), "060", "Adresa", "pera123", "pass", LocalDate.now());
		kp.dodajKorisnika(k);
		
		assertEquals("Broj korisnika se mora uvecati za 1", pocetnaVelicina + 1, kp.getKorisnici().size());
		assertTrue("Korisnik mora dobiti pozitivan ID", k.getId() > 0);
	}

	@Test
	public void testObrisiKorisnika() {
		Klijent k = new Klijent("Pera", "Peric", Pol.Muski, LocalDate.now(), "060", "Adresa", "pera123", "pass", LocalDate.now());
		kp.dodajKorisnika(k);
		int id = k.getId();
		
		assertNotNull("Korisnik mora postojati pre brisanja", kp.pronadjiKlijenta(id));
		
		kp.obrisiKorisnika(k);
		assertNull("Korisnik mora biti null nakon brisanja", kp.pronadjiKlijenta(id));
	}

	@Test
	public void testPronadjiKorisnikaPoId() {
		Klijent k = new Klijent("Mika", "Mikic", Pol.Muski, LocalDate.now(), "060", "Adresa", "mika123", "pass", LocalDate.now());
		kp.dodajKorisnika(k);
		
		Klijent pronadjen = kp.pronadjiKlijenta(k.getId());
		assertNotNull(pronadjen);
		assertEquals("Imena se moraju poklapati", "Mika", pronadjen.getIme());
	}

	@Test
	public void testLogin() {
		Klijent k = new Klijent("Ana", "Anic", Pol.Zenski, LocalDate.now(), "060", "Adresa", "ana123", "mojasifra", LocalDate.now());
		kp.dodajKorisnika(k);
		
		assertNotNull("Validan login mora vratiti korisnika", kp.login("ana123", "mojasifra"));
		assertNull("Pogresna sifra mora vratiti null", kp.login("ana123", "pogresna"));
		assertNull("Pogresan username mora vratiti null", kp.login("nepostojeci", "mojasifra"));
	}

	@Test
	public void testUcitajUpisi() throws IOException {
		KorisniciPodaci produkcioniKp = new KorisniciPodaci();
		produkcioniKp.getKorisnici().add(new Klijent(1, "Ime", "Prezime", Pol.Muski, LocalDate.now(), "060", "Adresa", "username", "pass", LocalDate.now()));
		produkcioniKp.upisi(testPutanja);
		
		KorisniciPodaci ucitaniKp = new KorisniciPodaci();
		ucitaniKp.ucitaj(testPutanja);
		
		assertEquals("Mora biti ucitano 1 korisnik", 1, ucitaniKp.getKorisnici().size());
		assertEquals("Username mora biti tacan", "username", ucitaniKp.getKorisnici().get(0).getKorisnickoIme());
	}
}
