package com.call.zoom.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

import lombok.Setter;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class CallingZoom {
	
	OkHttpClient client = new OkHttpClient().newBuilder() .build();
	MediaType mediaType = MediaType.parse("application/json,application/json");
	String access_toke = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOm51bGwsImlzcyI6IlNyNzhBTEExUUFXbmhyRVVmSlFKeEEiLCJleHAiOjE1ODg5MzY5MzAsImlhdCI6MTU4ODMzMjEzMn0.ZN7ZIUxM1DcZWivLmzSWkPr18fEiCe6wq9CaMqdLD6g";
	String UserId = "";
	String base_url = "https://api.zoom.us/v2/";
	
	void createMeeting(MeetingDataModel md) throws IOException {
		Gson gson = new Gson();  			       // Create an object of gson Class
		String queryString = gson.toJson(md);;     // gson to serialize data of the object for the param of the api
		RequestBody body = RequestBody.create(mediaType,queryString);
		UserId = getUserId();
		System.out.println(queryString);           // check the query_string data
		base_url+="users/ "+UserId+"";
		
		Request request = new Request.Builder()
				  .url(base_url)
				  .method("POST", body)
				  .addHeader("Content-Type", "application/json")
				  .addHeader("Authorization", "Bearer "+access_toke+"")
				  .build();
		try {
			Response response = client.newCall(request).execute();
			if(response.code() == 201) {
				System.out.println("Meeting has been created successfully");
			}
			else if(response.code() == 300){
				System.out.println("Invalid enforce_login_domains, separate multiple domains by semicolon.");
			}
			else {
				System.out.println("User not found.");
			}
			
			System.out.println(response.message());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	Boolean cancelMeeting(MeetingDataModel md)
	{
		Boolean flag = true;
		base_url += "meetings/"+md.getMeetingID()+"";
		RequestBody body = RequestBody.create(mediaType, "");
		Request request = new Request.Builder()
		  .url(base_url)
		  .method("DELETE", body)
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer "+access_toke+"")
		  .build();
				try {
					Response response = client.newCall(request).execute();
					if(response.code() == 204) {
						flag= true;
					}
					
					else {
						flag= false;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return flag;
	}
	
	void getMeetingInformation(MeetingDataModel md)
	{
		base_url += "meetings/"+md.getMeetingID()+"";
		Request request = new Request.Builder()
		  .url(base_url)
		  .method("GET", null)
		  .addHeader("Authorization", "Bearer "+access_toke+"")
		  .build();
			try {
				Response response = client.newCall(request).execute();
				JSONObject myon = new JSONObject(response.body().string());
				
				System.out.println("Meeting topc:"+" "+myon.getString("topic"));
				System.out.println("Meeting Status:"+" "+myon.getString("status"));
				System.out.println("Start Time:"+" "+myon.getString("start_time"));
				System.out.println("Meeting Duration:"+" "+myon.getInt("duration"));
				System.out.println("Start Url:"+" "+myon.getString("start_url"));
				System.out.println("Join Url:"+" "+myon.getString("join_url"));
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	}
	
	
	String getUserId() throws IOException
	{
				Request request = new Request.Builder()
				  .url("https://api.zoom.us/v2/users/")
				  .method("GET", null)
				  .addHeader("content-type", "application/json")
				  .addHeader("Authorization", "Bearer "+access_toke+"")
				  .build();
				Response response = client.newCall(request).execute();
				JSONObject myon = new JSONObject(response.body().string());
	
				JSONArray lineItems = myon.getJSONArray("users");
			    for (Object o : lineItems) {
			        JSONObject jsonLineItem = (JSONObject) o;
			        String key = jsonLineItem.optString("id");
			        UserId=key;
			    }
				
				return UserId;
	}
	
}


public class ZoomTestMeeting {
	
	public static void main(String[] args) throws IOException {
		
		CallingZoom cz = new CallingZoom();             // Create an object of Zoom Class
		MeetingDataModel md = new MeetingDataModel();    // Create an object of Model Class
		
		Scanner in = new Scanner(System.in);
		System.out.println("Make your choice for Zoom: "+ "\n"+ "1. Create a Zoom Meeting"+ "\n"+ "2. Cancel a Meeting"+ "\n"+ "3. Get Meeting Information"+ "\n");
        int choice = 0;
        BigInteger meetingID;
        System.out.println("Your Choice: "+ " ");
        
        //choose option for the input
        choice = in.nextInt();
        
        switch(choice) 
        {
        	case 1:
        		BufferedReader br=new  BufferedReader(new InputStreamReader(System.in));
        		String topic, startTime, timeZone, password, agenda;
        		int type,duration;
        		
        		System.out.println("Topic: ");              
                topic= br.readLine();
                System.out.println("Meeting Type: "+"\ninput 1 for 'Instant Meeting'"+
                		"\ninput 2 for 'Scheduled Meeting'"+
                		"\ninput 3 for 'Recurring Meeting with no Fixed Time'"+
                		"\ninput 8 for 'Recurring Meeting with Fixed Time'"+"): ");
                type = in.nextInt();
                
                System.out.println("startTime (YYYY-MM-DD HH:MM:ss):");
                startTime = br.readLine();
                for(;!(startTime.substring(4, 5).contains("-") && startTime.substring(7, 8).contains("-") && startTime.substring(13, 14).contains(":") && startTime.substring(16, 17).contains(":"));)
                {
                	System.out.println("Wrong format. Input startTime correctly like (YYYY-MM-DD HH:MM:ss):");
                    startTime = br.readLine();
                }
                
                System.out.println("duration(in minutes): ");
                duration = in.nextInt();
                
                System.out.println("timeZone: "+ " "+ "example: America/Los_Angeles");
                timeZone = br.readLine();
                
                System.out.println("password: ");
                password = br.readLine();
                
                System.out.println("agenda: ");
                agenda = br.readLine();
                
                //assigning value to the lombok setter
                md.setTopic(topic); 
                md.setType(type);
                md.setStartTime(startTime);
                md.setDuration(duration);
                md.setTimezone(timeZone);
                md.setPassword(password);
                md.setAgenda(agenda);
                
                cz.createMeeting(md);         // Call the create meeting method on the object
        		
        		break;
        		
        	case 2:
  			  Boolean delMeeting;
  			  
  			  System.out.println("Please enter the meeting ID you want to delete: ");
  			  meetingID = in.nextBigInteger();
  			  
  			  md.setMeetingID(meetingID);
  			  
  			  delMeeting = cz.cancelMeeting(md);
  			  if(delMeeting == true) {
  				  System.out.println("Meeting has been canceled successfully");
  			  }
  			  else {
  				  System.out.println("Please enter the valid meeting ID");
  			  }
  			  break;
  			  
        	case 3:
        		System.out.println("Please enter the meeting ID you want to get info: ");
  			  	meetingID = in.nextBigInteger();
  			  	
  			  	// assign value to the lombok setter
  			  	md.setMeetingID(meetingID);
        		
        		cz.getMeetingInformation(md);
        		break;
        	
        } 
	} 
}
