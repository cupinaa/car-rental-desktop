package korisnici;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Klijent extends Korisnik {

	protected LocalDate datumIzdavanjaVozacke;
	protected KategorijaKlijenata kategorijaKlijenata;

	public Klijent(int id, String ime, String prezime, Pol pol, LocalDate datumRodjenja, String telefon,
			String adresa, String korisnickoIme, String lozinka, LocalDate datumIzdavanjaVozacke,
			KategorijaKlijenata kategorijaKlijenata) {
		super(id, ime, prezime, pol, datumRodjenja, telefon, adresa, korisnickoIme, lozinka);
		this.datumIzdavanjaVozacke = datumIzdavanjaVozacke;
		this.kategorijaKlijenata = kategorijaKlijenata;
	}

	public Klijent(int id, String ime, String prezime, Pol pol, LocalDate datumRodjenja, String telefon,
			String adresa, String korisnickoIme, String lozinka, LocalDate datumIzdavanjaVozacke) {
		super(id, ime, prezime, pol, datumRodjenja, telefon, adresa, korisnickoIme, lozinka);
		this.datumIzdavanjaVozacke = datumIzdavanjaVozacke;
	}
	
	public Klijent(String ime, String prezime, Pol pol, LocalDate datumRodjenja, String telefon,
			String adresa, String korisnickoIme, String lozinka, LocalDate datumIzdavanjaVozacke,
			KategorijaKlijenata kategorijaKlijenata) {
		super(ime, prezime, pol, datumRodjenja, telefon, adresa, korisnickoIme, lozinka);
		this.datumIzdavanjaVozacke = datumIzdavanjaVozacke;
		this.kategorijaKlijenata = kategorijaKlijenata;
	}

	public Klijent(String ime, String prezime, Pol pol, LocalDate datumRodjenja, String telefon,
			String adresa, String korisnickoIme, String lozinka, LocalDate datumIzdavanjaVozacke) {
		super(ime, prezime, pol, datumRodjenja, telefon, adresa, korisnickoIme, lozinka);
		this.datumIzdavanjaVozacke = datumIzdavanjaVozacke;
	}

	public boolean imaPravoNaRezervaciju() {
		LocalDate danas = LocalDate.now();
		LocalDate preDveGodine = danas.minusYears(2);
		
		return preDveGodine.isAfter(this.datumIzdavanjaVozacke) || preDveGodine.isEqual(this.datumIzdavanjaVozacke);
	}

	public LocalDate getDatumIzdavanjaVozacke() {
		return datumIzdavanjaVozacke;
	}

	public void setDatumIzdavanjaVozacke(LocalDate datumIzdavanjaVozacke) {
		this.datumIzdavanjaVozacke = datumIzdavanjaVozacke;
	}

	public KategorijaKlijenata getKategorijaKlijenata() {
		return kategorijaKlijenata;
	}

	public void setKategorijaKlijenata(KategorijaKlijenata kategorijaKlijenata) {
		this.kategorijaKlijenata = kategorijaKlijenata;
	}

	protected LocalDateTime zabranaRezervisanjaDo;

	public LocalDateTime getZabranaRezervisanjaDo() {
		return zabranaRezervisanjaDo;
	}

	public void setZabranaRezervisanjaDo(LocalDateTime zabranaRezervisanjaDo) {
		this.zabranaRezervisanjaDo = zabranaRezervisanjaDo;
	}

	public boolean podZabranom() {
		if (zabranaRezervisanjaDo == null) {
			return false;
		}
		return LocalDateTime.now().isBefore(zabranaRezervisanjaDo);
	}

	protected int brojKasnjenja = 0;

	public int getBrojKasnjenja() {
		return brojKasnjenja;
	}

	public void setBrojKasnjenja(int brojKasnjenja) {
		this.brojKasnjenja = brojKasnjenja;
	}

}
