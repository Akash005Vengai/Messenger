package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.ApplicationVariables;

/**
 * Servlet implementation class LOGOUT
 */
@WebServlet("/user/LOGOUT")
public class LOGOUT extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String user = (String) request.getAttribute("user");
			ApplicationVariables.dbConnection.createStatement().execute("update Session set valid = 'F' where user = \'"+user+"\'");
			
		}catch (Exception e) {
			
		}
	}


}
