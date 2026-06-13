package korisnici;

import java.time.LocalDate;

public abstract class Korisnik {

	int id;
	protected String ime;
	protected String prezime;
	protected Pol pol;
	protected LocalDate datumRodjenja;
	protected String telefon;
	protected String adresa;
	protected String korisnickoIme;
	protected String lozinka;
	protected boolean prijavaljen = false;

	public Korisnik(int id, String ime, String prezime, Pol pol, LocalDate datumRodjenja, String telefon,
			String adresa, String korisnickoIme, String lozinka) {
		this.id = id;
		this.ime = ime;
		this.prezime = prezime;
		this.pol = pol;
		this.datumRodjenja = datumRodjenja;
		this.telefon = telefon;
		this.adresa = adresa;
		this.korisnickoIme = korisnickoIme;
		this.lozinka = lozinka;
	}
	
	public Korisnik(String ime, String prezime, Pol pol, LocalDate datumRodjenja, String telefon, String adresa,
			String korisnickoIme, String lozinka) {
		this.ime = ime;
		this.prezime = prezime;
		this.pol = pol;
		this.datumRodjenja = datumRodjenja;
		this.telefon = telefon;
		this.adresa = adresa;
		this.korisnickoIme = korisnickoIme;
		this.lozinka = lozinka;
	}

	@Override
	public String toString() {
		return "Korisnik [ID: " + id + " | Ime i prezime: " + ime + " " + prezime + 
		           " | Korisničko ime: " + korisnickoIme + 
		           " | Telefon: " + telefon + 
		           " | Adresa: " + adresa + 
		           " | Datum rođenja: " + datumRodjenja + "]"; 
	}
	
	public void prijava() {
		this.prijavaljen = true;
	}

	public void odjava() {
		this.prijavaljen = false;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public Pol getPol() {
		return pol;
	}

	public void setPol(Pol pol) {
		this.pol = pol;
	}

	public LocalDate getDatumRodjenja() {
		return datumRodjenja;
	}

	public void setDatumRodjenja(LocalDate datumRodjenja) {
		this.datumRodjenja = datumRodjenja;
	}

	public String getTelefon() {
		return telefon;
	}

	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}

	public String getKorisnickoIme() {
		return korisnickoIme;
	}

	public void setKorisnickoIme(String korisnickoIme) {
		this.korisnickoIme = korisnickoIme;
	}

	public String getLozinka() {
		return lozinka;
	}

	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}

	public boolean isPrijavaljen() {
		return prijavaljen;
	}

	public void setPrijavaljen(boolean prijavaljen) {
		this.prijavaljen = prijavaljen;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAdresa() {
		return adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

}
