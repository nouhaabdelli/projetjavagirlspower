package gui;

import java.util.function.Consumer;

public class javaBridge {


    private final Consumer<String> callback;

    public javaBridge(Consumer<String> callback) {
        this.callback = callback;
    }

    public void sendCoordinates(double lat, double lng) {
        String lieu = "Lat: " + lat + ", Lng: " + lng; // tu peux améliorer avec reverse geocoding
        callback.accept(lieu);
    }
    public void setLieu(String lieu) {
        System.out.println("Lieu depuis JS : " + lieu);
    }
}
