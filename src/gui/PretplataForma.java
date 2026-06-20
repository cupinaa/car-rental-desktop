package gui;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import korisnici.Pretplata;
import korisnici.StatusPretplate;
import podaci.PretplatePodaci;

public class PretplataForma extends JDialog {

	private JTextField txtDatum;
	private JComboBox<StatusPretplate> cbStatus;

	public PretplataForma(PretplatePodaci pp, Pretplata p, Runnable onSave) {
		setTitle("Izmeni Pretplatu");
		setSize(300, 200);
		setLocationRelativeTo(null);
		setModal(true);
		setLayout(new GridLayout(3, 2, 10, 10));

		add(new JLabel("Datum Isteka (YYYY-MM-DD):"));
		txtDatum = new JTextField(p.getDatumIsteka() == null ? "" : p.getDatumIsteka().toString());
		add(txtDatum);

		add(new JLabel("Status:"));
		cbStatus = new JComboBox<>(StatusPretplate.values());
		cbStatus.setSelectedItem(p.getStatus());
		add(cbStatus);

		JButton btnSacuvaj = new JButton("Sacuvaj");
		JButton btnOdustani = new JButton("Odustani");

		add(btnSacuvaj);
		add(btnOdustani);

		btnOdustani.addActionListener(e -> dispose());

		btnSacuvaj.addActionListener(e -> {
			try {
				String datumStr = txtDatum.getText().trim();
				if (!datumStr.isEmpty()) {
					p.setDatumIsteka(java.time.LocalDate.parse(datumStr));
				} else {
					p.setDatumIsteka(null);
				}
				p.setStatus((StatusPretplate) cbStatus.getSelectedItem());
				
				pp.upisi("pretplate.csv");
				onSave.run();
				dispose();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Neispravan datum ili greska.");
			}
		});
	}
}
