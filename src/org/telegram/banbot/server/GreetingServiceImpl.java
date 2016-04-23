package org.telegram.banbot.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;



//import org.telegram.BotConfig;
import org.telegram.banbot.client.GreetingService;
import org.telegram.banbot.client.GreetingServiceAsync;
import org.telegram.banbot.shared.Processing;
import org.json.JSONObject;



//import org.json
//import javax.serlet.http;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
    GreetingService {

	private static final String TOKENBAN = "token-details";
    private static final String USERNAMEBAN = "banbot";
    
	private static final String targetURL = "https://api.telegram.org/bot";
	public static int last_update_message_id = 0;
	public static String temp = "";
	public static String temp2 = "";
	
	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	
	private static Logger logger = Logger.getLogger("Greeting");
  public String greetServer(String input) throws IllegalArgumentException {
    // Verify that the input is valid. 
	  temp = "";
	//System.out.println("1");
	//logger.log(Level.INFO, "1");
    
    //System.out.println("2");
    //logger.log(Level.INFO, "2");
    
    String serverInfo = getServletContext().getServerInfo();
    String userAgent = getThreadLocalRequest().getHeader("User-Agent");
    //System.out.println("3");
    //logger.log(Level.INFO, "3");
    
    // Escape data from the client to avoid cross-site script vulnerabilities.
    input = escapeHtml(input);
    userAgent = escapeHtml(userAgent);
    //System.out.println("4");
    //logger.log(Level.INFO, "4");
    
    //final String temp;
    //System.out.println("5");
	//logger.log(Level.INFO, "5");
    
	
	

    return "Hello, " + input + "!<br><br>I am running " + serverInfo
        + ".<br><br>It looks like you are using:<br>" + userAgent
    + ".<br><br>It looks like you are using:<br>" +temp
    + ".<br><br>It looks like you are using:<br>" +temp2;
  }
  
   
  /**
   * Escape an html string. Escaping data received from the client helps to
   * prevent cross-site script vulnerabilities.
   * 
   * @param html the html string to escape
   * @return the escaped string
   */
  private String escapeHtml(String html) {
    if (html == null) {
      return null;
    }
    return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(
        ">", "&gt;");
  }

	
}
