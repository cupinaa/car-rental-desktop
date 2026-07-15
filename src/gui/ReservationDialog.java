package gui;

import java.awt.GridLayout;
import java.time.LocalDate;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import data.ReservationRepository;
import reservation.Reservation;
import reservation.ReservationStatus;

public class ReservationDialog extends JDialog {

	private JTextField txtDateOd;
	private JTextField txtDateDo;
	private JComboBox<ReservationStatus> cbStatus;

	public ReservationDialog(ReservationRepository rp, Reservation r, Runnable onSave) {
		setTitle("Edit reservation");
		setSize(300, 200);
		setLocationRelativeTo(null);
		setModal(true);
		setLayout(new GridLayout(4, 2, 10, 10));

		add(new JLabel("Date Od (YYYY-MM-DD):"));
		txtDateOd = new JTextField(r.getStartDate().toString());
		add(txtDateOd);

		add(new JLabel("Date Do (YYYY-MM-DD):"));
		txtDateDo = new JTextField(r.getEndDate().toString());
		add(txtDateDo);

		add(new JLabel("Status:"));
		cbStatus = new JComboBox<>(ReservationStatus.values());
		cbStatus.setSelectedItem(r.getReservationStatus());
		add(cbStatus);

		JButton btnSave = new JButton("Save");
		JButton btnOdustani = new JButton("Odustani");

		add(btnSave);
		add(btnOdustani);

		btnOdustani.addActionListener(e -> dispose());

		btnSave.addActionListener(e -> {
			try {
				LocalDate dateOd = LocalDate.parse(txtDateOd.getText().trim());
				LocalDate dateDo = LocalDate.parse(txtDateDo.getText().trim());
				ReservationStatus status = (ReservationStatus) cbStatus.getSelectedItem();

				r.setStartDate(dateOd);
				r.setEndDate(dateDo);
				r.setReservationStatus(status);

				rp.write("reservations.csv");
				onSave.run();
				dispose();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Invalid date.");
			}
		});
	}
}
