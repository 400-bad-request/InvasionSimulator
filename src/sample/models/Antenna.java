package sample.models;

import javafx.scene.canvas.GraphicsContext;
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

    private int visualizationInnerRadius = 5;
    private int visualizationOuterRadius = 0;

    public Location getLocation() { return this.location; }
    public double getA() { return this.a; }
    public double getN() { return this.n; }

    public void setLocation(Location location) { this.location = location; }
    public void setA(double a) { this.a = a; }
    public void setN(double n) { this.n = n; }

    public Antenna(Location location, double a, double n) {
        setLocation(location);
        setA(a);
        setN(n);
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

    /**
    * Method is used for visualisation of Antenna on canvas.
    *
    * @param ctx GraphicsContext object allowing to draw on selected canvas
    */
    public void draw(GraphicsContext ctx) {
        ctx.fillOval(
                location.getX() - visualizationInnerRadius,
                location.getY() - visualizationInnerRadius,
                2* visualizationInnerRadius,
                2* visualizationInnerRadius
        );

        ctx.strokeOval(
                location.getX() - visualizationOuterRadius,
                location.getY() - visualizationOuterRadius,
                2* visualizationOuterRadius,
                2* visualizationOuterRadius
        );

        visualizationOuterRadius = (visualizationOuterRadius + 2) % 20;
    }
}
