package cenovnik;

import vozila.KategorijaVozila;

public class StavkaCenovnika {

	protected int id;
	protected KategorijaVozila kategorijaVozila;
	protected double cenaPoDanu;

	public StavkaCenovnika(int id, KategorijaVozila kategorijaVozila, double cenaPoDanu) {
		this.id = id;
		this.kategorijaVozila = kategorijaVozila;
		this.cenaPoDanu = cenaPoDanu;
	}

	public StavkaCenovnika(KategorijaVozila kategorijaVozila, double cenaPoDanu) {
		this.kategorijaVozila = kategorijaVozila;
		this.cenaPoDanu = cenaPoDanu;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public KategorijaVozila getKategorijaVozila() {
		return kategorijaVozila;
	}

	public void setKategorijaVozila(KategorijaVozila kategorijaVozila) {
		this.kategorijaVozila = kategorijaVozila;
	}

	public double getCenaPoDanu() {
		return cenaPoDanu;
	}

	public void setCenaPoDanu(double cenaPoDanu) {
		this.cenaPoDanu = cenaPoDanu;
	}

}
