package sample.models;

import sample.models.objects.Location;
import sample.models.objects.SignalStrength;

/**
 * Created by Jakub Adamczyk on 15.12.2017
 */
public class Antenna {
    private Location location;

    // properties for calculatin RSSI signal strength:
    private double a;
    private double n;

    public Antenna(Location location, double a, double n) {
        this.location = location;
        this.a = a;
        this.n = n;
    }

    /**
     * Method is used for calculating signal strength for certain node/robot
     * @param robot Robot object for which we calculateStrength
     * @return SignalStrength obeject with signal strengths for antenna we use to calcualte
     */
    public SignalStrength calculateStrength(Robot robot) {

    }

    /**
     * Method is used for calculating signal strength for main motherRobot
     * @param motherRobot MotherRobot parameter for which we calculate signal strength
     * @return SignalStrength obeject with signal strengths for antenna we use to calcualte
     */
    public SignalStrength calculateStrength(MotherRobot motherRobot) {

    }

}
