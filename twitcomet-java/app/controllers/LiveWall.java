package controllers;

import static java.util.concurrent.TimeUnit.*;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import models.Message;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.util.Duration;
import play.Logger;
import play.cache.Cache;
import play.libs.Akka;
import play.libs.Comet;
import play.libs.F.Callback0;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class LiveWall extends Controller {

	final static ActorRef clock = LiveTweetSocket.instance;

	public static Result index() {
		return ok(new Comet("parent.receiveNewMessage") {
			public void onConnected() {
				clock.tell(this);
			}
		});
	}

	public static class LiveTweetSocket extends UntypedActor {

		static ActorRef instance = Akka.system().actorOf(new Props(LiveTweetSocket.class));

		// A quartz to send a refresh message every second
		static {
			Akka.system().scheduler().schedule(Duration.Zero(), Duration.create(500, MILLISECONDS), instance, "REFRESH");
		}

		List<Comet> sockets = new ArrayList<Comet>();

		public void onReceive(Object message) {

			// Handle connections
			if (message instanceof Comet) {
				final Comet cometSocket = (Comet) message;

				if (sockets.contains(cometSocket)) {

					// Brower is disconnected
					sockets.remove(cometSocket);
					Logger.info("Browser disconnected (" + sockets.size() + " browsers currently connected)");

				} else {

					// Register disconnected callback
					cometSocket.onDisconnected(new Callback0() {
						public void invoke() {
							getContext().self().tell(cometSocket);
						}
					});

					// New browser connected
					sockets.add(cometSocket);
					Logger.info("New browser connected (" + sockets.size() + " browsers currently connected)");

				}
			}

			// Send new messages to all connected browsers
			if ("REFRESH".equals(message)) {

				final Long lastIdSaved = getLastIdMessageSaved();
				final Long lastIdSent = getLastIdMessageSent();
				
				if (lastIdSent < lastIdSaved) {	// new messages incoming ?

					Logger.debug("There is new messages to send ("+lastIdSent+"/"+lastIdSaved+")");
					
					JsonNode msgToSendJson = Json.toJson(Message.findNewMessages(lastIdSent));					
					setLastIdMessageSent(lastIdSaved);

					// Send the new messages to all sockets
					for (Comet cometSocket : sockets) {
						cometSocket.sendMessage(msgToSendJson);
					}
				}

			}

		}

		/**
		 * Fetch the id of the latest message (from cache or DB)
		 * @return
		 */
		private Long getLastIdMessageSaved() {
			Long lastId = (Long) Cache.get("comet.lastidsaved");
			if (lastId == null) {
				lastId = Message.findLastId();
				Cache.set("comet.lastidsaved", lastId);
			}
			return lastId;
		}

		/**
		 * Get the id of the last message sent throught sockets
		 * @return
		 */
		private Long getLastIdMessageSent() {
			Long lastId = (Long) Cache.get("comet.lastidsent");
			if (lastId == null) {
				lastId = getLastIdMessageSaved();
				Cache.set("comet.lastidsent", lastId);
			}
			return lastId;
		}

		/**
		 * Set the id to the last message sent throught sockets
		 * @param lastId
		 */
		private void setLastIdMessageSent(Long lastId) {
			Cache.set("comet.lastidsent", lastId);
		}

	}

}
