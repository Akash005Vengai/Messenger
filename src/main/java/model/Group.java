package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONArray;

import common.ApplicationVariables;
import model.chatMessage.message;
import model.chatMessage.messagestatus;

public class Group extends Thread{

	List<chatMessage> messages = new LinkedList<>();
	 String Gname;
	 String des;
	 int Gid;
	 enum status{Anyone,Admin}
	 status sendMessage;
	 status changeDes;
	 String mainAdmin;
	 JSONArray Admins;
	 JSONArray members;
	
	public Group(int id){
		this.Gid = id;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		try {
			Admins = new JSONArray();
			members = new JSONArray();
			PreparedStatement pre = ApplicationVariables.dbConnection.prepareStatement("select * from groupPeople where id = ?");
			pre.setInt(1, Gid);
			ResultSet rs = pre.executeQuery();
			while(rs.next())
				if(rs.getString(3).equals("Admin")) {
					Admins.add(rs.getString(2));
				}else {
					members.add(rs.getString(2));
				}
			
			pre = ApplicationVariables.dbConnection.prepareStatement("select * from groupname where id =?");
			pre.setInt(1, Gid);
			rs = pre.executeQuery();
			rs.next();
			Gname = rs.getString(1);
			des = rs.getString(2);
			mainAdmin = rs.getString(3);
			pre = ApplicationVariables.dbConnection.prepareStatement("select * from groupMessage where G_id = ? and status != \"delete\"");
			pre.setInt(1, Gid);
			rs = pre.executeQuery();
			while(rs.next()) {
				messagestatus status = rs.getString(5).equals("delete")?messagestatus.delete:rs.getString(5).equals("forme")?messagestatus.forme:messagestatus.notdelete;
				messages.add(new chatMessage(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(6),status,(rs.getString(7).equals("view"))?message.view:message.notview));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getGName() {
		return Gname;
	}

	public void setGName(String name) {
		this.Gname = name;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public int getGId() {
		return this.Gid;
	}

	public void setId(int id) {
		this.Gid = id;
	}

	public status getSendMessage() {
		return sendMessage;
	}

	public void setSendMessage(status sendMessage) {
		this.sendMessage = sendMessage;
	}

	public status getChangeDes() {
		return changeDes;
	}

	public void setChangeDes(status changeDes) {
		this.changeDes = changeDes;
	}

	public String getMainAdmin() {
		return mainAdmin;
	}

	public JSONArray getAdmins() {
		return Admins;
	}

	public void setAdmins(JSONArray admins) {
		Admins = admins;
	}

	public JSONArray getMembers() {
		return members;
	}

	public List<chatMessage> getMessages() {
		return this.messages;
	}
	
}