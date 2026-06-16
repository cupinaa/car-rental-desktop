package cenovnik;

import java.time.LocalDate;
import java.util.ArrayList;

public class Cenovnik {

	protected int id;
	protected LocalDate pocetakVazenja;
	protected LocalDate krajVazenja;
	protected ArrayList<StavkaCenovnika> stavkeCenovnika;

	protected double cenaPretplate;
	protected double popustStudent; 
	protected double popustFirma;   
	protected double popustPenzioner; 
	protected double iznosKazne;

	protected java.util.Map<Integer, Double> ceneDodatnihUsluga;

	public Cenovnik(int id, LocalDate pocetakVazenja, LocalDate krajVazenja,
			ArrayList<StavkaCenovnika> stavkeCenovnika, double cenaPretplate, 
			double popustStudent, double popustFirma, double popustPenzioner, double iznosKazne, java.util.Map<Integer, Double> ceneDodatnihUsluga) {
		this.id = id;
		this.pocetakVazenja = pocetakVazenja;
		this.krajVazenja = krajVazenja;
		this.stavkeCenovnika = stavkeCenovnika;
		this.cenaPretplate = cenaPretplate;
		this.popustStudent = popustStudent;
		this.popustFirma = popustFirma;
		this.popustPenzioner = popustPenzioner;
		this.iznosKazne = iznosKazne;
		this.ceneDodatnihUsluga = ceneDodatnihUsluga;
	}

	public Cenovnik(LocalDate pocetakVazenja, LocalDate krajVazenja, ArrayList<StavkaCenovnika> stavkeCenovnika,
			double cenaPretplate, double popustStudent, double popustFirma, double popustPenzioner, double iznosKazne, java.util.Map<Integer, Double> ceneDodatnihUsluga) {
		this.pocetakVazenja = pocetakVazenja;
		this.krajVazenja = krajVazenja;
		this.stavkeCenovnika = stavkeCenovnika;
		this.cenaPretplate = cenaPretplate;
		this.popustStudent = popustStudent;
		this.popustFirma = popustFirma;
		this.popustPenzioner = popustPenzioner;
		this.iznosKazne = iznosKazne;
		this.ceneDodatnihUsluga = ceneDodatnihUsluga;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDate getPocetakVazenja() {
		return pocetakVazenja;
	}

	public void setPocetakVazenja(LocalDate pocetakVazenja) {
		this.pocetakVazenja = pocetakVazenja;
	}

	public LocalDate getKrajVazenja() {
		return krajVazenja;
	}

	public void setKrajVazenja(LocalDate krajVazenja) {
		this.krajVazenja = krajVazenja;
	}

	public ArrayList<StavkaCenovnika> getStavkeCenovnika() {
		return stavkeCenovnika;
	}

	public void setStavkeCenovnika(ArrayList<StavkaCenovnika> stavkeCenovnika) {
		this.stavkeCenovnika = stavkeCenovnika;
	}

	public double getCenaPretplate() {
		return cenaPretplate;
	}

	public void setCenaPretplate(double cenaPretplate) {
		this.cenaPretplate = cenaPretplate;
	}

	public double getPopustStudent() {
		return popustStudent;
	}

	public void setPopustStudent(double popustStudent) {
		this.popustStudent = popustStudent;
	}

	public double getPopustFirma() {
		return popustFirma;
	}

	public void setPopustFirma(double popustFirma) {
		this.popustFirma = popustFirma;
	}

	public double getPopustPenzioner() {
		return popustPenzioner;
	}

	public void setPopustPenzioner(double popustPenzioner) {
		this.popustPenzioner = popustPenzioner;
	}

	public double getIznosKazne() {
		return iznosKazne;
	}

	public void setIznosKazne(double iznosKazne) {
		this.iznosKazne = iznosKazne;
	}

	public java.util.Map<Integer, Double> getCeneDodatnihUsluga() {
		return ceneDodatnihUsluga;
	}

	public void setCeneDodatnihUsluga(java.util.Map<Integer, Double> ceneDodatnihUsluga) {
		this.ceneDodatnihUsluga = ceneDodatnihUsluga;
	}

}
