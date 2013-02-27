package jobs;

import java.util.*;

import yalp.*;
import yalp.jobs.*;

import utils.*;

@Youhou("fromJob")
public class JobWithContext extends Job<String> {

    public String doJobWithResult() {
        return Invoker.InvocationContext.current().toString();
    }
    
}

