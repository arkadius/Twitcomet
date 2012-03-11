package forms;

import play.data.validation.Constraints;
import play.data.validation.Constraints.Required;

public class Tweet {
	
	@Required
	@Constraints.MaxLength(141)
	public String message;

}
