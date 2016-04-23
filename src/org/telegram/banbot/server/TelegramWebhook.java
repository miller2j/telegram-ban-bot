package org.telegram.banbot.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
public class TelegramWebhook extends HttpServlet {
	private static final Logger _logger = Logger.getLogger(TelegramWebhook.class.getName());
	private static final String TOKENBAN = "token-details";
    public static final String USERNAMEBAN = "banbot";
    
	private static final String targetURL = "https://api.telegram.org/bot";
	public int last_update_message_id = 0;
	public static String temp = "";
	public static String temp2 = "";
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws IOException {
		BufferedReader bufferedReader = null;
		
		try {
			_logger.info("Webhook Called..");
			InputStream inputStream = req.getInputStream();
			StringBuilder builder = new StringBuilder();
		    if (inputStream != null) {
		        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		        char[] charBuffer = new char[128];
		        int bytesRead = -1;
		        while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
		        	builder.append(charBuffer, 0, bytesRead);
		        }
		    } 
		    _logger.info("JSON recieved: "+builder.toString());
		    JSONObject json = new JSONObject(builder.toString());
		      /*if (json.getBoolean("ok") == true){
		             	Processing.processMessages(json);
		          } */
		      Processing.processMessage(json);
		    resp.setStatus(HttpServletResponse.SC_ACCEPTED);
			
			//END
			_logger.info("Webhook has been executed");
			
		} catch (MalformedURLException e) {
		  	//e.setStackTrace(stackTrace);
		      e.printStackTrace();

		} catch (IOException e) {
		      e.printStackTrace();
		} catch (Exception ex) {
			//Log any exceptions in your Cron Job
			ex.printStackTrace();
		} finally {
		    if (bufferedReader != null) {
		        try {
		            bufferedReader.close();
		        } catch (IOException ex) {
		            throw ex;
		        }
		    }
		}
	}
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		doPost(req, resp);
	}
	
	
	public static void sendTelegramReply(String input){
		 try {

	             URL targetUrl = new URL(targetURL+TOKENBAN+"/sendMessage");
	             @SuppressWarnings("deprecation")
				String encodedData = URLEncoder.encode( input );
	             temp2 = "here..";
	             HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
	             httpConnection.setDoOutput(true);
	             httpConnection.setRequestMethod("POST");
	             httpConnection.setRequestProperty("Content-Type", "application/json");
	             httpConnection.setReadTimeout(10000); //10 Sec
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

	 }   

	
	public void setLastMessageId(int id){
		last_update_message_id = id;
	}

	public static void sendTelegramEditReply(String input) {
		// TODO Auto-generated method stub
		try {

            URL targetUrl = new URL(targetURL+TOKENBAN+"/editMessage");
            @SuppressWarnings("deprecation")
			String encodedData = URLEncoder.encode( input );
            temp2 = "here..";
            HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("POST");
            httpConnection.setRequestProperty("Content-Type", "application/json");
            httpConnection.setReadTimeout(10000); //10 Sec
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
	}
}