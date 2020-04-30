package com.call.calendly.app;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

class CalendlyApi {
	
	void callCalendly() throws IOException {
		
		int serverPort = 8088;
        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);

        HttpContext context =server.createContext("/calendly/scheduleddata", (exchange -> {
		  
			  if ("POST".equals(exchange.getRequestMethod())) {
				
					InputStream is = exchange.getRequestBody();               // retrieve the request json data
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					byte[] buffer = new byte[2048];
					int len;
					while ((len = is.read(buffer))>0) {
						bos.write(buffer, 0, len);
					}
					String data = new String(bos.toByteArray(), Charset.forName("UTF-8"));
					
					//print the scheduled data coming from webhooks
					System.out.println("Request: \n    " + data);
					System.out.println("\n");
					
					//formating JSON data
					JSONObject mainResponseObject = new JSONObject(data.toString());
					JSONObject payLoadObject = mainResponseObject.getJSONObject("payload");
					JSONObject eventTypeObject = payLoadObject.getJSONObject("event_type");
					JSONObject eventObject = payLoadObject.getJSONObject("event");
					JSONArray assignedto_value = eventObject.getJSONArray("assigned_to");
					JSONObject inviteeObject = payLoadObject.getJSONObject("invitee");
					
					System.out.println("Event Status:"+" "+mainResponseObject.getString("event"));
					System.out.println("UUID:"+" "+eventTypeObject.getString("uuid"));
					System.out.println("Meeting Type:"+" "+eventTypeObject.getString("kind"));
					System.out.println("Duration:"+" "+eventTypeObject.getString("slug"));
					System.out.println("Name:"+" "+eventTypeObject.getString("name"));
					System.out.println("Assigned to:"+" "+assignedto_value.getString(0));
					System.out.println("Start Time:"+" "+eventObject.getString("start_time"));
					System.out.println("End Time:"+" "+eventObject.getString("end_time"));
					System.out.println("Invitee Name:"+" "+inviteeObject.getString("name"));
					System.out.println("Invitee email:"+" "+inviteeObject.getString("email"));
					
					
					OutputStream os = exchange.getResponseBody();
					os.close();
		       }
			  }));

        server.setExecutor(null); // creates a default executor
        server.start();
	}
}


class Application {
    
}
