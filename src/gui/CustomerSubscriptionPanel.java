package gui;
import users.SubscriptionStatus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import users.Customer;
import users.Subscription;
import data.SubscriptionRepository;

public class CustomerSubscriptionPanel extends JPanel {

	private SubscriptionRepository pp;
	private Customer customer;

	private JLabel lblNaslov;
	private JLabel lblStatus;
	private JLabel lblIstek;
	private JButton btnSubmit;

	public CustomerSubscriptionPanel(SubscriptionRepository pp, Customer customer) {
		this.pp = pp;
		this.customer = customer;

		setLayout(new BorderLayout());

		JPanel centerPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.gridx = 0;
		gbc.gridy = 0;

		lblNaslov = new JLabel("Status Youre Subscriptions", SwingConstants.CENTER);
		centerPanel.add(lblNaslov, gbc);

		gbc.gridy++;
		lblStatus = new JLabel("", SwingConstants.CENTER);
		centerPanel.add(lblStatus, gbc);

		gbc.gridy++;
		lblIstek = new JLabel("", SwingConstants.CENTER);
		centerPanel.add(lblIstek, gbc);

		gbc.gridy++;
		btnSubmit = new JButton("Submit a subscription request");
		centerPanel.add(btnSubmit, gbc);

		add(centerPanel, BorderLayout.CENTER);

		btnSubmit.addActionListener(e -> submitRequest());

		refreshPrikaz();
	}

	private void refreshPrikaz() {
		Subscription p = pp.findSubscriptionForCustomer(customer.getId());

		if (p == null) {
			lblStatus.setText("You do not currently have an active subscription.");
			lblIstek.setText(" ");
			btnSubmit.setEnabled(true);
		} else {
			lblStatus.setText("Status: " + p.getStatus());
			lblIstek.setText("Valid until: " + p.getExpiryDate());

			if (p.getStatus() == SubscriptionStatus.ACTIVE || p.getStatus() == SubscriptionStatus.PENDING_APPROVAL) {
				btnSubmit.setEnabled(false);
			} else {
				btnSubmit.setEnabled(true);
			}
		}
	}

	private void submitRequest() {
		Object[] options = {"Da", "Ne"};
		int odziv = JOptionPane.showOptionDialog(this,
			"Are you sure you want to submit request for a new subscription?",
			"Submit request",
			JOptionPane.YES_NO_OPTION,
			JOptionPane.QUESTION_MESSAGE,
			null,
			options,
			options[0]
		);

		if (odziv == 0) {
			pp.submitSubscriptionRequest(customer);

			Subscription updatedSubscription = pp.findSubscriptionForCustomer(customer.getId());
			if (updatedSubscription != null && updatedSubscription.getStatus() == SubscriptionStatus.REJECTED) {
				JOptionPane.showMessageDialog(this, "Your request je AUTOMATSKI ODBIJEN because of too many delays (" + customer.getLateReturnCount() + ")!", "Request rejected", JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, "Request je successfully podnet! Await approvedje by agenta.");
			}
			refreshPrikaz();
		}
	}
}
