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
     *
     * @param robot Robot object for which we calculateStrength
     * @return double obeject with signal strengths for antenna we use to calcualte
     */
    public double calculateStrength(Robot robot) {
        double distance = Math.sqrt(
                Math.pow(Math.abs(location.getX() - robot.getLocation().getX()), 2) +
                Math.pow(Math.abs(location.getY() - robot.getLocation().getY()), 2)
        );

        return a - 10 * n * Math.log10(distance);
    }

    /**
     * Universal method for calculating signal strength
     * @param x double first location property
     * @param y double second location property
     * @return double with strength value
     */
    public double calculateStrength(double x, double y) {
        double distance = Math.sqrt(
                Math.pow(Math.abs(location.getX() - x), 2) +
                        Math.pow(Math.abs(location.getY() - y), 2)
        );

        return a - 10 * n * Math.log10(distance);
    }

    /**
     * Method is used for calculating signal strength for main motherRobot
     *
     * @param motherRobot MotherRobot parameter for which we calculate signal strength
     * @return double obeject with signal strengths for antenna we use to calcualte
     */
    public double calculateStrength(MotherRobot motherRobot) {
        double distance = Math.sqrt(
                Math.pow(Math.abs(location.getX() - motherRobot.getLocation().getX()), 2) +
                        Math.pow(Math.abs(location.getY() - motherRobot.getLocation().getY()), 2)
        );

        return a - 10 * n * Math.log10(distance);
    }

}
