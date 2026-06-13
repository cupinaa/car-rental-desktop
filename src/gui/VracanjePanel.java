package gui;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import izdavanje.Izdavanje;
import podaci.CenovniciPodaci;
import podaci.IzdavanjePodaci;
import podaci.KorisniciPodaci;
import podaci.RezervacijePodaci;

public class VracanjePanel extends JPanel {

	private IzdavanjePodaci ip;
	private RezervacijePodaci rp;
	private KorisniciPodaci kp;
	private CenovniciPodaci cp;
	
	private JTable tabela;
	private DefaultTableModel tableModel;

	public VracanjePanel(IzdavanjePodaci ip, RezervacijePodaci rp, KorisniciPodaci kp, CenovniciPodaci cp) {
		this.ip = ip;
		this.rp = rp;
		this.kp = kp;
		this.cp = cp;
		
		setLayout(new BorderLayout()); 
		
		String[] kolone = {"ID Izdavanja", "Klijent", "Vozilo", "Očekivani Kraj", "Početna KM"};
		tableModel = new DefaultTableModel(kolone, 0); 
		tabela = new JTable(tableModel);
		
		osveziTabelu();
		
		JScrollPane scrollPane = new JScrollPane(tabela);
		add(scrollPane, BorderLayout.CENTER); 
		
		JPanel panelDugmici = new JPanel();
		JButton btnVrati = new JButton("Evidentiraj Vraćanje");
		
		panelDugmici.add(btnVrati);
		add(panelDugmici, BorderLayout.SOUTH);
		
		btnVrati.addActionListener(e -> otvoriVracanje());
	}
	
	private void otvoriVracanje() {
		int selektovaniRed = tabela.getSelectedRow();
		if (selektovaniRed == -1) {
			JOptionPane.showMessageDialog(this, "Morate selektovati aktivno izdavanje!", "Upozorenje", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		int idIzdavanja = (int) tableModel.getValueAt(selektovaniRed, 0);
		Izdavanje i = null;
		for (Izdavanje iz : ip.getIzdavanja()) {
			if (iz.getId() == idIzdavanja) {
				i = iz;
				break;
			}
		}
		
		if (i != null) {
			VracanjeForma vf = new VracanjeForma(ip, rp, kp, cp, i, this::osveziTabelu);
			vf.setVisible(true);
		}
	}
	
	private void osveziTabelu() {
		tableModel.setRowCount(0);
		for (Izdavanje i : ip.getIzdavanja()) {
			// Prikazujemo samo aktivna izdavanja (gde je krajnja km = 0.0)
			if (i.getKrajnjaKilometraza() == 0.0) {
				String klijentInfo = i.getRezervacija().getKlijent().getIme() + " " + i.getRezervacija().getKlijent().getPrezime();
				String voziloInfo = i.getRezervacija().getVozilo().getModelVozila().getMarkaVozila() + " " + i.getRezervacija().getVozilo().getModelVozila().getNazivModela();
				
				Object[] red = { 
					i.getId(), 
					klijentInfo, 
					voziloInfo, 
					i.getRezervacija().getDatumKraja().toString(), 
					i.getPocetnaKilometraza()
				};
				tableModel.addRow(red);
			}
		}
	}
}

