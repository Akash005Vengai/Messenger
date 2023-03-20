package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import common.ApplicationVariables;

/**
 * Servlet implementation class createGroup
 */
@MultipartConfig(maxFileSize = 10024*1024*1024)
@WebServlet(urlPatterns = "/user/createGroup",loadOnStartup = 2)
public class createGroup extends HttpServlet {
	
	@Override
		public void init() throws ServletException {
			try {
				ResultSet pre = ApplicationVariables.dbConnection.createStatement().executeQuery("select count(*) from groupname");
				pre.next();
				id = pre.getInt(1);
				System.out.println(id);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	private static final long serialVersionUID = 1L;
	private static int id=0;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String user = (String) request.getAttribute("user");
		int id;
		synchronized (this) {
			id = ++this.id;
		}
		System.out.println(id);
		String name = request.getParameter("name");
		String des = request.getParameter("des");
		String users = request.getParameter("members");
		users = users.substring(1, users.length()-1);
		if(request.getParameter("image").equals("true")) {
			File file = new File("/home/akash-zstk286/Music/MessengerIMages/Group/"+id);
			Part part = request.getPart("img");
			InputStream in = part.getInputStream();
			byte[] bytes = in.readAllBytes();
			FileOutputStream out = new FileOutputStream(file);
			out.write(bytes);
			out.flush();
			out.close();
		}else {
			File file = new File("/home/akash-zstk286/Music/MessengerIMages/Group/"+id);
			FileInputStream in = new FileInputStream("/home/akash-zstk286/Music/MessengerIMages/Group/default");
			byte[] bytes = in.readAllBytes();
			FileOutputStream out = new FileOutputStream(file);
			out.write(bytes);
			out.flush();
			out.close();
			in.close();
		}
		
		try {
			PreparedStatement pre = ApplicationVariables.dbConnection.prepareStatement("insert into groupname value(?,?,?,?)");
			pre.setString(1, name);
			pre.setString(2, des);
			pre.setString(3, user);
			pre.setInt(4, id);
			pre.execute();
			pre = ApplicationVariables.dbConnection.prepareStatement("insert into groupPeople value(?,?,?,now())");
			pre.setInt(1, id);
			pre.setString(2, user);
			pre.setString(3, "Admin");
			pre.execute();
			for(String user1:users.split(",")){
				System.out.println(user1);
				pre = ApplicationVariables.dbConnection.prepareStatement("insert into groupPeople value(?,?,?,now())");
				pre.setInt(1, id);
				pre.setString(2, user1);
				pre.setString(3, "Member");
				pre.execute();
			}
				
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
