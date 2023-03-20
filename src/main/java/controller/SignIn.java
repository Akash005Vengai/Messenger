package controller;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import common.ApplicationVariables;
import model.User;

/**
 * Servlet implementation class SignIn
 */
@WebServlet("/SignIn")
public class SignIn extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("email");
		String pass = request.getParameter("pass");
		JSONObject json = new JSONObject();
		try {
			PreparedStatement pre = ApplicationVariables.dbConnection.prepareStatement("select * from users where email = ?");
			pre.setString(1, email);
			ResultSet re = pre.executeQuery();
			if(re.next()&&re.getString(2).equals(pass)){
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
				json.put("status", 200);
			}else {
				json.put("message", "Mail and password are Mismatched!!");
			}
		}catch (Exception e) {
			e.printStackTrace();
			json.put("status", 500);
			json.put("message","Some error occured please send report message!!");
		}
		response.getWriter().append(json.toString());
	}

}
