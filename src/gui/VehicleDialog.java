package gui;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import data.VehicleModelRepository;
import data.VehicleRepository;
import vehicles.VehicleModel;
import vehicles.VehicleStatus;
import vehicles.Vehicle;

public class VehicleDialog extends JDialog {

	private VehicleRepository vp;
	private Runnable onSuccess;
	private Vehicle vehicleToEdit;

	private JComboBox<VehicleModel> cbModel;
	private JTextField txtLicensePlate;
	private JComboBox<VehicleStatus> cbStatus;

	public VehicleDialog(VehicleRepository vp, VehicleModelRepository mp, Vehicle vehicleToEdit, Runnable onSuccess) {
		this.vp = vp;
		this.onSuccess = onSuccess;
		this.vehicleToEdit = vehicleToEdit;

		setTitle(vehicleToEdit == null ? "Novo vehicle" : "Izmena vehicles");
		setSize(400, 250);
		setLocationRelativeTo(null);
		setModal(true);
		setLayout(new GridLayout(4, 2, 5, 5));

		add(new JLabel("Model vehicles:"));
		cbModel = new JComboBox<>();
		for (VehicleModel m : mp.getModels()) {
			cbModel.addItem(m);
		}
		add(cbModel);

		add(new JLabel("Registarske tablice:"));
		txtLicensePlate = new JTextField();
		add(txtLicensePlate);

		add(new JLabel("Status vehicles:"));
		cbStatus = new JComboBox<>(VehicleStatus.values());
		add(cbStatus);

		JButton btnSave = new JButton("Save");
		add(new JLabel(""));
		add(btnSave);

		if (vehicleToEdit != null) {
			populateFields();
		}

		btnSave.addActionListener(e -> saveVehicle());
	}

	private void populateFields() {
		txtLicensePlate.setText(vehicleToEdit.getLicensePlate());
		cbStatus.setSelectedItem(vehicleToEdit.getVehicleStatus());



		cbModel.setSelectedItem(vehicleToEdit.getVehicleModel());
	}

	private void saveVehicle() {
		try {
			VehicleModel model = (VehicleModel) cbModel.getSelectedItem();
			String licensePlate = txtLicensePlate.getText();
			VehicleStatus status = (VehicleStatus) cbStatus.getSelectedItem();

			if (model == null || licensePlate.trim().isEmpty()) {
				JOptionPane.showMessageDialog(this, "You must select model i enter registraciju!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (vehicleToEdit == null) {
				Vehicle v = new Vehicle(0, model, licensePlate, status);
				vp.addVehicle(v);
				JOptionPane.showMessageDialog(this, "Successfully added!");
			} else {
				vehicleToEdit.setVehicleModel(model);
				vehicleToEdit.setLicensePlate(licensePlate);
				vehicleToEdit.setVehicleStatus(status);
				vp.saveChanges();
				JOptionPane.showMessageDialog(this, "Successfully changedo!");
			}

			onSuccess.run();
			dispose();

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error while saving data.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
