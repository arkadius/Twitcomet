package models;


import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.h2.util.StringUtils;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import com.avaje.ebean.validation.NotNull;

@Entity
@Table(name="users")
public class User extends Model {
	
	public static final String VALIDATION_FAILED_PASSWORD = "PasswordCopyInvalid";

	private static final long serialVersionUID = 5206221463639837118L;

	@Id
	public long id;
	
	@Constraints.Required
	@NotNull
	@Constraints.MaxLength(50)
	@Column(unique=true, length=50)
	public String login;
	
	@Constraints.Required
	@NotNull
	public String password;
	
	@Transient
	public String password2;
	
	@Constraints.Email
	@Constraints.Required
	@NotNull
	@Column(unique=true)
	public String email;

	@Constraints.MaxLength(50)
	@Column(length=50)
	public String lastname;
	
	@Constraints.MaxLength(50)
	@Column(length=50)
	public String firstname;
	
	@Lob
	public String description;
	
	public String avatar;

	@NotNull
	public Date registerDate;
	
	@ManyToMany
	@JoinTable(
	    name = "following",
	    joinColumns 		= @JoinColumn(name = "user_id", 	referencedColumnName = "id"),
	    inverseJoinColumns 	= @JoinColumn(name = "friend_id", 	referencedColumnName = "id")
    )
	public List<User> following;
	
	
	public static Finder<Long,User> find = new Finder<Long,User>(
		Long.class, User.class
	);
	
	public static boolean authentificate(final String login, final String password) {
		return find.where().eq("login", login).eq("password", password).findRowCount() == 1;
	}
	
	public static User findByLogin(final String login) {
		return find.where().eq("login", login).findUnique();
	}
	
	public String validate() {
		if (!StringUtils.equals(password, password2)) {
			return VALIDATION_FAILED_PASSWORD;
		}
		return null;
	}
	
	@Override
	public void save() {
		if (registerDate == null) {
			registerDate = new Date();
		}
		
		super.save();
	}
	

}
