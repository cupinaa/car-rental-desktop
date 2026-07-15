package reservation;

public class ExtraService {

	protected int id;
	protected String serviceName;

	public ExtraService(int id, String serviceName) {
		this.id = id;
		this.serviceName = serviceName;
	}

	public ExtraService(String serviceName) {
		this.serviceName = serviceName;
	}

	@Override
	public String toString() {
	    return serviceName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

}
