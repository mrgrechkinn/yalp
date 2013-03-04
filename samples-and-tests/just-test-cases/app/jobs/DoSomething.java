package jobs;

import yalp.jobs.*;
import java.util.*;

public class DoSomething extends Job<String> {
    
    long d;
    
    public DoSomething(long d) {
        this.d = d;
    }

    public String doJobWithResult() throws Exception {
        Thread.sleep(d);
        return "DONE:" + d;
    }
    
}

