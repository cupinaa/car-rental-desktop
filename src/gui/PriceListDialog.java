package gui;
import java.util.HashMap;
import java.util.Map;

import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import pricing.PriceList;
import pricing.PriceListItem;
import data.PriceListRepository;
import data.ExtraServiceRepository;
import reservation.ExtraService;
import vehicles.VehicleCategory;

public class PriceListDialog extends JDialog {

	private PriceListRepository cp;
	private ExtraServiceRepository dup;
	private Runnable onSuccess;
	private PriceList priceListToEdit;

	private JTextField txtStart, txtEnd, txtSubscription, studentDiscountField, companyDiscountField, retireeDiscountField, txtLateFee;
	private JTextField txtPriceEconomy, txtPriceStandard, txtPriceFamily, txtPriceLuxury;
	private Map<Integer, JTextField> txtCeneService = new HashMap<>();

	public PriceListDialog(PriceListRepository cp, ExtraServiceRepository dup, PriceList priceListToEdit, Runnable onSuccess) {
		this.cp = cp;
		this.dup = dup;
		this.onSuccess = onSuccess;
		this.priceListToEdit = priceListToEdit;

		setTitle(priceListToEdit == null ? "New PriceList" : "Izmena PriceLista");

		int numberService = dup.getServices().size();
		setSize(800, 450 + (numberService * 20));
		setLocationRelativeTo(null);
		setModal(true);
		setLayout(new GridLayout(0, 4, 10, 10));

		add(new JLabel("Start validity (YYYY-MM-DD):")); txtStart = new JTextField(); add(txtStart);
		add(new JLabel("End validity (YYYY-MM-DD):")); txtEnd = new JTextField(); add(txtEnd);

		add(new JLabel("Price subscriptions (RSD):")); txtSubscription = new JTextField(); add(txtSubscription);
		add(new JLabel("Student discount (0.0 - 1.0):")); studentDiscountField = new JTextField(); add(studentDiscountField);

		add(new JLabel("Company discount (0.0 - 1.0):")); companyDiscountField = new JTextField(); add(companyDiscountField);
		add(new JLabel("Retiree discount (0.0 - 1.0):")); retireeDiscountField = new JTextField(); add(retireeDiscountField);

		add(new JLabel("Late fee amount (day):")); txtLateFee = new JTextField(); add(txtLateFee);
		add(new JLabel("")); add(new JLabel(""));

		add(new JLabel("--- CENE ZA KATEGORIJE VOZILA ---")); add(new JLabel("")); add(new JLabel("")); add(new JLabel(""));

		add(new JLabel("Price/day ECONOMY:")); txtPriceEconomy = new JTextField(); add(txtPriceEconomy);
		add(new JLabel("Price/day STANDARD:")); txtPriceStandard = new JTextField(); add(txtPriceStandard);

		add(new JLabel("Price/day FAMILY:")); txtPriceFamily = new JTextField(); add(txtPriceFamily);
		add(new JLabel("Price/day LUXURY:")); txtPriceLuxury = new JTextField(); add(txtPriceLuxury);

		add(new JLabel("--- EXTRA SERVICE PRICES ---")); add(new JLabel("")); add(new JLabel("")); add(new JLabel(""));

		int serviceCount = 0;
		for (ExtraService du : dup.getServices()) {
			add(new JLabel("Price/day " + du.getServiceName() + ":"));
			JTextField txt = new JTextField();
			txtCeneService.put(du.getId(), txt);
			add(txt);
			serviceCount++;
		}
		if (serviceCount % 2 != 0) {
			add(new JLabel("")); add(new JLabel(""));
		}

		add(new JLabel("")); add(new JLabel(""));
		JButton btnSave = new JButton("Save");
		add(btnSave); add(new JLabel(""));

		if (priceListToEdit != null) {
			populateFields();
		}

		btnSave.addActionListener(e -> savePriceList());
	}

	private void populateFields() {
		txtStart.setText(priceListToEdit.getValidFrom().toString());
		txtEnd.setText(priceListToEdit.getValidUntil().toString());
		txtSubscription.setText(String.valueOf(priceListToEdit.getSubscriptionPrice()));
		studentDiscountField.setText(String.valueOf(priceListToEdit.getStudentDiscount()));
		companyDiscountField.setText(String.valueOf(priceListToEdit.getCompanyDiscount()));
		retireeDiscountField.setText(String.valueOf(priceListToEdit.getRetireeDiscount()));
		txtLateFee.setText(String.valueOf(priceListToEdit.getLateFee()));

		for (PriceListItem s : priceListToEdit.getStavkePriceLista()) {
			switch(s.getVehicleCategory()) {
				case ECONOMY: txtPriceEconomy.setText(String.valueOf(s.getDailyPrice())); break;
				case STANDARD: txtPriceStandard.setText(String.valueOf(s.getDailyPrice())); break;
				case FAMILY: txtPriceFamily.setText(String.valueOf(s.getDailyPrice())); break;
				case LUXURY: txtPriceLuxury.setText(String.valueOf(s.getDailyPrice())); break;
			}
		}

		if (priceListToEdit.getExtraServicePrices() != null) {
			for (Map.Entry<Integer, Double> entry : priceListToEdit.getExtraServicePrices().entrySet()) {
				if (txtCeneService.containsKey(entry.getKey())) {
					txtCeneService.get(entry.getKey()).setText(String.valueOf(entry.getValue()));
				}
			}
		}
	}

	private void savePriceList() {
		try {
			LocalDate start = LocalDate.parse(txtStart.getText());
			LocalDate end = LocalDate.parse(txtEnd.getText());
			double subscription = Double.parseDouble(txtSubscription.getText());
			double pStudent = Double.parseDouble(studentDiscountField.getText());
			double pFirma = Double.parseDouble(companyDiscountField.getText());
			double pPenzioner = Double.parseDouble(retireeDiscountField.getText());
			double lateFee = Double.parseDouble(txtLateFee.getText());

			double cEco = Double.parseDouble(txtPriceEconomy.getText());
			double cStd = Double.parseDouble(txtPriceStandard.getText());
			double cFam = Double.parseDouble(txtPriceFamily.getText());
			double cLux = Double.parseDouble(txtPriceLuxury.getText());

			Map<Integer, Double> mapePriceService = new HashMap<>();
			for (Map.Entry<Integer, JTextField> entry : txtCeneService.entrySet()) {
				mapePriceService.put(entry.getKey(), Double.parseDouble(entry.getValue().getText()));
			}

			ArrayList<PriceListItem> stavke = new ArrayList<>();
			stavke.add(new PriceListItem(VehicleCategory.ECONOMY, cEco));
			stavke.add(new PriceListItem(VehicleCategory.STANDARD, cStd));
			stavke.add(new PriceListItem(VehicleCategory.FAMILY, cFam));
			stavke.add(new PriceListItem(VehicleCategory.LUXURY, cLux));

			if (priceListToEdit == null) {
				PriceList c = new PriceList(start, end, stavke, subscription, pStudent, pFirma, pPenzioner, lateFee, mapePriceService);
				cp.addPriceList(c);
				JOptionPane.showMessageDialog(this, "Successfully added!");
			} else {
				priceListToEdit.setValidFrom(start);
				priceListToEdit.setValidUntil(end);
				priceListToEdit.setSubscriptionPrice(subscription);
				priceListToEdit.setStudentDiscount(pStudent);
				priceListToEdit.setCompanyDiscount(pFirma);
				priceListToEdit.setRetireeDiscount(pPenzioner);
				priceListToEdit.setLateFee(lateFee);

				for (PriceListItem sNew : stavke) {
					for (PriceListItem sStara : priceListToEdit.getStavkePriceLista()) {
						if (sNew.getVehicleCategory() == sStara.getVehicleCategory()) {
							sNew.setId(sStara.getId());
						}
					}
				}
				priceListToEdit.setStavkePriceLista(stavke);
				priceListToEdit.setExtraServicePrices(mapePriceService);

				cp.saveChanges();
				JOptionPane.showMessageDialog(this, "Successfully changedo!");
			}

			onSuccess.run();
			dispose();

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Proverite unos! Datei must be YYYY-MM-DD, a numbers valid.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
