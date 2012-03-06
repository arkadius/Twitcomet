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
			
			flash("error", "Merci de vérifier les informations saisies");
			return badRequest(index.render(formUser));
			
		} else {
			User user = formUser.get();
			user.save();
			
			Connection.login(user);
			flash("success", "Bienvenue sur TwitCommet "+user.login);
			
			return redirect(routes.Application.index());
		}
	}

}
