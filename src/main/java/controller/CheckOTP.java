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

@WebServlet("/CheckOTP")
public class CheckOTP extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject json = new JSONObject();
		try {
			String email = request.getParameter("email");
			String otp = request.getParameter("otp");
			if((SendOTP.OTP.get(email)).equals(otp)){
				PreparedStatement pre = ApplicationVariables.dbConnection.prepareStatement("select * from users where email = ?");
				pre.setString(1, email);
				ResultSet re = pre.executeQuery();
				if(re.next()) {
					json.put("status", 200);
				}else {
					json.put("status", 201);
				}
			}else {
				json.put("status",400);
				json.put("message", "Enter valid otp");
			}
		}catch(Exception e) {
			json.put("status",400);
			json.put("message", "some error acurred please contact administration.");
		}
		response.getWriter().append(json.toString());
	}

}
