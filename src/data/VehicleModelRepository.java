package data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import vehicles.VehicleCategory;
import vehicles.VehicleModel;

public class VehicleModelRepository {

    protected ArrayList<VehicleModel> modeli = new ArrayList<>();

    public void load(String putanja) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(putanja));
        String linija;
        while ((linija = br.readLine()) != null) {
            String[] delovi = linija.split("\\|");
            int id = Integer.parseInt(delovi[0]);
            String manufacturer = delovi[1];
            String modelName = delovi[2];
            VehicleCategory category = VehicleCategory.valueOf(delovi[3]);

            VehicleModel model = new VehicleModel(id, manufacturer, modelName, category);
            modeli.add(model);
        }
        br.close();
    }

    public void write(String putanja) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(putanja));
        for (VehicleModel m : modeli) {
            pw.println(m.getId() + "|" + m.getManufacturer() + "|" + m.getModelName() + "|" + m.getCategory());
        }
        pw.close();
    }

    private int generateNewId() {
        int maxId = 0;
        for (VehicleModel m : modeli) {
            if (m.getId() > maxId) {
                maxId = m.getId();
            }
        }
        return maxId + 1;
    }

    public void addModel(VehicleModel m) {
        m.setId(generateNewId());
        modeli.add(m);
        try {
            write("vehicle-models.csv");
        } catch (IOException e) {
            System.out.println("Error pri upisu modela: " + e.getMessage());
        }
    }


    public VehicleModel findModel(int id) {
        for (VehicleModel m : modeli) {
            if (m.getId() == id) {
                return m;
            }
        }
        return null;
    }

    public ArrayList<VehicleModel> getModels() {
        return modeli;
    }

	public void setModels(ArrayList<VehicleModel> modeli) {
		this.modeli = modeli;
	}
}
