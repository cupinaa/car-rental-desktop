package gui;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import data.ExtraServiceRepository;
import reservation.ExtraService;

public class ExtraServiceDialog extends JDialog {

	private ExtraServiceRepository dup;
	private Runnable onSuccess;
	private ExtraService serviceToEdit;

	private JTextField txtName;

	public ExtraServiceDialog(ExtraServiceRepository dup, ExtraService serviceToEdit, Runnable onSuccess) {
		this.dup = dup;
		this.onSuccess = onSuccess;
		this.serviceToEdit = serviceToEdit;

		setTitle(serviceToEdit == null ? "New service" : "Izmena services");
		setSize(300, 120);
		setLocationRelativeTo(null);
		setModal(true);
		setLayout(new GridLayout(2, 2, 5, 5));

		add(new JLabel("Name services:"));
		txtName = new JTextField();
		add(txtName);

		JButton btnSave = new JButton("Save");
		add(new JLabel(""));
		add(btnSave);

		if (serviceToEdit != null) {
			populateFields();
		}

		btnSave.addActionListener(e -> saveService());
	}

	private void populateFields() {
		txtName.setText(serviceToEdit.getServiceName());
	}

	private void saveService() {
		try {
			String name = txtName.getText();

			if (name.trim().isEmpty()) {
				JOptionPane.showMessageDialog(this, "You must enter name services!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (serviceToEdit == null) {
				ExtraService du = new ExtraService(0, name);
				dup.addService(du);
				JOptionPane.showMessageDialog(this, "Successfully added!");
			} else {
				serviceToEdit.setServiceName(name);
				dup.saveChanges();
				JOptionPane.showMessageDialog(this, "Successfully changedo!");
			}

			onSuccess.run();
			dispose();

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error while saving!", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
