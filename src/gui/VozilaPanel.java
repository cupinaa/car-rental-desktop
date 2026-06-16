package gui;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import podaci.ModeliVozilaPodaci;
import podaci.VozilaPodaci;
import vozila.Vozilo;
import korisnici.Korisnik;
import korisnici.Agent;

public class VozilaPanel extends JPanel {

	private VozilaPodaci vp;
	private ModeliVozilaPodaci mp;
	private JTable tabela;
	private DefaultTableModel tableModel;

	private Korisnik ulogovaniKorisnik;

	public VozilaPanel(VozilaPodaci vp, ModeliVozilaPodaci mp, Korisnik ulogovaniKorisnik) {
		this.vp = vp;
		this.mp = mp;
		this.ulogovaniKorisnik = ulogovaniKorisnik;
		setLayout(new BorderLayout()); 

		String[] kolone = {"ID", "Marka i Model", "Kategorija", "Registracija", "Status"};
		tableModel = new DefaultTableModel(kolone, 0); 
		tabela = new JTable(tableModel);

		osveziTabelu();

		JScrollPane scrollPane = new JScrollPane(tabela);
		add(scrollPane, BorderLayout.CENTER); 

		if (!(ulogovaniKorisnik instanceof Agent)) {
			JPanel panelDugmici = new JPanel();
			JButton btnDodaj = new JButton("Dodaj");
			JButton btnIzmeni = new JButton("Izmeni");
			JButton btnObrisi = new JButton("Obriši");

			panelDugmici.add(btnDodaj);
			panelDugmici.add(btnIzmeni);
			panelDugmici.add(btnObrisi);
			add(panelDugmici, BorderLayout.SOUTH);

			btnDodaj.addActionListener(e -> {
				VoziloForma vf = new VoziloForma(vp, mp, null, this::osveziTabelu);
				vf.setVisible(true); 
			});

			btnIzmeni.addActionListener(e -> {
				int selektovaniRed = tabela.getSelectedRow();
				if (selektovaniRed == -1) {
					javax.swing.JOptionPane.showMessageDialog(this, "Morate prvo selektovati vozilo iz tabele!", "Upozorenje", javax.swing.JOptionPane.WARNING_MESSAGE);
					return;
				}

				int idVozila = (int) tableModel.getValueAt(selektovaniRed, 0);
				Vozilo zaIzmenu = vp.pronadjiVozilo(idVozila);

				if (zaIzmenu != null) {
					VoziloForma vf = new VoziloForma(vp, mp, zaIzmenu, this::osveziTabelu);
					vf.setVisible(true);
				}
			});

			btnObrisi.addActionListener(e -> {
				int selektovaniRed = tabela.getSelectedRow();
				if (selektovaniRed == -1) {
					javax.swing.JOptionPane.showMessageDialog(this, "Morate prvo selektovati vozilo iz tabele!", "Upozorenje", javax.swing.JOptionPane.WARNING_MESSAGE);
					return;
				}

				int idVozila = (int) tableModel.getValueAt(selektovaniRed, 0);
				Vozilo zaBrisanje = vp.pronadjiVozilo(idVozila);

				if (zaBrisanje != null) {
					Object[] opcije = {"Da", "Ne"};
					int potvrda = javax.swing.JOptionPane.showOptionDialog(this,
							"Da li ste sigurni da želite da obrišete vozilo: " + zaBrisanje.getRegistarskeTablice() + "?",
							"Potvrda brisanja",
							javax.swing.JOptionPane.YES_NO_OPTION,
							javax.swing.JOptionPane.QUESTION_MESSAGE,
							null,
							opcije,
							opcije[1]);
					if (potvrda == javax.swing.JOptionPane.YES_OPTION) {
						vp.obrisiVozilo(zaBrisanje); 
						osveziTabelu(); 
					}
				}
			});
		}
	}

	private void osveziTabelu() {
		tableModel.setRowCount(0);
		for (Vozilo v : vp.getVozila()) {
			String markaModel = v.getModelVozila().getMarkaVozila() + " " + v.getModelVozila().getNazivModela();
			Object[] red = { 
				v.getId(), 
				markaModel, 
				v.getModelVozila().getKategorija().toString(), 
				v.getRegistarskeTablice(), 
				v.getStatusVozila().toString() 
			};
			tableModel.addRow(red);
		}
	}
}
