package test.data;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import users.Customer;
import users.Gender;
import users.Subscription;
import users.SubscriptionStatus;
import data.SubscriptionRepository;

public class SubscriptionRepositoryTest {

	private SubscriptionRepository pp;
	private String testPutanja = "test_subscriptions.csv";

	@Before
	public void setUp() {
		pp = new SubscriptionRepository() {
			@Override
			public void saveChanges() {

			}
		};
	}

	@After
	public void tearDown() {
		new File(testPutanja).delete();
	}

	@Test
	public void testAddSubscription() {
		int initialSize = pp.getSubscriptions().size();
		Customer k = new Customer(1, "A", "B", Gender.MALE, LocalDate.now(), "1", "A", "user", "pass", LocalDate.now());
		Subscription p = new Subscription(k, LocalDate.now().plusMonths(1), SubscriptionStatus.ACTIVE);

		pp.addSubscription(p);

		assertEquals("The subscription count must increase", initialSize + 1, pp.getSubscriptions().size());
		assertTrue("ID must be setovan", p.getId() > 0);
	}

	@Test
	public void testFindSubscription() {
		Customer k = new Customer(1, "A", "B", Gender.MALE, LocalDate.now(), "1", "A", "user", "pass", LocalDate.now());
		Subscription p = new Subscription(k, LocalDate.now().plusMonths(1), SubscriptionStatus.ACTIVE);
		pp.addSubscription(p);

		assertNotNull("Must find a subscription by ID", pp.findSubscription(p.getId()));
		assertNotNull("Must find a subscription by customer", pp.findSubscriptionForCustomer(k.getId()));
	}

	@Test
	public void testSubmitSubscriptionRequest() {
		Customer k1 = new Customer(1, "A", "B", Gender.MALE, LocalDate.now(), "1", "A", "user", "pass", LocalDate.now());
		k1.setLateReturnCount(2);

		pp.submitSubscriptionRequest(k1);

		Subscription p1 = pp.findSubscriptionForCustomer(k1.getId());
		assertNotNull("Subscription za prvog must be created", p1);
		assertEquals("Status must be PENDING_APPROVAL posto ima malo delays", SubscriptionStatus.PENDING_APPROVAL, p1.getStatus());

		Customer k2 = new Customer(2, "C", "D", Gender.MALE, LocalDate.now(), "1", "A", "user2", "pass", LocalDate.now());
		k2.setLateReturnCount(6);

		pp.submitSubscriptionRequest(k2);

		Subscription p2 = pp.findSubscriptionForCustomer(k2.getId());
		assertNotNull("Subscription za drugog must be created", p2);
		assertEquals("Status must be REJECTED because of previse delays", SubscriptionStatus.REJECTED, p2.getStatus());
	}

	@Test
	public void testLoadajUpisi() throws IOException {

		assertTrue(true);
	}
}
