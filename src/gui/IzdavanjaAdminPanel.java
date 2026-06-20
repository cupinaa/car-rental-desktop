package gui;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import izdavanje.Izdavanje;
import podaci.IzdavanjePodaci;

public class IzdavanjaAdminPanel extends JPanel {

	private IzdavanjePodaci ip;
	private JTable tabela;
	private DefaultTableModel tableModel;

	public IzdavanjaAdminPanel(IzdavanjePodaci ip) {
		this.ip = ip;
		setLayout(new BorderLayout());

		String[] kolone = {"ID", "Rezervacija", "Agent", "Pocetna KM", "Krajnja KM"};
		tableModel = new DefaultTableModel(kolone, 0);
		tabela = new JTable(tableModel);

		osveziTabelu();

		JScrollPane scrollPane = new JScrollPane(tabela);
		add(scrollPane, BorderLayout.CENTER);

		JPanel panelDugmici = new JPanel();
		JButton btnIzmeni = new JButton("Izmeni");
		JButton btnObrisi = new JButton("Obrisi");

		panelDugmici.add(btnIzmeni);
		panelDugmici.add(btnObrisi);
		add(panelDugmici, BorderLayout.SOUTH);

		btnIzmeni.addActionListener(e -> {
			int selektovaniRed = tabela.getSelectedRow();
			if (selektovaniRed == -1) {
				JOptionPane.showMessageDialog(this, "Odaberite izdavanje za izmenu.");
				return;
			}
			int idIzdavanja = (int) tableModel.getValueAt(selektovaniRed, 0);
			Izdavanje i = ip.getIzdavanja().stream().filter(izd -> izd.getId() == idIzdavanja).findFirst().orElse(null);
			if (i != null) {
				IzdavanjeForma iff = new IzdavanjeForma(ip, i, this::osveziTabelu);
				iff.setVisible(true);
			}
		});

		btnObrisi.addActionListener(e -> {
			int selektovaniRed = tabela.getSelectedRow();
			if (selektovaniRed == -1) {
				JOptionPane.showMessageDialog(this, "Odaberite izdavanje za brisanje.");
				return;
			}
			int idIzdavanja = (int) tableModel.getValueAt(selektovaniRed, 0);
			Izdavanje i = ip.getIzdavanja().stream().filter(izd -> izd.getId() == idIzdavanja).findFirst().orElse(null);
			if (i != null) {
				int potvrda = JOptionPane.showConfirmDialog(this, "Da li ste sigurni?", "Potvrda", JOptionPane.YES_NO_OPTION);
				if (potvrda == JOptionPane.YES_OPTION) {
					ip.getIzdavanja().remove(i);
					try {
						ip.upisi("izdavanja.csv");
						osveziTabelu();
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(this, "Greska pri brisanju.");
					}
				}
			}
		});
	}

	private void osveziTabelu() {
		tableModel.setRowCount(0);
		for (Izdavanje i : ip.getIzdavanja()) {
			Object[] red = { i.getId(), i.getRezervacija().getId(), i.getAgent().getIme(), i.getPocetnaKilometraza(), i.getKrajnjaKilometraza() };
			tableModel.addRow(red);
		}
	}
}
