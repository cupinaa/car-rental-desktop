package korisnici;

import java.time.LocalDate;

public class Pretplata {

	protected int id;
	protected Klijent klijent;
	protected LocalDate datumIsteka;
	protected StatusPretplate status;

	public Pretplata(int id, Klijent klijent, LocalDate datumIsteka, StatusPretplate status) {
		this.id = id;
		this.klijent = klijent;
		this.datumIsteka = datumIsteka;
		this.status = status;
	}

	public Pretplata(Klijent klijent, LocalDate datumIsteka, StatusPretplate status) {
		this.klijent = klijent;
		this.datumIsteka = datumIsteka;
		this.status = status;
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

	public LocalDate getDatumIsteka() {
		return datumIsteka;
	}

	public void setDatumIsteka(LocalDate datumIsteka) {
		this.datumIsteka = datumIsteka;
	}

	public StatusPretplate getStatus() {
		return status;
	}

	public void setStatus(StatusPretplate status) {
		this.status = status;
	}

}
