package sample.models;

import javafx.scene.canvas.GraphicsContext;
import sample.models.objects.Location;

import java.util.List;

/**
 * Created by Jakub Adamczyk on 15.12.2017
 */
public class MotherRobot {
    private Location location;
    private int visualizationRadius = 10;

    private List<Double> signalStrengths;

    public Location getLocation() {
        return location;
    }

    public MotherRobot(Location location, List<Double> signalStrengths) {
        this.location = location;
        this.signalStrengths = signalStrengths;
    }

    /**
    * Method is used for visualisation of Mother Robot on canvas.
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
}
