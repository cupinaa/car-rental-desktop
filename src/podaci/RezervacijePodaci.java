package podaci;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import cenovnik.Cenovnik;
import cenovnik.StavkaCenovnika;
import korisnici.Klijent;
import korisnici.Pretplata;
import korisnici.StatusPretplate;
import rezervacija.DodatnaUsluga;
import rezervacija.Rezervacija;
import rezervacija.StatusRezervacije;
import vozila.KategorijaVozila;
import vozila.ModelVozila;
import vozila.Vozilo;

public class RezervacijePodaci {

	protected ArrayList<Rezervacija> rezervacije = new ArrayList<>();

	public void ucitaj(String putanja, KorisniciPodaci kp, VozilaPodaci vp, DodatneUslugePodaci dup)
			throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(putanja));
		String linija;
		while ((linija = br.readLine()) != null) {
			String[] delovi = linija.split("\\|");

			int id = Integer.parseInt(delovi[0]);
			int idKlijenta = Integer.parseInt(delovi[1]);
			int idVozila = Integer.parseInt(delovi[2]);
			LocalDate datumPocetka = LocalDate.parse(delovi[3]);
			LocalDate datumKraja = LocalDate.parse(delovi[4]);
			double ukupnaCena = Double.parseDouble(delovi[5]);
			StatusRezervacije status = StatusRezervacije.valueOf(delovi[6]);

			Klijent klijent = kp.pronadjiKlijenta(idKlijenta);
			Vozilo vozilo = vp.pronadjiVozilo(idVozila);

			ArrayList<DodatnaUsluga> uslugeRezervacije = new ArrayList<>();
			if (delovi.length > 7 && !delovi[7].isEmpty()) {
				String[] idUsluga = delovi[7].split(",");
				for (String sId : idUsluga) {
					DodatnaUsluga du = dup.pronadjiUslugu(Integer.parseInt(sId));
					if (du != null)
						uslugeRezervacije.add(du);
				}
			}

			Rezervacija r = new Rezervacija(id, klijent, vozilo, datumPocetka, datumKraja, ukupnaCena, status,
					uslugeRezervacije);
			rezervacije.add(r);
		}
		br.close();
		
		boolean izmenjeno = false;
		for (Rezervacija r : rezervacije) {
			if (r.getStatusRezervacije() == StatusRezervacije.values()[0] && 
				!r.getDatumPocetka().isAfter(LocalDate.now())) {
				r.setStatusRezervacije(StatusRezervacije.ODBIJENA);
				izmenjeno = true;
			}
			
			// Ako se klijent nije pojavio do isteka pocetnog datuma
			if (r.getStatusRezervacije() == StatusRezervacije.values()[4] &&
				r.getDatumPocetka().isBefore(LocalDate.now())) {
				r.setStatusRezervacije(StatusRezervacije.OTKAZANA);
				
				// Zabrana od 24h zbog nepojavljivanja
				if (r.getKlijent() != null) {
					r.getKlijent().setZabranaRezervisanjaDo(java.time.LocalDateTime.now().plusHours(24));
				}
				izmenjeno = true;
			}
		}
		
		if (izmenjeno) {
			upisi(putanja);
			kp.sacuvajIzmene(); // Cuvamo izmene nad klijentima (njihove zabrane)
		}
	}

	public void upisi(String putanja) throws IOException {
		PrintWriter pw = new PrintWriter(new FileWriter(putanja));
		for (Rezervacija r : rezervacije) {

			String uslugeStr = "";
			for (int i = 0; i < r.getDodatneUsluge().size(); i++) {
				uslugeStr += r.getDodatneUsluge().get(i).getId();
				if (i < r.getDodatneUsluge().size() - 1)
					uslugeStr += ",";
			}

			pw.println(r.getId() + "|" + r.getKlijent().getId() + "|" + r.getVozilo().getId() + "|"
					+ r.getDatumPocetka() + "|" + r.getDatumKraja() + "|" + r.getUkupnaCena() + "|"
					+ r.getStatusRezervacije() + "|" + uslugeStr);
		}
		pw.close();
	}
	
	public Rezervacija pronadjiRezervaciju(int id) {
	    for (Rezervacija r : rezervacije) {
	        if (r.getId() == id) return r;
	    }
	    return null;
	}

	private int generisiNoviId() {
		int maxId = 0;
		for (Rezervacija r : rezervacije) {
			if (r.getId() > maxId)
				maxId = r.getId();
		}
		return maxId + 1;
	}

	public void dodajRezervaciju(Rezervacija r) {
		r.setId(generisiNoviId());
		rezervacije.add(r);
		try {
			upisi("rezervacije.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public double izracunajUkupnuCenu(Rezervacija r, Cenovnik aktuelni) {

		if (aktuelni == null) {
			return 0.0;
		}

		long brojDana = ChronoUnit.DAYS.between(r.getDatumPocetka(), r.getDatumKraja());
		if (brojDana <= 0) {
			brojDana = 1;
		}



		double cenaPoDanu = 0.0;
		KategorijaVozila katVozila = r.getVozilo().getModelVozila().getKategorija();

		for (StavkaCenovnika s : aktuelni.getStavkeCenovnika()) {
			if (s.getKategorijaVozila() == katVozila) {
				cenaPoDanu = s.getCenaPoDanu();
				break;
			}
		}

		double ukupnaCena = cenaPoDanu * brojDana;

		Podesavanja p = new Podesavanja();
		p.ucitaj();
		long podrazumevano = p.getPodrazumevanoTrajanjeNajma();

		for (DodatnaUsluga du : r.getDodatneUsluge()) {
			String imeUsluge = du.getDodatnaUsluga().toLowerCase();
			double cenaUsluge = 0.0;
			if (aktuelni.getCeneDodatnihUsluga() != null && aktuelni.getCeneDodatnihUsluga().containsKey(du.getId())) {
				cenaUsluge = aktuelni.getCeneDodatnihUsluga().get(du.getId());
			}
			
			if (imeUsluge.contains("produženo") || imeUsluge.contains("produzeno")) {
				long dodatniDani = brojDana - podrazumevano;
				if (dodatniDani > 0) {
					ukupnaCena += (cenaUsluge * dodatniDani);
				}
			} else {
				ukupnaCena += cenaUsluge; 
			}
		}
		
		if (r.getKlijent().getKategorijaKlijenata() != null) {
		    switch (r.getKlijent().getKategorijaKlijenata()) {
		        case STUDENT:
		            ukupnaCena = ukupnaCena * (1.0 - aktuelni.getPopustStudent());
		            break;
		        case FIRMA:
		            ukupnaCena = ukupnaCena * (1.0 - aktuelni.getPopustFirma());
		            break;
		        case PENZIONER:
		            ukupnaCena = ukupnaCena * (1.0 - aktuelni.getPopustPenzioner());
		            break;
		    }
		}

		return ukupnaCena;
	}

	public boolean daLiJeVoziloSlobodno(Vozilo v, LocalDate trazeniPocetak, LocalDate trazeniKraj) {
		for (Rezervacija r : rezervacije) {
			if (r.getVozilo() == null || r.getVozilo().getId() != v.getId()) {
				continue;
			}
			
			if (r.getStatusRezervacije() == StatusRezervacije.OTKAZANA || 
			    r.getStatusRezervacije() == StatusRezervacije.ODBIJENA) {
				continue;
			}
			
			boolean preklapaSe = !trazeniPocetak.isAfter(r.getDatumKraja()) && !trazeniKraj.isBefore(r.getDatumPocetka());
			
			if (preklapaSe) {
				return false; 
			}
		}
		
		return true; 
	}

	public Vozilo pronadjiSlobodnoVoziloZaModel(ModelVozila model, VozilaPodaci vp, LocalDate datumPocetka, LocalDate datumKraja) {
		for (Vozilo v : vp.getVozila()) {
			if (v.getModelVozila().getId() == model.getId()) {
				if (this.daLiJeVoziloSlobodno(v, datumPocetka, datumKraja)) {
					return v; 
				}
			}
		}
		return null; 
	}

	public Rezervacija napraviRezervaciju(Klijent k, Vozilo v, LocalDate datumPocetka, LocalDate datumKraja,
			ArrayList<DodatnaUsluga> dodatneUsluge, Cenovnik aktuelniCenovnik, PretplatePodaci pretplatePodaci) {

		Pretplata pretplata = pretplatePodaci.pronadjiPretplatuZaKlijenta(k.getId());

		if (pretplata == null || pretplata.getStatus() != StatusPretplate.AKTIVNA
				|| pretplata.getDatumIsteka().isBefore(LocalDate.now())) {
			System.out.println("Žao nam je, nemate važeću pretplatu! Podnesite zahtev.");
			return null;
		}

		if (!k.imaPravoNaRezervaciju()) {
			System.out.println("Klijent nema pravo na rezervaciju (vozačka kraće od 2 godine).");
			return null;
		}

		if (this.daLiJeVoziloSlobodno(v, datumPocetka, datumKraja)) {

			Rezervacija r = new Rezervacija(k, v, datumPocetka, datumKraja, dodatneUsluge);

			double izracunataCena = this.izracunajUkupnuCenu(r, aktuelniCenovnik);
			r.setUkupnaCena(izracunataCena);

			this.dodajRezervaciju(r);

			System.out.println("Uspesno kreirana rezervacija! Cena: " + izracunataCena);
			return r;

		} else {
			System.out.println("Vozilo je nažalost zauzeto u tom periodu.");
			return null;
		}
	}

	public double izracunajPrihode(LocalDate odDatuma, LocalDate doDatuma) {
		double ukupniPrihodi = 0;
		for (Rezervacija r : rezervacije) {
			if (r.getStatusRezervacije() != StatusRezervacije.OTKAZANA && 
				r.getStatusRezervacije() != StatusRezervacije.ODBIJENA) {
				
				if (!r.getDatumPocetka().isBefore(odDatuma) && !r.getDatumPocetka().isAfter(doDatuma)) {
					ukupniPrihodi += r.getUkupnaCena();
				}
			}
		}
		return ukupniPrihodi;
	}

	public ArrayList<Rezervacija> getRezervacije() {
		return rezervacije;
	}

	public void promeniStatusRezervacije(Rezervacija r, StatusRezervacije noviStatus) {
		r.setStatusRezervacije(noviStatus);
		sacuvajIzmene();
	}

	public void sacuvajIzmene() {
		try {
			upisi("rezervacije.csv");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}