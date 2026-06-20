package podaci;
import java.time.LocalDateTime;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import korisnici.Administrator;
import korisnici.Agent;
import korisnici.KategorijaKlijenata;
import korisnici.Klijent;
import korisnici.Korisnik;
import korisnici.Pol;
import korisnici.StrucnaSprema;
import korisnici.Zaposleni;


public class KorisniciPodaci {

	protected ArrayList<Korisnik> korisnici = new ArrayList<>();

	public void ucitaj(String putanja) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(putanja));
		String linija;
		while ((linija = br.readLine()) != null) {
			String[] delovi = linija.split("\\|");
			String tip = delovi[0];
			int id = Integer.parseInt(delovi[1]);
			String ime = delovi[2];
			String prezime = delovi[3];
			Pol pol = Pol.valueOf(delovi[4].trim());
			LocalDate datumRodjenja = LocalDate.parse(delovi[5]);
			String telefon = delovi[6];
			String adresa = delovi[7];
			String korisnickoIme = delovi[8];
			String lozinka = delovi[9];
			if (tip.equals("ADMINISTRATOR") || tip.equals("AGENT")) {
				String sprema = delovi[10];
				StrucnaSprema strucnaSprema = StrucnaSprema.valueOf(sprema);
				int godineStaza = Integer.parseInt(delovi[11]);
				double plata = Double.parseDouble(delovi[12]);
				if (tip.equals("ADMINISTRATOR")) {
					Administrator admin = new Administrator(id, ime, prezime, pol, datumRodjenja, telefon, adresa,
							korisnickoIme, lozinka, strucnaSprema, godineStaza, plata);
					korisnici.add(admin);
				} else {
					Agent agent = new Agent(id, ime, prezime, pol, datumRodjenja, telefon, adresa, korisnickoIme,
							lozinka, strucnaSprema, godineStaza, plata);
					korisnici.add(agent);
				}

			} else if (tip.equals("KLIJENT")) {
				LocalDate datumIzdavanjaVozacke = LocalDate.parse(delovi[10]);
				Klijent klijent = new Klijent(id, ime, prezime, pol, datumRodjenja, telefon, adresa, korisnickoIme, lozinka, datumIzdavanjaVozacke);

				if (delovi.length > 11 && !delovi[11].isEmpty() && !delovi[11].equals("null")) {
					try {
						KategorijaKlijenata kategorijaKlijenata = KategorijaKlijenata.valueOf(delovi[11]);
						klijent.setKategorijaKlijenata(kategorijaKlijenata);
					} catch (Exception e) {}
				}

				if (delovi.length > 12 && !delovi[12].isEmpty() && !delovi[12].equals("null")) {
					klijent.setZabranaRezervisanjaDo(LocalDateTime.parse(delovi[12]));
				}

				if (delovi.length > 13 && !delovi[13].isEmpty() && !delovi[13].equals("null")) {
					klijent.setBrojKasnjenja(Integer.parseInt(delovi[13]));
				}

				korisnici.add(klijent);
			}
		}
		br.close();
	}

	public void upisi(String putanja) throws IOException {
		PrintWriter pw = new PrintWriter(new FileWriter(putanja));
		try {
			for (Korisnik k : korisnici) {
				String zajednickiDeo = k.getId() + "|" + k.getIme() + "|" + k.getPrezime() + "|" + k.getPol() + "|"
						+ k.getDatumRodjenja() + "|" + k.getTelefon() + "|" + k.getAdresa() + "|" + k.getKorisnickoIme()
						+ "|" + k.getLozinka();

				if (k instanceof Administrator) {
					Administrator admin = (Administrator) k;
					pw.println("ADMINISTRATOR|" + zajednickiDeo + "|" + admin.getStrucnaSprema() + "|"
							+ admin.getGodineStaza() + "|" + admin.getPlata());
				} else if (k instanceof Agent) {
					Agent agent = (Agent) k;
					pw.println("AGENT|" + zajednickiDeo + "|" + agent.getStrucnaSprema() + "|" + agent.getGodineStaza()
							+ "|" + agent.getPlata());
				} else if (k instanceof Klijent) {
					Klijent klijent = (Klijent) k;
					String zabranaStr = (klijent.getZabranaRezervisanjaDo() != null) ? klijent.getZabranaRezervisanjaDo().toString() : "null";

					if (klijent.getKategorijaKlijenata() != null) {
				        pw.println("KLIJENT|" + zajednickiDeo + "|" + klijent.getDatumIzdavanjaVozacke() + "|" + klijent.getKategorijaKlijenata().toString() + "|" + zabranaStr + "|" + klijent.getBrojKasnjenja());
				    } else {
				        pw.println("KLIJENT|" + zajednickiDeo + "|" + klijent.getDatumIzdavanjaVozacke() + "|null|" + zabranaStr + "|" + klijent.getBrojKasnjenja()); 
				    }
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());

		}
		pw.close();
	}

	public Agent pronadjiAgenta(int id) {
	    for (Korisnik k : korisnici) {
	        if (k instanceof Agent && k.getId() == id) {
	        	return (Agent) k;
	        }
	    }
	    return null;
	}


	public Klijent pronadjiKlijenta(int id) {
	    for (Korisnik k : korisnici) {
	        if (k instanceof Klijent && k.getId() == id) {
	            return (Klijent) k;
	        }
	    }
	    return null;
	}

	public Korisnik login(String korisnickoIme, String lozinka) {
		for(Korisnik k : korisnici) {
			if(k.getKorisnickoIme().equals(korisnickoIme) && k.getLozinka().equals(lozinka)) {
				return k;
			}
		}
		return null;
	}

	public void dodajKorisnika(Korisnik k) {
		k.setId(generisiNoviId());
		korisnici.add(k);
		sacuvajIzmene();
	}

    private int generisiNoviId() {
        int maxId = 0;
        for (Korisnik k : korisnici) {
            if (k.getId() > maxId) {
                maxId = k.getId();
            }
        }
        return maxId + 1;
    }

	public void obrisiKorisnika(Korisnik k) {
		korisnici.remove(k);
		sacuvajIzmene();
	}

	public void sacuvajIzmene() {
		try {
			upisi("korisnici.csv");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public double izracunajRashode(LocalDate odDatuma, LocalDate doDatuma) {
		double ukupniRashodi = 0;

		long brojDana = ChronoUnit.DAYS.between(odDatuma, doDatuma);
		if (brojDana <= 0) {
			brojDana = 1;
		}

		for (Korisnik k : korisnici) {
			if (k instanceof Zaposleni) {
				double plataZaposlenog = ((Zaposleni) k).getPlata();
				double dnevnica = plataZaposlenog / 30.0;

				ukupniRashodi += (dnevnica * brojDana);
			}
		}
		return ukupniRashodi;
	}

	public ArrayList<Korisnik> getKorisnici() {
		return korisnici;
	}

	public void setKorisnici(ArrayList<Korisnik> korisnici) {
		this.korisnici = korisnici;
	}
}
