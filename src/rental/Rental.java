package rental;

import users.Agent;
import reservation.Reservation;

public class Rental {

	protected int id;
	protected Reservation reservation;
	protected Agent agent;

	protected double startingMileage;
	protected double endingMileage;

	public Rental(Reservation reservation, Agent agent, double startingMileage) {
		this.reservation = reservation;
		this.agent = agent;
		this.startingMileage = startingMileage;
		this.endingMileage = 0.0;
	}

	public Rental(int id, Reservation reservation, Agent agent, double startingMileage, double endingMileage) {
		this.id = id;
		this.reservation = reservation;
		this.agent = agent;
		this.startingMileage = startingMileage;
		this.endingMileage = endingMileage;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Reservation getReservation() {
		return reservation;
	}

	public void setReservation(Reservation reservation) {
		this.reservation = reservation;
	}

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public double getStartingMileage() {
		return startingMileage;
	}

	public void setStartingMileage(double startingMileage) {
		this.startingMileage = startingMileage;
	}

	public double getEndingMileage() {
		return endingMileage;
	}

	public void setEndingMileage(double endingMileage) {
		this.endingMileage = endingMileage;
	}
}
