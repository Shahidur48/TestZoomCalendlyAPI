package com.call.zoom.app;

import java.math.BigInteger;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MeetingDataModel {
	
	private String topic;
	private String startTime;
	private int type;
	private int duration;
	private String timezone;
	private String password;
	private String agenda;
	private BigInteger meetingID;
	
	
	

}
