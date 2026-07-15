package gui;
import java.time.LocalDate;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import users.Subscription;
import users.SubscriptionStatus;
import data.SubscriptionRepository;

public class SubscriptionsPanel extends JPanel {

	private SubscriptionRepository pp;
	private JTable table;
	private DefaultTableModel tableModel;
	private users.User loggedInUser;

	public SubscriptionsPanel(SubscriptionRepository pp, users.User loggedInUser) {
		this.pp = pp;
		this.loggedInUser = loggedInUser;
		setLayout(new BorderLayout());

		String[] columns = {"ID", "Customer", "Date Isteka", "Status"};
		tableModel = new DefaultTableModel(columns, 0);
		table = new JTable(tableModel);

		refreshTable();

		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		JButton btnApprove = new JButton("Approve");
		JButton btnOdbij = new JButton("Odbij");

		buttonPanel.add(btnApprove);
		buttonPanel.add(btnOdbij);
		add(buttonPanel, BorderLayout.SOUTH);

		btnApprove.addActionListener(e -> promeniStatus(SubscriptionStatus.ACTIVE));
		btnOdbij.addActionListener(e -> promeniStatus(SubscriptionStatus.REJECTED));

		if (loggedInUser instanceof users.Administrator) {
			JButton btnEdit = new JButton("Edit");
			JButton btnDelete = new JButton("Delete");
			buttonPanel.add(btnEdit);
			buttonPanel.add(btnDelete);

			btnEdit.addActionListener(e -> {
				int row = table.getSelectedRow();
				if (row == -1) { JOptionPane.showMessageDialog(this, "Select a subscription."); return; }
				int id = (int) tableModel.getValueAt(row, 0);
				Subscription p = pp.findSubscription(id);
				if (p != null) { new SubscriptionDialog(pp, p, this::refreshTable).setVisible(true); }
			});

			btnDelete.addActionListener(e -> {
				int row = table.getSelectedRow();
				if (row == -1) { JOptionPane.showMessageDialog(this, "Select a subscription."); return; }
				int id = (int) tableModel.getValueAt(row, 0);
				Subscription p = pp.findSubscription(id);
				if (p != null && JOptionPane.showConfirmDialog(this, "Sigurno?", "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					pp.getSubscriptions().remove(p);
					try { pp.write("subscriptions.csv"); refreshTable(); } catch (Exception ex) {}
				}
			});
		}
	}

	private void promeniStatus(SubscriptionStatus newStatus) {
		int selectedRow = table.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "You must select a subscription from the table!", "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}

		int idSubscriptions = (int) tableModel.getValueAt(selectedRow, 0);
		Subscription p = pp.findSubscription(idSubscriptions);

		if (p != null) {
			if (p.getStatus() != SubscriptionStatus.PENDING_APPROVAL) {
				JOptionPane.showMessageDialog(this, "Only subscriptions awaiting approval can change status.", "Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}

			p.setStatus(newStatus);


			if (newStatus == SubscriptionStatus.ACTIVE) {
				p.setExpiryDate(LocalDate.now().plusYears(1));
			}

			pp.saveChanges();
			refreshTable();
			JOptionPane.showMessageDialog(this, "Status subscriptions je successfully changed u " + newStatus + ".");
		}
	}

	private void refreshTable() {
		tableModel.setRowCount(0);
		for (Subscription p : pp.getSubscriptions()) {
			Object[] row = {
				p.getId(),
				p.getCustomer().getFirstName() + " " + p.getCustomer().getLastName() + " (" + p.getCustomer().getUsername() + ")",
				p.getExpiryDate() == null ? "Nije definisano" : p.getExpiryDate().toString(),
				p.getStatus().toString()
			};
			tableModel.addRow(row);
		}
	}
}
