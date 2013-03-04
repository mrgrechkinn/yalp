package controllers;

import java.util.Map;

import models.ComplexModel;

import yalp.mvc.Controller;
import yalp.utils.Utils;

public class CompositeMapBinding extends Controller {

	public static void index() {
		render();
	}
	
	public static void submit( 
			Map<String, String> composite,
			Map<String, String> items,
			Map<String, ComplexModel> models ) {
		render(composite, items, models);
	}
}
