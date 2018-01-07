package sample.models;

import java.util.ArrayList;
import java.util.List;

public class StageObjects {

    // PROPERTIES
    //==================================================================================================================
    //config:
    private Configuration config;

    // motherRobot:
    private MotherRobot motherRobot;

    public MotherRobot getMotherRobot() {
        return this.motherRobot;
    }

    // robots:
    private List<Robot> robots;

    public List<Robot> getRobots() {
        return this.robots;
    }

    // antennas:
    private List<Antenna> antennas;

    public List<Antenna> getAntennas() {
        return this.antennas;
    }

    //==================================================================================================================

    // CONSTRUCTORS
    //==================================================================================================================

    /**
     * Creating board content with all of it's elements like robots mother robot antennas
     *
     * @param config object containing required parameters to conduct simulation
     */
    public StageObjects(Configuration config) {

        this.config = config;

        // Generating randomly located antennas
        this.createAntennas();

        while (!validateAntennasPosition()) {
            this.createAntennas();
        }

        // Generating ArrayList of randomly located robots
        this.createRobots();
        // Generating randomly located mother robot
        this.createMotherRobot();
    }
    //==================================================================================================================

    // METHODS
    //==================================================================================================================

    /**
     * Method is used to create "randomly" located antennas
     */
    private void createAntennas() {

        this.antennas = new ArrayList<>();
        // Creating required amount of antennas
        for (int i = 0; i < 3; i++) {
            this.antennas.add(new Antenna(new Location(this.config.stageWidth, this.config.stageHeight), this.config.a, this.config.n));
        }
    }

    /**
     * Method is used to create "randomly" located mother robot
     */
    private void createMotherRobot() {
        Location location = new Location(this.config.stageWidth, this.config.stageHeight);

        List<Double> strengths = new ArrayList<>();
        for (Antenna antenna : antennas) {
            strengths.add(antenna.calculateStrength(location.getX(), location.getY()));
        }

        this.motherRobot = new MotherRobot(location, strengths);
    }

    /**
     * Method is used to create "randomly" located robots with selected density
     */
    private void createRobots() {

        // Generating ArrayList of randomly located robots
        this.robots = new ArrayList<>();

        // Iteration over horizontal divisions of field
        // Horizontal coordinate will be drown from the range of numbers between [horizontalLimit, horizontalLimit + division]
        for (int horizontalLimit = 0; horizontalLimit < this.config.stageWidth; horizontalLimit += this.config.division) {

            // Iteration over vertical divisions of field
            // Vertical coordinate will be drown from the range of numbers between [verticalLimit, verticalLimit + division]
            for (int verticalLimit = 0; verticalLimit < this.config.stageHeight; verticalLimit += this.config.division) {

                // Creating required amount of robots per quadrant
                for (int i = 0; i < this.config.robotsDensity; i++) {

                    // Random location of single robot within selected quadrant
                    Location location = new Location(horizontalLimit, horizontalLimit + this.config.division, verticalLimit, verticalLimit + this.config.division);

                    // Calculation of signal strength for subsequent antennas
                    List<Double> strengths = new ArrayList<>();
                    for (Antenna antenna : antennas) {
                        strengths.add(antenna.calculateStrength(location.getX(), location.getY()));

                        this.robots.add(new Robot(location, strengths));
                    }
                }
            }
        }
    }

    /**
     * Method checks whether antennas positions creates triangle with area higher than minimal allowed
     *
     * @return flag indicating whether the positions of the antennas are accepted
     */
    private boolean validateAntennasPosition() {

        // Definition of minimal ratio between triangle created by antennas position and total board field
        double minFieldRatio = 0.05;

        double triangleField = 0.5 * Math.abs(
                (this.antennas.get(0).getLocation().getX() - this.antennas.get(2).getLocation().getX()) *
                        (this.antennas.get(1).getLocation().getY() - this.antennas.get(0).getLocation().getY()) -
                        (this.antennas.get(0).getLocation().getX() - this.antennas.get(1).getLocation().getX()) *
                                (this.antennas.get(2).getLocation().getY() - this.antennas.get(0).getLocation().getY())
        );

        double boardField = this.config.stageWidth * this.config.stageHeight;
        return (triangleField > minFieldRatio * boardField);
    }
    //==================================================================================================================
}

