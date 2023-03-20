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

import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

import common.ApplicationVariables;

/**
 * Servlet implementation class SendGroupID
 */
@WebServlet("/user/SendGroupID")
public class SendGroupID extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
     
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			PreparedStatement pre = ApplicationVariables.dbConnection.prepareStatement("select gN.name,gN.id,max(time) b from groupname gN left join groupPeople gP on (gN.id = gP.id and gP.user = ?) left join groupMessage as gM on (gN.id=G_id and gP.jointime<=time) group by gN.id order by b;");
			pre.setString(1, (String)request.getAttribute("user"));
			ResultSet rs = pre.executeQuery();
			JSONObject obj = new JSONObject();
			int i = 0;
			while(rs.next())
					obj.put(rs.getInt(2), rs.getString(1)+" "+(i++));
			response.getWriter().append(obj.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}


