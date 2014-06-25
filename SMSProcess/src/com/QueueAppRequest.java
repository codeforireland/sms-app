package com;
import java.util.HashMap;


public class QueueAppRequest {
	
	private static final String URL = "http://api.prod.queue.appbucket.eu";
	private static final String GETQUEUE = "/queues/";
	private static final String GETSTATUS = "/stats";
	private static final String GETTICKETS = "/tickets/";
	private static final String POSTFEEDBACK = "/feedbacks";
	
	public QueueAppRequest() {
	
	}
	
	//Determine the request type
	public String getRequestAddress(HashMap<String, String> RequestContent) {
		
		String requestAddress = URL;
		
		
		//If request contains queue id
		if(RequestContent.get("queue id") != null) {
			requestAddress += GETQUEUE;
			requestAddress += RequestContent.get("queue id");
		}
		
		//If request contains ticket number
		if(RequestContent.get("ticket number") != null) {
			requestAddress += GETTICKETS;
			requestAddress += RequestContent.get("ticket number");
		}
		
		//If request is to send feedback
		if(RequestContent.get("feedback") != null) {
			requestAddress = POSTFEEDBACK;
		}
		
		//Append on parameter values for request if required
		if(requestAddress.contains("queues/") ) {

		}
		
		return requestAddress;
	}

}
