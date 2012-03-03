package controllers;

import java.util.List;

import models.Message;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {
  
  public static Result index() {
	List<Message> messages = Message.findGlobalLastMessages(40l);
	Long lastId = messages.get(messages.size()-1).id;
	
	
    return ok(index.render(messages, lastId));
  }
  
}