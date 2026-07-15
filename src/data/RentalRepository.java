package data;

import pricing.PriceList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import rental.Rental;
import users.Agent;
import users.Customer;
import reservation.Reservation;
import vehicles.VehicleStatus;

public class RentalRepository {

	protected ArrayList<Rental> rentals = new ArrayList<>();

	public void load(String putanja, ReservationRepository rp, UserRepository kp) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(putanja));
		String linija;
		while ((linija = br.readLine()) != null) {
			String[] delovi = linija.split("\\|");

			int id = Integer.parseInt(delovi[0]);
			int idRez = Integer.parseInt(delovi[1]);
			int idAgenta = Integer.parseInt(delovi[2]);
			double pocetnaK = Double.parseDouble(delovi[3]);
			double endnjaK = Double.parseDouble(delovi[4]);

			Reservation r = rp.findReservation(idRez);
			Agent a = kp.findAgent(idAgenta);

			if (r != null && a != null) {
				Rental i = new Rental(id, r, a, pocetnaK, endnjaK);
				rentals.add(i);
			}
		}
		br.close();
	}

	public void write(String putanja) throws IOException {
		PrintWriter pw = new PrintWriter(new FileWriter(putanja));
		for (Rental i : rentals) {
			pw.println(i.getId() + "|" + i.getReservation().getId() + "|" + i.getAgent().getId() + "|"
					+ i.getStartingMileage() + "|" + i.getEndingMileage());
		}
		pw.close();
	}

	public void izdajVehicle(Reservation r, Agent a, double trenutnaKilometraza) {
		r.getVehicle().setVehicleStatus(VehicleStatus.RENTED);
		Rental rental = new Rental(r, a, trenutnaKilometraza);
		this.addRental(rental);
	}

	public void returnVehicle(Rental i, double newKilometraza, LocalDate returnDate, ReservationRepository rp,
			PriceList currentPriceList, UserRepository kp) {
		i.setEndingMileage(newKilometraza);
		i.getReservation().getVehicle().setVehicleStatus(VehicleStatus.AVAILABLE);

		long delay = ChronoUnit.DAYS.between(i.getReservation().getEndDate(), returnDate);

		if (delay > 0 && currentPriceList != null) {
			double lateFee = delay * currentPriceList.getLateFee();
			double staraPrice = i.getReservation().getTotalPrice();

			i.getReservation().setTotalPrice(staraPrice + lateFee);

			Customer k = i.getReservation().getCustomer();
			k.setLateReturnCount(k.getLateReturnCount() + 1);
			kp.saveChanges();

			System.out.println("Customer je kasnio " + delay + " days! Dodata je lateFee od " + lateFee + " din.");
			rp.saveChanges();
		}

		try {
			write("rentals.csv");
		} catch (Exception e) {
		}
	}

	private int generateNewId() {
		int maxId = 0;
		for (Rental i : rentals) {
			if (i.getId() > maxId)
				maxId = i.getId();
		}
		return maxId + 1;
	}

	public void addRental(Rental i) {
		i.setId(generateNewId());
		rentals.add(i);
		try {
			write("rentals.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Rental> getRentals() {
		return rentals;
	}
}
