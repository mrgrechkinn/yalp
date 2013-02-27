package controllers;

import yalp.mvc.*;
import yalp.db.jpa.*;

import models.*;


@yalp.db.jpa.NoTransaction
public class Transactional2 extends Controller {

	//This should be excluded from any transactions.
	public static void disabledTransactionTest() {
		renderText("isInsideTransaction: " + JPA.isInsideTransaction());
	}
	
}

