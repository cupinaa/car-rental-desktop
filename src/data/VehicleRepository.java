package data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import vehicles.VehicleModel;
import vehicles.VehicleStatus;
import vehicles.Vehicle;

public class VehicleRepository {

    protected ArrayList<Vehicle> vehicles = new ArrayList<>();

    public void load(String putanja, VehicleModelRepository mp) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(putanja));
        String linija;
        while ((linija = br.readLine()) != null) {
            String[] delovi = linija.split("\\|");

            int idVozila = Integer.parseInt(delovi[0]);
            int idModela = Integer.parseInt(delovi[1]);
            String licensePlate = delovi[2];
            VehicleStatus status = VehicleStatus.valueOf(delovi[3]);

            VehicleModel povezaniModel = mp.findModel(idModela);

            if (povezaniModel != null) {
                Vehicle v = new Vehicle(idVozila, povezaniModel, licensePlate, status);
                vehicles.add(v);
            } else {
                System.out.println("Warning: Model sa ID-jem " + idModela + " nije found za vehicle " + idVozila);
            }
        }
        br.close();
    }

    public void write(String putanja) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(putanja));
        for (Vehicle v : vehicles) {
            pw.println(v.getId() + "|" + v.getVehicleModel().getId() + "|" + v.getLicensePlate() + "|" + v.getVehicleStatus());
        }
        pw.close();
    }

    private int generateNewId() {
        int maxId = 0;
        for (Vehicle v : vehicles) {
            if (v.getId() > maxId) {
                maxId = v.getId();
            }
        }
        return maxId + 1;
    }

    public void addVehicle(Vehicle v) {
        v.setId(generateNewId());
        vehicles.add(v);
        saveChanges();
    }


    public Vehicle findVehicle(int id) {
        for (Vehicle v : vehicles) {
            if (v.getId() == id) {
                return v;
            }
        }
        return null;
    }


    public void deleteVehicle(Vehicle v) {
        vehicles.remove(v);
        saveChanges();
    }

    public void saveChanges() {
        try {
            write("vehicles.csv");
        } catch (IOException e) {
            System.out.println("Error pri saving vehicles: " + e.getMessage());
        }
    }

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }
}
