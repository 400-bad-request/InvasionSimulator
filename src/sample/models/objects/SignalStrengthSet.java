package sample.models.objects;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Jakub Adamczyk on 15.12.2017
 */
public class SignalStrengthSet {
    private SignalStrength first;
    private SignalStrength second;
    private SignalStrength third;

    public SignalStrengthSet(SignalStrength first, SignalStrength second, SignalStrength third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

//    Getters

    public SignalStrength getFirst() {
        return first;
    }

    public SignalStrength getSecond() {
        return second;
    }

    public SignalStrength getThird() {
        return third;
    }

    public List<SignalStrength> asList() {
        return Arrays.asList(this.first, this.second, this.third);
    }
}
