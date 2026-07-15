package users;

import java.time.LocalDate;

public class Subscription {

	protected int id;
	protected Customer customer;
	protected LocalDate expiryDate;
	protected SubscriptionStatus status;

	public Subscription(int id, Customer customer, LocalDate expiryDate, SubscriptionStatus status) {
		this.id = id;
		this.customer = customer;
		this.expiryDate = expiryDate;
		this.status = status;
	}

	public Subscription(Customer customer, LocalDate expiryDate, SubscriptionStatus status) {
		this.customer = customer;
		this.expiryDate = expiryDate;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public LocalDate getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
	}

	public SubscriptionStatus getStatus() {
		return status;
	}

	public void setStatus(SubscriptionStatus status) {
		this.status = status;
	}

}
