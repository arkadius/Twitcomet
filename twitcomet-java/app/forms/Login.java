package forms;

import models.User;
import play.data.validation.Constraints.Required;

public class Login {
	
	@Required
	public String login;
	
	@Required
	public String password;

	public String validate() {
		if (!User.authentificate(login, password)) {
			return "Connexion impossible";
		}
		return null;
	}
	


}
