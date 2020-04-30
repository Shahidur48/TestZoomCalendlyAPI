package com.call.calendly.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Test_Calendly {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		CalendlyApi calendly = new CalendlyApi();
		WebhookReg webhook = new WebhookReg();
		WebhookModel webModel = new WebhookModel();
		
		Scanner in = new Scanner(System.in);
		System.out.println("Please Choose to Operate: "+ "\n"+ "1. Webhook Operation"+ "\n"+ "2. Calling Calendly for Scheduled Data"+"\n");
        int choice = 0;
        choice = in.nextInt();
        String url;
        String events = "invitee.created";
        BigInteger hookId;
        
        switch(choice) 
        {
        	case 1:
        		String c = "";
        		do {
        			System.out.println("Please give input for Webhook: "+ "\n"+ "1. Auhtorization Token"+ "\n"+ "2. Webhook Registration"+"\n"+ "3. Get all the registered Webhook"+"\n"+"4. Get a speific Webhook"+"\n"+ "5. Delete a Webhok"+"\n");
    	        	choice = in.nextInt();
    	        	
    	        	switch(choice) 
    	            {
    	        		case 1:
    	        			webhook.authToken();
    	        			break;
    	        		case 2:
    	        			BufferedReader br=new  BufferedReader(new InputStreamReader(System.in));
    	        			System.out.println("Please Enter the url you want to Register: ");
    	        			
    	        			url = br.readLine();
    	        			url+="/calendly/scheduleddata";
    	        			
    	        			System.out.println("Please give input for any of the Following: "+ "\n"+"1. for invitee.created"+"\n"+"2. for invitee.canceled"+"\n");
    	        			choice = in.nextInt();
    	                	
    	                	switch(choice) 
    	                    {
    	                		case 1:
    	                			events = "invitee.created";
    	                			break;
    	                		case 2:
    	                			events = "invitee.canceled";
    	                			break;
    	                    }
    	        			
    	        			webModel.setEvents(events);
    	        			webModel.setUrl(url);
    	        			
    	        			webhook.subscribeHook(webModel);
    	        			break;
    	        		case 3:
    	        			webhook.getAlltheHooks();
    	        			break;
    	        		case 4:
    	        			hookId = in.nextBigInteger();
    	        			System.out.println("Please Enter Hook Id: ");
    	        			webModel.setHookId(hookId);
    	        			webhook.getAHook(webModel);
    	        			break;
    	        		case 5:
    	        			System.out.println("Please Enter Hook Id You Want to Delete: ");
    	        			hookId = in.nextBigInteger();
    	        			webModel.setHookId(hookId);
    	        			webhook.deleteAHook(webModel);
    	        			break;
    	            }
    	        	System.out.println("Do you want to continue? Type y for Yes or n for No");
    	            c = in.next();
        			
        		}while(c.equalsIgnoreCase("Y"));
	        	break;
        	
        	case 2:
        		calendly.callCalendly();
        		break;
        
        
        }

	}

}
