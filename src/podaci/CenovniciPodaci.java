package podaci;
import java.util.HashMap;
import java.util.Map;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;

import cenovnik.Cenovnik;
import cenovnik.StavkaCenovnika;
import vozila.KategorijaVozila;

public class CenovniciPodaci {

	protected ArrayList<Cenovnik> cenovnici = new ArrayList<>();

	public void ucitaj(String putCenovnici, String putStavke, String putStavkeUsluga) throws IOException {
		BufferedReader brC = new BufferedReader(new FileReader(putCenovnici));
		String linija;
		while ((linija = brC.readLine()) != null) {
			String[] delovi = linija.split("\\|");
			int id = Integer.parseInt(delovi[0]);
			LocalDate pocetak = LocalDate.parse(delovi[1]);
			LocalDate kraj = LocalDate.parse(delovi[2]);

			Cenovnik c = null;
			if (delovi.length > 3) {
				double cenaPretplate = Double.parseDouble(delovi[3]);
				double popustStudent = Double.parseDouble(delovi[4]);
				double popustFirma = Double.parseDouble(delovi[5]);
				double popustPenzioner = Double.parseDouble(delovi[6]);
				double iznosKazne = Double.parseDouble(delovi[7]);
				c = new Cenovnik(id, pocetak, kraj, new ArrayList<StavkaCenovnika>(), cenaPretplate, popustStudent, popustFirma, popustPenzioner, iznosKazne, new HashMap<>());
			} else {
				c = new Cenovnik(id, pocetak, kraj, new ArrayList<StavkaCenovnika>(), 5000.0, 0.10, 0.25, 0.15, 2000.0, new HashMap<>());
			}
			cenovnici.add(c);
		}
		brC.close();

		BufferedReader brS = new BufferedReader(new FileReader(putStavke));
		while ((linija = brS.readLine()) != null) {
			String[] delovi = linija.split("\\|");
			int idStavke = Integer.parseInt(delovi[0]);
			int idCenovnika = Integer.parseInt(delovi[1]);
			KategorijaVozila kategorija = KategorijaVozila.valueOf(delovi[2]);
			double cena = Double.parseDouble(delovi[3]);

			StavkaCenovnika stavka = new StavkaCenovnika(idStavke, kategorija, cena);

			Cenovnik c = pronadjiCenovnik(idCenovnika);
			if (c != null) {
				c.getStavkeCenovnika().add(stavka);
			}
		}
		brS.close();

		try {
			BufferedReader brU = new BufferedReader(new FileReader(putStavkeUsluga));
			while ((linija = brU.readLine()) != null) {
				String[] delovi = linija.split("\\|");
				int idCenovnika = Integer.parseInt(delovi[0]);
				int idUsluge = Integer.parseInt(delovi[1]);
				double cena = Double.parseDouble(delovi[2]);

				Cenovnik c = pronadjiCenovnik(idCenovnika);
				if (c != null) {
					c.getCeneDodatnihUsluga().put(idUsluge, cena);
				}
			}
			brU.close();
		} catch (Exception e) {

		}
	}

	public void upisi(String putCenovnici, String putStavke, String putStavkeUsluga) throws IOException {
		PrintWriter pwC = new PrintWriter(new FileWriter(putCenovnici));
		PrintWriter pwS = new PrintWriter(new FileWriter(putStavke));
		PrintWriter pwU = new PrintWriter(new FileWriter(putStavkeUsluga));

		for (Cenovnik c : cenovnici) {
			pwC.println(c.getId() + "|" + c.getPocetakVazenja() + "|" + c.getKrajVazenja() + "|" + 
						c.getCenaPretplate() + "|" + c.getPopustStudent() + "|" + 
						c.getPopustFirma() + "|" + c.getPopustPenzioner() + "|" + c.getIznosKazne());
			for (StavkaCenovnika s : c.getStavkeCenovnika()) {
				pwS.println(s.getId() + "|" + c.getId() + "|" + s.getKategorijaVozila() + "|" + s.getCenaPoDanu());
			}
			if (c.getCeneDodatnihUsluga() != null) {
				for (Map.Entry<Integer, Double> entry : c.getCeneDodatnihUsluga().entrySet()) {
					pwU.println(c.getId() + "|" + entry.getKey() + "|" + entry.getValue());
				}
			}
		}
		pwC.close();
		pwS.close();
		pwU.close();
	}

	public Cenovnik pronadjiCenovnik(int id) {
		for (Cenovnik c : cenovnici) {
			if (c.getId() == id)
				return c;
		}
		return null;
	}

	public Cenovnik pronadjiVazeciCenovnik(LocalDate datum) {
		for (Cenovnik c : cenovnici) {
			if ((c.getPocetakVazenja().isBefore(datum) || c.getPocetakVazenja().isEqual(datum))
					&& (c.getKrajVazenja().isAfter(datum) || c.getKrajVazenja().isEqual(datum))) {
				return c;
			}
		}
		return null;

	}

	private int generisiNoviId() {
		int maxId = 0;
		for (Cenovnik c : cenovnici) {
			if (c.getId() > maxId) {
				maxId = c.getId();
			}
		}
		return maxId + 1;
	}

	private int generisiNoviIdStavkeGlobal() {
		int maxId = 0;
		for (Cenovnik c : cenovnici) {
			for (StavkaCenovnika sc : c.getStavkeCenovnika()) {
				if (sc.getId() > maxId) {
					maxId = sc.getId();
				}
			}
		}
		return maxId + 1;
	}

	public void dodajCenovnik(Cenovnik c) {
		c.setId(generisiNoviId());

		int globalStavkaId = generisiNoviIdStavkeGlobal();
		for(StavkaCenovnika sc : c.getStavkeCenovnika()) {
			sc.setId(globalStavkaId++);
		}

		cenovnici.add(c);
		sacuvajIzmene();
	}

	public void obrisiCenovnik(Cenovnik c) {
		cenovnici.remove(c);
		sacuvajIzmene();
	}

	public void sacuvajIzmene() {
		try {
			upisi("cenovnici.csv", "stavke_cenovnika.csv", "stavke_usluga_cenovnika.csv");
		} catch (IOException e) {
			System.out.println("Greška pri čuvanju cenovnika: " + e.getMessage());
		}
	}

	public ArrayList<Cenovnik> getCenovnici() {
		return cenovnici;
	}
}