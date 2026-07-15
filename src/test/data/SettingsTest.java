package test.data;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import data.Settings;

public class SettingsTest {

	@Test
	public void testDefaultVrownost() {
		Settings p3 = new Settings();
		assertTrue("The initial default value must be greater than zero", p3.getDefaultRentalDuration() > 0);
	}

	@Test
	public void testSetAndRestore() {
		Settings p = new Settings();
		p.load();
		int original = p.getDefaultRentalDuration();

		p.setPodrazumevanoDurationNajma(15);
		assertEquals("Duration must be promenjeno", 15, p.getDefaultRentalDuration());

		p.setPodrazumevanoDurationNajma(original);
	}
}
