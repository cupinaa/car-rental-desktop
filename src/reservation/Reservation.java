package reservation;

import java.time.LocalDate;
import java.util.ArrayList;

import users.Customer;
import vehicles.Vehicle;

public class Reservation {

	protected int id;
	protected Customer customer;
	protected Vehicle vehicle;
	protected LocalDate startDate;
	protected LocalDate endDate;
	protected double totalPrice;
	protected ReservationStatus reservationStatus = ReservationStatus.PENDING;


	protected ArrayList<ExtraService> extraServices;


	public Reservation(Customer customer, Vehicle vehicle, LocalDate startDate, LocalDate endDate,
			ArrayList<ExtraService> extraServices) {
		this.customer = customer;
		this.vehicle = vehicle;
		this.startDate = startDate;
		this.endDate = endDate;
		this.extraServices = extraServices;
	}


	public Reservation(int id, Customer customer, Vehicle vehicle, LocalDate startDate, LocalDate endDate,
			double totalPrice, ReservationStatus reservationStatus, ArrayList<ExtraService> extraServices) {
		this.id = id;
		this.customer = customer;
		this.vehicle = vehicle;
		this.startDate = startDate;
		this.endDate = endDate;
		this.totalPrice = totalPrice;
		this.reservationStatus = reservationStatus;
		this.extraServices = extraServices;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public Customer getCustomer() {
		return customer;
	}


	public void setCustomer(Customer customer) {
		this.customer = customer;
	}


	public Vehicle getVehicle() {
		return vehicle;
	}


	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}


	public LocalDate getStartDate() {
		return startDate;
	}


	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}


	public LocalDate getEndDate() {
		return endDate;
	}


	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}


	public double getTotalPrice() {
		return totalPrice;
	}


	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}


	public ReservationStatus getReservationStatus() {
		return reservationStatus;
	}


	public void setReservationStatus(ReservationStatus reservationStatus) {
		this.reservationStatus = reservationStatus;
	}


	public ArrayList<ExtraService> getExtraServices() {
		return extraServices;
	}


	public void setExtraServices(ArrayList<ExtraService> extraServices) {
		this.extraServices = extraServices;
	}


}
