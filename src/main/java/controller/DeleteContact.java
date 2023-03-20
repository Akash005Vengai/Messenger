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
 * Servlet implementation class DeleteContact
 */
@WebServlet("/user/DeleteContact")
public class DeleteContact extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String user = (String) request.getAttribute("user");
		String user1 = (String) request.getParameter("user");
		try {
			PreparedStatement pre = ApplicationVariables.dbConnection.prepareStatement("delete from contact where user1 = ? and user2 = ?");
			pre.setString(1, user);
			pre.setString(2, user1);
			pre.execute();
			ApplicationVariables.users.get(user).getContact().remove(user1);
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
}
