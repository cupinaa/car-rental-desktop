package podaci;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Podesavanja {
	
	protected int podrazumevanoTrajanjeNajma = 3;
	
	
	public void ucitaj() {
		try {
			BufferedReader br = new BufferedReader(new FileReader("podesavanja.txt"));
			String linija = br.readLine();
			if (linija != null && !linija.trim().isEmpty()) {
				podrazumevanoTrajanjeNajma = Integer.parseInt(linija.trim());
			}
			br.close();
		} catch (Exception e) {
			sacuvaj();
		}
	}

	public void sacuvaj() {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter("podesavanja.txt"));
			pw.println(podrazumevanoTrajanjeNajma);
			pw.close();
		} catch (Exception e) {
			System.out.println("Greska pri cuvanju podesavanja.");
		}
	}

	public int getPodrazumevanoTrajanjeNajma() {
		return podrazumevanoTrajanjeNajma;
	}

	public void setPodrazumevanoTrajanjeNajma(int podrazumevanoTrajanjeNajma) {
		this.podrazumevanoTrajanjeNajma = podrazumevanoTrajanjeNajma;
		sacuvaj();
	}
}
