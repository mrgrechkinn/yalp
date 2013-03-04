import java.util.*;

import yalp.jobs.*;
import yalp.test.*;

import models.*;

@OnApplicationStart
public class Bootstrap extends Job {

    public void doJob() {
        if(Contact.count() == 0) {
            Fixtures.load("data.yml");
        }
    }
    
}

