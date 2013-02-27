package controllers;

import yalp.*;
import yalp.mvc.*;
import yalp.cache.*;

import java.util.*;

import models.*;

public class UseCache extends Controller {

    public static void index() {
        render();
    }
    
    @CacheFor("2s")
    public static void getDate() {
        Date a = new Date();
        render(a);
    }
    
}

