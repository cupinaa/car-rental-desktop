package gui;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import cenovnik.Cenovnik;
import podaci.CenovniciPodaci;
import podaci.DodatneUslugePodaci;

public class CenovnikPanel extends JPanel {

	private CenovniciPodaci cp;
	private DodatneUslugePodaci dup;
	private JTable tabela;
	private DefaultTableModel tableModel;

	public CenovnikPanel(CenovniciPodaci cp, DodatneUslugePodaci dup) {
		this.cp = cp;
		this.dup = dup;
		setLayout(new BorderLayout()); 

		String[] kolone = {"ID", "Početak Važenja", "Kraj Važenja", "Pretplata (RSD)", "Kazna/Dan"};
		tableModel = new DefaultTableModel(kolone, 0); 
		tabela = new JTable(tableModel);

		osveziTabelu();

		JScrollPane scrollPane = new JScrollPane(tabela);
		add(scrollPane, BorderLayout.CENTER); 

		JPanel panelDugmici = new JPanel();
		JButton btnDodaj = new JButton("Dodaj");
		JButton btnIzmeni = new JButton("Izmeni");
		JButton btnObrisi = new JButton("Obriši");

		panelDugmici.add(btnDodaj);
		panelDugmici.add(btnIzmeni);
		panelDugmici.add(btnObrisi);
		add(panelDugmici, BorderLayout.SOUTH);

		btnDodaj.addActionListener(e -> {
			CenovnikForma cf = new CenovnikForma(cp, dup, null, this::osveziTabelu);
			cf.setVisible(true); 
		});

		btnIzmeni.addActionListener(e -> {
			int selektovaniRed = tabela.getSelectedRow();
			if (selektovaniRed == -1) {
				javax.swing.JOptionPane.showMessageDialog(this, "Morate prvo selektovati cenovnik iz tabele!", "Upozorenje", javax.swing.JOptionPane.WARNING_MESSAGE);
				return;
			}

			int idCenovnika = (int) tableModel.getValueAt(selektovaniRed, 0);
			Cenovnik zaIzmenu = cp.pronadjiCenovnik(idCenovnika);

			if (zaIzmenu != null) {
				CenovnikForma cf = new CenovnikForma(cp, dup, zaIzmenu, this::osveziTabelu);
				cf.setVisible(true);
			}
		});

		btnObrisi.addActionListener(e -> {
			int selektovaniRed = tabela.getSelectedRow();
			if (selektovaniRed == -1) {
				javax.swing.JOptionPane.showMessageDialog(this, "Morate prvo selektovati cenovnik iz tabele!", "Upozorenje", javax.swing.JOptionPane.WARNING_MESSAGE);
				return;
			}

			int idCenovnika = (int) tableModel.getValueAt(selektovaniRed, 0);
			Cenovnik zaBrisanje = cp.pronadjiCenovnik(idCenovnika);

			if (zaBrisanje != null) {
				Object[] opcije = {"Da", "Ne"};
				int potvrda = javax.swing.JOptionPane.showOptionDialog(this,
						"Da li ste sigurni da želite da obrišete ovaj cenovnik?",
						"Potvrda brisanja",
						javax.swing.JOptionPane.YES_NO_OPTION,
						javax.swing.JOptionPane.QUESTION_MESSAGE,
						null,
						opcije,
						opcije[1]);
				if (potvrda == javax.swing.JOptionPane.YES_OPTION) {
					cp.obrisiCenovnik(zaBrisanje); 
					osveziTabelu(); 
				}
			}
		});
	}

	private void osveziTabelu() {
		tableModel.setRowCount(0);
		for (Cenovnik c : cp.getCenovnici()) {
			Object[] red = { 
				c.getId(), 
				c.getPocetakVazenja().toString(), 
				c.getKrajVazenja().toString(), 
				c.getCenaPretplate(),
				c.getIznosKazne()
			};
			tableModel.addRow(red);
		}
	}
}
