package izdavanje;

import korisnici.Agent;
import rezervacija.Rezervacija;

public class Izdavanje {

	protected int id;
	protected Rezervacija rezervacija;
	protected Agent agent;

	protected double pocetnaKilometraza;
	protected double krajnjaKilometraza;

	public Izdavanje(Rezervacija rezervacija, Agent agent, double pocetnaKilometraza) {
		this.rezervacija = rezervacija;
		this.agent = agent;
		this.pocetnaKilometraza = pocetnaKilometraza;
		this.krajnjaKilometraza = 0.0;
	}

	public Izdavanje(int id, Rezervacija rezervacija, Agent agent, double pocetnaKilometraza, double krajnjaKilometraza) {
		this.id = id;
		this.rezervacija = rezervacija;
		this.agent = agent;
		this.pocetnaKilometraza = pocetnaKilometraza;
		this.krajnjaKilometraza = krajnjaKilometraza;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Rezervacija getRezervacija() {
		return rezervacija;
	}

	public void setRezervacija(Rezervacija rezervacija) {
		this.rezervacija = rezervacija;
	}

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public double getPocetnaKilometraza() {
		return pocetnaKilometraza;
	}

	public void setPocetnaKilometraza(double pocetnaKilometraza) {
		this.pocetnaKilometraza = pocetnaKilometraza;
	}

	public double getKrajnjaKilometraza() {
		return krajnjaKilometraza;
	}

	public void setKrajnjaKilometraza(double krajnjaKilometraza) {
		this.krajnjaKilometraza = krajnjaKilometraza;
	}
}
