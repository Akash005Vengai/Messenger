package controller;

import java.io.IOException;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import common.ApplicationVariables;
import model.User;

@WebServlet("/ChangePassword")
public class ChangePassword extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject json = new JSONObject();
		try {
			
			String email = request.getParameter("email");
			String pass = request.getParameter("pass");
			if(pass.length()>8&&pass.length()<18){
				PreparedStatement pre = ApplicationVariables.dbConnection.prepareStatement("update users set password = ? where email = ?");
				pre.setString(1, pass);
				pre.setString(2, email);
				pre.executeUpdate();
				pre = ApplicationVariables.dbConnection.prepareStatement("update Session set valid = 'F' where user = ?");
			    pre.setString(1, email);
			    pre.execute();
				String cook = ApplicationVariables.createSessionId();
				response.addCookie(new Cookie("SessionID", cook));
				pre = ApplicationVariables.dbConnection.prepareStatement("insert into Session value (?,?,'T')");
				pre.setString(1, cook);
				pre.setString(2, email);
				pre.execute();
				if(ApplicationVariables.users.get(email)!=null){
				     synchronized (this){
				    	 User user = new User(email);
				    	ApplicationVariables.users.put(email, user);
				    	user.start();
				     }
				}
			    json.put("status",201);
			}else {
				json.put("status", 400);
				json.put("message", "Enter password (8-18) character");
			}
		}catch (Exception e) {
			json.put("status",500);
			json.put("message", "Some error accured please contact administration.");
		}
		response.getWriter().append(json.toString());
	}

}
