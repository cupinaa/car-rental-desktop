package gui;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import data.VehicleModelRepository;
import vehicles.VehicleModel;

public class VehicleModelsPanel extends JPanel {

	private VehicleModelRepository mp;
	private JTable table;
	private DefaultTableModel tableModel;

	public VehicleModelsPanel(VehicleModelRepository mp) {
		this.mp = mp;
		setLayout(new BorderLayout());

		String[] columns = {"ID", "Manufacturer", "Model", "Category"};
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
			new VehicleModelDialog(mp, null, this::refreshTable).setVisible(true);
		});

		btnEdit.addActionListener(e -> {
			int row = table.getSelectedRow();
			if (row == -1) { JOptionPane.showMessageDialog(this, "Select a model."); return; }
			int id = (int) tableModel.getValueAt(row, 0);
			VehicleModel m = mp.findModel(id);
			if (m != null) { new VehicleModelDialog(mp, m, this::refreshTable).setVisible(true); }
		});

		btnDelete.addActionListener(e -> {
			int row = table.getSelectedRow();
			if (row == -1) { JOptionPane.showMessageDialog(this, "Select a model."); return; }
			int id = (int) tableModel.getValueAt(row, 0);
			VehicleModel m = mp.findModel(id);
			if (m != null && JOptionPane.showConfirmDialog(this, "Sigurno?", "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				mp.getModels().remove(m);
				try { mp.write("vehicle-models.csv"); refreshTable(); } catch (Exception ex) {}
			}
		});
	}

	private void refreshTable() {
		tableModel.setRowCount(0);
		for (VehicleModel m : mp.getModels()) {
			Object[] row = { m.getId(), m.getManufacturer(), m.getModelName(), m.getCategory() };
			tableModel.addRow(row);
		}
	}
}
