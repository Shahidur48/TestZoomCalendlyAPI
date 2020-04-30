package com.call.calendly.app;

import java.io.IOException;
import java.math.BigInteger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


class WebhookReg {
	String xcess = "X-TOKEN";
	String xToken = "BCLLOMCKKQBD5U2S22UBPDAMS3MMALYI";
	String baseUrl = "https://calendly.com/api/v1/hooks";
	String content_Type = "Content-Type";
	String form_format = "application/x-www-form-urlencoded";
	String url = null;
	String[] events;
	OkHttpClient client = new OkHttpClient().newBuilder()
			  .build();
	MediaType mediaType = MediaType.parse("text/plain"); 
	RequestBody body = RequestBody.create(mediaType, "");
	
	void authToken() throws IOException 
	{   
		Request request = new Request.Builder()
		  .url("https://calendly.com/api/v1/echo")
		  .method("GET", null)
		  .addHeader(xcess, xToken)
		  .build();
		Response res = client.newCall(request).execute();
		System.out.println("See the"+res.body().string());
	}
	//Subscribe a Hook
	void subscribeHook(WebhookModel webhookModel) throws IOException
	{
		MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
		String queryString  = "url="+webhookModel.getUrl()+"&events[]="+webhookModel.getEvents()+"";
		RequestBody body = RequestBody.create(mediaType, queryString);
		  
		  System.out.println(queryString);  
		  
		  Request req = new Request.Builder()
			  .url(baseUrl) .method("POST", body)
			  .addHeader(xcess, xToken)
			  .addHeader(content_Type, form_format) .build();
		  Response res = client.newCall(req).execute();
		  
		  System.out.println("See the"+res.body().string());
	}
	
	//Get a specific hooks
	void getAHook(WebhookModel webhookModel) throws IOException 
	{
		baseUrl+="/"+webhookModel.getHookId()+"";
		Request request = new Request.Builder()
		  .url(baseUrl).method("GET", null)
		  .addHeader(xcess, xToken).build();
		Response res = client.newCall(request).execute();
		AllHookDetails(res);
	}
	
	//Get all the hooks
	void getAlltheHooks() throws IOException 
	{
		Request request = new Request.Builder()
		  .url(baseUrl) .method("GET", null)
		  .addHeader(xcess, xToken) .build(); 
		Response res = client.newCall(request).execute();		
		AllHookDetails(res);
	}
	//Delete a hook
	void deleteAHook(WebhookModel webhookModel) throws IOException
	{
		
		baseUrl+="/"+webhookModel.getHookId()+"";
		Request request = new Request.Builder()
		  .url(baseUrl) .method("DELETE", body)
		  .addHeader(xcess, xToken) .build(); 
		Response res = client.newCall(request).execute();
		PrintStatusCode(res);
	}	
	
	void AllHookDetails(Response res) throws JSONException, IOException {
		JSONObject myon = new JSONObject(res.body().string());
		JSONArray ja = myon.getJSONArray("data");

        // ITERATE THROUGH AND RETRIEVE CLUB FIELDS
        int n = ja.length();
        
        for (int i = 0; i < n; i++) {
            // GET INDIVIDUAL JSON OBJECT FROM JSON ARRAY
            JSONObject jo = ja.getJSONObject(i);

            // RETRIEVE EACH JSON OBJECT'S FIELDS
            
            JSONObject attributesObj = jo.getJSONObject("attributes");
            int no = i + 1;
            System.out.println("Hook No:"+ no);
            
            System.out.println("Type:"+" "+jo.getString("type"));
            System.out.println("Hook Id:"+" "+jo.getBigInteger("id"));
            System.out.println("Hook Url:"+" "+attributesObj.getString("url"));
            System.out.println("Created Date:"+" "+attributesObj.getString("created_at"));
            System.out.println("State:"+" "+attributesObj.getString("state"));
            
            System.out.println("\n");
            
        }
	}
	
	void PrintStatusCode(Response res) {
		if(res.code() == 200) {
			System.out.println("WebHook has been Deleted");
		}
		else if(res.code() == 401){
			System.out.println("Invalid token");
		}
		else {
			System.out.println("Couldn't find Hook");
		}
	}
	
	
}





