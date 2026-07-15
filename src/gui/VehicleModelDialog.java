package gui;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import data.VehicleModelRepository;
import vehicles.VehicleCategory;
import vehicles.VehicleModel;

public class VehicleModelDialog extends JDialog {

	private JTextField txtManufacturer;
	private JTextField txtName;
	private JComboBox<VehicleCategory> cbCategory;

	public VehicleModelDialog(VehicleModelRepository mp, VehicleModel m, Runnable onSave) {
		setTitle(m == null ? "New Model" : "Edit Model");
		setSize(300, 200);
		setLocationRelativeTo(null);
		setModal(true);
		setLayout(new GridLayout(4, 2, 10, 10));

		add(new JLabel("Manufacturer:"));
		txtManufacturer = new JTextField(m == null ? "" : m.getManufacturer());
		add(txtManufacturer);

		add(new JLabel("Name Modela:"));
		txtName = new JTextField(m == null ? "" : m.getModelName());
		add(txtName);

		add(new JLabel("Category:"));
		cbCategory = new JComboBox<>(VehicleCategory.values());
		if (m != null) cbCategory.setSelectedItem(m.getCategory());
		add(cbCategory);

		JButton btnSave = new JButton("Save");
		JButton btnOdustani = new JButton("Odustani");
		add(btnSave);
		add(btnOdustani);

		btnOdustani.addActionListener(e -> dispose());

		btnSave.addActionListener(e -> {
			if (txtManufacturer.getText().isEmpty() || txtName.getText().isEmpty()) {
				JOptionPane.showMessageDialog(this, "All fields are requirow!");
				return;
			}

			if (m != null) {
				m.setManufacturer(txtManufacturer.getText());
				m.setModelName(txtName.getText());
				m.setCategory((VehicleCategory) cbCategory.getSelectedItem());
			} else {
				VehicleModel newModel = new VehicleModel(mp.getModels().stream().mapToInt(x -> x.getId()).max().orElse(0) + 1, txtManufacturer.getText(), txtName.getText(), (VehicleCategory) cbCategory.getSelectedItem());
				mp.getModels().add(newModel);
			}

			try {
				mp.write("vehicle-models.csv");
				onSave.run();
				dispose();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Error pri cuvanju!");
			}
		});
	}
}
