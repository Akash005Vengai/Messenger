package controller;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import common.ApplicationVariables;

/**
 * Servlet implementation class SettingDetails
 */
@WebServlet("/user/SettingDetails")
public class SettingDetails extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String user = (String) request.getAttribute("user");
		JSONObject json = new JSONObject();
		if(user!=null) {	
			try {
				PreparedStatement pre = ApplicationVariables.dbConnection.prepareStatement("select * from setting where user = ?");
				pre.setString(1, user);
				ResultSet rs = pre.executeQuery();
				rs.next();
				json.put("status", 200);
				json.put("showProfile", rs.getString(2));
				json.put("sendColor", rs.getString(3));
				json.put("receiveColor", rs.getString(4));
				json.put("sendNameColor", rs.getString(5));
				json.put("receiveNameColor", rs.getString(6));
			}catch (Exception e) {
				json.put("status", 500);
			}
		}else {
			json.put("status", 400);
		}
		response.getWriter().append(json.toString());
	}
	

}
