package sample.models;

import java.util.Random;

/**
 * Location wrapper class that holds two coordinates
 */
public class Location {

    // PROPERTIES
    //==================================================================================================================

    // x
    private double x;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    // y
    private double y;

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
    //==================================================================================================================

    // CONSTRUCTORS
    //==================================================================================================================

    /**
     * Explicit constructor
     *
     * @param x value on X axis coordinate
     * @param y value on Y axis coordinate
     */
    public Location(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructor that creates random location within the range of full stage
     *
     * @param stageWidth  the maximum allowed value on X axis coordinate
     * @param stageHeight the maximum allowed value on Y axis coordinate
     */
    public Location(int stageWidth, int stageHeight) {
        Random random = new Random();
        this.x = random.nextDouble() * stageWidth;
        this.y = random.nextDouble() * stageHeight;
    }

    /**
     * Constructor that creates random location within selected quadrant
     *
     * @param minX the minimum allowed value on X axis coordinate
     * @param maxX the maximum allowed value on X axis coordinate
     * @param minY the minimum allowed value on Y axis coordinate
     * @param maxY the maximum allowed value on Y axis coordinate
     */
    public Location(int minX, int maxX, int minY, int maxY) {
        Random random = new Random();
        this.x = minX + random.nextDouble() * (maxX - minX);
        this.y = minY + random.nextDouble() * (maxY - minY);
    }
    //==================================================================================================================
}
