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
            2* visualizationRadius,
            2* visualizationRadius
        );
    }

    //	* Signal strengths property
//	* Getter for all properties together through signalStrength
//	* Set Signal Strengths by getting signal strength



}
