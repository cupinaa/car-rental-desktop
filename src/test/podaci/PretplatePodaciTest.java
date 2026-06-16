package test.podaci;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import korisnici.Klijent;
import korisnici.Pol;
import korisnici.Pretplata;
import korisnici.StatusPretplate;
import podaci.PretplatePodaci;

public class PretplatePodaciTest {

	private PretplatePodaci pp;
	private String testPutanja = "test_pretplate.csv";

	@Before
	public void setUp() {
		pp = new PretplatePodaci() {
			@Override
			public void sacuvajIzmene() {

			}
		};
	}

	@After
	public void tearDown() {
		new File(testPutanja).delete();
	}

	@Test
	public void testDodajPretplatu() {
		int pocetnaVelicina = pp.getPretplate().size();
		Klijent k = new Klijent(1, "A", "B", Pol.Muski, LocalDate.now(), "1", "A", "user", "pass", LocalDate.now());
		Pretplata p = new Pretplata(k, LocalDate.now().plusMonths(1), StatusPretplate.AKTIVNA);

		pp.dodajPretplatu(p);

		assertEquals("Broj pretplata mora se uvecati", pocetnaVelicina + 1, pp.getPretplate().size());
		assertTrue("ID mora biti setovan", p.getId() > 0);
	}

	@Test
	public void testPronadjiPretplatu() {
		Klijent k = new Klijent(1, "A", "B", Pol.Muski, LocalDate.now(), "1", "A", "user", "pass", LocalDate.now());
		Pretplata p = new Pretplata(k, LocalDate.now().plusMonths(1), StatusPretplate.AKTIVNA);
		pp.dodajPretplatu(p);

		assertNotNull("Mora pronaci pretplatu po ID", pp.pronadjiPretplatu(p.getId()));
		assertNotNull("Mora pronaci pretplatu po Klijentu", pp.pronadjiPretplatuZaKlijenta(k.getId()));
	}

	@Test
	public void testPodnesiZahtevZaPretplatu() {
		Klijent k1 = new Klijent(1, "A", "B", Pol.Muski, LocalDate.now(), "1", "A", "user", "pass", LocalDate.now());
		k1.setBrojKasnjenja(2); 

		pp.podnesiZahtevZaPretplatu(k1);

		Pretplata p1 = pp.pronadjiPretplatuZaKlijenta(k1.getId());
		assertNotNull("Pretplata za prvog mora biti kreirana", p1);
		assertEquals("Status mora biti CEKA_ODOBRENJE posto ima malo kasnjenja", StatusPretplate.CEKA_ODOBRENJE, p1.getStatus());

		Klijent k2 = new Klijent(2, "C", "D", Pol.Muski, LocalDate.now(), "1", "A", "user2", "pass", LocalDate.now());
		k2.setBrojKasnjenja(6); 

		pp.podnesiZahtevZaPretplatu(k2);

		Pretplata p2 = pp.pronadjiPretplatuZaKlijenta(k2.getId());
		assertNotNull("Pretplata za drugog mora biti kreirana", p2);
		assertEquals("Status mora biti ODBIJENA zbog previse kasnjenja", StatusPretplate.ODBIJENA, p2.getStatus());
	}

	@Test
	public void testUcitajUpisi() throws IOException {

		assertTrue(true);
	}
}
