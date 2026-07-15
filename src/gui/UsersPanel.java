package gui;
import javax.swing.JOptionPane;
import users.Employee;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import data.UserRepository;
import users.User;
import users.Administrator;
import users.Agent;
import users.Customer;

public class UsersPanel extends JPanel {

	private UserRepository kp;
	private JTable table;
	private DefaultTableModel tableModel;

	private User loggedInUser;
	private boolean showCustomere;

	public UsersPanel(UserRepository kp, User loggedInUser, boolean showCustomere) {
		this.kp = kp;
		this.loggedInUser = loggedInUser;
		this.showCustomere = showCustomere;
		setLayout(new BorderLayout());

		String[] columns;
		if (showCustomere) {
			columns = new String[]{"ID", "FirstName", "LastName", "E-mail", "Category", "Blocked until", "License issue date"};
		} else {
			columns = new String[]{"ID", "Role", "FirstName", "LastName", "E-mail", "Salary"};
		}
		tableModel = new DefaultTableModel(columns, 0);
		table = new JTable(tableModel);

		refreshTable();

		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		JButton btnAdd = new JButton("Add");
		JButton btnEdit = new JButton("Edit");
		JButton btnDelete = new JButton("Delete");

		buttonPanel.add(btnAdd);
		buttonPanel.add(btnEdit);
		buttonPanel.add(btnDelete);
		add(buttonPanel, BorderLayout.SOUTH);

		btnAdd.addActionListener(e -> {
			UserDialog kf = new UserDialog(kp, null, () -> refreshTable(), loggedInUser, showCustomere);
			kf.setVisible(true);
		});

		btnDelete.addActionListener(e -> {
			int selectedRow = table.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(this, "You must select usera from the table!", "Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}

			int idUsera = (int) tableModel.getValueAt(selectedRow, 0);
			User itemToDelete = null;
			for (User k : kp.getUsers()) {
				if (k.getId() == idUsera) { itemToDelete = k; break; }
			}

			if (itemToDelete != null) {
				Object[] options = {"Da", "Ne"};
				int confirmation = JOptionPane.showOptionDialog(this,
						"Are you sure you want to delete usera: " + itemToDelete.getUsername() + "?",
						"Confirmation brisanja",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null,
						options,
						options[1]);
				if (confirmation == JOptionPane.YES_OPTION) {
					kp.deleteUsera(itemToDelete);
					refreshTable();
				}
			}
		});

		btnEdit.addActionListener(e -> {
			int selectedRow = table.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(this, "You must select usera from the table!", "Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}

			int idUsera = (int) tableModel.getValueAt(selectedRow, 0);
			User itemToEdit = null;
			for (User k : kp.getUsers()) {
				if (k.getId() == idUsera) { itemToEdit = k; break; }
			}

			if (itemToEdit != null) {

				UserDialog kf = new UserDialog(kp, itemToEdit, this::refreshTable, loggedInUser, showCustomere);
				kf.setVisible(true);
			}
		});
	}

	private void refreshTable() {
		tableModel.setRowCount(0);
		for (User k : kp.getUsers()) {
			if (showCustomere && !(k instanceof Customer)) continue;
			if (!showCustomere && (k instanceof Customer)) continue;

			String uloga = k.getClass().getSimpleName();

			if (showCustomere) {
				Customer kl = (Customer) k;
				String category = kl.getCustomerCategory() != null ? kl.getCustomerCategory().name() : "NEMA";
				String zabrana = kl.getBookingBlockedUntil() != null ? kl.getBookingBlockedUntil().toString() : "-";
				Object[] row = { kl.getId(), kl.getFirstName(), kl.getLastName(), kl.getUsername(), category, zabrana, kl.getLicenseIssueDate().toString() };
				tableModel.addRow(row);
			} else {
				String plata = "-";
				if (k instanceof Employee) {
					plata = String.valueOf(((Employee) k).getSalary());
				}
				Object[] row = { k.getId(), uloga, k.getFirstName(), k.getLastName(), k.getUsername(), plata };
				tableModel.addRow(row);
			}
		}
	}
}
