package users;

public enum EducationLevel {

	HIGH_SCHOOL(1.0), BACHELORS(1.5), MASTERS(1.8);

	private double salaryMultiplier;

	EducationLevel(double salaryMultiplier){
		this.salaryMultiplier = salaryMultiplier;
	}

	public double getSalaryMultiplier() {
		return salaryMultiplier;
	}


}
