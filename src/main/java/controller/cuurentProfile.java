package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class cuurentProfile
 */
@WebServlet("/user/currentProfile")
public class cuurentProfile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse res) throws ServletException, IOException {
		String user = (String) request.getAttribute("user");
		res.setContentType("image/png");
		FileInputStream in = null;
		try {
			in = new FileInputStream(new File("/home/akash-zstk286/Music/MessengerIMages/Profile/"+user));
		}catch (Exception e) {
			in = new FileInputStream(new File("/home/akash-zstk286/Music/MessengerIMages/Profile/default"));
		}
		ServletOutputStream out = res.getOutputStream();
		byte[] bytes = new byte[1024];
		int bytesRead ;
		while((bytesRead = in.read(bytes))!=-1)
			out.write(bytes, 0, bytesRead);
		
		in.close();
		out.close();
		
	}

}
