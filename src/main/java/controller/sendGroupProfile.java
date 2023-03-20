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
 * Servlet implementation class sendGroupProfile
 */
@WebServlet("/user/sendGroupProfile")
public class sendGroupProfile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public sendGroupProfile() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String user = (String) request.getAttribute("user");
		int name = Integer.parseInt(request.getParameter("id"));
		try {
			PreparedStatement pre = ApplicationVariables.dbConnection.prepareStatement("select * from groupPeople where user = ? and id=?");
			pre.setString(1, user);
			pre.setInt(2, name);
			ResultSet rs = pre.executeQuery();
			boolean valid = rs.next();
			FileInputStream in = null;
			if(valid) {
				in = new FileInputStream(new File("/home/akash-zstk286/Music/MessengerIMages/Group/"+name));
			}else {
				in = new FileInputStream(new File("/home/akash-zstk286/Music/MessengerIMages/Group/default"));
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
