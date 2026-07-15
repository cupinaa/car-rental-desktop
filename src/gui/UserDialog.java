package gui;
import java.util.Locale;

import java.awt.GridLayout;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import users.Administrator;
import users.Agent;
import users.CustomerCategory;
import users.Customer;
import users.User;
import users.Gender;
import users.EducationLevel;
import users.Employee;
import data.UserRepository;

public class UserDialog extends JDialog {

	private UserRepository kp;
	private Runnable onSuccess;
	private User userToEdit;

	private JComboBox<String> cbTip;
	private JComboBox<Gender> cbPol;
	private JTextField firstNameField, lastNameField, dateOfBirthField, phoneField, addressField, usernameField, txtPassword;

	private JLabel lblSprema, lblStaz, lblOsnew;
	private JComboBox<EducationLevel> cbSprema;
	private JTextField txtStaz, txtOsnew;

	private JLabel lblVozacka, lblCategory;
	private JTextField txtDateVozacke;
	private JComboBox<String> cbCategory;


	public UserDialog(UserRepository kp, User userToEdit, Runnable onSuccess, User loggedInUser, boolean showCustomere) {
		this.kp = kp;
		this.onSuccess = onSuccess;
		this.userToEdit = userToEdit;

		setTitle(userToEdit == null ? "New user" : "Izmena usera");
		setSize(400, 600);
		setLocationRelativeTo(null);
		setModal(true);
		setLayout(new GridLayout(16, 2, 5, 5));

		add(new JLabel("Tip usera:"));
		if (loggedInUser instanceof Agent || showCustomere) {
			cbTip = new JComboBox<>(new String[] {"Customer"});
		} else {
			cbTip = new JComboBox<>(new String[] {"Agent", "Administrator"});
		}
		add(cbTip);

		add(new JLabel("First name:")); firstNameField = new JTextField(); add(firstNameField);
		add(new JLabel("Last name:")); lastNameField = new JTextField(); add(lastNameField);
		add(new JLabel("Gender:")); cbPol = new JComboBox<>(Gender.values()); add(cbPol);
		add(new JLabel("Date of birth (YYYY-MM-DD):")); dateOfBirthField = new JTextField(); add(dateOfBirthField);
		add(new JLabel("Telefon:")); phoneField = new JTextField(); add(phoneField);
		add(new JLabel("Address:")); addressField = new JTextField(); add(addressField);
		add(new JLabel("E-mail address (Username firstName):")); usernameField = new JTextField(); add(usernameField);
		add(new JLabel("Password:")); txtPassword = new JTextField(); add(txtPassword);

		lblVozacka = new JLabel("Date license (YYYY-MM-DD):");
		txtDateVozacke = new JTextField();
		add(lblVozacka); add(txtDateVozacke);

		lblCategory = new JLabel("Category (only za customere):");
		cbCategory = new JComboBox<>(new String[] {"NEMA", "STUDENT", "RETIREE", "COMPANY"});
		add(lblCategory); add(cbCategory);

		lblSprema = new JLabel("Education level:");
		cbSprema = new JComboBox<>(EducationLevel.values());
		add(lblSprema); add(cbSprema);

		lblStaz = new JLabel("Years service:");
		txtStaz = new JTextField("0");
		add(lblStaz); add(txtStaz);

		lblOsnew = new JLabel("Osnew za Platu:");
		txtOsnew = new JTextField("50000.0");
		add(lblOsnew); add(txtOsnew);

		JButton btnSave = new JButton("Save");
		add(new JLabel(""));
		add(btnSave);

		cbTip.addActionListener(e -> refreshVidljivostFields());


		if (userToEdit != null) {
			populateFields();
		}

		refreshVidljivostFields();

		btnSave.addActionListener(e -> saveUsera());
	}

	private void populateFields() {
		firstNameField.setText(userToEdit.getFirstName());
		lastNameField.setText(userToEdit.getLastName());
		cbPol.setSelectedItem(userToEdit.getGender());
		dateOfBirthField.setText(userToEdit.getDateOfBirth().toString());
		phoneField.setText(userToEdit.getPhone());
		addressField.setText(userToEdit.getAddress());
		usernameField.setText(userToEdit.getUsername());
		txtPassword.setText(userToEdit.getPassword());

		cbTip.setEnabled(false);

		if (userToEdit instanceof Customer) {
			cbTip.setSelectedItem("Customer");
			Customer kl = (Customer) userToEdit;
			txtDateVozacke.setText(kl.getLicenseIssueDate().toString());
			if (kl.getCustomerCategory() != null) {
				cbCategory.setSelectedItem(kl.getCustomerCategory().name());
			} else {
				cbCategory.setSelectedItem("NEMA");
			}
		} else if (userToEdit instanceof Employee) {
			Employee z = (Employee) userToEdit;
			cbTip.setSelectedItem(z instanceof Agent ? "Agent" : "Administrator");
			cbSprema.setSelectedItem(z.getEducationLevel());
			txtStaz.setText(String.valueOf(z.getYearsOfService()));


			double obrnutaOsnew = z.getSalary() / (z.getEducationLevel().getSalaryMultiplier() + 0.004 * z.getYearsOfService());
			txtOsnew.setText(String.format(Locale.US, "%.2f", obrnutaOsnew));
		}
	}

	private void refreshVidljivostFields() {
		String tip = cbTip.getSelectedItem().toString();
		boolean jeCustomer = tip.equals("Customer");

		lblVozacka.setVisible(jeCustomer);
		txtDateVozacke.setVisible(jeCustomer);
		lblCategory.setVisible(jeCustomer);
		cbCategory.setVisible(jeCustomer);

		lblSprema.setVisible(!jeCustomer);
		cbSprema.setVisible(!jeCustomer);
		lblStaz.setVisible(!jeCustomer);
		txtStaz.setVisible(!jeCustomer);
		lblOsnew.setVisible(!jeCustomer);
		txtOsnew.setVisible(!jeCustomer);
	}

	private void saveUsera() {
		try {
			String tip = cbTip.getSelectedItem().toString();
			String firstName = firstNameField.getText();
			String lastName = lastNameField.getText();
			Gender gender = (Gender) cbPol.getSelectedItem();
			LocalDate dateOfBirth = LocalDate.parse(dateOfBirthField.getText());
			String phone = phoneField.getText();
			String address = addressField.getText();
			String korFirstName = usernameField.getText();
			String loz = txtPassword.getText();

			if (userToEdit == null) {

				if(tip.equals("Customer")) {
					LocalDate vozacka = LocalDate.parse(txtDateVozacke.getText());
					String katStr = cbCategory.getSelectedItem().toString();
					CustomerCategory category = katStr.equals("NEMA") ? null : CustomerCategory.valueOf(katStr);
					Customer k = new Customer(0, firstName, lastName, gender, dateOfBirth, phone, address, korFirstName, loz, vozacka, category);
					kp.addUsera(k);
				}
				else {
					EducationLevel sprema = (EducationLevel) cbSprema.getSelectedItem();
					int staz = Integer.parseInt(txtStaz.getText());
					double osnew = Double.parseDouble(txtOsnew.getText());

					if(tip.equals("Agent")) {
						Agent a = new Agent(0, firstName, lastName, gender, dateOfBirth, phone, address, korFirstName, loz, sprema, staz, 0);
						a.racunanjePlate(osnew);
						kp.addUsera(a);
					} else {
						Administrator ad = new Administrator(0, firstName, lastName, gender, dateOfBirth, phone, address, korFirstName, loz, sprema, staz, 0);
						ad.racunanjePlate(osnew);
						kp.addUsera(ad);
					}
				}
				JOptionPane.showMessageDialog(this, "Successfully added!");
			} else {

				userToEdit.setFirstName(firstName);
				userToEdit.setLastName(lastName);
				userToEdit.setGender(gender);
				userToEdit.setDateOfBirth(dateOfBirth);
				userToEdit.setPhone(phone);
				userToEdit.setAddress(address);
				userToEdit.setUsername(korFirstName);
				userToEdit.setPassword(loz);

				if (userToEdit instanceof Customer) {
					Customer kl = (Customer) userToEdit;
					kl.setLicenseIssueDate(LocalDate.parse(txtDateVozacke.getText()));
					String katStr = cbCategory.getSelectedItem().toString();
					kl.setCustomerCategory(katStr.equals("NEMA") ? null : CustomerCategory.valueOf(katStr));
				} else if (userToEdit instanceof Employee) {
					Employee z = (Employee) userToEdit;
					z.setEducationLevel((EducationLevel) cbSprema.getSelectedItem());
					z.setYearsOfService(Integer.parseInt(txtStaz.getText()));
					z.racunanjePlate(Double.parseDouble(txtOsnew.getText()));
				}

				kp.saveChanges();
				JOptionPane.showMessageDialog(this, "Successfully changedo!");
			}

			onSuccess.run();
			dispose();

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error u format data! Proveri datee i numbereve.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
