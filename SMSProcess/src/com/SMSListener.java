package com;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SMSListener
 */
@WebServlet("/SMSListener")
public class SMSListener extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SMSListener() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Send reply message to Interact SMS address: http://api.interactsms.com/HTTP_API/V1/sendmessage.aspx

	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Receive post from SMS Gateway
		
		HashMap<String, String> SMSParams = new HashMap<String, String>();

		//Retrieve sms message parameters
		SMSParams.put("SMS-Content",(request.getParameter("SMS-Content").toString()));
		SMSParams.put("SMS-KeyPhrase",(request.getParameter("SMS-KeyPhrase").toString()));
//		SMSParams.put("SMS-Type",(request.getParameter("SMS-Type").toString()));
//		SMSParams.put("SMS-Network",(request.getParameter("SMS-Network").toString()));
		SMSParams.put("SMS-From",(request.getParameter("SMS-From").toString()));
//		SMSParams.put("SMS-To",(request.getParameter("SMS-To").toString()));
//		SMSParams.put("SMS-TimeStamp",(request.getParameter("SMS-TimeStamp").toString()));
//		SMSParams.put("SMS-Verify",(request.getParameter("SMS-Verify").toString()));
		
		//Only proceed if SMS is for MyQ
		if(SMSParams.get("SMS-KeyPhrase") != null && SMSParams.get("SMS-KeyPhrase").contains("MYQ")) {
			
			//Get client number
			
			JSONFormatter jsonFormatter = new JSONFormatter(SMSParams);
			QueueAppRequest queueAppRequest = new QueueAppRequest();
			
			SMSParams.putAll(jsonFormatter.getSMSParameters(SMSParams.get("SMS-Content")));
			
			//Set queueApp request type
			String requestAddress = queueAppRequest.getRequestAddress(SMSParams);
			
			//Get request
			String getResponse = SendGetRequest(requestAddress);
			
			//Get response for SMSGateway
			String postURL = jsonFormatter.getURLParameters(getResponse);
			
//			SentPostResponse(postURL, )
			
			
			//String queueRequest = jsonFormatter.getJSONString(SMSParams.get("SMS-Content"));
		
		}
	
	}
	
	private String SendGetRequest(String requestURL) {
		
		//Create request
		HttpURLConnection connection = null;
        StringBuffer response = new StringBuffer();
        
		try {
			//Get connection
			URL url = new URL(requestURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/json");
			int responseCode = connection.getResponseCode();
			
			//Valid response
			if(responseCode == 200) {
				
				//Read response
		        BufferedReader in = new BufferedReader(
		                new InputStreamReader(connection.getInputStream()));
		        String inputLine;
		        while ((inputLine = in.readLine()) != null) {
		            response.append(inputLine);
		        }
		        in.close();	
			}
			
			return response.toString();
				
		}
		catch(Exception e) {
			//TODO: Write error to log
			
			return "error";
		}
		
		
	}
	
	private void SendPostResponse(String postResponse, HttpServletResponse response) {

		
		try {
			
		//Reply to post
		ServletOutputStream servletResponse = response.getOutputStream();
		servletResponse.write(postResponse.getBytes());
		servletResponse.flush();
		
		}
		catch(IOException e) {
			//TODO: Write error to log
		}
	}
	
	private String CreateResponse() {
		return null;
	}

}
