package sample.viewcontroller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
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
    // Flag that indicate whether grid will be rendered
    private boolean gridActive = true;

    // Declaration of graphics contexts for canvases
    private GraphicsContext constCtx;
    private GraphicsContext activeCtx;
    private GraphicsContext passiveCtx;

    // Declaration stage data object that will hold all model information
    private StageObjects stage;

    // Shapes used for visualization
    private Shape antennasTriangle;
    private Shape simpleTriangulationAreaTP;
    private Shape simpleTriangulationAreaFP;
    private Shape simpleTriangulationAreaFN;

    // REFERENCE TO JAVA FX CANVAS OBJECTS
    //==================================================================================================================

    @FXML private Canvas constCanvas;
    @FXML private Canvas activeCanvas;
    @FXML private Canvas passiveCanvas;
    @FXML private StackPane holder;
    @FXML private Group vizGroup;
    @FXML private ToggleButton regularViewButton;
    @FXML private ToggleButton heatMapButton;
    @FXML private ToggleButton simpleTriangulation;
    @FXML private Button gridButton;

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

                if (activeRobot == null) {
                    this.drawRobots(activeCtx, stage.getRobots());
                } else {
                    activeCtx.setFill(Color.BLACK);
                    activeRobot.draw(activeCtx);
                    activeCtx.setStroke(Color.BLACK);
                    activeCtx.strokeText(activeRobot.returnSignalInfo(), activeRobot.getLocation().getX() + 10, activeRobot.getLocation().getY() + 10);

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
     * Method is used to load full visualization on canvas.
     */
    private void fullRender() {

        this.clearCanvas(this.passiveCtx, Main.config.stageWidth, Main.config.stageHeight);
        this.clearCanvas(this.constCtx, Main.config.stageWidth, Main.config.stageHeight);

        // Drawing grid
        if (gridActive) {
            this.drawGrid(this.constCtx, Main.config.stageWidth, Main.config.stageHeight, Main.config.division);
        }

        // Draw antennas
        this.drawAntennas(this.passiveCtx, stage.getAntennas());

        // Draw mother robot
        this.drawMotherRobot(this.passiveCtx, stage.getMotherRobot());

        // Rendering visualization on active stage
        this.activeRender();
    }

    /**
     * Method is used to load active view on canvas.
     */
    private void activeRender() {
        // Clearing active canvas
        this.clearCanvas(this.activeCtx, Main.config.stageWidth, Main.config.stageHeight);
        // Removing simple triangulation positive area from scene
        if( this.simpleTriangulationAreaTP != null && this.simpleTriangulationAreaFP != null && this.simpleTriangulationAreaFN != null) {
            vizGroup.getChildren().removeAll(this.simpleTriangulationAreaTP, this.simpleTriangulationAreaFP, this.simpleTriangulationAreaFN);
        }
        // Loading appropriate visualization
        switch (this.activeView) {
            case "heat_map":
                this.drawHeatMap(activeCtx, stage.getRobots());
                break;
            case "regular":
                this.drawRobots(activeCtx, stage.getRobots());
                break;
            case "triangulation":
                this.drawTriangulation(activeCtx, stage.getRobots(), stage.getAntennas());
                break;
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

        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(new Double[]{
                antennas.get(0).getLocation().getX(), antennas.get(0).getLocation().getY(),
                antennas.get(1).getLocation().getX(), antennas.get(1).getLocation().getY(),
                antennas.get(2).getLocation().getX(), antennas.get(2).getLocation().getY() });

        this.antennasTriangle = polygon;

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
     * @param spacing     int number of pixels between each line of grid
     */
    private void drawGrid(GraphicsContext ctx, int stageWidth, int stageHeight, int spacing) {

        // Set new value of line width
        ctx.setLineWidth(1);

        // Number of lines to draw horizontally and vertically
        final int hLineCount = (int) Math.floor((stageHeight - 1) / spacing);
        final int vLineCount = (int) Math.floor((stageWidth - 1) / spacing);

        ctx.setStroke(Color.WHITE);

        for (int i = 1; i <= hLineCount; i++) {
            ctx.strokeLine(0, i * spacing, stageWidth, i * spacing);
        }

        for (int i = 1; i <= vLineCount; i++) {
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
        // Change the appearance of the cursor over the canvas
        passiveCanvas.setCursor(Cursor.CROSSHAIR);
        // Marking the default view button as pressed
        regularViewButton.setSelected(true);
        if (this.gridActive) {
            gridButton.setText("Grid on");
        } else {
            gridButton.setText("Grid off");
        }
    }

    /**
     * Method that is executed after clicking Heat Map button in view.
     * Method loads Heat Map on canvas.
     */
    public void heatMap() {
        // Change value of active view to heat map
        if (this.activeView.equals("heat_map")) {
            heatMapButton.setSelected(true);
        } else {
            this.activeView = "heat_map";
            this.activeRender();
        }
    }

    /**
     * Method that is executed after clicking Regular View button in view.
     * Method loads Regular View on canvas.
     */
    public void regularView() {
        // Change value of active view to regular
        if (this.activeView.equals("regular")) {
            regularViewButton.setSelected(true);
        } else {
            this.activeView = "regular";
            this.activeRender();
        }
    }

    /**
     * Method that is executed after clicking Method 1 button in view.
     * Method loads Simple Triangulation visualization on canvas.
     */
    public void triangulationView() {
        // Change value of active view to regular
        if (this.activeView.equals("triangulation")) {
            simpleTriangulation.setSelected(true);
        } else {
            this.activeView = "triangulation";
            this.activeRender();
        }
    }

    /**
     * Method that is executed after clicking New Model button in view.
     * Method creates new Stage Objects instance, and reload visualization for new data.
     */
    public void newModel() {
        // Creating stage object that will produce stage content information.
        this.stage = new StageObjects(Main.config);
        // Rendering visualization
        this.fullRender();
    }

    public void gridView() {

        if(this.gridActive) {
            this.clearCanvas(constCtx, Main.config.stageWidth, Main.config.stageHeight);
            gridButton.setText("Grid off");
        } else {
            this.drawGrid(constCtx, Main.config.stageWidth, Main.config.stageHeight, Main.config.division);
            gridButton.setText("Grid on");
        }

        this.gridActive = !this.gridActive;
    }

    /**
     * Method that is executed after clicking Back button in view.
     * Method load Configuration Scene on stage.
     * @param actionEvent event triggered after Back button click
     */
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
            // Timer shot down
            this.stopTimer();

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TODO: REFACTOR OF CODE BELOW.

    private void drawTriangulation(GraphicsContext ctx, List<Robot> robots, List<Antenna> antennas) {

        Circle c1 = new Circle();

        c1.setRadius(Math.min(
                calculateDistance2P(antennas.get(0).getLocation(), antennas.get(1).getLocation()),
                calculateDistance2P(antennas.get(0).getLocation(), antennas.get(2).getLocation())
        ));

        c1.setCenterX(antennas.get(0).getLocation().getX());
        c1.setCenterY(antennas.get(0).getLocation().getY());

        Circle c2 = new Circle();

        c2.setRadius(Math.min(
                calculateDistance2P(antennas.get(1).getLocation(), antennas.get(0).getLocation()),
                calculateDistance2P(antennas.get(1).getLocation(), antennas.get(2).getLocation())
        ));

        c2.setCenterX(antennas.get(1).getLocation().getX());
        c2.setCenterY(antennas.get(1).getLocation().getY());

        Circle c3 = new Circle();

        c3.setRadius(Math.min(
                calculateDistance2P(antennas.get(2).getLocation(), antennas.get(0).getLocation()),
                calculateDistance2P(antennas.get(2).getLocation(), antennas.get(1).getLocation())
        ));

        c3.setCenterX(antennas.get(2).getLocation().getX());
        c3.setCenterY(antennas.get(2).getLocation().getY());

        Rectangle r1 = new Rectangle();
        r1.setX(0);
        r1.setY(0);
        r1.setHeight(Main.config.stageHeight);
        r1.setWidth(Main.config.stageWidth);

        Shape area = Shape.intersect(c1, c2);
        area = Shape.intersect(area, c3);
        area = Shape.intersect(area, r1);

        this.simpleTriangulationAreaTP = Shape.intersect(area, this.antennasTriangle);
        this.simpleTriangulationAreaTP.setFill(Color.rgb(0, 255, 0, 0.2));
        this.simpleTriangulationAreaTP.setStroke(Color.WHITE);

        this.simpleTriangulationAreaFP = Shape.subtract(area, this.antennasTriangle);
        this.simpleTriangulationAreaFP.setFill(Color.rgb(255, 0, 0, 0.2));
        this.simpleTriangulationAreaFP.setStroke(Color.WHITE);

        this.simpleTriangulationAreaFN = Shape.subtract(this.antennasTriangle, area);
        this.simpleTriangulationAreaFN.setFill(Color.rgb(0, 0, 255, 0.2));
        this.simpleTriangulationAreaFN.setStroke(Color.WHITE);

        this.simpleTriangulationAreaTP.setCursor(Cursor.CROSSHAIR);
        this.simpleTriangulationAreaFP.setCursor(Cursor.CROSSHAIR);
        this.simpleTriangulationAreaFN.setCursor(Cursor.CROSSHAIR);
        vizGroup.getChildren().addAll(this.simpleTriangulationAreaTP, this.simpleTriangulationAreaFN, this.simpleTriangulationAreaFP);
    }

    private double calculateDistance2P(Location point_1, Location point_2) {

        return  Math.sqrt(
                Math.pow(Math.abs(point_1.getX() - point_2.getX()), 2) +
                        Math.pow(Math.abs(point_1.getY() - point_2.getY()), 2)
        );
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
