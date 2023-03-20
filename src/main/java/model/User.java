package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import common.ApplicationVariables;

public class User extends Thread{

	 String mail;	 JSONObject chatIDs = new JSONObject();
	 List<Integer> joingroupID = new ArrayList<>();	 JSONObject contact = new JSONObject();
	 String lastLogin;
	 String name;
	 
	 
	
	public User(String mail) {
		super();
		this.mail = mail;
	}

	@SuppressWarnings("unchecked")
	public void run() {
		try {
			PreparedStatement pre = ApplicationVariables.dbConnection.prepareStatement("select * from contact where user1 = ?");
			pre.setString(1, mail);
			ResultSet rs = pre.executeQuery();
			while(rs.next())
				contact.put(rs.getString(2), rs.getString(3));
			pre = ApplicationVariables.dbConnection.prepareStatement("select * from chat where user1 =? or user2=?");
			pre.setString(1, mail);
			pre.setString(2, mail);
			rs = pre.executeQuery();
			while(rs.next()) {
				synchronized (this){
					if(!ApplicationVariables.chats.containsKey(rs.getInt(1))){
						Chat chat = new Chat(rs.getInt(1));
						chat.start();
						ApplicationVariables.chats.put(rs.getInt(1), chat);
					}					
				}
				if(rs.getString(2).equals(mail)) {
					chatIDs.put(rs.getString(3), rs.getInt(1));
				}else {
					chatIDs.put(rs.getString(2), rs.getInt(1));
				}
			}
			pre = ApplicationVariables.dbConnection.prepareStatement("select * from groupPeople where user = ?");
			pre.setString(1,mail);
			rs = pre.executeQuery();
			while(rs.next()) {
				synchronized (this){
					if(!ApplicationVariables.groups.containsKey(rs.getInt(1))){
						Group chat = new Group(rs.getInt(1));
						chat.start();
						ApplicationVariables.groups.put(rs.getInt(1), chat);
					}					
				}
				joingroupID.add(rs.getInt(1));
			}
			pre = ApplicationVariables.dbConnection.prepareStatement("select * from users where email = ?");
			pre.setString(1, mail);
			rs = pre.executeQuery();
			rs.next();
			lastLogin = rs.getString(4);
			name = rs.getString(1);
		}catch (Exception e) {
			System.out.println(e.getMessage()+" "+mail);
		}
	}

	public String getMail() {
		return mail;
	}

	public JSONObject getChatIDs() {
		return chatIDs;
	}

	public List<Integer> getJoingroupID() {
		return joingroupID;
	}

	public JSONObject getContact() {
		return contact;
	}

	public String getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getUserName() {
		return name;
	}

	public void setUserName(String name){
		this.name = name;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public void setChatIDs(JSONObject chatIDs) {
		this.chatIDs = chatIDs;
	}

	public void setJoingroupID(List<Integer> joingroupID) {
		this.joingroupID = joingroupID;
	}

	public void setContact(JSONObject contact) {
		this.contact = contact;
	}

	
	
}
