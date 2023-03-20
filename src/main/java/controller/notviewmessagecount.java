package controller;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.ApplicationVariables;

/**
 * Servlet implementation class notviewmessagecount
 */
@WebServlet("/user/notviewmessagecount")
public class notviewmessagecount extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			PreparedStatement pre = ApplicationVariables.dbConnection.prepareStatement("select count(*) from messages where chatroomid = ? and sender != ? and con = \"notview\";");
			pre.setInt(1, Integer.parseInt((String)request.getParameter("id")));
			pre.setString(2, (String)request.getAttribute("user"));
			ResultSet rs = pre.executeQuery();
			rs.next();
			response.getWriter().append(rs.getInt(1)+"");
		}catch (Exception e) {
			// TODO: handle exception
		}
	}

}
