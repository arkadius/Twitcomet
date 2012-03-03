package global;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Random;

import models.Mention;
import models.Message;
import models.User;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.libs.Yaml;
import play.mvc.Action;
import play.mvc.Http.Request;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.TxRunnable;

public class Global extends GlobalSettings {

    public void onStart(Application app) {
    	if (app.isDev() || app.isTest()) {
    		InitialData.insert();
    	}
    }
    
    @Override
    public Action<?> onRequest(Request req, Method method) {
    	
    	// Active les logs SQL
    	Ebean.getServer(null).getAdminLogging().setDebugGeneratedSql(true);
    	
    	return super.onRequest(req, method);
    }
    
    
    
    private static class InitialData {
        
    	private static final int FREQUENCE_MESSAGE_REFERENCE = 4;
		private static final int NB_MENTIONS = 50;
		private static final int NB_MESSAGES = 200;
	
        public static void insert() {
            if (User.find.findRowCount() == 0) {
            	
            	Ebean.execute(new TxRunnable() {
					
					@Override
					public void run() {
						
						Logger.info("Chargement des données initiales");
						Random rand = new Random();
		                
		                @SuppressWarnings("unchecked")
						final Map<String,List<Object>> all = (Map<String,List<Object>>)Yaml.load("initial-data.yml");
		                
		                // Insertion des utilisateurs et de leurs followers
		                Ebean.save(all.get("users"));
		                for(Object user: all.get("users")) {
		                    Ebean.saveManyToManyAssociations(user, "following");
		                }
		                
		                // Génération et insertion des messages
		                final GregorianCalendar cal = new GregorianCalendar();
		                for (int i=0; i<NB_MESSAGES; i++) {
		                	Message msg = new Message();
		                	msg.author = User.find.byId(rand.nextInt(3)+1l);
		                	msg.text = "Lorem ipsum dolor sit amet "+i;
		                	
		                	if (0 == rand.nextInt(FREQUENCE_MESSAGE_REFERENCE) && i>2) {
		                		msg.reference = Message.find.byId(new Long(rand.nextInt(i)));
		                	}
		                	
		                	cal.add(Calendar.MINUTE, (-1)*rand.nextInt(10));
		                	cal.add(Calendar.SECOND, (-1)*rand.nextInt(60));
		                	msg.date = cal.getTime();
		                	
		                	msg.save();
		                }
		                
		                // Génération et insertion des mentions
		                for (int i=0; i<NB_MENTIONS; i++) {
	                		Mention mention = new Mention();
	                		Message msg = Message.find.byId(rand.nextInt(NB_MESSAGES)+1l);
	                		mention.message = msg;
	                		mention.user = User.find.byId(rand.nextInt(3)+1l);
	                		
	                		mention.save();
	                		
	                		msg.text += " @" + mention.user.login;
	                		msg.save();
		                }	
		                
		                Logger.info("Chargement des données initiales terminé avec succès");
		                
					}
				});
            	
            }
        }
        
    }
}
