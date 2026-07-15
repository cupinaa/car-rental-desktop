package vehicles;

public class VehicleModel {

	protected int id;
	protected String manufacturer;
	protected String modelName;
	protected VehicleCategory category;

	public VehicleModel(String manufacturer, String modelName, VehicleCategory category) {
		this.manufacturer = manufacturer;
		this.modelName = modelName;
		this.category = category;
	}

	public VehicleModel(int id, String manufacturer, String modelName, VehicleCategory category) {
		this.id = id;
		this.manufacturer = manufacturer;
		this.modelName = modelName;
		this.category = category;
	}

	@Override
	public String toString() {
		return "Model: " + manufacturer + " " + modelName + " (" + category + ")";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public VehicleCategory getCategory() {
		return category;
	}

	public void setCategory(VehicleCategory category) {
		this.category = category;
	}


}
