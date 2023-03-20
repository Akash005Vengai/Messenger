package controller;

import java.io.IOException;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.ApplicationVariables;
import model.Chat;

/**
 * Servlet implementation class ReadAllMessage
 */
@WebServlet("/user/readAllMessage")
public class ReadAllMessage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReadAllMessage() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String user = (String) request.getAttribute("user");
		try {
			PreparedStatement pre = ApplicationVariables.dbConnection.prepareStatement("update messages set con = \'view\' where chatroomid = ? and sender !=? and con=\"notview\"");
			pre.setInt(1, Integer.parseInt(""+request.getParameter("id")));
			pre.setString(2, user);
			pre.execute();
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
