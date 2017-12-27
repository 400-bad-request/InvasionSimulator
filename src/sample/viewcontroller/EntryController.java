package sample.viewcontroller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import sample.models.Antenna;
import sample.models.Configuration;
import sample.models.Robot;
import sample.models.StageObjects;

import java.util.List;


public class EntryController {

//    public void drawRandomStage() {

        @FXML // fx:id="stageCanvas"
        private Canvas stageCanvas;

        // New stage object along with configuration object as a constructor parameter
        Configuration config = new Configuration();
        StageObjects stage = new StageObjects(config);

        @FXML
        void initialize() {
            GraphicsContext gc = stageCanvas.getGraphicsContext2D();
            drawAntenas( gc, stage.getAntennas() );
            drawRobots( gc, stage.getRobots() );
        }

        private void drawAntenas(GraphicsContext ctx, List<Antenna> antennas) {

            ctx.setFill(Color.RED);
            ctx.setStroke(Color.RED);

            for (Antenna element : antennas) {
                ctx.fillOval(element.getLocation().getX()-5, element.getLocation().getY()-5, 10, 10);
            }

            ctx.setFill(Color.rgb(255, 0, 0, 0.5));

            ctx.fillPolygon (
                new double[] { antennas.get(0).getLocation().getX(), antennas.get(1).getLocation().getX(), antennas.get(2).getLocation().getX() },
                new double[] { antennas.get(0).getLocation().getY(), antennas.get(1).getLocation().getY(), antennas.get(2).getLocation().getY() }, 3
            );
        }

        private void drawRobots(GraphicsContext ctx, List<Robot> robots) {

            ctx.setFill(Color.BLUE);
            ctx.setStroke(Color.BLUE);

            for (Robot element : robots) {
                ctx.fillOval(element.getLocation().getX()-5, element.getLocation().getY()-5, 10, 10);
            }
        }

//    }

}
