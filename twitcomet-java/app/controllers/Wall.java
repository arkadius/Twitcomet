package controllers;

import java.util.List;

import forms.Tweet;
import models.*;
import play.data.Form;
import play.mvc.*;
import views.html.index;

public class Wall extends Controller {

	public static Result index() {
		final List<Message> messages = Message.findInitialMessages();
		final Long lastId = messages.get(messages.size() - 1).id;

		return ok(index.render(messages, lastId));
	}

	public static Result fetchMessages(Long firstId) {
		return ok(play.libs.Json.toJson(Message.findNewMessages(firstId))).as("application/json");
	}

	public static Result create() {

		final Form<Tweet> formTweet = form(Tweet.class).bindFromRequest();

		if (formTweet.hasErrors()) {
			return badRequest();
		} else {
			final Message message = new Message(
					formTweet.get(), 
					User.find.byId(new Long(session("user.id"))));
			message.save();
			return ok();
		}

	}

}