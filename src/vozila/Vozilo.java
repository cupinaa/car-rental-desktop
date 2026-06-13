package vozila;

public class Vozilo {

	protected int id;
	protected ModelVozila modelVozila;
	protected String registarskeTablice;
	protected StatusVozila statusVozila;

	public Vozilo(int id, ModelVozila modelVozila, String registarskeTablice, StatusVozila statusVozila) {
		this.id = id;
		this.modelVozila = modelVozila;
		this.registarskeTablice = registarskeTablice;
		this.statusVozila = statusVozila;
	}
	

	public Vozilo(ModelVozila modelVozila, String registarskeTablice, StatusVozila statusVozila) {
		this.modelVozila = modelVozila;
		this.registarskeTablice = registarskeTablice;
		this.statusVozila = statusVozila;
	}
	

	@Override
	public String toString() {
	    return "Vozilo: " + registarskeTablice + " | Status: " + statusVozila + 
	           " | Model: " + modelVozila.getMarkaVozila() + " " + modelVozila.getNazivModela();
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ModelVozila getModelVozila() {
		return modelVozila;
	}

	public void setModelVozila(ModelVozila modelVozila) {
		this.modelVozila = modelVozila;
	}

	public String getRegistarskeTablice() {
		return registarskeTablice;
	}

	public void setRegistarskeTablice(String registarskeTablice) {
		this.registarskeTablice = registarskeTablice;
	}

	public StatusVozila getStatusVozila() {
		return statusVozila;
	}

	public void setStatusVozila(StatusVozila statusVozila) {
		this.statusVozila = statusVozila;
	}

}
