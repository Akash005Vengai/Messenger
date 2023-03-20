package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.json.simple.JSONObject;

import common.ApplicationVariables;

public class chatMessage {
	
	String sender;
	String message;
	String time;
	public enum messagestatus{delete,notdelete,forme};
	messagestatus status;
	public enum message{view,notview};
	message messagestate;
	Integer file;
	
	
	
	public message getMessageStatus() {
		return messagestate;
	}
	
	public chatMessage(String sender, String message, String time, String file,messagestatus status,message messagestatus) {
		this.sender = sender;
		this.message = message;
		this.time = time;
		this.file = file==null?0:Integer.parseInt(file);
		this.status = status;
		this.messagestate = messagestatus;
	}
	
	
	public String gettime1() {
		return this.time;
	}
	
	@SuppressWarnings("unchecked")
	public String get(String user,String time){
		JSONObject obj = new JSONObject();
//		if(user.equals(this.sender)&&status!=messagestatus.notdelete) {
//			return obj.toString();
//		}
		obj.put("sender", (user.equals(this.sender))?"You":this.sender);
		obj.put("message", message);
		obj.put("file", file);
		try {
			obj.put("time", new SimpleDateFormat("hh:mm aa").format(ApplicationVariables.sp.parse(this.time)));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if(!new SimpleDateFormat("dd/MM/yyyy").format(ApplicationVariables.sp.parse(time)).equals(new SimpleDateFormat("dd/MM/yyyy").format(ApplicationVariables.sp.parse(time)))){
				obj.put("notify", new SimpleDateFormat("dd/MM/yyyy").format(ApplicationVariables.sp.parse(time)));
			}
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return obj.toString();
	}
	
}
