package org.telegram.banbot.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.json.JSONObject;
import org.telegram.banbot.shared.Processing;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheService.IdentifiableValue;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

@SuppressWarnings("serial")
public class TelegramImpl extends HttpServlet {
	private static final Logger _logger = Logger.getLogger(TelegramImpl.class.getName());
	private static final String TOKENBAN = "token-details";
    public static final String USERNAMEBAN = "banbot";
    
	private static final String targetURL = "https://api.telegram.org/bot";
	public int last_update_message_id = 0;
	public static String temp = "";
	public static String temp2 = "";
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws IOException {
		
		try {
			_logger.info("Cron Job is being executed");
			MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
			IdentifiableValue oldValue = syncCache.getIdentifiable(USERNAMEBAN);
			//BEGIN
			String theurl = targetURL+TOKENBAN+"/getUpdates";
		      
		      //if (last_update_message_id > 0) {
			if (oldValue != null && !oldValue.equals(null)){
			if (oldValue.toString() != null && !oldValue.toString().isEmpty()){
			if(oldValue.getValue().toString() != null && !oldValue.getValue().toString().isEmpty()){
				last_update_message_id = Integer.valueOf(oldValue.getValue().toString());
	    	  if (last_update_message_id > 0) {
			      	theurl = targetURL+TOKENBAN+"/getUpdates?offset="+(last_update_message_id+1);
			      	//theurl = targetURL+TOKENBAN+"/getUpdates?offset="+(Integer.valueOf(oldValue.getValue().toString())+1);
		      } 
			}
			}
			}
			temp = "";
		      URL restServiceURL= new URL(theurl);
		      System.out.println(theurl);
		      HttpURLConnection httpConnection = (HttpURLConnection) restServiceURL.openConnection();
		      httpConnection.setRequestMethod("GET");
		      httpConnection.setRequestProperty("Accept", "application/json");
		      if (httpConnection.getResponseCode() != 200) {

		          throw new RuntimeException("HTTP GET Request Failed with Error code : "+ httpConnection.getResponseCode());

		      }
		      BufferedReader responseBuffer = new BufferedReader(new InputStreamReader(
		          (httpConnection.getInputStream())));
		      String output = "";
		      JSONObject json;
		      System.out.println("Output from Server:  \n");
		      while ((output = responseBuffer.readLine()) != null) {
		          System.out.println(output);
		          temp += output;
		          
		      }
		      json = new JSONObject(temp);
		      if (json.getBoolean("ok") == true){
		             	Processing.processMessages(json);
		          }

		      httpConnection.disconnect();
			//END
			_logger.info("Cron Job has been executed");
			
		} catch (MalformedURLException e) {
		  	//e.setStackTrace(stackTrace);
		      e.printStackTrace();

		} catch (IOException e) {
		      e.printStackTrace();
		} catch (Exception ex) {
			//Log any exceptions in your Cron Job
			ex.printStackTrace();
		}
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		doGet(req, resp);
	}
	
	
	/*public static void sendTelegramReply(String input){
		 try {

	             URL targetUrl = new URL(targetURL+TOKENBAN+"/sendMessage");
	             @SuppressWarnings("deprecation")
				String encodedData = URLEncoder.encode( input );
	             temp2 = "here..";
	             HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
	             httpConnection.setDoOutput(true);
	             httpConnection.setRequestMethod("POST");
	             httpConnection.setRequestProperty("Content-Type", "application/json");
	             //httpConnection.
	             System.out.println("Input to Server:\n"+input);
	             //String input = "{\"id\":1,\"firstName\":\"Liam\",\"age\":22,\"lastName\":\"Marco\"}";

	             //OutputStream outputStream = httpConnection.getOutputStream();
	             DataOutputStream outputStream = new DataOutputStream(httpConnection.getOutputStream());
	             outputStream.write(input.getBytes());
	             //outputStream.writeChars(input);
	             outputStream.flush();
	             System.out.println(httpConnection.getResponseCode()+" - "+httpConnection.getResponseMessage());
	             if (httpConnection.getResponseCode() != 200) {
	                 throw new RuntimeException("Failed : HTTP error code : "
	                     + httpConnection.getResponseCode());
	             }
	             BufferedReader responseBuffer = new BufferedReader(new InputStreamReader(
	                     (httpConnection.getInputStream())));
	             String output;
	             System.out.println("Output from Server:\n");
	             while ((output = responseBuffer.readLine()) != null) {
	                 System.out.println(output);
	                 temp2 += output;
	             }
	              
	             httpConnection.disconnect();

	           } catch (MalformedURLException e) {

	             e.printStackTrace();

	           } catch (IOException e) {

	             e.printStackTrace();

	          }

	 }   */
	
	public void setLastMessageId(int id){
		last_update_message_id = id;
	}
}