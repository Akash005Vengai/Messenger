package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.ApplicationVariables;

/**
 * Servlet implementation class sendProfile
 */
@WebServlet(urlPatterns = "/user/sendProfile")
public class sendProfile extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String user = (String) request.getAttribute("user");
		String name = request.getParameter("name");
		try {
			PreparedStatement pre = ApplicationVariables.dbConnection.prepareStatement("select * from setting where user = ?");
			pre.setString(1, name);
			ResultSet rs = pre.executeQuery();
			rs.next();
			String profile = rs.getString(2);
			FileInputStream in = null;
			if(profile.equals("show")||(profile.equals("contact")&&ApplicationVariables.users.get(name).getContact().containsKey(user))){
				in = new FileInputStream(new File("/home/akash-zstk286/Music/MessengerIMages/Profile/"+name));
			}else {
				in = new FileInputStream(new File("/home/akash-zstk286/Music/MessengerIMages/Profile/default"));
			}
			ServletOutputStream out = response.getOutputStream();
			byte[] bytes = new byte[1024];
			int bytesRead ;
			while((bytesRead = in.read(bytes))!=-1)
				out.write(bytes, 0, bytesRead);
			
			in.close();
			out.close();
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}


}
