package org.telegram.banbot.shared;

//import org.telegram.banbot.server.BotServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.telegram.banbot.server.GreetingServiceImpl;
import org.telegram.banbot.server.TelegramImpl;
import org.telegram.banbot.server.TelegramWebhook;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

/**
 * <p>
 * FieldVerifier validates that the name the user enters is valid.
 * </p>
 */
public class Processing {

	private static final String botname = "@ban_test_bot";
	public static void processMessages(JSONObject json){
		
		//List<String> list = new ArrayList<String>();
		//JSONArray array = json.getJSONArray("result");
		//JSONArray array = json.get("result").isArray();
		//System.out.println("1a"+json.toString());
		//Vector<int> messages_to_be_deleted;
		List<Integer> messages_to_be_deleted = new ArrayList<Integer>();
		//JSONArray messages_to_be_deleted_array = new JSONArray();
		
		JSONArray array = json.getJSONArray("result");
		//System.out.println("array"+array.toString());
		//long unixTime = System.currentTimeMillis() / 1000L;
		for(int i = 0 ; i < array.length() ; i++){
			//System.out.println("i:"+i+"array.length():"+array.length());
		    //list.add();
			//if (array.getJSONObject(i).getJSONObject("message").getJSONArray("entities").getJSONObject(0).getString("type").equals("bot_command")){
			//System.out.println("2a");
			JSONObject iObject = array.getJSONObject(i);
			JSONObject message = iObject.getJSONObject("message");
			//int update_id = array.getJSONObject(i).getInt("update_id");
			int update_id = iObject.getInt("update_id");
			int message_id = message.getInt("message_id");
			//int chat_id = array.getJSONObject(i).getJSONObject("message").getJSONObject("chat").getInt("id");
			int chat_id = message.getJSONObject("chat").getInt("id");
			//String text = array.getJSONObject(i).getJSONObject("message").getString("text");
			String text = message.getString("text");
			String[] splitted = text.split("\\s+");
			//System.out.println("5a"+splitted[0]);
			String from = message.getJSONObject("from").getString("username");
			if (message.has("entities")){
				JSONArray entities = message.getJSONArray("entities");
				String type = entities.getJSONObject(0).getString("type");
				//System.out.println("3a");
				if (type.equals("bot_command")){
					//System.out.println("4a");
					switch (splitted[0]){
						case "/ban":
						case "\\/ban":
						case "/ban"+botname:
						case "\\/ban"+botname:
							if (splitted.length > 1){
								if (splitted[1].equals("@ban_test_bot")){
									audaciousAttemptMethod(splitted[1],message_id, chat_id);
								} else {
									banMethod(splitted[1],message_id, chat_id, text);
								}
							} else {
								missingUsernameBanMethod(message_id, chat_id);
							}
							break;
						case "/unban":
						case "\\/unban":
						case "/unban"+botname:
						case "\\/unban"+botname:
							if (splitted.length > 1){
								if (splitted[1].equals("@"+from) ){
									nopermissionsMethod(splitted[1],message_id, chat_id);
								} else if (UserOperations.getBannedStatusperChatid(chat_id).contains(splitted[1])){
									unbanMethod(splitted[1],message_id, chat_id, text);
								}
							} else {
								missingUsernameUnBanMethod(message_id, chat_id);
							}
							break;
						case "/banstatus":
						case "\\/banstatus":
						case "/banstatus"+botname:
						case "\\/banstatus"+botname:
							banstatusMethod(message_id, chat_id);
							break;
						default:
							unknownMethod(from,message_id, chat_id);
							break;
					}
					
				} 
				} else {
					System.out.println("checking group messages..");
					//int temp =UserOperations.removeMessagesFromBannedUsers(message_id, chat_id, from);
					boolean reply =UserOperations.replyMessagesFromBannedUsers(chat_id, from);
					System.out.println("from:"+from+" reply:"+reply);
					if (reply){
						banResponseMethod(from,message_id, chat_id);
						//messages_to_be_deleted.add(temp);
						//messages_to_be_deleted_array.put(temp);
					}
			} 
			/*if (messages_to_be_deleted != null){
				//messages_to_be_deleted_array = new JSONArray(Arrays.asList(messages_to_be_deleted));
				JSONObject deletejson = new JSONObject();
				json.put("id", new JSONArray(Arrays.asList(messages_to_be_deleted)));
				//deleteMessagesMethod(new JSONArray(Arrays.asList(messages_to_be_deleted)));
				deleteMessagesMethod(deletejson);
			}*/
			//TelegramImpl.last_update_message_id = iObject.getInt("update_id");
			//TelegramImpl.setLastMessageId(iObject.getInt("update_id"));
			// Using the synchronous cache.
			  MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
			  syncCache.put(TelegramImpl.USERNAMEBAN, iObject.getInt("update_id"));
		}
	}
	
	public static void processMessage(JSONObject json){
		
			JSONObject message = json.getJSONObject("message");
			//int update_id = array.getJSONObject(i).getInt("update_id");
			int update_id = json.getInt("update_id");
			int message_id = message.getInt("message_id");
			//int chat_id = array.getJSONObject(i).getJSONObject("message").getJSONObject("chat").getInt("id");
			int chat_id = message.getJSONObject("chat").getInt("id");
			//String text = array.getJSONObject(i).getJSONObject("message").getString("text");
			String text = message.getString("text").replace("?", "");
			String[] splitted = text.split("\\s+");
			//System.out.println("5a"+splitted[0]);
			String from = message.getJSONObject("from").getString("username");
			if (message.has("entities")){
				JSONArray entities = message.getJSONArray("entities");
				String type = entities.getJSONObject(0).getString("type");
				//System.out.println("3a");
				if (type.equals("bot_command")){
					//System.out.println("4a");
					switch (splitted[0]){
						case "/ban":
						case "\\/ban":
						case "/ban"+botname:
						case "\\/ban"+botname:
							if (splitted.length > 1){
								String[] only_user = splitted[1].split("\\?");
								if (only_user[0].equals("@ban_test_bot")){
									audaciousAttemptMethod(only_user[0],message_id, chat_id);
								} else if(!only_user[0].contains("@")){
									missingUsernameBanMethod(message_id, chat_id);
								} else {
									banMethod(only_user[0],message_id, chat_id, text);
								}
							} else {
								missingUsernameBanMethod(message_id, chat_id);
							}
							break;
						case "/unban":
						case "\\/unban":
						case "/unban"+botname:
						case "\\/unban"+botname:
							if (splitted.length > 1){
								String[] only_user = splitted[1].split("\\?");
								if (only_user[0].equals("@"+from) ){
									nopermissionsMethod(only_user[0],message_id, chat_id);
								} else if(!only_user[0].contains("@")){
									missingUsernameUnBanMethod(message_id, chat_id);
								} else if (PersistentOperations.getBannedStatusperChatid(chat_id).contains(only_user[0])){
								//} else if (UserOperations.getBannedStatusperChatid(chat_id).contains(only_user[0])){
								//} else if (UserOperations.getBannedStatusperChatid(chat_id).contains(splitted[1]+"/\n/")){
									unbanMethod(only_user[0],message_id, chat_id, text);
								}
							} else {
								missingUsernameUnBanMethod(message_id, chat_id);
							}
							break;
						case "/unbanall":
						case "\\/unbanall":
						case "/unbanall"+botname:
						case "\\/unbanall"+botname:
							//boolean reply =UserOperations.replyMessagesFromBannedUsers(chat_id, from);
							boolean reply =PersistentOperations.replyMessagesFromBannedUsers(chat_id, from);
							if (reply){
								nopermissionsMethod(splitted[1],message_id, chat_id);
							} else {
								unbanallMethod(message_id, chat_id);
							}
							
							break;
						case "/banstatus":
						case "\\/banstatus":
						case "/banstatus"+botname:
						case "\\/banstatus"+botname:
							banstatusMethod(message_id, chat_id);
							break;
						default:
							unknownMethod(text,message_id, chat_id);
							break;
					}
					
				} else {
					//System.out.println("checking group messages..");
					//int temp =UserOperations.removeMessagesFromBannedUsers(message_id, chat_id, from);
					boolean reply =PersistentOperations.replyMessagesFromBannedUsers(chat_id, from);
					//boolean reply =UserOperations.replyMessagesFromBannedUsers(chat_id, from);
					//System.out.println("First - from:"+from+" reply:"+reply);
					if (reply){
						banResponseMethod(from,message_id, chat_id);
						//banResponseEditMethod(from,message_id, chat_id);
						//messages_to_be_deleted.add(temp);
						//messages_to_be_deleted_array.put(temp);
					}
				} 
			} else {
				//System.out.println("checking group messages..");
				//int temp =UserOperations.removeMessagesFromBannedUsers(message_id, chat_id, from);
				boolean reply =PersistentOperations.replyMessagesFromBannedUsers(chat_id, from);
				//boolean reply =UserOperations.replyMessagesFromBannedUsers(chat_id, from);
				//System.out.println("Second - from:"+from+" reply:"+reply);
				if (reply){
					banResponseMethod(from,message_id, chat_id);
					//banResponseEditMethod(from,message_id, chat_id);
					//messages_to_be_deleted.add(temp);
					//messages_to_be_deleted_array.put(temp);
				}
			} 
			
	}
	
	private static void unbanallMethod( int message_id,
			int chat_id) {
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		json.put("chat_id", chat_id);
		//json.put("chat_id", chat_id_value);
		PersistentOperations.removeAllBannedStatusperChatid(chat_id);
		//UserOperations.removeAllBannedStatusperChatid(chat_id);
		json.put("text", "All users have been <b>unbanned</b>.");
		//json.put("text", text);
		json.put("disable_notification", false);
		//json.put("disable_notification", disable_notification);
		//json.put("reply_to_message_id", message_id);
		json.put("parse_mode", "HTML");
		
		JSONObject replyKeyboardHide = new JSONObject();
		//replyKeyboardHide.put("hide_keyboard", hide_keyboard);
		replyKeyboardHide.put("hide_keyboard", true);
		//replyKeyboardHide.put("selective", selective);
		replyKeyboardHide.put("selective", true);
		json.put("reply_markup", replyKeyboardHide);
		
		TelegramWebhook.sendTelegramReply(json.toString());
	}

	private static void nopermissionsMethod(String only_user, int message_id,
			int chat_id) {
		JSONObject json = new JSONObject();
		json.put("chat_id", chat_id);
		//json.put("chat_id", chat_id_value);
		json.put("text", "You are not able to perform this action. ");
		//json.put("text", text);
		json.put("disable_notification", false);
		//json.put("disable_notification", disable_notification);
		json.put("reply_to_message_id", message_id);
		json.put("parse_mode", "HTML");
		TelegramWebhook.sendTelegramReply(json.toString());
	}
	
	private static void audaciousAttemptMethod(String only_user, int message_id,
			int chat_id) {
		JSONObject json = new JSONObject();
		json.put("chat_id", chat_id);
		//json.put("chat_id", chat_id_value);
		json.put("text", "You are attempting act of trying to <b>BAN</b> the bot.\n Tsk Tsk Tsk ... ");
		//json.put("text", text);
		json.put("disable_notification", false);
		//json.put("disable_notification", disable_notification);
		json.put("reply_to_message_id", message_id);
		json.put("parse_mode", "HTML");
		TelegramWebhook.sendTelegramReply(json.toString());
	}

	private static void banResponseMethod(String from, int message_id,
			int chat_id) {
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		json.put("chat_id", chat_id);
		//json.put("chat_id", chat_id_value);
		//UserOperations.removeBannedStatusperChatid(chat_id,splitted[1]+"\n");
		json.put("text", "@"+from+" is currently <b>BANNED</b>.");
		//json.put("text", text);
		json.put("disable_notification", false);
		//json.put("disable_notification", disable_notification);
		//json.put("reply_to_message_id", message_id);
		json.put("reply_to_message_id", message_id);
		json.put("parse_mode", "HTML");
		
		JSONObject replyKeyboardHide = new JSONObject();
		//replyKeyboardHide.put("hide_keyboard", hide_keyboard);
		replyKeyboardHide.put("hide_keyboard", true);
		//replyKeyboardHide.put("selective", selective);
		replyKeyboardHide.put("selective", true);
		json.put("reply_markup", replyKeyboardHide);
		
		TelegramWebhook.sendTelegramReply(json.toString());
		
	}
	private static void banResponseEditMethod(String from, int message_id,
			int chat_id) {
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		json.put("chat_id", chat_id);
		//json.put("chat_id", chat_id_value);
		//UserOperations.removeBannedStatusperChatid(chat_id,splitted[1]+"\n");
		json.put("text", "");
		//json.put("text", text);
		//json.put("disable_notification", false);
		//json.put("disable_notification", disable_notification);
		//json.put("reply_to_message_id", message_id);
		json.put("message_id", message_id);
		json.put("parse_mode", "HTML");
		
		
		TelegramWebhook.sendTelegramEditReply(json.toString());
		
	}

	/*private static void deleteMessagesMethod(JSONObject jsonArray) {
		// TODO Auto-generated method stub
		GreetingServiceImpl.deleteTelegramReply(jsonArray.toString());
	}*/

	private static void banMethod(String only_user, int message_id, int chat_id, String text){
		// To create request to send 'ReplyKeyboardMarkup' Type from BOT using 'sendMessage' method
		
		JSONObject json = new JSONObject();
		json.put("chat_id", chat_id);
		//json.put("chat_id", chat_id_value);
		PersistentOperations.addBannedStatusperChatid(chat_id,only_user);
		//UserOperations.addBannedStatusperChatid(chat_id,only_user+"\n");
		//json.put("text", only_user.trim()+" has been <b>banned.</b> \n"+text);
		json.put("text", only_user.trim()+" has been <b>banned.</b>");
		//json.put("text", text);
		json.put("disable_notification", false);
		//json.put("disable_notification", disable_notification);
		//json.put("reply_to_message_id", message_id);
		json.put("parse_mode", "HTML");
		
		
		JSONObject replyKeyboardMarkup = new JSONObject();
		//replyKeyboardMarkup.put("hide_keyboard", hide_keyboard);
		replyKeyboardMarkup.put("hide_keyboard", true);
		//replyKeyboardMarkup.put("selective", selective);
		replyKeyboardMarkup.put("selective", true);
		
		
		JSONObject KeyBoardButton = new JSONObject();
		KeyBoardButton.put("text","BANNED");
		//KeyBoardButton.put("text",text_value);
		//KeyBoardButton.put("request_contact",request_contact);
		KeyBoardButton.put("request_contact",false);
		//KeyBoardButton.put("request_location",request_location);
		KeyBoardButton.put("request_location",false);
		
		JSONArray first = new JSONArray();
		first.put(0, KeyBoardButton);
		
		JSONArray second = new JSONArray();
		second.put(0, first);
		
		replyKeyboardMarkup.put("keyboard",second);
		
		json.put("reply_markup", replyKeyboardMarkup);
		
		TelegramWebhook.sendTelegramReply(json.toString());
	}
	
	private static void unbanMethod(String only_user, int message_id, int chat_id, String text){
		// To create request to send 'ReplyKeyboardHide' Type from BOT using 'sendMessage' method
				
		JSONObject json = new JSONObject();
		json.put("chat_id", chat_id);
		//json.put("chat_id", chat_id_value);
		PersistentOperations.removeBannedStatusperChatid(chat_id,only_user);
		//UserOperations.removeBannedStatusperChatid(chat_id,only_user+"\n");
		//json.put("text", only_user.trim()+" has been <b>unbanned</b>.\n"+text);
		json.put("text", only_user.trim()+" has been <b>unbanned</b>.");
		//json.put("text", text);
		json.put("disable_notification", false);
		//json.put("disable_notification", disable_notification);
		//json.put("reply_to_message_id", message_id);
		json.put("parse_mode", "HTML");
		
		JSONObject replyKeyboardHide = new JSONObject();
		//replyKeyboardHide.put("hide_keyboard", hide_keyboard);
		replyKeyboardHide.put("hide_keyboard", true);
		//replyKeyboardHide.put("selective", selective);
		replyKeyboardHide.put("selective", true);
		json.put("reply_markup", replyKeyboardHide);
		
		TelegramWebhook.sendTelegramReply(json.toString());
	}

	private static void banstatusMethod(int message_id, int chat_id){
		// To show list of banned statuses
		JSONObject json = new JSONObject();
		json.put("chat_id", chat_id);
		//json.put("chat_id", chat_id_value);
		String list_users = PersistentOperations.getBannedStatusperChatid(chat_id);
		//String list_users = UserOperations.getBannedStatusperChatid(chat_id);
		if (list_users != null){
			json.put("text", "Please see list of banned users below:\n"+list_users.replace(" ", "\n"));
			//json.put("text", "Please see list of banned users below:\n"+list_users.replaceAll(" ", "\n"));
		} else {
			json.put("text", "No users are banned for this chat ");
		}
		//json.put("text", "Please see list of banned users below:\n @username \n @username ");
		//json.put("text", text);
		json.put("disable_notification", false);
		//json.put("disable_notification", disable_notification);
		//json.put("reply_to_message_id", message_id);
		json.put("parse_mode", "HTML");
		TelegramWebhook.sendTelegramReply(json.toString());
		//object.
	}
	
	private static void unknownMethod(String text, int message_id, int chat_id){
		JSONObject json = new JSONObject();
		json.put("chat_id", chat_id);
		//json.put("chat_id", chat_id_value);
		json.put("text", "Please see list of method avaliable below:\n /ban @username \n /unban @username  \n /unbanall \n /banstatus "+text);
		//json.put("text", text);
		json.put("disable_notification", false);
		//json.put("disable_notification", disable_notification);
		json.put("reply_to_message_id", message_id);
		json.put("parse_mode", "HTML");
		TelegramWebhook.sendTelegramReply(json.toString());
	}
	
	private static void missingUsernameMethod(String[] splitted, int message_id, int chat_id){
		// To create request to send reply message from BOT using 'sendMessage' method
		
		JSONObject json = new JSONObject();
		json.put("chat_id", chat_id);
		//json.put("chat_id", chat_id_value);
		json.put("text", "Please enter the username also, see example below:\n /ban @username \n /unban @username ");
		//json.put("text", text);
		json.put("disable_notification", false);
		//json.put("disable_notification", disable_notification);
		json.put("reply_to_message_id", message_id);
		json.put("parse_mode", "HTML");
		TelegramWebhook.sendTelegramReply(json.toString());
	}
	
	private static void missingUsernameBanMethod(int message_id, int chat_id){
		// To create request to send reply message from BOT using 'sendMessage' method
		
		JSONObject json = new JSONObject();
		json.put("chat_id", chat_id);
		//json.put("chat_id", chat_id_value);
		json.put("text", "Please enter the username also, see example below:\n /ban @username ");
		//json.put("text", text);
		json.put("disable_notification", false);
		//json.put("disable_notification", disable_notification);
		json.put("reply_to_message_id", message_id);
		json.put("parse_mode", "HTML");
		TelegramWebhook.sendTelegramReply(json.toString());
	}
	
	private static void missingUsernameUnBanMethod( int message_id, int chat_id){
		// To create request to send reply message from BOT using 'sendMessage' method
		
		JSONObject json = new JSONObject();
		json.put("chat_id", chat_id);
		//json.put("chat_id", chat_id_value);
		json.put("text", "Please enter the username also, see example below:\n /unban @username ");
		//json.put("text", text);
		json.put("disable_notification", false);
		//json.put("disable_notification", disable_notification);
		json.put("reply_to_message_id", message_id);
		json.put("parse_mode", "HTML");
		TelegramWebhook.sendTelegramReply(json.toString());
	}
}
