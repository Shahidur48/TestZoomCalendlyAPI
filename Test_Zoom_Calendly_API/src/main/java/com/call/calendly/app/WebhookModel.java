package com.call.calendly.app;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WebhookModel {
	
	private String url;
	private BigInteger hookId;
	private String events; //declare without size
}
