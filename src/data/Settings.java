package data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Settings {

	protected int defaultRentalDuration = 3;


	public void load() {
		try {
			BufferedReader br = new BufferedReader(new FileReader("settings.txt"));
			String linija = br.readLine();
			if (linija != null && !linija.trim().isEmpty()) {
				defaultRentalDuration = Integer.parseInt(linija.trim());
			}
			br.close();
		} catch (Exception e) {
			save();
		}
	}

	public void save() {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter("settings.txt"));
			pw.println(defaultRentalDuration);
			pw.close();
		} catch (Exception e) {
			System.out.println("Error pri cuvanju settings.");
		}
	}

	public int getDefaultRentalDuration() {
		return defaultRentalDuration;
	}

	public void setPodrazumevanoDurationNajma(int defaultRentalDuration) {
		this.defaultRentalDuration = defaultRentalDuration;
		save();
	}
}
