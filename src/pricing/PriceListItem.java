package pricing;

import vehicles.VehicleCategory;

public class PriceListItem {

	protected int id;
	protected VehicleCategory vehicleCategory;
	protected double dailyPrice;

	public PriceListItem(int id, VehicleCategory vehicleCategory, double dailyPrice) {
		this.id = id;
		this.vehicleCategory = vehicleCategory;
		this.dailyPrice = dailyPrice;
	}

	public PriceListItem(VehicleCategory vehicleCategory, double dailyPrice) {
		this.vehicleCategory = vehicleCategory;
		this.dailyPrice = dailyPrice;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public VehicleCategory getVehicleCategory() {
		return vehicleCategory;
	}

	public void setVehicleCategory(VehicleCategory vehicleCategory) {
		this.vehicleCategory = vehicleCategory;
	}

	public double getDailyPrice() {
		return dailyPrice;
	}

	public void setDailyPrice(double dailyPrice) {
		this.dailyPrice = dailyPrice;
	}

}
