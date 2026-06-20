package gui;
import java.time.LocalDate;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import korisnici.Pretplata;
import korisnici.StatusPretplate;
import podaci.PretplatePodaci;

public class PretplatePanel extends JPanel {

	private PretplatePodaci pp;
	private JTable tabela;
	private DefaultTableModel tableModel;
	private korisnici.Korisnik ulogovaniKorisnik;

	public PretplatePanel(PretplatePodaci pp, korisnici.Korisnik ulogovaniKorisnik) {
		this.pp = pp;
		this.ulogovaniKorisnik = ulogovaniKorisnik;
		setLayout(new BorderLayout()); 

		String[] kolone = {"ID", "Klijent", "Datum Isteka", "Status"};
		tableModel = new DefaultTableModel(kolone, 0); 
		tabela = new JTable(tableModel);

		osveziTabelu();

		JScrollPane scrollPane = new JScrollPane(tabela);
		add(scrollPane, BorderLayout.CENTER); 

		JPanel panelDugmici = new JPanel();
		JButton btnOdobri = new JButton("Odobri");
		JButton btnOdbij = new JButton("Odbij");

		panelDugmici.add(btnOdobri);
		panelDugmici.add(btnOdbij);
		add(panelDugmici, BorderLayout.SOUTH);

		btnOdobri.addActionListener(e -> promeniStatus(StatusPretplate.AKTIVNA));
		btnOdbij.addActionListener(e -> promeniStatus(StatusPretplate.ODBIJENA));

		if (ulogovaniKorisnik instanceof korisnici.Administrator) {
			JButton btnIzmeni = new JButton("Izmeni");
			JButton btnObrisi = new JButton("Obrisi");
			panelDugmici.add(btnIzmeni);
			panelDugmici.add(btnObrisi);

			btnIzmeni.addActionListener(e -> {
				int red = tabela.getSelectedRow();
				if (red == -1) { JOptionPane.showMessageDialog(this, "Odaberite pretplatu."); return; }
				int id = (int) tableModel.getValueAt(red, 0);
				Pretplata p = pp.pronadjiPretplatu(id);
				if (p != null) { new PretplataForma(pp, p, this::osveziTabelu).setVisible(true); }
			});

			btnObrisi.addActionListener(e -> {
				int red = tabela.getSelectedRow();
				if (red == -1) { JOptionPane.showMessageDialog(this, "Odaberite pretplatu."); return; }
				int id = (int) tableModel.getValueAt(red, 0);
				Pretplata p = pp.pronadjiPretplatu(id);
				if (p != null && JOptionPane.showConfirmDialog(this, "Sigurno?", "Potvrda", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					pp.getPretplate().remove(p);
					try { pp.upisi("pretplate.csv"); osveziTabelu(); } catch (Exception ex) {}
				}
			});
		}
	}

	private void promeniStatus(StatusPretplate noviStatus) {
		int selektovaniRed = tabela.getSelectedRow();
		if (selektovaniRed == -1) {
			JOptionPane.showMessageDialog(this, "Morate prvo selektovati pretplatu iz tabele!", "Upozorenje", JOptionPane.WARNING_MESSAGE);
			return;
		}

		int idPretplate = (int) tableModel.getValueAt(selektovaniRed, 0);
		Pretplata p = pp.pronadjiPretplatu(idPretplate);

		if (p != null) {
			if (p.getStatus() != StatusPretplate.CEKA_ODOBRENJE) {
				JOptionPane.showMessageDialog(this, "Možete menjati status samo pretplatama koje čekaju odobrenje!", "Upozorenje", JOptionPane.WARNING_MESSAGE);
				return;
			}

			p.setStatus(noviStatus);


			if (noviStatus == StatusPretplate.AKTIVNA) {
				p.setDatumIsteka(LocalDate.now().plusYears(1));
			}

			pp.sacuvajIzmene();
			osveziTabelu();
			JOptionPane.showMessageDialog(this, "Status pretplate je uspešno izmenjen u " + noviStatus + ".");
		}
	}

	private void osveziTabelu() {
		tableModel.setRowCount(0);
		for (Pretplata p : pp.getPretplate()) {
			Object[] red = { 
				p.getId(), 
				p.getKlijent().getIme() + " " + p.getKlijent().getPrezime() + " (" + p.getKlijent().getKorisnickoIme() + ")", 
				p.getDatumIsteka() == null ? "Nije definisano" : p.getDatumIsteka().toString(), 
				p.getStatus().toString() 
			};
			tableModel.addRow(red);
		}
	}
}
