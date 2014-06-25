package com;
import java.util.HashMap;
import com.google.gson.Gson;


public class JSONFormatter implements IJSONFormatter {
	
	String[] Content = {"queue id", "ticket number", "feedback", "current service number"};
	HashMap<String, String> PostParams = new HashMap<String, String>();
	Gson json;
	String URLString = "";
	
	public JSONFormatter(HashMap<String, String> ClientParams) {
//		json = new Gson(); TO DO: Resolve runtime issue
		//Mandatory params for Interact SMS
		PostParams.put("user", "user");
		PostParams.put("password", "pw");
		PostParams.put("api_id", "123");
		PostParams.put("To", ClientParams.get("SMS-From").toString());
		//Gateway address
		URLString.concat("http://api.interactsms.com/HTTP_API/V1/sendmessage.aspx");
		
	}
	
	public String getJSONString(String SMSContent) {
		
		HashMap<String, String> SMSParameters = new HashMap<String, String>();
		
		//TO DO: Content Validation
		
		
		//Split each parameter by comma
		String[] SMSContentParams =  SMSContent.split(",");
		//Populate SMSParameters
		for(int idx = 0; idx < SMSContentParams.length; idx++) {
			String[] paramValues = SMSContentParams[idx].split(" ");//NOT SPLITTING
			SMSParameters.put(paramValues[idx], paramValues[idx]);
		}
		
		//Get SMS content and format into JSON
		String JSONString = "{";
		
		//If client is sending current service number
		if(SMSParameters.containsKey("queue id") && SMSParameters.containsKey("serviced ticket number")) {
			JSONString.concat(
					"servicedTicketNumber :  "+SMSParameters.get("serviced ticket number")
					);
		}
		
		//If client is sending feedback
		else if(SMSParameters.containsKey("rating") && SMSParameters.containsKey("queue id")) {
			JSONString.concat(
					"rating :  "+SMSParameters.get("rating") + "queue id :  "+SMSParameters.get("queue id")
					);
			if(SMSParameters.containsKey("comment")) {
				JSONString.concat("comment :  "+SMSParameters.get("comment"));
			}
		}
		
		//Complete string
		JSONString.concat("}");
		return JSONString;
	
	}
	
	public HashMap<String, String> getSMSParameters(String SMSContent) {
		HashMap<String, String> SMSParameters = new HashMap<String, String>();

		//Split each parameter by comma
		String[] SMSContentParams =  SMSContent.split(",");
		//Populate SMSParameters
		for(int idx = 0; idx < Content.length; idx++) {
			for(int paramIdx = 0; paramIdx < SMSContentParams.length; paramIdx ++) {
				if(SMSContentParams[paramIdx].contains(Content[idx])) {
					String paramValue = SMSContentParams[paramIdx].replace(Content[idx], "");
					SMSParameters.put(Content[idx], paramValue.trim());
				}
			}
		}
		
		return SMSParameters;
		
	}
	
	public String getURLParameters(String jsonString) {
		
		URLString.concat("?");
		
		//Convert json to hash map
		HashMap<String, String> SMSContent = new HashMap<String, String>();
//		SMSContent = (HashMap<String, String>)json.fromJson(jsonString, SMSContent.getClass());
		
		//Add URL parameters
		for(String key : PostParams.keySet()) {
			URLString.concat(key+"="+PostParams.get(key).toString());
		}
		//Add test params
		for(String key : SMSContent.keySet()) {
			URLString.concat(key+"="+SMSContent.get(key).toString());
		}
		
		return URLString;
	}

}
