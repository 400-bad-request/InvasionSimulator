package sample.viewcontroller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sample.models.*;

import java.util.List;


public class EntryController {

//    public void drawRandomStage() {

        @FXML
        // Reference to the JavaFX Canvas object with fx:id="stageCanvas"
        private Canvas stageCanvas;

        // New stage object along with configuration object as a constructor parameter
        Configuration config = new Configuration();
        StageObjects stage = new StageObjects(config);

        @FXML
        void initialize() {
            // Setting canvas width and height
            stageCanvas.setWidth(config.stageWidth);
            stageCanvas.setHeight(config.stageWidth);

            // Acquisition of canvas context
            GraphicsContext gc = stageCanvas.getGraphicsContext2D();

            // Drawing stage content
            drawAntennas( gc, stage.getAntennas() );
            drawRobots( gc, stage.getRobots() );
            drawMotherRobot( gc, stage.getMotherRobot() );
        }

        /**
        * Method is used for visualisation of Antennas location.
        *
        * @param ctx GraphicsContext object allowing to draw on selected canvas
        * @param antennas List<Antenna> object containing information about the location of antennas
        */
        private void drawAntennas(GraphicsContext ctx, List<Antenna> antennas) {

            ctx.setFill(Color.RED);
            ctx.setStroke(Color.RED);
            int innerRadius = 5;
            int outerRadius = 10;

            for (Antenna element : antennas) {
                ctx.fillOval(element.getLocation().getX() - innerRadius, element.getLocation().getY() - innerRadius, 2* innerRadius, 2* innerRadius);
                ctx.strokeOval(element.getLocation().getX() - outerRadius, element.getLocation().getY() - outerRadius, 2* outerRadius, 2* outerRadius);
            }

            ctx.setFill(Color.rgb(255, 0, 0, 0.2));

            ctx.fillPolygon (
                new double[] { antennas.get(0).getLocation().getX(), antennas.get(1).getLocation().getX(), antennas.get(2).getLocation().getX() },
                new double[] { antennas.get(0).getLocation().getY(), antennas.get(1).getLocation().getY(), antennas.get(2).getLocation().getY() }, 3
            );

            ctx.strokePolygon (
                new double[] { antennas.get(0).getLocation().getX(), antennas.get(1).getLocation().getX(), antennas.get(2).getLocation().getX() },
                new double[] { antennas.get(0).getLocation().getY(), antennas.get(1).getLocation().getY(), antennas.get(2).getLocation().getY() }, 3
            );
        }

        /**
        * Method is used for visualisation of Robots location.
        *
        * @param ctx GraphicsContext object allowing to draw on selected canvas
        * @param robots List<Robot> object containing information about the location of robots
        */
        private void drawRobots(GraphicsContext ctx, List<Robot> robots) {

            ctx.setFill(Color.BLUE);
            ctx.setStroke(Color.BLUE);
            int radius = 5;

            for (Robot element : robots) {
                ctx.fillOval(element.getLocation().getX() - radius, element.getLocation().getY() - radius, 2* radius, 2* radius);
            }
        }

        /**
        * Method is used for visualisation of Mother Robot location.
        *
        * @param ctx GraphicsContext object allowing to draw on selected canvas
        * @param motherRobot  MotherRobot object containing information about the location of mother robot
        */
        private void drawMotherRobot(GraphicsContext ctx, MotherRobot motherRobot) {

            ctx.setFill(Color.BLUE);
            ctx.setStroke(Color.BLUE);
            int radius = 10;

            ctx.fillOval(motherRobot.getLocation().getX() - radius, motherRobot.getLocation().getY() - radius, 2* radius, 2* radius);

        }

//    }

}
