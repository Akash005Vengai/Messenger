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
 * Servlet implementation class notviewmessageforgroup
 */
@WebServlet("/user/notviewmessageforgroup")
public class notviewmessageforgroup extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public notviewmessageforgroup() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			PreparedStatement pre = ApplicationVariables.dbConnection.prepareStatement("select count(*) from groupMessage where G_id = ? and sender != ? and con = \"notview\"");
			pre.setInt(1, Integer.parseInt((String)request.getParameter("id")));
			pre.setString(2, (String)request.getAttribute("user"));
			ResultSet rs = pre.executeQuery();
			rs.next();
			response.getWriter().append(rs.getInt(1)+"");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}