package controller;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.ApplicationVariables;

@WebServlet(urlPatterns = "/SendOTP",loadOnStartup = 0)
public class SendOTP extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	public static Map<String,String> OTP = new HashMap<>();
    
	public void init() throws ServletException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
    		ApplicationVariables.dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Message", "akash", "Ak005Vengai");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String email = request.getParameter("email");
			String otp = "";
			otp = ""+(int)( Math.random()*999999);
			for(int i=otp.length();i<6;i++)
				otp = "0"+otp;
			
			synchronized(this){
				OTP.put(email, otp);
			}
			
            URL url = new URL("https://api.sendinblue.com/v3/smtp/email");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("api-key","" );
            con.setDoOutput(true);
            
            String payload = "{\"sender\":{\"email\":\"ak005messenger@gmail.com\"},\"to\":[{\"email\":\""+email+"\"}],\"subject\":\"Verification mail don't share it .Valid for 2 minute only \",\"textContent\":\"otp code :"+otp+"\"}";
            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
            writer.write(payload);
            writer.flush(); 
            writer.close();
            System.out.println(con.getResponseCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

}
