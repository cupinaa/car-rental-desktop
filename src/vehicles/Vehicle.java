package vehicles;

public class Vehicle {

	protected int id;
	protected VehicleModel modelVozila;
	protected String licensePlate;
	protected VehicleStatus vehicleStatus;

	public Vehicle(int id, VehicleModel modelVozila, String licensePlate, VehicleStatus vehicleStatus) {
		this.id = id;
		this.modelVozila = modelVozila;
		this.licensePlate = licensePlate;
		this.vehicleStatus = vehicleStatus;
	}


	public Vehicle(VehicleModel modelVozila, String licensePlate, VehicleStatus vehicleStatus) {
		this.modelVozila = modelVozila;
		this.licensePlate = licensePlate;
		this.vehicleStatus = vehicleStatus;
	}


	@Override
	public String toString() {
	    return "Vehicle: " + licensePlate + " | Status: " + vehicleStatus +
	           " | Model: " + modelVozila.getManufacturer() + " " + modelVozila.getModelName();
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public VehicleModel getVehicleModel() {
		return modelVozila;
	}

	public void setVehicleModel(VehicleModel modelVozila) {
		this.modelVozila = modelVozila;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public VehicleStatus getVehicleStatus() {
		return vehicleStatus;
	}

	public void setVehicleStatus(VehicleStatus vehicleStatus) {
		this.vehicleStatus = vehicleStatus;
	}

}
