package test.podaci;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cenovnik.Cenovnik;
import cenovnik.StavkaCenovnika;
import podaci.CenovniciPodaci;
import vozila.KategorijaVozila;

public class CenovniciPodaciTest {

	private CenovniciPodaci cp;
	private String testPutanjaCenovnika = "test_cenovnici.csv";
	private String testPutanjaStavki = "test_stavke.csv";
	private String testPutanjaUsluga = "test_usluge.csv";

	@Before
	public void setUp() {
		cp = new CenovniciPodaci() {
			@Override
			public void sacuvajIzmene() {
				// Ne radimo nista da ne menjamo produkcione fajlove
			}
		};
	}

	@After
	public void tearDown() {
		new File(testPutanjaCenovnika).delete();
		new File(testPutanjaStavki).delete();
		new File(testPutanjaUsluga).delete();
	}

	@Test
	public void testDodajCenovnik() {
		ArrayList<StavkaCenovnika> stavke = new ArrayList<>();
		stavke.add(new StavkaCenovnika(KategorijaVozila.ECONOMY, 2000.0));
		Cenovnik c = new Cenovnik(LocalDate.now(), LocalDate.now().plusMonths(1), stavke, 10000.0, 0.1, 0.15, 0.2, 500.0, new java.util.HashMap<>());
		
		int staraVelicina = cp.getCenovnici().size();
		cp.dodajCenovnik(c);
		
		assertEquals("Broj cenovnika bi trebao da se uveca za 1", staraVelicina + 1, cp.getCenovnici().size());
		assertNotNull("Cenovnik mora dobiti ID", c.getId());
	}

	@Test
	public void testPronadjiCenovnik() {
		ArrayList<StavkaCenovnika> stavke = new ArrayList<>();
		Cenovnik c = new Cenovnik(LocalDate.now(), LocalDate.now().plusMonths(1), stavke, 10000.0, 0.1, 0.15, 0.2, 500.0, new java.util.HashMap<>());
		cp.dodajCenovnik(c);
		
		Cenovnik pronadjeni = cp.pronadjiCenovnik(c.getId());
		assertNotNull("Cenovnik mora biti pronadjen", pronadjeni);
		assertEquals("ID pronadjenog se mora poklapati", c.getId(), pronadjeni.getId());
		
		Cenovnik nepostojeci = cp.pronadjiCenovnik(-999);
		assertNull("Trazenje nepostojeceg ID-ja treba vratiti null", nepostojeci);
	}

	@Test
	public void testPronadjiVazeciCenovnik() {
		ArrayList<StavkaCenovnika> stavke = new ArrayList<>();
		
		Cenovnik aktivan = new Cenovnik(LocalDate.now().minusDays(10), LocalDate.now().plusDays(10), stavke, 1000.0, 0.0, 0.0, 0.0, 100.0, new java.util.HashMap<>());
		Cenovnik buduci = new Cenovnik(LocalDate.now().plusDays(20), LocalDate.now().plusDays(30), stavke, 2000.0, 0.0, 0.0, 0.0, 100.0, new java.util.HashMap<>());
		
		cp.dodajCenovnik(aktivan);
		cp.dodajCenovnik(buduci);
		
		Cenovnik vazeciDanas = cp.pronadjiVazeciCenovnik(LocalDate.now());
		assertNotNull("Danasnji cenovnik mora postojati", vazeciDanas);
		assertEquals("Danas mora biti pronadjen prvi cenovnik", aktivan.getId(), vazeciDanas.getId());
		
		Cenovnik vazeciUbuducnosti = cp.pronadjiVazeciCenovnik(LocalDate.now().plusDays(25));
		assertNotNull("Cenovnik u buducnosti mora postojati", vazeciUbuducnosti);
		assertEquals("Mora biti pronadjen buduci cenovnik", buduci.getId(), vazeciUbuducnosti.getId());
		
		Cenovnik nepostojeciDatum = cp.pronadjiVazeciCenovnik(LocalDate.now().plusDays(50));
		assertNull("Ne postoji cenovnik za dati datum", nepostojeciDatum);
	}

	@Test
	public void testUcitajUpisi() throws IOException {
		CenovniciPodaci produkcioniCp = new CenovniciPodaci();
		
		ArrayList<StavkaCenovnika> stavke = new ArrayList<>();
		StavkaCenovnika st = new StavkaCenovnika(1, KategorijaVozila.ECONOMY, 3000.0);
		stavke.add(st);
		
		java.util.Map<Integer, Double> ceneUsluga = new java.util.HashMap<>();
		ceneUsluga.put(1, 500.0);
		
		Cenovnik c = new Cenovnik(1, LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31), stavke, 20000.0, 0.1, 0.2, 0.3, 1500.0, ceneUsluga);
		produkcioniCp.getCenovnici().add(c);
		
		produkcioniCp.upisi(testPutanjaCenovnika, testPutanjaStavki, testPutanjaUsluga);
		
		CenovniciPodaci ucitaniCp = new CenovniciPodaci();
		ucitaniCp.ucitaj(testPutanjaCenovnika, testPutanjaStavki, testPutanjaUsluga);
		
		assertEquals("Mora da ucitana ispravan broj cenovnika", 1, ucitaniCp.getCenovnici().size());
		Cenovnik ucitani = ucitaniCp.getCenovnici().get(0);
		assertEquals("Pretplata mora biti ista", 20000.0, ucitani.getCenaPretplate(), 0.01);
		assertEquals("Broj stavki mora biti isti", 1, ucitani.getStavkeCenovnika().size());
		assertEquals("Cena economy vozila mora biti ista", 3000.0, ucitani.getStavkeCenovnika().get(0).getCenaPoDanu(), 0.01);
		assertEquals("Cena dodatne usluge mora biti ucitana", 500.0, ucitani.getCeneDodatnihUsluga().get(1), 0.01);
	}
}
