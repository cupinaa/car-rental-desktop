package gui;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import podaci.DodatneUslugePodaci;
import rezervacija.DodatnaUsluga;

public class UslugePanel extends JPanel {

	private DodatneUslugePodaci dup;
	private JTable tabela;
	private DefaultTableModel tableModel;

	public UslugePanel(DodatneUslugePodaci dup) {
		this.dup = dup;
		setLayout(new BorderLayout()); 
		
		String[] kolone = {"ID", "Naziv Usluge"};
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
			UslugaForma uf = new UslugaForma(dup, null, this::osveziTabelu);
			uf.setVisible(true); 
		});
		
		btnIzmeni.addActionListener(e -> {
			int selektovaniRed = tabela.getSelectedRow();
			if (selektovaniRed == -1) {
				javax.swing.JOptionPane.showMessageDialog(this, "Morate prvo selektovati uslugu iz tabele!", "Upozorenje", javax.swing.JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			int idUsluge = (int) tableModel.getValueAt(selektovaniRed, 0);
			DodatnaUsluga zaIzmenu = dup.pronadjiUslugu(idUsluge);
			
			if (zaIzmenu != null) {
				UslugaForma uf = new UslugaForma(dup, zaIzmenu, this::osveziTabelu);
				uf.setVisible(true);
			}
		});
		
		btnObrisi.addActionListener(e -> {
			int selektovaniRed = tabela.getSelectedRow();
			if (selektovaniRed == -1) {
				javax.swing.JOptionPane.showMessageDialog(this, "Morate prvo selektovati uslugu iz tabele!", "Upozorenje", javax.swing.JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			int idUsluge = (int) tableModel.getValueAt(selektovaniRed, 0);
			DodatnaUsluga zaBrisanje = dup.pronadjiUslugu(idUsluge);
			
			if (zaBrisanje != null) {
				Object[] opcije = {"Da", "Ne"};
				int potvrda = javax.swing.JOptionPane.showOptionDialog(this,
						"Da li ste sigurni da želite da obrišete uslugu: " + zaBrisanje.getDodatnaUsluga() + "?",
						"Potvrda brisanja",
						javax.swing.JOptionPane.YES_NO_OPTION,
						javax.swing.JOptionPane.QUESTION_MESSAGE,
						null,
						opcije,
						opcije[1]);
				if (potvrda == javax.swing.JOptionPane.YES_OPTION) {
					dup.obrisiUslugu(zaBrisanje); 
					osveziTabelu(); 
				}
			}
		});
	}
	
	private void osveziTabelu() {
		tableModel.setRowCount(0);
		for (DodatnaUsluga du : dup.getUsluge()) {
			Object[] red = { 
				du.getId(), 
				du.getDodatnaUsluga() 
			};
			tableModel.addRow(red);
		}
	}
}
