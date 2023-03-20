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

@WebServlet("/CheckSession")
public class CheckSession extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject json = new JSONObject();
		Cookie[] cook = req.getCookies();
		String value = null;
		if(cook!=null) {
			for(int i =0;i<cook.length;i++) {
				if(cook[i].getName().equals("SessionID")){
					value= cook[i].getValue();
					break;
				}
			}
			if(value!=null){
					try {
					PreparedStatement pre = ApplicationVariables.dbConnection.prepareStatement("select * from Session where id = ? and valid = 'T'");
					pre.setString(1,value);
					ResultSet rs = pre.executeQuery();
					if(rs.next()) {
						json.put("status", 200);
					}else {
						json.put("status", 400);
					}
				}catch (Exception e) {
					json.put("status", 500);
				}
			}else {
				json.put("status", 400);
			}
		}else {
			json.put("status", 400);
		}
		resp.getWriter().append(json.toString());
	}
	
}
