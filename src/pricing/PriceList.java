package pricing;
import java.util.Map;

import java.time.LocalDate;
import java.util.ArrayList;

public class PriceList {

	protected int id;
	protected LocalDate validFrom;
	protected LocalDate validUntil;
	protected ArrayList<PriceListItem> stavkePriceLista;

	protected double subscriptionPrice;
	protected double studentDiscount;
	protected double companyDiscount;
	protected double retireeDiscount;
	protected double lateFee;

	protected Map<Integer, Double> extraServicePrices;

	public PriceList(int id, LocalDate validFrom, LocalDate validUntil,
			ArrayList<PriceListItem> stavkePriceLista, double subscriptionPrice,
			double studentDiscount, double companyDiscount, double retireeDiscount, double lateFee, Map<Integer, Double> extraServicePrices) {
		this.id = id;
		this.validFrom = validFrom;
		this.validUntil = validUntil;
		this.stavkePriceLista = stavkePriceLista;
		this.subscriptionPrice = subscriptionPrice;
		this.studentDiscount = studentDiscount;
		this.companyDiscount = companyDiscount;
		this.retireeDiscount = retireeDiscount;
		this.lateFee = lateFee;
		this.extraServicePrices = extraServicePrices;
	}

	public PriceList(LocalDate validFrom, LocalDate validUntil, ArrayList<PriceListItem> stavkePriceLista,
			double subscriptionPrice, double studentDiscount, double companyDiscount, double retireeDiscount, double lateFee, Map<Integer, Double> extraServicePrices) {
		this.validFrom = validFrom;
		this.validUntil = validUntil;
		this.stavkePriceLista = stavkePriceLista;
		this.subscriptionPrice = subscriptionPrice;
		this.studentDiscount = studentDiscount;
		this.companyDiscount = companyDiscount;
		this.retireeDiscount = retireeDiscount;
		this.lateFee = lateFee;
		this.extraServicePrices = extraServicePrices;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDate getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(LocalDate validFrom) {
		this.validFrom = validFrom;
	}

	public LocalDate getValidUntil() {
		return validUntil;
	}

	public void setValidUntil(LocalDate validUntil) {
		this.validUntil = validUntil;
	}

	public ArrayList<PriceListItem> getStavkePriceLista() {
		return stavkePriceLista;
	}

	public void setStavkePriceLista(ArrayList<PriceListItem> stavkePriceLista) {
		this.stavkePriceLista = stavkePriceLista;
	}

	public double getSubscriptionPrice() {
		return subscriptionPrice;
	}

	public void setSubscriptionPrice(double subscriptionPrice) {
		this.subscriptionPrice = subscriptionPrice;
	}

	public double getStudentDiscount() {
		return studentDiscount;
	}

	public void setStudentDiscount(double studentDiscount) {
		this.studentDiscount = studentDiscount;
	}

	public double getCompanyDiscount() {
		return companyDiscount;
	}

	public void setCompanyDiscount(double companyDiscount) {
		this.companyDiscount = companyDiscount;
	}

	public double getRetireeDiscount() {
		return retireeDiscount;
	}

	public void setRetireeDiscount(double retireeDiscount) {
		this.retireeDiscount = retireeDiscount;
	}

	public double getLateFee() {
		return lateFee;
	}

	public void setLateFee(double lateFee) {
		this.lateFee = lateFee;
	}

	public Map<Integer, Double> getExtraServicePrices() {
		return extraServicePrices;
	}

	public void setExtraServicePrices(Map<Integer, Double> extraServicePrices) {
		this.extraServicePrices = extraServicePrices;
	}

}
