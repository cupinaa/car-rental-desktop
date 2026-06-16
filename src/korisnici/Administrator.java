package korisnici;
import java.time.LocalDate;

public class Administrator extends Zaposleni {

	public Administrator(String ime, String prezime, Pol pol, LocalDate datumRodjenja, String telefon,
			String adresa, String korisnickoIme, String lozinka, StrucnaSprema strucnaSprema, int godineStaza) {
		super(ime, prezime, pol, datumRodjenja, telefon, adresa, korisnickoIme, lozinka, strucnaSprema,
				godineStaza);
	}



	public Administrator(int id, String ime, String prezime, Pol pol, LocalDate datumRodjenja, String telefon,
			String adresa, String korisnickoIme, String lozinka, StrucnaSprema strucnaSprema, int godineStaza,
			double plata) {
		super(id, ime, prezime, pol, datumRodjenja, telefon, adresa, korisnickoIme, lozinka, strucnaSprema, godineStaza,
				plata);
	}



}
