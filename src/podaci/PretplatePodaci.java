package podaci;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;

import korisnici.Klijent;
import korisnici.Pretplata;
import korisnici.StatusPretplate;

public class PretplatePodaci {

	protected ArrayList<Pretplata> pretplate = new ArrayList<>();

	public void ucitaj(String putanja, KorisniciPodaci kp) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(putanja));
		String linija;
		while ((linija = br.readLine()) != null) {
			String[] delovi = linija.split("\\|");

			int id = Integer.parseInt(delovi[0]);
			int idKlijenta = Integer.parseInt(delovi[1]);
			LocalDate datumIsteka = LocalDate.parse(delovi[2]);
			StatusPretplate status = StatusPretplate.valueOf(delovi[3]);

			Klijent klijent = kp.pronadjiKlijenta(idKlijenta);

			if (klijent != null) {
				Pretplata p = new Pretplata(id, klijent, datumIsteka, status);
				pretplate.add(p);
			}
		}
		br.close();
	}

	public void upisi(String putanja) throws IOException {
		PrintWriter pw = new PrintWriter(new FileWriter(putanja));
		for (Pretplata p : pretplate) {
			pw.println(p.getId() + "|" + p.getKlijent().getId() + "|" + p.getDatumIsteka() + "|" + p.getStatus());
		}
		pw.close();
	}

	public Pretplata pronadjiPretplatu(int id) {
		for (Pretplata p : pretplate) {
			if (p.getId() == id) {
				return p;
			}
		}
		return null;
	}
	
	public Pretplata pronadjiPretplatuZaKlijenta(int idKlijenta) {
		for (Pretplata p : pretplate) {
			if (p.getKlijent().getId() == idKlijenta) {
				return p;
			}
		}
		return null;
	}

	private int generisiNoviId() {
		int maxId = 0;
		for (Pretplata p : pretplate) {
			if (p.getId() > maxId) {
				maxId = p.getId();
			}
		}
		return maxId + 1;
	}

	public void dodajPretplatu(Pretplata p) {
		p.setId(generisiNoviId());
		pretplate.add(p);
		sacuvajIzmene();
	}

	public void podnesiZahtevZaPretplatu(Klijent k) {
		Pretplata postojeca = pronadjiPretplatuZaKlijenta(k.getId());
		
		if (k.getBrojKasnjenja() > 5) {
			if (postojeca != null) {
				postojeca.setStatus(StatusPretplate.ODBIJENA);
			} else {
				Pretplata nova = new Pretplata(k, LocalDate.now(), StatusPretplate.ODBIJENA);
				this.dodajPretplatu(nova);
			}
			System.out.println("Zahtev ODBIJEN zbog previše kašnjenja (" + k.getBrojKasnjenja() + ").");
		} else {
			if (postojeca != null) {
				postojeca.setStatus(StatusPretplate.CEKA_ODOBRENJE);
			} else {
				Pretplata nova = new Pretplata(k, LocalDate.now(), StatusPretplate.CEKA_ODOBRENJE);
				this.dodajPretplatu(nova);
			}
			System.out.println("Zahtev poslat! Čeka se agent.");
		}
		
		sacuvajIzmene();
	}

	public void sacuvajIzmene() {
		try {
			upisi("pretplate.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Pretplata> getPretplate() {
		return pretplate;
	}
}
