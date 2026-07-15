package data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import reservation.ExtraService;

public class ExtraServiceRepository {

    protected ArrayList<ExtraService> services = new ArrayList<>();

    public void load(String putanja) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(putanja));
        String linija;
        while ((linija = br.readLine()) != null) {
            String[] delovi = linija.split("\\|");
            int id = Integer.parseInt(delovi[0]);
            String name = delovi[1];

            ExtraService du = new ExtraService(id, name);
            services.add(du);
        }
        br.close();
    }

    public void write(String putanja) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(putanja));
        for (ExtraService du : services) {
            pw.println(du.getId() + "|" + du.getServiceName());
        }
        pw.close();
    }

    public ExtraService findService(int id) {
        for (ExtraService du : services) {
            if (du.getId() == id) {
                return du;
            }
        }
        return null;
    }

    private int generateNewId() {
        int maxId = 0;
        for (ExtraService du : services) {
            if (du.getId() > maxId) {
                maxId = du.getId();
            }
        }
        return maxId + 1;
    }

    public void addService(ExtraService du) {
        du.setId(generateNewId());
        services.add(du);
        saveChanges();
    }

    public void deleteService(ExtraService du) {
        services.remove(du);
        saveChanges();
    }

    public void saveChanges() {
        try {
            write("extra-services.csv");
        } catch (IOException e) {
            System.out.println("Error pri saving service: " + e.getMessage());
        }
    }

    public ArrayList<ExtraService> getServices() {
        return services;
    }
}
