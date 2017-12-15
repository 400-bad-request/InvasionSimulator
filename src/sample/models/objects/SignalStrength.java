package sample.models.objects;

/**
 * Created by Jakub Adamczyk on 15.12.2017
 */
public class SignalStrength {
    private double value;
    private int id;

    public SignalStrength(double value, int id) {
        this.value = value;
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public int getId() {
        return id;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
