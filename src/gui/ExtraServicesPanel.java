package gui;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import data.ExtraServiceRepository;
import reservation.ExtraService;

public class ExtraServicesPanel extends JPanel {

	private ExtraServiceRepository dup;
	private JTable table;
	private DefaultTableModel tableModel;

	public ExtraServicesPanel(ExtraServiceRepository dup) {
		this.dup = dup;
		setLayout(new BorderLayout());

		String[] columns = {"ID", "Name Services"};
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
			ExtraServiceDialog uf = new ExtraServiceDialog(dup, null, this::refreshTable);
			uf.setVisible(true);
		});

		btnEdit.addActionListener(e -> {
			int selectedRow = table.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(this, "You must select a service from the table!", "Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}

			int idServices = (int) tableModel.getValueAt(selectedRow, 0);
			ExtraService itemToEdit = dup.findService(idServices);

			if (itemToEdit != null) {
				ExtraServiceDialog uf = new ExtraServiceDialog(dup, itemToEdit, this::refreshTable);
				uf.setVisible(true);
			}
		});

		btnDelete.addActionListener(e -> {
			int selectedRow = table.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(this, "You must select a service from the table!", "Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}

			int idServices = (int) tableModel.getValueAt(selectedRow, 0);
			ExtraService itemToDelete = dup.findService(idServices);

			if (itemToDelete != null) {
				Object[] options = {"Da", "Ne"};
				int confirmation = JOptionPane.showOptionDialog(this,
						"Are you sure you want to delete the service: " + itemToDelete.getServiceName() + "?",
						"Confirmation brisanja",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null,
						options,
						options[1]);
				if (confirmation == JOptionPane.YES_OPTION) {
					dup.deleteService(itemToDelete);
					refreshTable();
				}
			}
		});
	}

	private void refreshTable() {
		tableModel.setRowCount(0);
		for (ExtraService du : dup.getServices()) {
			Object[] row = {
				du.getId(),
				du.getServiceName()
			};
			tableModel.addRow(row);
		}
	}
}
