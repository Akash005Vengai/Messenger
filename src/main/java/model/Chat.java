package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

import common.ApplicationVariables;
import model.chatMessage.message;
import model.chatMessage.messagestatus;

public class Chat extends Thread{

	String user1;
	String user2;
	List<chatMessage> messages = new LinkedList<>();
	int id;
	int user1notseenMessage;
	int user2notseenMessage;
	
	public Chat(int id){
		this.id = id;
	}
	
	public String getUser1() {
		return user1;
	}

	public String getUser2() {
		return user2;
	}

	public List<chatMessage> getMessages() {
		return messages;
	}

	public void setUser1notseenMessage(int user1notseenMessage) {
		this.user1notseenMessage = user1notseenMessage;
	}

	public void setUser2notseenMessage(int user2notseenMessage) {
		this.user2notseenMessage = user2notseenMessage;
	}
	
	public void setMessages(List<chatMessage> messages) {
		this.messages = messages;
	}
	
	public String getAnotherUser(String user) {
		return user.equals(user1)?user2:user1;
	}

	public int notseenMessage(String user){
		List<chatMessage> obj = messages.stream().filter(n->!(n.sender.equals(user))&&n.getMessageStatus().equals(message.notview)).toList();
		return obj.size();
	}
	
	@Override
	public void run() {
		try {
		PreparedStatement pre = ApplicationVariables.dbConnection.prepareStatement("select * from messages where chatroomid = ? and status != \"delete\"");
		pre.setInt(1, id);
		ResultSet rs = pre.executeQuery();
		while(rs.next()) {
			messagestatus status = rs.getString(5).equals("delete")?messagestatus.delete:rs.getString(5).equals("forme")?messagestatus.forme:messagestatus.notdelete;
			messages.add(new chatMessage(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(6),status,(rs.getString(7).equals("view"))?message.view:message.notview));
		}
		pre = ApplicationVariables.dbConnection.prepareStatement("select * from chat where id = ?");
		pre.setInt(1, id);
		rs = pre.executeQuery();
		rs.next();
		user1 = rs.getString(2);
		user2 = rs.getString(3);
		}catch (Exception e) {
			System.out.println(id+" "+messages);
		}
	}
	
}
