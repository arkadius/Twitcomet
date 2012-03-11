package models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

import play.api.Play;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import com.avaje.ebean.Query;
import com.avaje.ebean.validation.NotNull;

import forms.Tweet;

@Entity
@Table(name="messages")
public class Message extends Model {
	
	private static final long serialVersionUID = 1607739783510045149L;

	private static final SimpleDateFormat datetimeFr = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	@Id
	public long id;
	
	@Constraints.Required
	@Constraints.MaxLength(141)	// better than Twitter !
	@Column(length=141)
	@NotNull
	public String text;
	
	@NotNull
	@ManyToOne
	public User author;
	
	@ManyToOne
	public Message reference;
	
	@NotNull
	@JsonIgnore
	public Date date;
	
	public static Finder<Long,Message> find = new Finder<Long,Message>(
			Long.class, Message.class
	);

	public Message() {
		
	}
	
	public Message(Tweet tweet, User author) {
		this.text = tweet.message;
		this.author = author;
		this.date = new Date();
	}
	
	/**
	 * Retourne les derniers N derniers messages postés sur le mur global
	 * @param lastId Si null, retourne les derniers messages
	 * @return
	 */
	public static List<Message> findInitialMessages() {
		return find
				.fetch("reference")
				.fetch("reference.author")
				.fetch("author")
				.orderBy("id DESC")
				.setMaxRows(getPaginationFirstCall())
				.findList();
	}
	
	public static List<Message> findNewMessages(final Long firstId) {
		return find
				.fetch("reference")
				.fetch("reference.author")
				.fetch("author")
				.orderBy("id DESC")
				.where().gt("id", firstId)
				.findList();
	}


	private static int getPaginationFirstCall() {
		final scala.Option<Object> paginationConfig = Play.current().configuration().getInt("pagination.first");
		return (Integer) (paginationConfig.isDefined() ? paginationConfig.get() : 20);
	}
	
	public String getDateFr() {
		return datetimeFr.format(date);
	}

}
