package data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;

import users.Customer;
import users.Subscription;
import users.SubscriptionStatus;

public class SubscriptionRepository {

	protected ArrayList<Subscription> subscriptions = new ArrayList<>();

	public void load(String putanja, UserRepository kp) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(putanja));
		String linija;
		while ((linija = br.readLine()) != null) {
			String[] delovi = linija.split("\\|");

			int id = Integer.parseInt(delovi[0]);
			int idCustomera = Integer.parseInt(delovi[1]);
			LocalDate expiryDate = LocalDate.parse(delovi[2]);
			SubscriptionStatus status = SubscriptionStatus.valueOf(delovi[3]);

			Customer customer = kp.findCustomer(idCustomera);

			if (customer != null) {
				Subscription p = new Subscription(id, customer, expiryDate, status);
				subscriptions.add(p);
			}
		}
		br.close();
	}

	public void write(String putanja) throws IOException {
		PrintWriter pw = new PrintWriter(new FileWriter(putanja));
		for (Subscription p : subscriptions) {
			pw.println(p.getId() + "|" + p.getCustomer().getId() + "|" + p.getExpiryDate() + "|" + p.getStatus());
		}
		pw.close();
	}

	public Subscription findSubscription(int id) {
		for (Subscription p : subscriptions) {
			if (p.getId() == id) {
				return p;
			}
		}
		return null;
	}

	public Subscription findSubscriptionForCustomer(int idCustomera) {
		for (Subscription p : subscriptions) {
			if (p.getCustomer().getId() == idCustomera) {
				return p;
			}
		}
		return null;
	}

	private int generateNewId() {
		int maxId = 0;
		for (Subscription p : subscriptions) {
			if (p.getId() > maxId) {
				maxId = p.getId();
			}
		}
		return maxId + 1;
	}

	public void addSubscription(Subscription p) {
		p.setId(generateNewId());
		subscriptions.add(p);
		saveChanges();
	}

	public void submitSubscriptionRequest(Customer k) {
		Subscription postojeca = findSubscriptionForCustomer(k.getId());

		if (k.getLateReturnCount() > 5) {
			if (postojeca != null) {
				postojeca.setStatus(SubscriptionStatus.REJECTED);
			} else {
				Subscription newSubscription = new Subscription(k, LocalDate.now(), SubscriptionStatus.REJECTED);
				this.addSubscription(newSubscription);
			}
			System.out.println("Request ODBIJEN because of too many delays (" + k.getLateReturnCount() + ").");
		} else {
			if (postojeca != null) {
				postojeca.setStatus(SubscriptionStatus.PENDING_APPROVAL);
			} else {
				Subscription newSubscription = new Subscription(k, LocalDate.now(), SubscriptionStatus.PENDING_APPROVAL);
				this.addSubscription(newSubscription);
			}
			System.out.println("Request submitted! Awaiting agent approval.");
		}

		saveChanges();
	}

	public void saveChanges() {
		try {
			write("subscriptions.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Subscription> getSubscriptions() {
		return subscriptions;
	}
}
