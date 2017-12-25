package sample.models;

import sample.models.objects.Location;

import java.util.List;

/**
 * Created by Jakub Adamczyk on 15.12.2017
 */
public class MotherRobot {
    private Location location;

    private List<Double> signalStrengths;

    public Location getLocation() {
        return location;
    }

    public MotherRobot(Location location, List<Double> signalStrengths) {
        this.location = location;
        this.signalStrengths = signalStrengths;
    }
}
