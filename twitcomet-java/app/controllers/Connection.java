package controllers;

import models.User;
import play.data.Form;
import static play.data.Form.*;
import play.mvc.Controller;
import play.mvc.Result;
import forms.Login;

public class Connection extends Controller {

	public static Result authentificate() {
		
		Form<Login> formLogin = form(Login.class).bindFromRequest();
		
		if (formLogin.hasErrors()) {
			flash("error", "Connexion impossible : merci de vérifier vos identifiants");
		} else {
			User user = User.findByLogin(formLogin.get().login);
			flash("success", "Bonjour "+user.firstname+" "+user.lastname);
			login(user);
		}
		
		return redirect(routes.Wall.index());
	}

	public static Result logout() {
		session().clear();
		flash("success", "Have a nice day !");
		return redirect(routes.Wall.index());
	}
	
	public static void login(User user) {
		session("user.id", String.valueOf(user.id));
		session("user.login", user.login);
	}

}
