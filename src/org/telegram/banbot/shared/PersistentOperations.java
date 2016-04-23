package org.telegram.banbot.shared;

//import org.telegram.banbot.server.BotServiceImpl;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;


/**
 * <p>
 * FieldVerifier validates that the name the user enters is valid.
 * </p>
 */

public class PersistentOperations {

	private static final String BANNED_DETAILS = "banned_details";
	private static String banned_status_user;
	private static Logger logger = Logger.getLogger(PersistentOperations.class.getName());
	public static String getBannedStatusperChatid(int chat_id){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		//PersistenceManager pm = PersistenceManager.class.getPersistenceManager();
		try {
	        
			Key k = KeyFactory.createKey(Chat.class.getSimpleName(), BANNED_DETAILS+"chat_id_"+chat_id);
			//JDOHelper.
	        Chat c = pm.getObjectById(Chat.class, k);
	        if (c != null){
	        	//c.setUsers(c.getUsers()+username_details+"\n");
	        	banned_status_user = c.getUsers();
	        	pm.makePersistent(c);
	        } 

		} catch (JDOObjectNotFoundException e ){
			banned_status_user = null;
        } finally {
            pm.close();
        }
		
		return banned_status_user;
	}
	
	public static void addBannedStatusperChatid(int chat_id, String username_details){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		//PersistenceManager pm = PersistenceManager.class.getPersistenceManager();
		try {
	        
			Key k = KeyFactory.createKey(Chat.class.getSimpleName(), BANNED_DETAILS+"chat_id_"+chat_id);
	        Chat c = pm.getObjectById(Chat.class, k);
	        if (c != null){
	        	c.setUsers(c.getUsers()+username_details+" ");
	        	pm.makePersistent(c);
	        } else {
	        	Chat c2 = new Chat(username_details);
	        	c2.setUsers(username_details+" ");
	        	c2.setKey(k);
	        	pm.makePersistent(c2);
	        }

            
		} catch (JDOObjectNotFoundException e ){
			//banned_status_user = null;
			Key k = KeyFactory.createKey(Chat.class.getSimpleName(), BANNED_DETAILS+"chat_id_"+chat_id);
			Chat c2 = new Chat(username_details);
        	c2.setUsers(username_details+" ");
        	c2.setKey(k);
        	pm.makePersistent(c2);
        } finally {
            pm.close();
        }
		
		
	}
	
	public static void removeBannedStatusperChatid(int chat_id, String username_details){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		//PersistenceManager pm = PersistenceManager.class.getPersistenceManager();
		try {
	        
			Key k = KeyFactory.createKey(Chat.class.getSimpleName(), BANNED_DETAILS+"chat_id_"+chat_id);
	        Chat c = pm.getObjectById(Chat.class, k);
	        if (c != null){
	        	c.setUsers(c.getUsers().replace("@"+username_details+" ", ""));
	        	pm.makePersistent(c);
	        }

		} catch (JDOObjectNotFoundException e ){
			// Do nothing
        } finally {
            pm.close();
        }
		
	}

	public static int removeMessagesFromBannedUsers(int message_id,int chat_id, String text) {
		// TODO Auto-generated method stub
		PersistenceManager pm = PMF.get().getPersistenceManager();
		//PersistenceManager pm = PersistenceManager.class.getPersistenceManager();
		int return_value =0;
		try {
	        
			Key k = KeyFactory.createKey(Chat.class.getSimpleName(), BANNED_DETAILS+"chat_id_"+chat_id);
	        Chat c = pm.getObjectById(Chat.class, k);
	        if (c != null){
	        	//c.setUsers(c.getUsers()+username_details+"\n");
	        	banned_status_user = c.getUsers();
	        	if (c.getUsers().contains("@"+text)){
	        		return_value = message_id;
	        	} else{
	        		return_value =0;
	        	}
	        	pm.makePersistent(c);
	        } else {
	        	return_value =0;
	        }

            
		} catch (JDOObjectNotFoundException e ){
			// Do nothing
			return_value =0;
        } finally {
            pm.close();
        }
		return return_value;
		
	}

	public static boolean replyMessagesFromBannedUsers(int chat_id, String from) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		//PersistenceManager pm = PersistenceManager.class.getPersistenceManager();
		boolean return_value =false;
		try {
	        
			Key k = KeyFactory.createKey(Chat.class.getSimpleName(), BANNED_DETAILS+"chat_id_"+chat_id);
	        Chat c = pm.getObjectById(Chat.class, k);
	        if (c != null){
	        	//c.setUsers(c.getUsers()+username_details+"\n");
	        	//banned_status_user = c.getUsers();
	        	//logger.log(Level.INFO, "c.getUsers():"+c.getUsers());
	        	//logger.log(Level.INFO, "from.trim():"+from.trim());
	        	//logger.log(Level.INFO, "result:"+c.getUsers().contains(from.trim()));
	        	if (c.getUsers().contains(from.trim())){
        		//if (c.getUsers().toLowerCase().contains(from.trim())){
    	        		//logger.log(Level.INFO, "true:");
	        		return_value = true;
	        	} else{
	        		//logger.log(Level.INFO, "false1:");
	        		return_value =false;
	        	}
	        	pm.makePersistent(c);
	        } else {
	        	//logger.log(Level.INFO, "false2:");
	        	return_value =false;
	        }
		} catch (JDOObjectNotFoundException e ){
			// Do nothing
			//logger.log(Level.INFO, "JDOObjectNotFoundException:"+e.getMessage());
			//logger.log(Level.INFO, "false3:");
			return_value =false;
        } finally {
            pm.close();
        }
		return return_value;
	
	}

	public static void removeAllBannedStatusperChatid(int chat_id) {
		// TODO Auto-generated method stub
		PersistenceManager pm = PMF.get().getPersistenceManager();
		//PersistenceManager pm = PersistenceManager.class.getPersistenceManager();
		try {
	        
			Key k = KeyFactory.createKey(Chat.class.getSimpleName(), BANNED_DETAILS+"chat_id_"+chat_id);
	        Chat c = pm.getObjectById(Chat.class, k);
	        if (c != null){
	        	pm.deletePersistent(c);
	        } 

            
		} catch (JDOObjectNotFoundException e ){
			// Do nothing
			
        } finally {
            pm.close();
        }
		
	}
	
	
}
