package sample.viewcontroller;

import sample.models.Configuration;
import sample.models.Stage;

public class EntryController {

    public void drawRandomStage() {

        // New stage object along with configuration object as a constructor parameter
        Configuration config = new Configuration();
        Stage stage = new Stage(config);


    }

}
