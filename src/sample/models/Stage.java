package sample.models;

import sample.models.objects.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Stage {
    private MotherRobot motherRobot;
    private List<Robot> robots;
    private List<Antenna> antennas;

    public Stage(Configuration config) {
        Random random = new Random();
        this.antennas = new ArrayList<Antenna>(
                Arrays.asList(
                        new Antenna(new Location(random.nextDouble(), random.nextDouble()), config.a, config.n),
                        new Antenna(new Location(random.nextDouble(), random.nextDouble()), config.a, config.n),
                        new Antenna(new Location(random.nextDouble(), random.nextDouble()), config.a, config.n)
                )
        );
    }
}

