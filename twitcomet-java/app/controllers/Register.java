package controllers;

import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.register.index;

public class Register extends Controller {
	
	public static Result index() {
		Form<User> formUser = form(User.class);
		return ok(index.render(formUser));
	}
	
	public static Result validate() {
		Form<User> formUser = form(User.class).bindFromRequest();
		if (formUser.hasErrors()) {
		
			// TODO : Doesn't support multiple global errors. Fix it !
			if (formUser.error("") != null && User.VALIDATION_FAILED_PASSWORD.equals(formUser.error("").message())) {
				formUser.reject("password2", "Try again !");
			}
			
			flash("error", "Hey guy, please fill ALL the form ;)");
			return badRequest(index.render(formUser));
			
		} else {
			User user = formUser.get();
			user.save();
			
			Connection.login(user);
			flash("success", "Welcome on TwitCommet "+user.login);
			
			return redirect(routes.Wall.index());
		}
	}

}
