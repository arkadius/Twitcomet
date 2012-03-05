package controllers;

import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import forms.Login;

public class Connection extends Controller {

	public static Result authentificate() {
		
		Form<Login> formLogin = form(Login.class).bindFromRequest();
		
		if (formLogin.hasErrors()) {
			flash("error", "Connexion impossible");
		} else {
			User user = User.findByLogin(formLogin.get().login);
			flash("success", "Bonjour "+user.login);
			
			session("user.id", String.valueOf(user.id));
			session("user.login", user.login);
		}
		
		return redirect(routes.Application.index());
	}

	public static Result logout() {
		session().clear();
		return redirect(routes.Application.index());
	}

}
