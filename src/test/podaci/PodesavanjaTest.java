package test.podaci;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import podaci.Podesavanja;

public class PodesavanjaTest {

	@Test
	public void testDefaultVrednost() {
		Podesavanja p3 = new Podesavanja();
		assertTrue("Mora postojati neka podrazumevana vrednost veca od nule na pocetku", p3.getPodrazumevanoTrajanjeNajma() > 0);
	}

	@Test
	public void testSetAndRestore() {
		Podesavanja p = new Podesavanja();
		p.ucitaj();
		int original = p.getPodrazumevanoTrajanjeNajma();

		p.setPodrazumevanoTrajanjeNajma(15);
		assertEquals("Trajanje mora biti promenjeno", 15, p.getPodrazumevanoTrajanjeNajma());

		p.setPodrazumevanoTrajanjeNajma(original);
	}
}
