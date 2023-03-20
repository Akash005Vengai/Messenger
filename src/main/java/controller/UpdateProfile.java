package controller;

import java.io.IOException;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import common.ApplicationVariables;

/**
 * Servlet implementation class UpdateProfile
 */
@WebServlet("/user/UpdateProfile")
public class UpdateProfile extends HttpServlet {
	
	private static final long serialVersionUID = 1L;   
   
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject obj = new JSONObject();
		try {
			String user = (String)request.getAttribute("user");
			String key = request.getParameter("key");
			String value = request.getParameter("value");
			PreparedStatement pre = ApplicationVariables.dbConnection.prepareStatement("update setting set "+key+" = ? where user = ?");
			pre.setString(1, value);
			pre.setString(2, user);
			pre.executeUpdate();
			obj.put("status", 200);
		}catch (Exception e){
			System.out.println(e.getMessage());
			obj.put("status", 500);
			obj.put("message", "some error occured please message ak005messenger@gmail.com");
		}
		response.getWriter().append(obj.toString());
	}

}
