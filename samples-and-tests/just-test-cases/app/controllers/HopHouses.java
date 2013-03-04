package controllers;

import models.*;
import yalp.*;
import yalp.mvc.*;
import java.util.*;

public class HopHouses extends Controller {

    public static void index() {
        render();
    }
    
    public static void submit(House h) {
        render(h);
    }
    
}

