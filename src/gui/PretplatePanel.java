package gui;

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

	public PretplatePanel(PretplatePodaci pp) {
		this.pp = pp;
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
	}
	
	private void promeniStatus(StatusPretplate noviStatus) {
		int selektovaniRed = tabela.getSelectedRow();
		if (selektovaniRed == -1) {
			javax.swing.JOptionPane.showMessageDialog(this, "Morate prvo selektovati pretplatu iz tabele!", "Upozorenje", javax.swing.JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		int idPretplate = (int) tableModel.getValueAt(selektovaniRed, 0);
		Pretplata p = pp.pronadjiPretplatu(idPretplate);
		
		if (p != null) {
			if (p.getStatus() != StatusPretplate.CEKA_ODOBRENJE) {
				javax.swing.JOptionPane.showMessageDialog(this, "Možete menjati status samo pretplatama koje čekaju odobrenje!", "Upozorenje", javax.swing.JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			p.setStatus(noviStatus);
			
			// Ako je odobrena, stavljamo joj datum isteka na godinu dana od danas
			if (noviStatus == StatusPretplate.AKTIVNA) {
				p.setDatumIsteka(java.time.LocalDate.now().plusYears(1));
			}
			
			pp.sacuvajIzmene();
			osveziTabelu();
			javax.swing.JOptionPane.showMessageDialog(this, "Status pretplate je uspešno izmenjen u " + noviStatus + ".");
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
