package sample.viewcontroller;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sample.models.*;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class BoardController {

//    public void drawRandomStage() {

        // Reference to the JavaFX Canvas objects
        @FXML
        private Canvas constCanvas;
        @FXML
        private Canvas activeCanvas;
        @FXML
        private Canvas passiveCanvas;
        @FXML
        private Group stageGroup;

        // New stage object along with configuration object as a constructor parameter
        Configuration config = new Configuration();
        StageObjects stage = new StageObjects(config);

        @FXML
        void initialize() {
            // Setting canvas width and height
            constCanvas.setWidth(config.stageWidth);
            activeCanvas.setWidth(config.stageWidth);
            passiveCanvas.setWidth(config.stageWidth);

            constCanvas.setHeight(config.stageWidth);
            activeCanvas.setHeight(config.stageWidth);
            passiveCanvas.setHeight(config.stageWidth);

            // Acquisition of canvas context
            GraphicsContext constCtx = constCanvas.getGraphicsContext2D();
            GraphicsContext activeCtx = activeCanvas.getGraphicsContext2D();
            GraphicsContext passiveCtx = passiveCanvas.getGraphicsContext2D();

            // Drawing grid
            drawGrid( constCtx, config.stageWidth, config.stageHeight);

            // Drawing stage content
            drawAntennas( passiveCtx, stage.getAntennas() );
            drawRobots( activeCtx, stage.getRobots() );
            drawMotherRobot( passiveCtx, stage.getMotherRobot() );

            // Creating event listener
            stageGroup.setOnMouseMoved(event -> {
                Robot activeRobot = robotOnHover(stage.getRobots(), event.getX(), event.getY());

                clearCanvas(activeCtx, config.stageWidth, config.stageHeight);

                if (activeRobot != null) {
                    activeCtx.setFill(Color.BLUE);
                    activeRobot.draw(activeCtx);


                    activeCtx.strokeText(activeRobot.returnSignalInfo(), activeRobot.getLocation().getX() + 10, activeRobot.getLocation().getY() + 10);
                } else {
                    drawRobots( activeCtx, stage.getRobots() );
                }

            });

            // Antenna animation
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    clearCanvas(passiveCtx, config.stageWidth, config.stageHeight);
                    drawAntennas( passiveCtx, stage.getAntennas() );
                    drawMotherRobot( passiveCtx, stage.getMotherRobot() );
                }
            }, 0, 100);
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

            for (Antenna element : antennas) {
                element.draw(ctx);
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
            // Setting visualization color.
            ctx.setFill(Color.BLUE);

            for (Robot element : robots) {
                element.draw(ctx);
            }
        }

        /**
        * Method is used for visualisation of Mother Robot location.
        *
        * @param ctx GraphicsContext object allowing to draw on selected canvas
        * @param motherRobot  MotherRobot object containing information about the location of mother robot
        */
        private void drawMotherRobot(GraphicsContext ctx, MotherRobot motherRobot) {
            // Setting visualization color.
            ctx.setFill(Color.BLUE);
            motherRobot.draw(ctx);
        }

        /**
        * Method is used for creating background grid.
        *
        * @param ctx GraphicsContext object allowing to draw on selected canvas
        * @param stageWidth int width of canvas.
        * @param stageHeight int height of canvas.
        */
        private void drawGrid(GraphicsContext ctx, int stageWidth, int stageHeight) {
            // Spacing between lines of the grid
            int spacing = 50;

            // Set new value of line width
            ctx.setLineWidth(1);

            // Number of lines to draw horizontally and vertically
            final int hLineCount = (int) Math.floor((stageHeight + 1) / spacing);
            final int vLineCount = (int) Math.floor((stageWidth + 1) / spacing);

            ctx.setStroke(Color.LIGHTGRAY);

            for (int i = 0; i < hLineCount; i++) {
                ctx.strokeLine(0, i * spacing, stageWidth,i * spacing);
            }

            for (int i = 0; i < vLineCount; i++) {
                ctx.strokeLine(i * spacing, 0, i * spacing, stageHeight);
            }
        }

        /**
        * Method is used for clearing canvas.
        *
        * @param ctx GraphicsContext object allowing to draw on selected canvas
        * @param stageWidth int width of canvas.
        * @param stageHeight int height of canvas.
        */
        private void clearCanvas(GraphicsContext ctx, int stageWidth, int stageHeight) {
            ctx.clearRect(0, 0, stageWidth, stageHeight);
        }

        private Robot robotOnHover(List<Robot> robots, double mouseX, double mouseY) {

            for (Robot element : robots) {
                if ( (Math.pow((element.getLocation().getX() - mouseX), 2) + Math.pow((element.getLocation().getY() - mouseY), 2))  <= 25) {
                    return element;
                }
            }
            return null;
        }

//    }

}
