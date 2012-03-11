package controllers;

import play.*;
import play.mvc.*;

public class Application extends Controller {
	
    public static Result javascriptRoutes() {
        response().setContentType("text/javascript");
        return ok(
            Routes.javascriptRouter("jsRoutes",
            
            	controllers.routes.javascript.Wall.index(),
                controllers.routes.javascript.Wall.create(),
                controllers.routes.javascript.Wall.fetchMessages()
                
            )
        );
    }

}
