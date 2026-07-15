package main;

import java.io.IOException;

import data.PriceListRepository;
import data.ExtraServiceRepository;
import data.RentalRepository;
import data.UserRepository;
import data.VehicleModelRepository;
import data.SubscriptionRepository;
import data.ReservationRepository;
import data.VehicleRepository;
import gui.LoginWindow;

public class SystemManager {

    private UserRepository kp;
    private VehicleModelRepository mp;
    private VehicleRepository vp;
    private ExtraServiceRepository dup;
    private PriceListRepository cp;
    private ReservationRepository rp;
    private RentalRepository ip;
    private SubscriptionRepository pp;

    public SystemManager() {
        kp = new UserRepository();
        mp = new VehicleModelRepository();
        vp = new VehicleRepository();
        dup = new ExtraServiceRepository();
        cp = new PriceListRepository();
        rp = new ReservationRepository();
        ip = new RentalRepository();
        pp = new SubscriptionRepository();
    }

    public void pokreni() {
        try {
            kp.load("users.csv");
            mp.load("vehicle-models.csv");
            dup.load("extra-services.csv");

            vp.load("vehicles.csv", mp);
            cp.load("price-lists.csv", "stavke_pricinga.csv", "stavke_service_pricinga.csv");
            rp.load("reservations.csv", kp, vp, dup);
            ip.load("rentals.csv", rp, kp);
            pp.load("subscriptions.csv", kp);

            LoginWindow login = new LoginWindow(kp, mp, vp, dup, cp, rp, ip, pp);
            login.setVisible(true);

        } catch (IOException e) {
            System.err.println("Error pri loading: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
