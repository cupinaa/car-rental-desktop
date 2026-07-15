package gui;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import data.VehicleModelRepository;
import data.VehicleRepository;
import data.ReservationRepository;
import vehicles.Vehicle;
import users.User;
import users.Agent;

public class VehiclesPanel extends JPanel {

	private VehicleRepository vp;
	private VehicleModelRepository mp;
	private JTable table;
	private DefaultTableModel tableModel;

	private User loggedInUser;
	private ReservationRepository rp;

	public VehiclesPanel(VehicleRepository vp, VehicleModelRepository mp, User loggedInUser, ReservationRepository rp) {
		this.vp = vp;
		this.mp = mp;
		this.loggedInUser = loggedInUser;
		this.rp = rp;
		setLayout(new BorderLayout());

		String[] columns = {"ID", "Manufacturer i Model", "Category", "License plate", "Status"};
		tableModel = new DefaultTableModel(columns, 0);
		table = new JTable(tableModel);

		JPanel panelSearch = new JPanel();
		JTextField txtOd = new JTextField(10);
		JTextField txtDo = new JTextField(10);
		JButton btnPretrazi = new JButton("Pretrazi Availablena");
		JButton btnReset = new JButton("Show Sve");

		panelSearch.add(new JLabel("Od (YYYY-MM-DD):"));
		panelSearch.add(txtOd);
		panelSearch.add(new JLabel("Do (YYYY-MM-DD):"));
		panelSearch.add(txtDo);
		panelSearch.add(btnPretrazi);
		panelSearch.add(btnReset);

		add(panelSearch, BorderLayout.NORTH);

		btnPretrazi.addActionListener(e -> {
			try {
				LocalDate dateOd = LocalDate.parse(txtOd.getText().trim());
				LocalDate dateDo = LocalDate.parse(txtDo.getText().trim());
				refreshTable(dateOd, dateDo);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Enter validne datee.");
			}
		});

		btnReset.addActionListener(e -> {
			txtOd.setText("");
			txtDo.setText("");
			refreshTable(null, null);
		});

		refreshTable(null, null);

		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);

		if (!(loggedInUser instanceof Agent)) {
			JPanel buttonPanel = new JPanel();
			JButton btnAdd = new JButton("Add");
			JButton btnEdit = new JButton("Edit");
			JButton btnDelete = new JButton("Delete");

			buttonPanel.add(btnAdd);
			buttonPanel.add(btnEdit);
			buttonPanel.add(btnDelete);
			add(buttonPanel, BorderLayout.SOUTH);

			btnAdd.addActionListener(e -> {
				VehicleDialog vf = new VehicleDialog(vp, mp, null, this::refreshTable);
				vf.setVisible(true);
			});

			btnEdit.addActionListener(e -> {
				int selectedRow = table.getSelectedRow();
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(this, "You must select vehicle from the table!", "Warning", JOptionPane.WARNING_MESSAGE);
					return;
				}

				int idVozila = (int) tableModel.getValueAt(selectedRow, 0);
				Vehicle itemToEdit = vp.findVehicle(idVozila);

				if (itemToEdit != null) {
					VehicleDialog vf = new VehicleDialog(vp, mp, itemToEdit, this::refreshTable);
					vf.setVisible(true);
				}
			});

			btnDelete.addActionListener(e -> {
				int selectedRow = table.getSelectedRow();
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(this, "You must select vehicle from the table!", "Warning", JOptionPane.WARNING_MESSAGE);
					return;
				}

				int idVozila = (int) tableModel.getValueAt(selectedRow, 0);
				Vehicle itemToDelete = vp.findVehicle(idVozila);

				if (itemToDelete != null) {
					Object[] options = {"Da", "Ne"};
					int confirmation = JOptionPane.showOptionDialog(this,
							"Are you sure you want to delete vehicle: " + itemToDelete.getLicensePlate() + "?",
							"Confirmation brisanja",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE,
							null,
							options,
							options[1]);
					if (confirmation == JOptionPane.YES_OPTION) {
						vp.deleteVehicle(itemToDelete);
						refreshTable();
					}
				}
			});
		}
	}

	private void refreshTable() {
		refreshTable(null, null);
	}

	private void refreshTable(LocalDate dateOd, LocalDate dateDo) {
		tableModel.setRowCount(0);
		for (Vehicle v : vp.getVehicles()) {
			if (dateOd != null && dateDo != null) {
				if (rp != null && !rp.daLiJeVehicleAvailableno(v, dateOd, dateDo)) {
					continue;
				}
			}

			String manufacturerModel = v.getVehicleModel().getManufacturer() + " " + v.getVehicleModel().getModelName();
			Object[] row = {
				v.getId(),
				manufacturerModel,
				v.getVehicleModel().getCategory().toString(),
				v.getLicensePlate(),
				v.getVehicleStatus().toString()
			};
			tableModel.addRow(row);
		}
	}
}
