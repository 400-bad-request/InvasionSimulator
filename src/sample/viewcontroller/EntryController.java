package sample.viewcontroller;

import sample.models.Configuration;
import sample.models.StageObjects;

public class EntryController {

    public void drawRandomStage() {

        // New stage object along with configuration object as a constructor parameter
        Configuration config = new Configuration();
        StageObjects stage = new StageObjects(config);


    }

}
