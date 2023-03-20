package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * Servlet implementation class UpdateCurrentProfile
 */
@MultipartConfig(maxFileSize = (1024*1024*1024*2)-5)
@WebServlet("/user/UpdateCurrentProfile")
public class UpdateCurrentProfile extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try {
			String user = (String) req.getAttribute("user");
			Part part = req.getPart("file");
			InputStream in = part.getInputStream();
			byte[] bytes = in.readAllBytes();
			File file = new File("/home/akash-zstk286/Music/MessengerIMages/Profile/"+user);
			file.createNewFile();
			FileOutputStream out = new FileOutputStream(file);
			out.write(bytes);
			out.flush();
			out.close();
			res.getWriter().write("success");
		}catch (Exception e) {
			res.getWriter().append("fail");
		}
	}

}
