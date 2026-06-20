package podaci;

import cenovnik.Cenovnik;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import izdavanje.Izdavanje;
import korisnici.Agent;
import korisnici.Klijent;
import rezervacija.Rezervacija;
import vozila.StatusVozila;

public class IzdavanjePodaci {

	protected ArrayList<Izdavanje> izdavanja = new ArrayList<>();

	public void ucitaj(String putanja, RezervacijePodaci rp, KorisniciPodaci kp) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(putanja));
		String linija;
		while ((linija = br.readLine()) != null) {
			String[] delovi = linija.split("\\|");

			int id = Integer.parseInt(delovi[0]);
			int idRez = Integer.parseInt(delovi[1]);
			int idAgenta = Integer.parseInt(delovi[2]);
			double pocetnaK = Double.parseDouble(delovi[3]);
			double krajnjaK = Double.parseDouble(delovi[4]);

			Rezervacija r = rp.pronadjiRezervaciju(idRez);
			Agent a = kp.pronadjiAgenta(idAgenta);

			if (r != null && a != null) {
				Izdavanje i = new Izdavanje(id, r, a, pocetnaK, krajnjaK);
				izdavanja.add(i);
			}
		}
		br.close();
	}

	public void upisi(String putanja) throws IOException {
		PrintWriter pw = new PrintWriter(new FileWriter(putanja));
		for (Izdavanje i : izdavanja) {
			pw.println(i.getId() + "|" + i.getRezervacija().getId() + "|" + i.getAgent().getId() + "|"
					+ i.getPocetnaKilometraza() + "|" + i.getKrajnjaKilometraza());
		}
		pw.close();
	}

	public void izdajVozilo(Rezervacija r, Agent a, double trenutnaKilometraza) {
		r.getVozilo().setStatusVozila(StatusVozila.IZNAJMLJENO);
		Izdavanje izdavanje = new Izdavanje(r, a, trenutnaKilometraza);
		this.dodajIzdavanje(izdavanje);
	}

	public void vratiVozilo(Izdavanje i, double novaKilometraza, LocalDate datumVracanja, RezervacijePodaci rp,
			Cenovnik aktuelniCenovnik, KorisniciPodaci kp) {
		i.setKrajnjaKilometraza(novaKilometraza);
		i.getRezervacija().getVozilo().setStatusVozila(StatusVozila.RASPOLOZIVO);

		long kasnjenje = ChronoUnit.DAYS.between(i.getRezervacija().getDatumKraja(), datumVracanja);

		if (kasnjenje > 0 && aktuelniCenovnik != null) {
			double kazna = kasnjenje * aktuelniCenovnik.getIznosKazne();
			double staraCena = i.getRezervacija().getUkupnaCena();

			i.getRezervacija().setUkupnaCena(staraCena + kazna);

			Klijent k = i.getRezervacija().getKlijent();
			k.setBrojKasnjenja(k.getBrojKasnjenja() + 1);
			kp.sacuvajIzmene();

			System.out.println("Klijent je kasnio " + kasnjenje + " dana! Dodata je kazna od " + kazna + " din.");
			rp.sacuvajIzmene();
		}

		try {
			upisi("izdavanja.csv");
		} catch (Exception e) {
		}
	}

	private int generisiNoviId() {
		int maxId = 0;
		for (Izdavanje i : izdavanja) {
			if (i.getId() > maxId)
				maxId = i.getId();
		}
		return maxId + 1;
	}

	public void dodajIzdavanje(Izdavanje i) {
		i.setId(generisiNoviId());
		izdavanja.add(i);
		try {
			upisi("izdavanja.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Izdavanje> getIzdavanja() {
		return izdavanja;
	}
}