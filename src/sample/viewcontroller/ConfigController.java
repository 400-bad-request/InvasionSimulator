package sample.viewcontroller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;
import sample.Main;
import sample.models.Configuration;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;


public class ConfigController {

    // Reference to the JavaFX objects from FXML
    @FXML private TextField aCoefficient;
    @FXML private TextField nCoefficient;
    @FXML private TextField fieldWidth;
    @FXML private TextField fieldHeight;
    @FXML private TextField robotsDensity;
    @FXML private TextField division;

    @FXML
    public void initialize() {
        // Assign value to config field
        Main.config = new Configuration();
        // Setting default values to text boxes
        setDefaultValues();
    }

    /**
     * Set default values to text fields and configure field filters and converters
     */
    private void setDefaultValues() {

        // Pattern for double number field
        Pattern validEditingStateDouble = Pattern.compile("-?(([1-9][0-9]*)|0)?(\\.[0-9]*)?");
        // Pattern for integer number field
        Pattern validEditingStateInteger = Pattern.compile("-?([0-9]+)?");

        // Filter for double number field
        UnaryOperator<TextFormatter.Change> filterDouble = c -> {
            String text = c.getControlNewText();
            if (validEditingStateDouble.matcher(text).matches()) {
                return c ;
            } else {
                return null ;
            }
        };

        // Filter for integer number field
        UnaryOperator<TextFormatter.Change> filterInteger = c -> {
            String text = c.getControlNewText();
            if (validEditingStateInteger.matcher(text).matches()) {
                return c ;
            } else {
                return null ;
            }
        };

        // Converter for double number field
        StringConverter<Double> converterDouble = new StringConverter<Double>() {

            @Override
            public Double fromString(String s) {
                if (s.isEmpty() || "-".equals(s) || ".".equals(s) || "-.".equals(s)) {
                    return 0.0 ;
                } else {
                    return Double.valueOf(s);
                }
            }

            @Override
            public String toString(Double d) {
                return d.toString();
            }
        };

        // Converter for integer number field
        StringConverter<Integer> converterInteger = new StringConverter<Integer>() {

            @Override
            public Integer fromString(String s) {
                if (s.isEmpty() || "-".equals(s) || ".".equals(s) || "-.".equals(s)) {
                    return 0 ;
                } else {
                    return Integer.valueOf(s);
                }
            }

            @Override
            public String toString(Integer i) {
                return i.toString();
            }
        };

        // Setting default text fields values
        TextFormatter<Double> aCoefficientTextFormatter = new TextFormatter<>(converterDouble, Main.config.a, filterDouble);
        aCoefficient.setTextFormatter(aCoefficientTextFormatter);

        TextFormatter<Double> nCoefficientTextFormatter = new TextFormatter<>(converterDouble, Main.config.n, filterDouble);
        nCoefficient.setTextFormatter(nCoefficientTextFormatter);

        TextFormatter<Integer> stageWidthTextFormatter = new TextFormatter<>(converterInteger, Main.config.stageWidth, filterInteger);
        fieldWidth.setTextFormatter(stageWidthTextFormatter);

        TextFormatter<Integer> stageHeightTextFormatter = new TextFormatter<>(converterInteger, Main.config.stageHeight, filterInteger);
        fieldHeight.setTextFormatter(stageHeightTextFormatter);

        TextFormatter<Integer> robotsDensityTextFormatter = new TextFormatter<>(converterInteger, Main.config.robotsDensity, filterInteger);
        robotsDensity.setTextFormatter(robotsDensityTextFormatter);

        TextFormatter<Integer> divisionTextFormatter = new TextFormatter<>(converterInteger, Main.config.division, filterInteger);
        division.setTextFormatter(divisionTextFormatter);
    }

}
