package data;

import java.time.LocalDateTime;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import pricing.PriceList;
import pricing.PriceListItem;
import users.Customer;
import users.Subscription;
import users.SubscriptionStatus;
import reservation.ExtraService;
import reservation.Reservation;
import reservation.ReservationStatus;
import vehicles.VehicleCategory;
import vehicles.VehicleModel;
import vehicles.Vehicle;

public class ReservationRepository {

	protected ArrayList<Reservation> reservations = new ArrayList<>();

	public void load(String putanja, UserRepository kp, VehicleRepository vp, ExtraServiceRepository dup)
			throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(putanja));
		String linija;
		while ((linija = br.readLine()) != null) {
			String[] delovi = linija.split("\\|");

			int id = Integer.parseInt(delovi[0]);
			int idCustomera = Integer.parseInt(delovi[1]);
			int idVozila = Integer.parseInt(delovi[2]);
			LocalDate startDate = LocalDate.parse(delovi[3]);
			LocalDate endDate = LocalDate.parse(delovi[4]);
			double totalPrice = Double.parseDouble(delovi[5]);
			ReservationStatus status = ReservationStatus.valueOf(delovi[6]);

			Customer customer = kp.findCustomer(idCustomera);
			Vehicle vehicle = vp.findVehicle(idVozila);

			ArrayList<ExtraService> servicesReservations = new ArrayList<>();
			if (delovi.length > 7 && !delovi[7].isEmpty()) {
				String[] idService = delovi[7].split(",");
				for (String sId : idService) {
					ExtraService du = dup.findService(Integer.parseInt(sId));
					if (du != null)
						servicesReservations.add(du);
				}
			}

			Reservation r = new Reservation(id, customer, vehicle, startDate, endDate, totalPrice, status,
					servicesReservations);
			reservations.add(r);
		}
		br.close();

		boolean changedo = false;
		for (Reservation r : reservations) {
			if (r.getReservationStatus() == ReservationStatus.values()[0]
					&& !r.getStartDate().isAfter(LocalDate.now())) {
				r.setReservationStatus(ReservationStatus.REJECTED);
				changedo = true;
			}

			if (r.getReservationStatus() == ReservationStatus.values()[4]
					&& r.getStartDate().isBefore(LocalDate.now())) {
				r.setReservationStatus(ReservationStatus.CANCELLED);

				if (r.getCustomer() != null) {
					r.getCustomer().setBookingBlockedUntil(LocalDateTime.now().plusHours(24));
				}
				changedo = true;
			}
		}

		if (changedo) {
			write(putanja);
			kp.saveChanges();
		}
	}

	public void write(String putanja) throws IOException {
		PrintWriter pw = new PrintWriter(new FileWriter(putanja));
		for (Reservation r : reservations) {

			String servicesStr = "";
			for (int i = 0; i < r.getExtraServices().size(); i++) {
				servicesStr += r.getExtraServices().get(i).getId();
				if (i < r.getExtraServices().size() - 1)
					servicesStr += ",";
			}

			pw.println(r.getId() + "|" + r.getCustomer().getId() + "|" + r.getVehicle().getId() + "|"
					+ r.getStartDate() + "|" + r.getEndDate() + "|" + r.getTotalPrice() + "|"
					+ r.getReservationStatus() + "|" + servicesStr);
		}
		pw.close();
	}

	public Reservation findReservation(int id) {
		for (Reservation r : reservations) {
			if (r.getId() == id)
				return r;
		}
		return null;
	}

	private int generateNewId() {
		int maxId = 0;
		for (Reservation r : reservations) {
			if (r.getId() > maxId)
				maxId = r.getId();
		}
		return maxId + 1;
	}

	public void addReservation(Reservation r) {
		r.setId(generateNewId());
		reservations.add(r);
		try {
			write("reservations.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public double calculateTotalPrice(Reservation r, PriceList current) {

		if (current == null) {
			return 0.0;
		}

		long numberOfDays = ChronoUnit.DAYS.between(r.getStartDate(), r.getEndDate());
		if (numberOfDays <= 0) {
			numberOfDays = 1;
		}

		double dailyPrice = 0.0;
		VehicleCategory katVozila = r.getVehicle().getVehicleModel().getCategory();

		for (PriceListItem s : current.getStavkePriceLista()) {
			if (s.getVehicleCategory() == katVozila) {
				dailyPrice = s.getDailyPrice();
				break;
			}
		}

		double totalPrice = dailyPrice * numberOfDays;

		Settings p = new Settings();
		p.load();
		long podrazumevano = p.getDefaultRentalDuration();

		for (ExtraService du : r.getExtraServices()) {
			String serviceName = du.getServiceName().toLowerCase();
			double servicePrice = 0.0;
			if (current.getExtraServicePrices() != null && current.getExtraServicePrices().containsKey(du.getId())) {
				servicePrice = current.getExtraServicePrices().get(du.getId());
			}

			if (serviceName.contains("extended") || serviceName.contains("extended")) {
				long dodatniDani = numberOfDays - podrazumevano;
				if (dodatniDani > 0) {
					totalPrice += (servicePrice * dodatniDani);
				}
			} else {
				totalPrice += servicePrice;
			}
		}

		if (r.getCustomer().getCustomerCategory() != null) {
			switch (r.getCustomer().getCustomerCategory()) {
			case STUDENT:
				totalPrice = totalPrice * (1.0 - current.getStudentDiscount());
				break;
			case COMPANY:
				totalPrice = totalPrice * (1.0 - current.getCompanyDiscount());
				break;
			case RETIREE:
				totalPrice = totalPrice * (1.0 - current.getRetireeDiscount());
				break;
			}
		}

		return totalPrice;
	}

	public boolean daLiJeVehicleAvailableno(Vehicle v, LocalDate trazeniStart, LocalDate trazeniEnd) {
		return daLiJeVehicleAvailableno(v, trazeniStart, trazeniEnd, -1);
	}

	public boolean daLiJeVehicleAvailableno(Vehicle v, LocalDate trazeniStart, LocalDate trazeniEnd,
			int iskljuciIdReservations) {
		for (Reservation r : reservations) {
			if (r.getId() == iskljuciIdReservations) {
				continue;
			}
			if (r.getVehicle() == null || r.getVehicle().getId() != v.getId()) {
				continue;
			}

			if (r.getReservationStatus() == ReservationStatus.CANCELLED
					|| r.getReservationStatus() == ReservationStatus.REJECTED) {
				continue;
			}

			boolean preklapaSe = !trazeniStart.isAfter(r.getEndDate())
					&& !trazeniEnd.isBefore(r.getStartDate());

			if (preklapaSe) {
				return false;
			}
		}

		return true;
	}

	public Vehicle findAvailablenoVehicleZaModel(VehicleModel model, VehicleRepository vp, LocalDate startDate,
			LocalDate endDate) {
		for (Vehicle v : vp.getVehicles()) {
			if (v.getVehicleModel().getId() == model.getId()) {
				if (this.daLiJeVehicleAvailableno(v, startDate, endDate)) {
					return v;
				}
			}
		}
		return null;
	}

	public Reservation createReservation(Customer k, Vehicle v, LocalDate startDate, LocalDate endDate,
			ArrayList<ExtraService> extraServices, PriceList currentPriceList, SubscriptionRepository subscriptionsPodaci) {

		Subscription subscription = subscriptionsPodaci.findSubscriptionForCustomer(k.getId());

		if (subscription == null || subscription.getStatus() != SubscriptionStatus.ACTIVE
				|| subscription.getExpiryDate().isBefore(LocalDate.now())) {
			System.out.println("Sorry, you do not have an active subscription. Submit a request.");
			return null;
		}

		if (!k.isEligibleToBook()) {
			System.out.println("Customer is not eligible to book (the driving license must be at least two years old).");
			return null;
		}

		if (this.daLiJeVehicleAvailableno(v, startDate, endDate)) {

			Reservation r = new Reservation(k, v, startDate, endDate, extraServices);

			double calculatedPrice = this.calculateTotalPrice(r, currentPriceList);
			r.setTotalPrice(calculatedPrice);

			this.addReservation(r);

			System.out.println("Reservation created. Price: " + calculatedPrice);
			return r;

		} else {
			System.out.println("Vehicle je unfortunately zauzeto u tom periodu.");
			return null;
		}
	}

	public double calculatePrihode(LocalDate odDatea, LocalDate doDatea) {
		double totalRevenue = 0;
		for (Reservation r : reservations) {
			if (r.getReservationStatus() != ReservationStatus.CANCELLED
					&& r.getReservationStatus() != ReservationStatus.REJECTED) {

				if (!r.getStartDate().isBefore(odDatea) && !r.getStartDate().isAfter(doDatea)) {
					totalRevenue += r.getTotalPrice();
				}
			}
		}
		return totalRevenue;
	}

	public void promeniReservationStatus(Reservation r, ReservationStatus newStatus) {
		r.setReservationStatus(newStatus);
		saveChanges();
	}

	public void saveChanges() {
		try {
			write("reservations.csv");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public ArrayList<Reservation> getReservations() {
		return reservations;
	}
}
