package sample.models.objects;

import java.util.Random;

/**
 * Updated by Piotr Skalski on 28.12.2017
 */
public class Location {
    private double x;
    private double y;

    /**
     * Explicit constructor
     * @param x value on X axis coordinate
     * @param y value on Y axis coordinate
     */
    public Location(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructor that creates random location within the range of full stage
     * @param stageWidth the maximum allowed value on X axis coordinate
     * @param stageHeight the maximum allowed value on Y axis coordinate
     */
    public Location(int stageWidth, int stageHeight) {
        Random random = new Random();
        this.x = random.nextDouble() * stageWidth;
        this.y = random.nextDouble() * stageHeight;
    }

    /**
     * Constructor that creates random location within selected quadrant
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

    // Getters
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    // Setters
    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}
