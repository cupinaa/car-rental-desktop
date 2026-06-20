package gui;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import podaci.ModeliVozilaPodaci;
import vozila.ModelVozila;

public class ModeliPanel extends JPanel {

	private ModeliVozilaPodaci mp;
	private JTable tabela;
	private DefaultTableModel tableModel;

	public ModeliPanel(ModeliVozilaPodaci mp) {
		this.mp = mp;
		setLayout(new BorderLayout());

		String[] kolone = {"ID", "Marka", "Model", "Kategorija"};
		tableModel = new DefaultTableModel(kolone, 0);
		tabela = new JTable(tableModel);

		osveziTabelu();

		JScrollPane scrollPane = new JScrollPane(tabela);
		add(scrollPane, BorderLayout.CENTER);

		JPanel panelDugmici = new JPanel();
		JButton btnDodaj = new JButton("Dodaj");
		JButton btnIzmeni = new JButton("Izmeni");
		JButton btnObrisi = new JButton("Obrisi");

		panelDugmici.add(btnDodaj);
		panelDugmici.add(btnIzmeni);
		panelDugmici.add(btnObrisi);
		add(panelDugmici, BorderLayout.SOUTH);

		btnDodaj.addActionListener(e -> {
			new ModelForma(mp, null, this::osveziTabelu).setVisible(true);
		});

		btnIzmeni.addActionListener(e -> {
			int red = tabela.getSelectedRow();
			if (red == -1) { JOptionPane.showMessageDialog(this, "Odaberite model."); return; }
			int id = (int) tableModel.getValueAt(red, 0);
			ModelVozila m = mp.pronadjiModel(id);
			if (m != null) { new ModelForma(mp, m, this::osveziTabelu).setVisible(true); }
		});

		btnObrisi.addActionListener(e -> {
			int red = tabela.getSelectedRow();
			if (red == -1) { JOptionPane.showMessageDialog(this, "Odaberite model."); return; }
			int id = (int) tableModel.getValueAt(red, 0);
			ModelVozila m = mp.pronadjiModel(id);
			if (m != null && JOptionPane.showConfirmDialog(this, "Sigurno?", "Potvrda", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				mp.getModeli().remove(m);
				try { mp.upisi("modeli.csv"); osveziTabelu(); } catch (Exception ex) {}
			}
		});
	}

	private void osveziTabelu() {
		tableModel.setRowCount(0);
		for (ModelVozila m : mp.getModeli()) {
			Object[] red = { m.getId(), m.getMarkaVozila(), m.getNazivModela(), m.getKategorija() };
			tableModel.addRow(red);
		}
	}
}
