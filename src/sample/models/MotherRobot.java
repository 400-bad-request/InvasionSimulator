package sample.models;

import java.util.List;

/**
 * Special kind of Robot, Mother Robot, inherits almost everything from normal Robot
 */
public class MotherRobot extends Robot {

    // CONSTRUCTORS
    //==================================================================================================================

    /**
     * Explicit constructor for mother robot, only diffrence between Robot and Mother robot constructor is
     * changing visualization Radius
     *
     * @param location        location for mother robot
     * @param signalStrengths signal strengths List for mother robot
     */
    public MotherRobot(Location location, List<Double> signalStrengths) {
        super(location, signalStrengths);
        this.visualizationRadius = 10;
    }
    //==================================================================================================================
}
