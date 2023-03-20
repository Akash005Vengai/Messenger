package controller;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import common.ApplicationVariables;

/**
 * Servlet implementation class SendChatId
 */
@WebServlet("/user/SendChatId")
public class SendChatId extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendChatId() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			PreparedStatement pre = ApplicationVariables.dbConnection.prepareStatement("select id ,max(time)b from chat left join messages on (id = chatroomid and status != \"delete\" and time >= ?) where user1 = ? or user2 = ? group by id order by b desc");
			pre.setString(1, ApplicationVariables.users.get((String)request.getAttribute("user")).getLastLogin());
			pre.setString(2, (String)request.getAttribute("user"));
			pre.setString(3,(String)request.getAttribute("user"));
			ResultSet rs = pre.executeQuery();
			JSONObject obj = new JSONObject();
			int i = 0;
			while(rs.next())
				if(rs.getString(2)!=null) {
					obj.put(rs.getInt(1), ApplicationVariables.chats.get(rs.getInt(1)).getAnotherUser((String)request.getAttribute("user"))+" "+(i++));
				}
			
			response.getWriter().append(obj.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
