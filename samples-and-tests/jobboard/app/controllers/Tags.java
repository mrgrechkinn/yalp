package controllers;

import yalp.mvc.*;

public class Tags extends Administration {

    @Before
    static void checkSuper() {
        if (!session.contains("superadmin")) {
            Jobs.list();
        }
    }
}

