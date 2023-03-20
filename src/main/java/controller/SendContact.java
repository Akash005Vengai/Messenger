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
import model.User;

/**
 * Servlet implementation class SendContact
 */
@WebServlet(urlPatterns = "/user/SendContact",loadOnStartup = 1)
public class SendContact extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    private static int roomid = 0;  
	
	
	@Override
		public void init() throws ServletException {
			try {
				PreparedStatement pre = ApplicationVariables.dbConnection.prepareStatement("select * from users");
				ResultSet rs = pre.executeQuery();
				while(rs.next()) {
					User use = new User(rs.getString(3));
					use.start();
					ApplicationVariables.users.put(rs.getString(3), use);
				}
				pre = ApplicationVariables.dbConnection.prepareStatement("select max(id) from chat");
				rs = pre.executeQuery();
				rs.next();
				roomid = rs.getInt(1);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append(ApplicationVariables.users.get((String) request.getAttribute("user")).getContact().toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	protected synchronized void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ApplicationVariables.users.get((String) request.getAttribute("user")).getContact().put(request.getParameter("email"), request.getParameter("name"));
		try {
			PreparedStatement pre = ApplicationVariables.dbConnection.prepareStatement("insert into contact value(?,?,?)");
			pre.setString(1, (String) request.getAttribute("user"));
			pre.setString(2, request.getParameter("email"));
			pre.setString(3, request.getParameter("name"));
			pre.execute();
			pre = ApplicationVariables.dbConnection.prepareStatement("select * from chat where (user1 = ? and user2=?) or (user1=? and user2=?)");
			pre.setString(1, request.getParameter("email"));
			pre.setString(2, (String) request.getAttribute("user"));
			pre.setString(3, (String) request.getAttribute("user"));
			pre.setString(4, request.getParameter("email"));
			ResultSet rs = pre.executeQuery();
			if(!rs.next()){
				pre = ApplicationVariables.dbConnection.prepareStatement("insert into chat value(?,?,?)");
				int room = 0;
				synchronized (this) {
					room = ++roomid;
				}
				pre.setInt(1, room);
				pre.setString(2, request.getParameter("email"));
				pre.setString(3, (String) request.getAttribute("user"));
				pre.execute();
				response.getWriter().append(""+room);
			}
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
