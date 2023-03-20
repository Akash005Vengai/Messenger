package common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import model.Chat;
import model.Group;
import model.User;

public class ApplicationVariables {

	public static Connection dbConnection;	public static Calendar cal = Calendar.getInstance();	public static SimpleDateFormat sp = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
	public static Map<String , User> users = new HashMap<>();
	public static Map<Integer , Chat> chats = new HashMap<>();
	public static Map<Integer , Group> groups = new HashMap<>();
	
	public static String time() {
		return sp.format(ApplicationVariables.cal.getTime());
	}
	
	public static String createSessionId() {
		try {
			UUID ses =UUID.randomUUID();
			PreparedStatement pre = ApplicationVariables.dbConnection.prepareStatement("select * from Session where id = ?");
			pre.setString(1,""+ses);
			ResultSet rs = pre.executeQuery();
			return rs.next()?createSessionId():""+ses;
		}catch (Exception e) {
			return createSessionId();
		}
	
	}

	public static String fileName(int id) {
		try {
			PreparedStatement pre = dbConnection.prepareStatement("select * from file where id=?");
			pre.setInt(1, id);
			ResultSet rs = pre.executeQuery();
			rs.next();
			return rs.getString(2);
		}catch (Exception e) {
			
		}
		return null;
	}

	public static String fileContentType(int id) {
		try {
			PreparedStatement pre = dbConnection.prepareStatement("select * from file where id=?");
			pre.setInt(1, id);
			ResultSet rs = pre.executeQuery();
			rs.next();
			return rs.getString(3);
		}catch (Exception e) {
			
		}
		return null;
	}
	
}
