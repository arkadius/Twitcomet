package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.avaje.ebean.validation.NotNull;

import play.data.validation.Constraints;
import play.db.ebean.Model;

@Entity
@Table(name="messages")
public class Message extends Model {
	
	@Id
	public long id;
	
	@Constraints.Required
	@Constraints.MaxLength(141)	// betten than Twitter !
	@Column(length=141)
	@NotNull
	public String text;
	
	@NotNull
	@ManyToOne
	public User author;
	
	@ManyToOne
	public Message reference;
	
	@NotNull
	public Date date;
	
	public static Finder<Long,Message> find = new Finder<Long,Message>(
			Long.class, Message.class
	);

}
