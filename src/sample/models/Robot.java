package sample.models;

import sample.models.objects.Location;

import java.util.List;

/**
 * Created by Jakub Adamczyk on 15.12.2017
 */
public class Robot {
    private Location location;
    private List<Double> signalStrengths;

    public Location getLocation() {
        return location;
    }

    public Robot(Location location, List<Double> signalStrengths) {
        this.location = location;
        this.signalStrengths = signalStrengths;
    }

    //	* Signal strengths property
//	* Getter for all properties together through signalStrength
//	* Set Signal Strengths by getting signal strength



}
