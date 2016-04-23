package org.telegram.banbot.shared;

//import org.telegram.banbot.server.BotServiceImpl;

import org.telegram.banbot.server.GreetingServiceImpl;
import org.telegram.banbot.server.TelegramImpl;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.memcache.MemcacheService.IdentifiableValue;

/**
 * <p>
 * FieldVerifier validates that the name the user enters is valid.
 * </p>
 */
public class UserOperations {

	private static final String BANNED_DETAILS = "banned_details";
	private static String banned_status_user;
	public static String getBannedStatusperChatid(int chat_id){
		//String banned_users = null;
			// Using the synchronous cache.
			MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
			IdentifiableValue oldValue = syncCache.getIdentifiable(BANNED_DETAILS+"chat_id_"+chat_id);
			if (oldValue != null && !oldValue.equals(null)){
				if (oldValue.toString() != null && !oldValue.toString().isEmpty()){
					if(oldValue.getValue().toString() != null && !oldValue.getValue().toString().isEmpty()){
						banned_status_user = oldValue.getValue().toString();
						//banned_users = banned_status_user.split(",");
			    	  
					}
				}
			}
		return banned_status_user;
	}
	
	public static void addBannedStatusperChatid(int chat_id, String username_details){
		//String banned_users = null;
			// Using the synchronous cache.
			MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
			IdentifiableValue oldValue = syncCache.getIdentifiable(BANNED_DETAILS+"chat_id_"+chat_id);
			if (oldValue == null) {
			      // Key doesn't exist. We can safely put it in cache.
			      syncCache.put(BANNED_DETAILS+"chat_id_"+chat_id, username_details);
			      //break;
			   // } else if (syncCache.putIfUntouched(key, oldValue, username_details)) {
			}else {
			      // newValue has been successfully put into cache.
				if (!syncCache.get(BANNED_DETAILS+"chat_id_"+chat_id).toString().contains(username_details)){
					//syncCache.put(BANNED_DETAILS+"chat_id_"+chat_id, oldValue.getValue().toString()+username_details);
					syncCache.putIfUntouched(BANNED_DETAILS+"chat_id_"+chat_id, oldValue, oldValue.getValue().toString()+username_details);
				}  //break;
			}
			
			//syncCache.in
	}
	
	public static void removeBannedStatusperChatid(int chat_id, String username_details){
		//String banned_users = null;
			// Using the synchronous cache.
			MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
			IdentifiableValue oldValue = syncCache.getIdentifiable(BANNED_DETAILS+"chat_id_"+chat_id);
			if (oldValue != null) {
			      // newValue has been successfully removed cache.
				//syncCache.put(BANNED_DETAILS+"chat_id_"+chat_id, oldValue.getValue().toString().replaceAll(username_details, ""));
				syncCache.putIfUntouched(BANNED_DETAILS+"chat_id_"+chat_id, oldValue, oldValue.getValue().toString().replace(username_details, ""));
			      //break;
			}
			
		
	}

	public static int removeMessagesFromBannedUsers(int message_id,int chat_id, String text) {
		// TODO Auto-generated method stub
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		IdentifiableValue oldValue = syncCache.getIdentifiable(BANNED_DETAILS+"chat_id_"+chat_id);
		if (oldValue != null && !oldValue.equals(null)){
			if (oldValue.toString() != null && !oldValue.toString().isEmpty()){
				if(oldValue.getValue().toString() != null && !oldValue.getValue().toString().isEmpty()){
					if (oldValue.getValue().toString().toLowerCase().contains(text.toLowerCase())){
						return message_id;
					} else {
						return 0;
					}
					
				}else {
					return 0;
				}
			}else {
				return 0;
			}
		} else {
			return 0;
		}
		//return 0;
	}

	public static boolean replyMessagesFromBannedUsers(int chat_id, String from) {
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		IdentifiableValue oldValue = syncCache.getIdentifiable(BANNED_DETAILS+"chat_id_"+chat_id);
		if (oldValue != null && !oldValue.equals(null)){
			if (oldValue.toString() != null && !oldValue.toString().isEmpty()){
				if(oldValue.getValue().toString() != null && !oldValue.getValue().toString().isEmpty()){
					if (oldValue.getValue().toString().toLowerCase().contains(from.toLowerCase())){
					//if (from.contains(oldValue.getValue().toString())){
							return true;
					} else {
						return false;
					}
					
				}else {
					return false;
				}
			}else {
				return false;
			}
		} else {
			return false;
		}
		//return false;
	}

	public static void removeAllBannedStatusperChatid(int chat_id) {
		// TODO Auto-generated method stub
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		IdentifiableValue oldValue = syncCache.getIdentifiable(BANNED_DETAILS+"chat_id_"+chat_id);
		if (oldValue != null) {
		      // Key doesn't exist. We can safely put it in cache.
		      syncCache.delete(BANNED_DETAILS+"chat_id_"+chat_id);
		      //break;
		   // } else if (syncCache.putIfUntouched(key, oldValue, username_details)) {
		}
	}
	
	
}
