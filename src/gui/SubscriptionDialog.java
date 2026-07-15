package gui;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import users.Subscription;
import users.SubscriptionStatus;
import data.SubscriptionRepository;

public class SubscriptionDialog extends JDialog {

	private JTextField txtDate;
	private JComboBox<SubscriptionStatus> cbStatus;

	public SubscriptionDialog(SubscriptionRepository pp, Subscription p, Runnable onSave) {
		setTitle("Edit subscription");
		setSize(300, 200);
		setLocationRelativeTo(null);
		setModal(true);
		setLayout(new GridLayout(3, 2, 10, 10));

		add(new JLabel("Date Isteka (YYYY-MM-DD):"));
		txtDate = new JTextField(p.getExpiryDate() == null ? "" : p.getExpiryDate().toString());
		add(txtDate);

		add(new JLabel("Status:"));
		cbStatus = new JComboBox<>(SubscriptionStatus.values());
		cbStatus.setSelectedItem(p.getStatus());
		add(cbStatus);

		JButton btnSave = new JButton("Save");
		JButton btnOdustani = new JButton("Odustani");

		add(btnSave);
		add(btnOdustani);

		btnOdustani.addActionListener(e -> dispose());

		btnSave.addActionListener(e -> {
			try {
				String dateStr = txtDate.getText().trim();
				if (!dateStr.isEmpty()) {
					p.setExpiryDate(java.time.LocalDate.parse(dateStr));
				} else {
					p.setExpiryDate(null);
				}
				p.setStatus((SubscriptionStatus) cbStatus.getSelectedItem());

				pp.write("subscriptions.csv");
				onSave.run();
				dispose();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Invalid date.");
			}
		});
	}
}
