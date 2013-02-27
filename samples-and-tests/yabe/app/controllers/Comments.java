package controllers;

import yalp.*;
import yalp.mvc.*;

@Check("admin")
@With(Secure.class)
public class Comments extends CRUD {    
}