package controllers;

import models.*;
import yalp.*;
import yalp.mvc.*;
import java.util.*;
import yalp.data.validation.*;

public class Sample4 extends Application {

    public static void index() {
        render();
    }
    
    public static void handleSubmit(@Valid User user) {
        
        // Handle errors
        if(validation.hasErrors()) {
            render("@index");
        }
        
        // Ok, display the created user
        render(user);
    }
    
}

