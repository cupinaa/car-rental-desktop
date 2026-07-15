package data;
import java.util.HashMap;
import java.util.Map;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;

import pricing.PriceList;
import pricing.PriceListItem;
import vehicles.VehicleCategory;

public class PriceListRepository {

	protected ArrayList<PriceList> cenovnici = new ArrayList<>();

	public void load(String putCenovnici, String putStavke, String putStavkeService) throws IOException {
		BufferedReader brC = new BufferedReader(new FileReader(putCenovnici));
		String linija;
		while ((linija = brC.readLine()) != null) {
			String[] delovi = linija.split("\\|");
			int id = Integer.parseInt(delovi[0]);
			LocalDate start = LocalDate.parse(delovi[1]);
			LocalDate end = LocalDate.parse(delovi[2]);

			PriceList c = null;
			if (delovi.length > 3) {
				double subscriptionPrice = Double.parseDouble(delovi[3]);
				double studentDiscount = Double.parseDouble(delovi[4]);
				double companyDiscount = Double.parseDouble(delovi[5]);
				double retireeDiscount = Double.parseDouble(delovi[6]);
				double lateFee = Double.parseDouble(delovi[7]);
				c = new PriceList(id, start, end, new ArrayList<PriceListItem>(), subscriptionPrice, studentDiscount, companyDiscount, retireeDiscount, lateFee, new HashMap<>());
			} else {
				c = new PriceList(id, start, end, new ArrayList<PriceListItem>(), 5000.0, 0.10, 0.25, 0.15, 2000.0, new HashMap<>());
			}
			cenovnici.add(c);
		}
		brC.close();

		BufferedReader brS = new BufferedReader(new FileReader(putStavke));
		while ((linija = brS.readLine()) != null) {
			String[] delovi = linija.split("\\|");
			int idStavke = Integer.parseInt(delovi[0]);
			int idPriceLista = Integer.parseInt(delovi[1]);
			VehicleCategory category = VehicleCategory.valueOf(delovi[2]);
			double price = Double.parseDouble(delovi[3]);

			PriceListItem stavka = new PriceListItem(idStavke, category, price);

			PriceList c = findPriceList(idPriceLista);
			if (c != null) {
				c.getStavkePriceLista().add(stavka);
			}
		}
		brS.close();

		try {
			BufferedReader brU = new BufferedReader(new FileReader(putStavkeService));
			while ((linija = brU.readLine()) != null) {
				String[] delovi = linija.split("\\|");
				int idPriceLista = Integer.parseInt(delovi[0]);
				int idServices = Integer.parseInt(delovi[1]);
				double price = Double.parseDouble(delovi[2]);

				PriceList c = findPriceList(idPriceLista);
				if (c != null) {
					c.getExtraServicePrices().put(idServices, price);
				}
			}
			brU.close();
		} catch (Exception e) {

		}
	}

	public void write(String putCenovnici, String putStavke, String putStavkeService) throws IOException {
		PrintWriter pwC = new PrintWriter(new FileWriter(putCenovnici));
		PrintWriter pwS = new PrintWriter(new FileWriter(putStavke));
		PrintWriter pwU = new PrintWriter(new FileWriter(putStavkeService));

		for (PriceList c : cenovnici) {
			pwC.println(c.getId() + "|" + c.getValidFrom() + "|" + c.getValidUntil() + "|" +
						c.getSubscriptionPrice() + "|" + c.getStudentDiscount() + "|" +
						c.getCompanyDiscount() + "|" + c.getRetireeDiscount() + "|" + c.getLateFee());
			for (PriceListItem s : c.getStavkePriceLista()) {
				pwS.println(s.getId() + "|" + c.getId() + "|" + s.getVehicleCategory() + "|" + s.getDailyPrice());
			}
			if (c.getExtraServicePrices() != null) {
				for (Map.Entry<Integer, Double> entry : c.getExtraServicePrices().entrySet()) {
					pwU.println(c.getId() + "|" + entry.getKey() + "|" + entry.getValue());
				}
			}
		}
		pwC.close();
		pwS.close();
		pwU.close();
	}

	public PriceList findPriceList(int id) {
		for (PriceList c : cenovnici) {
			if (c.getId() == id)
				return c;
		}
		return null;
	}

	public PriceList findVazeciPriceList(LocalDate date) {
		for (PriceList c : cenovnici) {
			if ((c.getValidFrom().isBefore(date) || c.getValidFrom().isEqual(date))
					&& (c.getValidUntil().isAfter(date) || c.getValidUntil().isEqual(date))) {
				return c;
			}
		}
		return null;

	}

	private int generateNewId() {
		int maxId = 0;
		for (PriceList c : cenovnici) {
			if (c.getId() > maxId) {
				maxId = c.getId();
			}
		}
		return maxId + 1;
	}

	private int generateNewIdStavkeGlobal() {
		int maxId = 0;
		for (PriceList c : cenovnici) {
			for (PriceListItem sc : c.getStavkePriceLista()) {
				if (sc.getId() > maxId) {
					maxId = sc.getId();
				}
			}
		}
		return maxId + 1;
	}

	public void addPriceList(PriceList c) {
		c.setId(generateNewId());

		int globalStavkaId = generateNewIdStavkeGlobal();
		for(PriceListItem sc : c.getStavkePriceLista()) {
			sc.setId(globalStavkaId++);
		}

		cenovnici.add(c);
		saveChanges();
	}

	public void deletePriceList(PriceList c) {
		cenovnici.remove(c);
		saveChanges();
	}

	public void saveChanges() {
		try {
			write("price-lists.csv", "stavke_pricinga.csv", "stavke_service_pricinga.csv");
		} catch (IOException e) {
			System.out.println("Error pri saving pricinga: " + e.getMessage());
		}
	}

	public ArrayList<PriceList> getPriceLists() {
		return cenovnici;
	}
}
