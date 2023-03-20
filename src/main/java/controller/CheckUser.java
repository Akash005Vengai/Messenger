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
 * Servlet implementation class CheckUser
 */
@WebServlet("/user/CheckUser")
public class CheckUser extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject obj = new JSONObject();
		obj.put("status", false);
		try {
			String user = (String) request.getAttribute("user");
			String user2 = request.getParameter("user");
			PreparedStatement pre = ApplicationVariables.dbConnection.prepareStatement("select * from users where email = ?");
			pre.setString(1, user2);
			ResultSet rs = pre.executeQuery();
			if(rs.next()&&!user.equals(user2)){
				pre = ApplicationVariables.dbConnection.prepareStatement("select * from contact where user1 =? and user2=?");
				pre.setString(1, user);
				pre.setString(2, user2);
				rs = pre.executeQuery();
				obj.put("status", !rs.next());
			}else {
				obj.put("status", false);
			}
		}catch (Exception e) {
			System.err.println(e.getMessage());
		}
		response.getWriter().append(obj.toString());
	}

}
