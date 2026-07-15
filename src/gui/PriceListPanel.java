package gui;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import pricing.PriceList;
import data.PriceListRepository;
import data.ExtraServiceRepository;

public class PriceListPanel extends JPanel {

	private PriceListRepository cp;
	private ExtraServiceRepository dup;
	private JTable table;
	private DefaultTableModel tableModel;

	public PriceListPanel(PriceListRepository cp, ExtraServiceRepository dup) {
		this.cp = cp;
		this.dup = dup;
		setLayout(new BorderLayout());

		String[] columns = {"ID", "Valid from", "Valid until", "Subscription (RSD)", "Late fee/day"};
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
			PriceListDialog cf = new PriceListDialog(cp, dup, null, this::refreshTable);
			cf.setVisible(true);
		});

		btnEdit.addActionListener(e -> {
			int selectedRow = table.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(this, "You must select pricing from the table!", "Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}

			int idPriceLista = (int) tableModel.getValueAt(selectedRow, 0);
			PriceList itemToEdit = cp.findPriceList(idPriceLista);

			if (itemToEdit != null) {
				PriceListDialog cf = new PriceListDialog(cp, dup, itemToEdit, this::refreshTable);
				cf.setVisible(true);
			}
		});

		btnDelete.addActionListener(e -> {
			int selectedRow = table.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(this, "You must select pricing from the table!", "Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}

			int idPriceLista = (int) tableModel.getValueAt(selectedRow, 0);
			PriceList itemToDelete = cp.findPriceList(idPriceLista);

			if (itemToDelete != null) {
				Object[] options = {"Da", "Ne"};
				int confirmation = JOptionPane.showOptionDialog(this,
						"Are you sure you want to delete ovaj pricing?",
						"Confirmation brisanja",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null,
						options,
						options[1]);
				if (confirmation == JOptionPane.YES_OPTION) {
					cp.deletePriceList(itemToDelete);
					refreshTable();
				}
			}
		});
	}

	private void refreshTable() {
		tableModel.setRowCount(0);
		for (PriceList c : cp.getPriceLists()) {
			Object[] row = {
				c.getId(),
				c.getValidFrom().toString(),
				c.getValidUntil().toString(),
				c.getSubscriptionPrice(),
				c.getLateFee()
			};
			tableModel.addRow(row);
		}
	}
}
