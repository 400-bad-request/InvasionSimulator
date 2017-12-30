package sample.viewcontroller;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import sample.Main;
import sample.models.*;
import sample.models.Robot;

import java.util.*;

/**
 * Created by Piotr Skalski on 27.12.2017
 */
public class BoardController {

    private Timer timer = new Timer();
    private ArrayList<Color> antennasColors = new ArrayList<Color>(Arrays.asList(
            Color.rgb(255,0,0),
            Color.rgb(0,255,0),
            Color.rgb(0,0,255)
    ));
    private boolean heatMapActive = false;

    // Reference to the JavaFX Canvas objects
    @FXML
    private Canvas constCanvas;
    @FXML
    private Canvas activeCanvas;
    @FXML
    private Canvas passiveCanvas;
    @FXML
    private StackPane holder;
    @FXML
    private Button heatMapButton;


    @FXML
    void initialize() {

        // Creating stage object that will produce stage content information.
        StageObjects stage = new StageObjects(Main.config);

        // Setting canvas width and height
        setStage(Main.config.stageWidth, Main.config.stageHeight);

        // Acquisition of canvas context
        GraphicsContext constCtx = constCanvas.getGraphicsContext2D();
        GraphicsContext activeCtx = activeCanvas.getGraphicsContext2D();
        GraphicsContext passiveCtx = passiveCanvas.getGraphicsContext2D();

        // Drawing grid
        drawGrid(constCtx, Main.config.stageWidth, Main.config.stageHeight);

        // Drawing stage content
        drawAntennas(passiveCtx, stage.getAntennas());
        drawRobots(activeCtx, stage.getRobots());
        drawMotherRobot(passiveCtx, stage.getMotherRobot());

        // Event listener that is responsible for showing robot info on hover.
        passiveCanvas.setOnMouseMoved(event -> {

            if(this.heatMapActive) {
                clearCanvas(activeCtx, Main.config.stageWidth, Main.config.stageHeight);
                this.drawHeatMap(activeCtx, stage.getRobots());
            } else {
                Robot activeRobot = robotOnHover(stage.getRobots(), event.getX(), event.getY());
                clearCanvas(activeCtx, Main.config.stageWidth, Main.config.stageHeight);

                if (activeRobot != null) {
                    activeCtx.setFill(Color.BLUE);
                    activeRobot.draw(activeCtx);

                    activeCtx.strokeText(activeRobot.returnSignalInfo(), activeRobot.getLocation().getX() + 10, activeRobot.getLocation().getY() + 10);
                } else {
                    drawRobots(activeCtx, stage.getRobots());
                }
            }
        });

        // Timer that allow for Antennas signal animation.
        this.timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                clearCanvas(passiveCtx, Main.config.stageWidth, Main.config.stageHeight);
                drawAntennas(passiveCtx, stage.getAntennas());
                drawMotherRobot(passiveCtx, stage.getMotherRobot());
            }
        }, 0, 100);
    }

    /**
     * Method is used for visualisation of Antennas location.
     *
     * @param ctx      GraphicsContext object allowing to draw on selected canvas
     * @param antennas List<Antenna> object containing information about the location of antennas
     */
    private void drawAntennas(GraphicsContext ctx, List<Antenna> antennas) {

        for (int i = 0; i < antennas.size(); i++) {
            ctx.setFill(antennasColors.get(i));
            ctx.setStroke(antennasColors.get(i));
            antennas.get(i).draw(ctx);
        }


        ctx.setStroke(Color.RED);
        ctx.setFill(Color.rgb(255, 0, 0, 0.2));

        ctx.fillPolygon(
                new double[]{antennas.get(0).getLocation().getX(), antennas.get(1).getLocation().getX(), antennas.get(2).getLocation().getX()},
                new double[]{antennas.get(0).getLocation().getY(), antennas.get(1).getLocation().getY(), antennas.get(2).getLocation().getY()}, 3
        );

        ctx.strokePolygon(
                new double[]{antennas.get(0).getLocation().getX(), antennas.get(1).getLocation().getX(), antennas.get(2).getLocation().getX()},
                new double[]{antennas.get(0).getLocation().getY(), antennas.get(1).getLocation().getY(), antennas.get(2).getLocation().getY()}, 3
        );
    }

    /**
     * Method is used for visualisation of Robots location.
     *
     * @param ctx    GraphicsContext object allowing to draw on selected canvas
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
     * @param ctx         GraphicsContext object allowing to draw on selected canvas
     * @param motherRobot MotherRobot object containing information about the location of mother robot
     */
    private void drawMotherRobot(GraphicsContext ctx, MotherRobot motherRobot) {
        // Setting visualization color.
        ctx.setFill(Color.GREEN);
        motherRobot.draw(ctx);
    }

    /**
     * Method is used for creating background grid.
     *
     * @param ctx         GraphicsContext object allowing to draw on selected canvas
     * @param stageWidth  int width of canvas.
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

        ctx.setStroke(Color.DARKGRAY);

        for (int i = 0; i < hLineCount; i++) {
            ctx.strokeLine(0, i * spacing, stageWidth, i * spacing);
        }

        for (int i = 0; i < vLineCount; i++) {
            ctx.strokeLine(i * spacing, 0, i * spacing, stageHeight);
        }
    }

    /**
     * Method is used for clearing canvas.
     *
     * @param ctx         GraphicsContext object allowing to draw on selected canvas
     * @param stageWidth  int width of canvas.
     * @param stageHeight int height of canvas.
     */
    private void clearCanvas(GraphicsContext ctx, int stageWidth, int stageHeight) {
        ctx.clearRect(0, 0, stageWidth, stageHeight);
    }

    /**
     * Method detect if any robot is located under mouse cursor on canvas.
     * If there is, it returns this instance of robot.
     *
     * @param robots List<Robot> object containing information about the location of robots
     * @param mouseX double mouse X coordinate
     * @param mouseY double mouse Y coordinate
     * @return robot object on success or null on fail
     */
    private Robot robotOnHover(List<Robot> robots, double mouseX, double mouseY) {

        for (Robot element : robots) {
            if ((Math.pow((element.getLocation().getX() - mouseX), 2) + Math.pow((element.getLocation().getY() - mouseY), 2)) <= 25) {
                return element;
            }
        }
        return null;
    }

    /**
     * Method sets location and size of view objects
     *
     * @param stageWidth  width of field
     * @param stageHeight height of field
     */
    private void setStage(int stageWidth, int stageHeight) {
        // Setting width of all canvases
        constCanvas.setWidth(stageWidth);
        activeCanvas.setWidth(stageWidth);
        passiveCanvas.setWidth(stageWidth);
        holder.setMaxWidth(stageWidth);
        // Setting height of all canvases
        constCanvas.setHeight(stageHeight);
        activeCanvas.setHeight(stageHeight);
        passiveCanvas.setHeight(stageHeight);
        holder.setMaxHeight(stageHeight);

        passiveCanvas.setCursor(Cursor.CROSSHAIR);
    }

    public void heatMap() {
        //Change value of heat map flag, what will turns on or off heatMap view
        this.heatMapActive = !this.heatMapActive;
    }

    private void drawHeatMap(GraphicsContext ctx, List<Robot> robots) {

        Robot minX = robots.get(0);
        Robot maxX = robots.get(0);

        Robot minY = robots.get(0);
        Robot maxY = robots.get(0);

        Robot minZ = robots.get(0);
        Robot maxZ = robots.get(0);

        for (Robot element : robots) {

            if (element.getSignalStrengths().get(0) < minX.getSignalStrengths().get(0)) minX = element;
            if (element.getSignalStrengths().get(0) > maxX.getSignalStrengths().get(0)) maxX = element;

            if (element.getSignalStrengths().get(1) < minY.getSignalStrengths().get(1)) minY = element;
            if (element.getSignalStrengths().get(1) > maxY.getSignalStrengths().get(1)) maxY = element;

            if (element.getSignalStrengths().get(2) < minZ.getSignalStrengths().get(2)) minZ = element;
            if (element.getSignalStrengths().get(2) > maxZ.getSignalStrengths().get(2)) maxZ = element;
        }

        double deltaX = Math.abs(maxX.getSignalStrengths().get(0) - minX.getSignalStrengths().get(0));
        double deltaY = Math.abs(maxY.getSignalStrengths().get(1) - minY.getSignalStrengths().get(1));
        double deltaZ = Math.abs(maxZ.getSignalStrengths().get(2) - minZ.getSignalStrengths().get(2));

        for (Robot element : robots) {

            int red = (int) (255 * Math.abs(element.getSignalStrengths().get(0) - minX.getSignalStrengths().get(0)) / deltaX);
            int green = (int) (255 * Math.abs(element.getSignalStrengths().get(1) - minY.getSignalStrengths().get(1)) / deltaY);
            int blue = (int) (255 * Math.abs(element.getSignalStrengths().get(2) - minZ.getSignalStrengths().get(2)) / deltaZ);

//            System.out.println(red);
//            System.out.println(green);
//            System.out.println(blue);

            ctx.setFill(Color.rgb(red,green,blue));
            element.draw(ctx);
        }
    }

    public void stopTimer() {
        this.timer.cancel();
        this.timer.purge();
    }
}
