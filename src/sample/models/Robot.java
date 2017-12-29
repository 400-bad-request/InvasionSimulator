package sample.models;

import javafx.scene.canvas.GraphicsContext;
import sample.models.objects.Location;

import java.util.List;

/**
 * Created by Jakub Adamczyk on 15.12.2017
 */
public class Robot {
    private Location location;
    private List<Double> signalStrengths;
    private int visualizationRadius = 5;

    public Location getLocation() {
        return location;
    }

    public List<Double> getSignalStrengths() {
        return signalStrengths;
    }

    public Robot(Location location, List<Double> signalStrengths) {
        this.location = location;
        this.signalStrengths = signalStrengths;
    }

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
    }

    /**
    * Method is create string containing informations about signal strengths.
    */
    public String returnSignalInfo() {
        StringBuilder info = new StringBuilder();
        int i = 1;
        for(Double value : signalStrengths) {
            info.append(Integer.toString(i)).append(": ").append(Double.toString(value)).append(System.lineSeparator());
            i++;
        }
//        return info.substring(0, info.length() - 1);
        return info.toString();
    }

    //	* Signal strengths property
//	* Getter for all properties together through signalStrength
//	* Set Signal Strengths by getting signal strength



}
