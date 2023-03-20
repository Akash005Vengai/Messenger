package controller;

import java.io.IOException;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.ApplicationVariables;

/**
 * Servlet implementation class ReportMessage
 */
@WebServlet("/ReportMessage")
public class ReportMessage extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String message = request.getParameter("message");
		String email = request.getParameter("email");
		try {
			PreparedStatement pre = ApplicationVariables.dbConnection.prepareStatement("insert into reportmessage value(?,?)");
			pre.setString(1, email);
			pre.setString(2, message);
			pre.execute();
		}catch (Exception e) {}
	}

}
