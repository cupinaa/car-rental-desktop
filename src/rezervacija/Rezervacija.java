package rezervacija;

import java.time.LocalDate;
import java.util.ArrayList;

import korisnici.Klijent;
import vozila.Vozilo;

public class Rezervacija {

	protected int id;
	protected Klijent klijent;
	protected Vozilo vozilo;
	protected LocalDate datumPocetka;
	protected LocalDate datumKraja;
	protected double ukupnaCena;
	protected StatusRezervacije statusRezervacije = StatusRezervacije.NA_ČEKANJU;
	
	
	protected ArrayList<DodatnaUsluga> dodatneUsluge;


	public Rezervacija(Klijent klijent, Vozilo vozilo, LocalDate datumPocetka, LocalDate datumKraja,
			ArrayList<DodatnaUsluga> dodatneUsluge) {
		this.klijent = klijent;
		this.vozilo = vozilo;
		this.datumPocetka = datumPocetka;
		this.datumKraja = datumKraja;
		this.dodatneUsluge = dodatneUsluge;
	}


	public Rezervacija(int id, Klijent klijent, Vozilo vozilo, LocalDate datumPocetka, LocalDate datumKraja,
			double ukupnaCena, StatusRezervacije statusRezervacije, ArrayList<DodatnaUsluga> dodatneUsluge) {
		this.id = id;
		this.klijent = klijent;
		this.vozilo = vozilo;
		this.datumPocetka = datumPocetka;
		this.datumKraja = datumKraja;
		this.ukupnaCena = ukupnaCena;
		this.statusRezervacije = statusRezervacije;
		this.dodatneUsluge = dodatneUsluge;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public Klijent getKlijent() {
		return klijent;
	}


	public void setKlijent(Klijent klijent) {
		this.klijent = klijent;
	}


	public Vozilo getVozilo() {
		return vozilo;
	}


	public void setVozilo(Vozilo vozilo) {
		this.vozilo = vozilo;
	}


	public LocalDate getDatumPocetka() {
		return datumPocetka;
	}


	public void setDatumPocetka(LocalDate datumPocetka) {
		this.datumPocetka = datumPocetka;
	}


	public LocalDate getDatumKraja() {
		return datumKraja;
	}


	public void setDatumKraja(LocalDate datumKraja) {
		this.datumKraja = datumKraja;
	}


	public double getUkupnaCena() {
		return ukupnaCena;
	}


	public void setUkupnaCena(double ukupnaCena) {
		this.ukupnaCena = ukupnaCena;
	}


	public StatusRezervacije getStatusRezervacije() {
		return statusRezervacije;
	}


	public void setStatusRezervacije(StatusRezervacije statusRezervacije) {
		this.statusRezervacije = statusRezervacije;
	}


	public ArrayList<DodatnaUsluga> getDodatneUsluge() {
		return dodatneUsluge;
	}


	public void setDodatneUsluge(ArrayList<DodatnaUsluga> dodatneUsluge) {
		this.dodatneUsluge = dodatneUsluge;
	}
	

}
