package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import data.Settings;

public class SettingsPanel extends JPanel {

	private Settings p;

	public SettingsPanel(Settings p) {
		this.p = p;
		setLayout(new BorderLayout());

		JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));

		centerPanel.add(new JLabel("Podrazumevano duration najma (u dayima):"));
		JTextField txtDuration = new JTextField(10);
		txtDuration.setText(String.valueOf(p.getDefaultRentalDuration()));
		centerPanel.add(txtDuration);

		JButton btnSave = new JButton("Save");
		centerPanel.add(btnSave);

		add(centerPanel, BorderLayout.NORTH);

		btnSave.addActionListener(e -> {
			try {
				int novoDuration = Integer.parseInt(txtDuration.getText().trim());
				if (novoDuration <= 0) {
					JOptionPane.showMessageDialog(this, "Duration najma must be pozitivan number!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				p.setPodrazumevanoDurationNajma(novoDuration);
				JOptionPane.showMessageDialog(this, "Settings saved successfully!");
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Please unesite validay ceo number!", "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
	}
}
