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
 * Servlet implementation class readAllMessage
 */
@WebServlet("/user/ReadAllGroupMessage")
public class ReadAllGroupMessage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReadAllGroupMessage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String user = (String) request.getAttribute("user");
		try {
			PreparedStatement pre = ApplicationVariables.dbConnection.prepareStatement("update groupMessage set con = \'view\' where G_id = ? and sender !=? and con=\"notview\"");
			pre.setInt(1, Integer.parseInt(""+request.getParameter("id")));
			pre.setString(2, user);
			pre.execute();
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
