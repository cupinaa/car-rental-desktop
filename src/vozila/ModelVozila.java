package vozila;

public class ModelVozila {

	protected int id;
	protected String markaVozila;
	protected String nazivModela;
	protected KategorijaVozila kategorija;

	public ModelVozila(String markaVozila, String nazivModela, KategorijaVozila kategorija) {
		this.markaVozila = markaVozila;
		this.nazivModela = nazivModela;
		this.kategorija = kategorija;
	}

	public ModelVozila(int id, String markaVozila, String nazivModela, KategorijaVozila kategorija) {
		this.id = id;
		this.markaVozila = markaVozila;
		this.nazivModela = nazivModela;
		this.kategorija = kategorija;
	}
	
	@Override
	public String toString() {
		return "Model: " + markaVozila + " " + nazivModela + " (" + kategorija + ")";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMarkaVozila() {
		return markaVozila;
	}

	public void setMarkaVozila(String markaVozila) {
		this.markaVozila = markaVozila;
	}

	public String getNazivModela() {
		return nazivModela;
	}

	public void setNazivModela(String nazivModela) {
		this.nazivModela = nazivModela;
	}

	public KategorijaVozila getKategorija() {
		return kategorija;
	}

	public void setKategorija(KategorijaVozila kategorija) {
		this.kategorija = kategorija;
	}


}
