package sample.models;

import javafx.scene.canvas.GraphicsContext;

import java.util.List;

/**
 * Created by Jakub Adamczyk on 15.12.2017
 */
public class Robot {
    // PROPERTIES
    //==================================================================================================================
    private Location location;

    // Location getters/setters:
    public Location getLocation() {
        return location;
    }

    private List<Double> signalStrengths;

    // signal strengths getters/setters:
    public List<Double> getSignalStrengths() {
        return signalStrengths;
    }

    protected int visualizationRadius;
    //==================================================================================================================

    // CONSTRUCTORS
    //==================================================================================================================

    /**
     * @param location
     * @param signalStrengths
     */
    public Robot(Location location, List<Double> signalStrengths) {
        this.location = location;
        this.signalStrengths = signalStrengths;
        this.visualizationRadius = 5;
    }
    //==================================================================================================================

    // METHODS
    //==================================================================================================================

    /**
     * Method is used for visualisation of Robot on canvas.
     *
     * @param ctx GraphicsContext object allowing to draw on selected canvas
     */
    public void draw(GraphicsContext ctx) {
        ctx.fillOval(
                location.getX() - visualizationRadius,
                location.getY() - visualizationRadius,
                2 * visualizationRadius,
                2 * visualizationRadius
        );

        ctx.strokeOval(
                location.getX() - visualizationRadius,
                location.getY() - visualizationRadius,
                2 * visualizationRadius,
                2 * visualizationRadius
        );
    }

    /**
     * Method is create string containing informations about signal strengths.
     */
    public String returnSignalInfo() {
        StringBuilder info = new StringBuilder();
        int i = 1;
        for (Double value : signalStrengths) {
            info.append(Integer.toString(i)).append(": ").append(Double.toString(Math.round(value * 100.0) / 100.0)).append(System.lineSeparator());
            i++;
        }
        return info.toString();
    }
    //==================================================================================================================
}
