package controllers;

import models.*;
import yalp.*;
import yalp.mvc.*;
import java.util.*;
import yalp.data.validation.*;

public class Sample6 extends Application {

    public static void index() {
        render();
    }
    
    public static void handleSubmit(@Valid ComplicatedUser user) {
        
        // Handle errors
        if(validation.hasErrors()) {
            render("@index", user);
        }
        
        // Ok, display the created user
        render(user);
    }
    
}

