package controllers;

import yalp.*;
import yalp.mvc.*;

@Check("admin")
@With(Secure.class)
public class Users extends CRUD {    
}