package sample.viewcontroller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sample.Main;
import sample.models.*;
import sample.models.Robot;

import java.io.IOException;
import java.util.*;

/**
 * Created by Piotr Skalski on 27.12.2017
 */
public class BoardController {

    // PROPERTIES
    //==================================================================================================================

    // Timer allowing animation execution
    private Timer timer = new Timer();

    // Array that holds colors assigned to appropriate antennas
    private ArrayList<Color> antennasColors = new ArrayList<>(Arrays.asList(
            Color.rgb(255,0,0),
            Color.rgb(0,255,0),
            Color.rgb(0,0,255)
    ));

    // String that represents type of view that is currently displayed on canvas
    private String activeView = "regular";

    // Declaration of graphics contexts for canvases
    private GraphicsContext constCtx;
    private GraphicsContext activeCtx;
    private GraphicsContext passiveCtx;

    // Declaration stage data object that will hold all model information
    private StageObjects stage;

    // REFERENCE TO JAVA FX CANVAS OBJECTS
    //==================================================================================================================

    @FXML private Canvas constCanvas;
    @FXML private Canvas activeCanvas;
    @FXML private Canvas passiveCanvas;
    @FXML private StackPane holder;
    @FXML private HBox content;
    @FXML private ToggleButton regularViewButton;
    @FXML private ToggleButton heatMapButton;

    // METHODS
    //==================================================================================================================

    @FXML
    void initialize() {

        // Creating stage object that will produce stage content information.
        this.stage = new StageObjects(Main.config);

        // Acquisition of canvas context
        this.constCtx = constCanvas.getGraphicsContext2D();
        this.activeCtx = activeCanvas.getGraphicsContext2D();
        this.passiveCtx = passiveCanvas.getGraphicsContext2D();

        // Setting size, position and styling of components inside stage
        setStage(Main.config.stageWidth, Main.config.stageHeight);

        // Rendering visualization
        this.fullRender();

        // Event listener that is responsible for showing robot info on hover.
        this.passiveCanvas.setOnMouseMoved(event -> {

            if(this.activeView.equals("regular")) {

                Robot activeRobot = this.robotOnHover(stage.getRobots(), event.getX(), event.getY());
                this.clearCanvas(activeCtx, Main.config.stageWidth, Main.config.stageHeight);

                if (activeRobot != null) {

                    activeCtx.setFill(Color.BLACK);
                    activeRobot.draw(activeCtx);
                    activeCtx.setStroke(Color.BLACK);
                    activeCtx.strokeText(activeRobot.returnSignalInfo(), activeRobot.getLocation().getX() + 10, activeRobot.getLocation().getY() + 10);

                } else {

                    this.drawRobots(activeCtx, stage.getRobots());

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

    private void fullRender() {

        this.clearCanvas(this.passiveCtx, Main.config.stageWidth, Main.config.stageHeight);
        this.clearCanvas(this.constCtx, Main.config.stageWidth, Main.config.stageHeight);

        // Drawing grid
        this.drawGrid(this.constCtx, Main.config.stageWidth, Main.config.stageHeight);

        // Draw antennas
        this.drawAntennas(this.passiveCtx, stage.getAntennas());

        // Draw mother robot
        this.drawMotherRobot(this.passiveCtx, stage.getMotherRobot());

        // Rendering visualization on active stage
        this.activeRender();
    }

    private void activeRender() {

        this.clearCanvas(this.activeCtx, Main.config.stageWidth, Main.config.stageHeight);

        if(this.activeView.equals("heat_map")) {
            this.drawHeatMap(activeCtx, stage.getRobots());
        } else if(this.activeView.equals("regular")) {
            this.drawRobots(activeCtx, stage.getRobots());
        }

    }

    /**
     * Method is used for visualisation of Antennas location.
     *
     * @param ctx      GraphicsContext object allowing to draw on selected canvas
     * @param antennas List<Antenna> object containing information about the location of antennas
     */
    private void drawAntennas(GraphicsContext ctx, List<Antenna> antennas) {

        ctx.setStroke(Color.WHITE);
        ctx.setFill(Color.rgb(255, 255, 255, 0.2));

        ctx.fillPolygon(
                new double[]{antennas.get(0).getLocation().getX(), antennas.get(1).getLocation().getX(), antennas.get(2).getLocation().getX()},
                new double[]{antennas.get(0).getLocation().getY(), antennas.get(1).getLocation().getY(), antennas.get(2).getLocation().getY()}, 3
        );

        ctx.strokePolygon(
                new double[]{antennas.get(0).getLocation().getX(), antennas.get(1).getLocation().getX(), antennas.get(2).getLocation().getX()},
                new double[]{antennas.get(0).getLocation().getY(), antennas.get(1).getLocation().getY(), antennas.get(2).getLocation().getY()}, 3
        );

        for (int i = 0; i < antennas.size(); i++) {
            ctx.setFill(antennasColors.get(i));
            ctx.setStroke(Color.WHITE);
            antennas.get(i).draw(ctx);
        }
    }

    /**
     * Method is used for visualisation of Robots location.
     *
     * @param ctx    GraphicsContext object allowing to draw on selected canvas
     * @param robots List<Robot> object containing information about the location of robots
     */
    private void drawRobots(GraphicsContext ctx, List<Robot> robots) {
        // Setting visualization color.
        ctx.setStroke(Color.DARKGRAY);
        ctx.setFill(Color.BLACK);

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
        ctx.setFill(Color.WHITE);
        ctx.setStroke(Color.BLACK);
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

        ctx.setStroke(Color.WHITE);

        for (int i = 1; i < hLineCount; i++) {
            ctx.strokeLine(0, i * spacing, stageWidth, i * spacing);
        }

        for (int i = 1; i < vLineCount; i++) {
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
        regularViewButton.setSelected(true);
    }

    public void heatMap() {
        // Change value of active view to heat map
        if (this.activeView.equals("heat_map")) {
            heatMapButton.setSelected(true);
        } else {
            this.activeView = "heat_map";
            this.activeRender();
        }
    }

    public void regularView() {
        // Change value of active view to regular
        if (this.activeView.equals("regular")) {
            regularViewButton.setSelected(true);
        } else {
            this.activeView = "regular";
            this.activeRender();
        }
    }

    public void newModel() {
        // Creating stage object that will produce stage content information.
        this.stage = new StageObjects(Main.config);
        // Rendering visualization
        this.fullRender();
    }

    public void newConfig(ActionEvent actionEvent) {
        try {
            // Load new view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("config.fxml"));
            Parent root = loader.load();

            // Get screen size of monitor
            Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
            // Creating new scene
            Scene newScene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
            // Acquire stage
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            // Embedding new stage and configuring it's parameters
            stage.setMaximized(true);
            stage.setScene(newScene);
            stage.setTitle("Model Configuration");

            this.stopTimer();

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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


            ctx.setFill(Color.rgb(red,green,blue));
            ctx.setStroke(Color.BLACK);
            element.draw(ctx);
        }
    }

    public void stopTimer() {
        this.timer.cancel();
        this.timer.purge();
    }
}
