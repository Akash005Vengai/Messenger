package WebSocket;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import common.ApplicationVariables;
import model.chatMessage;
import model.chatMessage.messagestatus;

@ServerEndpoint(value = "/chatroom/{id}")
public class Chatroom {
	
	private Session ses;
	public static Map<String,Set<Session>> rooms = new HashMap<>();
	
	@OnOpen
	public void open(Session ses,@PathParam("id") String chatId){
		
		if(!chatId.equals("0")) {
			this.ses = ses;
			if(rooms.containsKey(chatId)) {
				boolean valid = true;
				for (Session s : rooms.get(chatId)) {
			        if (s.getId() == ses.getId()){
			            valid = false;
			        }
			    }
				if(valid) {
					rooms.get(chatId).add(this.ses);
				}
			}else {
				Set<Session> ses1 = new HashSet<>();
				ses1.add(this.ses);
				rooms.put(chatId, ses1);
			}
		}
	}
	
	
	@OnClose
	public void close(Session ses){
		rooms.forEach((n,m)->m.remove(ses));
	}

	@SuppressWarnings("unchecked")
	@OnMessage
	public void message(String message, Session session, @PathParam("id") String chatId){
		if((chatId.startsWith("c")&&chatId.replaceAll("[0-9]", "").length()==1)||(chatId.startsWith("g")&&chatId.replaceAll("[0-9]", "").length()==1)) {
			Set<Session> Sessions = rooms.get(chatId);
			try {
				JSONObject obj = (JSONObject) new JSONParser().parse(message);
				PreparedStatement pre = ApplicationVariables.dbConnection.prepareStatement("select * from Session where id = ? and valid = 'T'");
				pre.setString(1,(String)obj.get("cookie"));
				ResultSet rs = pre.executeQuery();
				boolean valid = rs.next();
				String user = rs.getString(2);
				int id = Integer.parseInt(chatId.substring(1));
				if(chatId.startsWith("c")) {
				if(valid&&(ApplicationVariables.chats.get(Integer.parseInt(chatId.replaceAll("[^0-9]", "")))).getUser2().equals(user)||ApplicationVariables.chats.get(Integer.parseInt(chatId.replaceAll("[^0-9]", ""))).getUser1().equals(user)){
					obj.remove("cookie");
					obj.put("time",new SimpleDateFormat("hh:mm aa").format(Calendar.getInstance().getTime()));
					obj.put("id", chatId);
					String time = ApplicationVariables.sp.format(ApplicationVariables.cal.getTime());
					pre = ApplicationVariables.dbConnection.prepareStatement("insert into messages value(?,?,?,now(),\"notdelete\",?,\"notview\")");
					pre.setInt(1, id);
					pre.setString(2, user);
					pre.setString(3, (String)obj.get("message"));
					pre.setInt(4, Integer.parseInt(""+obj.get("file")));
					pre.execute();
					ApplicationVariables.chats.get(Integer.parseInt(chatId.replace("c", ""))).getMessages().add(new chatMessage(user, ""+obj.get("message"), time,""+obj.get("file"), messagestatus.notdelete, model.chatMessage.message.notview));
					Sessions.forEach(n->{
						if(session.getId() == n.getId()){
							obj.put("sender", "You");
						}else {
							obj.put("sender", user);
						}
						try {
							n.getBasicRemote().sendText(obj.toString());
						} catch (Exception e) {
							e.printStackTrace();
						}
						});
					}
				}else {
					if(valid&&(ApplicationVariables.groups.get(id).getAdmins().contains(user)||ApplicationVariables.groups.get(id).getMembers().contains(user))) {
						obj.remove("cookie");
						obj.put("time",new SimpleDateFormat("hh:mm aa").format(Calendar.getInstance().getTime()));
						obj.put("id", chatId);
						String time = ApplicationVariables.sp.format(ApplicationVariables.cal.getTime());
						PreparedStatement pre1 = ApplicationVariables.dbConnection.prepareStatement("insert into groupMessage value(?,?,?,now(),\"notdelete\",?,\"notview\")");
						pre1.setInt(1, id);
						pre1.setString(2, user);
						pre1.setString(3, (String)obj.get("message"));
						pre1.setInt(4, Integer.parseInt(""+obj.get("file")));
						pre1.execute();
						ApplicationVariables.groups.get(id).getMessages().add(new chatMessage(user, ""+obj.get("message"), time,""+obj.get("file"), messagestatus.notdelete, model.chatMessage.message.notview));
						Sessions.forEach(n->{
							if(session.getId() == n.getId()){
								obj.put("sender", "You");
							}else {
								obj.put("sender", user);
							}
							try {
								n.getBasicRemote().sendText(obj.toString());
							} catch (Exception e) {
								e.printStackTrace();
							}
							});
					}
				}
			}catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}		
}
