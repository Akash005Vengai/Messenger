package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import common.ApplicationVariables;
import model.User;

/**
 * Servlet implementation class SignUp
 */
@WebServlet("/SignUp")
public class SignUp extends HttpServlet {

	private static final long serialVersionUID = 1L;
    
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject obj = new JSONObject();
		BufferedReader jsonReader = request.getReader();
		String jsonInput = "";
		String str = "";
		while((str = jsonReader.readLine()) != null) {
			jsonInput = jsonInput + str; 
		}
		JSONParser parser = new JSONParser();
		try {
			JSONObject json = (JSONObject)parser.parse(jsonInput);
			String img = (String)json.get("img");
			String email = (String)json.get("email");
			String name = (String)json.get("name");
			String pass = (String)json.get("pass");
			if(pass.length()>8&&pass.length()<=18&&name.trim().length()!=0){
				PreparedStatement pre = ApplicationVariables.dbConnection.prepareStatement("insert into users value(?,?,?,?)");
				pre.setString(1, name);
				pre.setString(2, pass);
				pre.setString(3, email);
				pre.setString(4, ApplicationVariables.time());
				pre.execute();
		        byte[] imageBytes = Base64.getDecoder().decode(img.split(",")[1]);
		        Path path = Paths.get("/home/akash-zstk286/Music/MessengerIMages/Profile/"+email);
		        Files.write(path, imageBytes);
		        try {
					PreparedStatement pre1 = ApplicationVariables.dbConnection.prepareStatement("update set valid = 'F' where user = ?");
					pre1.setString(1, email);
					pre1.executeUpdate();
				}catch(Exception e) {}
		        String cook = ApplicationVariables.createSessionId();
		        response.addCookie(new Cookie("SessionID", cook));
		        pre = ApplicationVariables.dbConnection.prepareStatement("insert into Session value (?,?,'T')");
		        pre.setString(1, cook);
		        pre.setString(2, email);
		        pre.execute();
		        pre = ApplicationVariables.dbConnection.prepareStatement("insert into setting value (?,?,?,?,?,?)");
		        pre.setString(1, email);
		        pre.setString(2, "show");
		        pre.setString(3, "#ffffff");
		        pre.setString(4, "#ffffff");
		        pre.setString(5, "#ffffff");
		        pre.setString(6, "#ffffff");
		        pre.execute();
		        synchronized (this){
					ApplicationVariables.users.put(email, new User(email));
					ApplicationVariables.users.get(email).start();
				 }
		        obj.put("status", true);
			}
		}catch (Exception e) {
			obj.put("status", false);
			e.printStackTrace();
			obj.put("message", "Enter valid input");
		}
		response.getWriter().append(obj.toString());
	}

}
