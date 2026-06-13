package rezervacija;

public class DodatnaUsluga {

	protected int id;
	protected String dodatnaUsluga;

	public DodatnaUsluga(int id, String dodatnaUsluga) {
		this.id = id;
		this.dodatnaUsluga = dodatnaUsluga;
	}

	public DodatnaUsluga(String dodatnaUsluga) {
		this.dodatnaUsluga = dodatnaUsluga;
	}

	@Override
	public String toString() {
	    return dodatnaUsluga;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDodatnaUsluga() {
		return dodatnaUsluga;
	}

	public void setDodatnaUsluga(String dodatnaUsluga) {
		this.dodatnaUsluga = dodatnaUsluga;
	}

}
