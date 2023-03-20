package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import common.ApplicationVariables;

/**
 * Servlet implementation class ReceiveImage
 */
@MultipartConfig(maxFileSize = (1024*1024*1024*2)-5)
@WebServlet(urlPatterns = "/user/ReceiveImage",loadOnStartup = 1)
public class ReceiveImage extends HttpServlet {
	
	private static int fileid = 0;
	private static final long serialVersionUID = 1L;
	
	@Override
	public void init() throws ServletException {
		try {
			ResultSet rs = ApplicationVariables.dbConnection.createStatement().executeQuery("select max(id) from file");
			rs.next();
			fileid = rs.getInt(1);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
       
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		int id = Integer.parseInt(""+req.getParameter("id"));
		res.setHeader("Content-Disposition", "attachment; filename = \""+ApplicationVariables.fileName(id)+"\"");
		File file = new File("/home/akash-zstk286/Music/MessengerIMages/Message/"+id);
		res.setContentType(ApplicationVariables.fileContentType(id));
		FileInputStream in = new FileInputStream(file);
		ServletOutputStream out = res.getOutputStream();
		byte[] bytes = new byte[1024];
		int bytesRead ;
		while((bytesRead = in.read(bytes))!=-1) {
			out.write(bytes, 0, bytesRead);
		}
		in.close();
		out.close();
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try {
			int id = 0;
			synchronized (this) {
				id = ++fileid;
			}
			File file = new File("/home/akash-zstk286/Music/MessengerIMages/Message/"+id);
			Part part = req.getPart("file");
			InputStream in = part.getInputStream();
			String header = part.getHeader("Content-Disposition");
			String filename = header.substring(header.lastIndexOf("\"",header.length()-2)+1, header.lastIndexOf("\""));
			byte[] bytes = in.readAllBytes();
			FileOutputStream out = new FileOutputStream(file);
			out.write(bytes);
			out.flush();
			out.close();
			PreparedStatement pre = ApplicationVariables.dbConnection.prepareStatement("insert into file value (?,?,?)");
			pre.setInt(1, id);
			pre.setString(2, filename);
			pre.setString(3, part.getContentType());
			pre.execute();
			res.getWriter().write(""+id);
		}catch (Exception e) {
			res.getWriter().append("fail");
		}
	}

}
