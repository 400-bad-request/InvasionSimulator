package sample.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class RSSIMethodLocator {

    // list of robots
    private List<Robot> robots;
    // list that will hold robots located the closest to antenna corresponding to each index
    private List<Robot> closestRobots;

    private double delta = 10;

    public RSSIMethodLocator(List<Robot> robots) {
        this.robots = robots;
        this.getClosestRobots();
    }

    private void getClosestRobots() {

        this.closestRobots = new ArrayList<>(Arrays.asList(this.robots.get(0), this.robots.get(0), this.robots.get(0)));

        for (Robot element : robots) {
            for (int index = 0; index < this.closestRobots.size(); index++) {
                if (element.getSignalStrengths().get(index) > this.closestRobots.get(index).getSignalStrengths().get(index))
                    this.closestRobots.set(index, element);
            }
        }
    }

    public boolean locate(MotherRobot mother) {

        System.out.println("1x = " + this.closestRobots.get(0).getSignalStrengths().get(0));
        System.out.println("1y = " + this.closestRobots.get(0).getSignalStrengths().get(1));
        System.out.println("1z = " + this.closestRobots.get(0).getSignalStrengths().get(2));
        System.out.println(" ------------------------------------------------------------- ");
        System.out.println("2x = " + this.closestRobots.get(1).getSignalStrengths().get(0));
        System.out.println("2y = " + this.closestRobots.get(1).getSignalStrengths().get(1));
        System.out.println("2z = " + this.closestRobots.get(1).getSignalStrengths().get(2));
        System.out.println(" ------------------------------------------------------------- ");
        System.out.println("3x = " + this.closestRobots.get(2).getSignalStrengths().get(0));
        System.out.println("3y = " + this.closestRobots.get(2).getSignalStrengths().get(1));
        System.out.println("3z = " + this.closestRobots.get(2).getSignalStrengths().get(2));
        System.out.println(" ------------------------------------------------------------- ");
        System.out.println("Mother.x = " + mother.getSignalStrengths().get(0));
        System.out.println("Mother.y = " + mother.getSignalStrengths().get(1));
        System.out.println("Mother.z = " + mother.getSignalStrengths().get(2));

        // iteration over signal values for mother robot
        for (int i = 0; i < mother.getSignalStrengths().size(); i++) {
            // iteration over robots found by getClosestRobots method
            // robots with max value of signal strength produced by corresponding antenna
            for (int j = 0; j < this.closestRobots.size(); j++) {


                if (i != j && mother.getSignalStrengths().get(i) < this.closestRobots.get(j).getSignalStrengths().get(i)) {
                    System.out.println("i = " + i);
                    System.out.println("j = " + j);
                    return false;
                }

            }
        }
        return true;
    }

    private boolean checkSingleDimention(int dimention, MotherRobot mother) {

        int firstDimention, secondDimention;

        switch (dimention) {
            case 0:
                firstDimention = 1;
                secondDimention = 2;
                break;
            case 1:
                firstDimention = 0;
                secondDimention = 2;
                break;
            case 2:
            default:
                firstDimention = 0;
                secondDimention = 1;
                break;
        }

        List<Robot> fistStep = new LinkedList<>();
        List<Robot> secondStep = new LinkedList<>();

        for (Robot element:this.robots) {
            if ((element.getSignalStrengths().get(firstDimention) > mother.getSignalStrengths().get(firstDimention) - this.delta) && (element.getSignalStrengths().get(firstDimention) < mother.getSignalStrengths().get(firstDimention) + this.delta)) {
                fistStep.add(element);
            }
        }

        for (Robot element:fistStep) {
            if ((element.getSignalStrengths().get(secondDimention) > mother.getSignalStrengths().get(secondDimention) - this.delta) && (element.getSignalStrengths().get(secondDimention) < mother.getSignalStrengths().get(secondDimention) + this.delta)) {
                secondStep.add(element);
            }
        }

        Robot max = secondStep.get(0);
        Robot min = secondStep.get(0);

        for (Robot element:secondStep) {
            if(element.getSignalStrengths().get(dimention) > max.getSignalStrengths().get(dimention)) {
                max = element;
            }
            if(element.getSignalStrengths().get(dimention) < min.getSignalStrengths().get(dimention)) {
                min = element;
            }
        }

        return (mother.getSignalStrengths().get(dimention) > (min.getSignalStrengths().get(dimention) + max.getSignalStrengths().get(dimention))/2);
    }

    public boolean locateNew(MotherRobot mother) {

        for(int i = 0; i < 3; i++) {
            if(!checkSingleDimention(i, mother)) {
                return false;
            }
        }
        return true;
    }
}
