package korisnici;

public enum StrucnaSprema {

	SSS(1.0),VSS(1.5),MAGISTAR(1.8);
	
	private double koeficijent;
	
	StrucnaSprema(double koeficijent){
		this.koeficijent = koeficijent;
	}

	public double getKoeficijent() {
		return koeficijent;
	}


}
