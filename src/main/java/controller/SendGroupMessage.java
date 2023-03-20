package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import common.ApplicationVariables;
import model.Group;
import model.chatMessage;

/**
 * Servlet implementation class SendGroupMessage
 */
@WebServlet("/user/SendGroupMessage")
public class SendGroupMessage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendGroupMessage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String user = (String) request.getAttribute("user");
		int id = Integer.parseInt((String) request.getParameter("id"));
		try {
			Group chat = ApplicationVariables.groups.get(id);
			List<chatMessage> messages = chat.getMessages();
			JSONArray arr = new JSONArray();
			JSONObject obj = new JSONObject();
			if(messages.size()!=0){
				String[] time = new String[1];
				time[0] = messages.get(0).gettime1();
				messages.forEach(n->{
				arr.add(n.get(user, time[0]));
				time[0] = n.gettime1();
			});
			}
			obj.put("arr", arr.toString());
			response.getWriter().append(obj.toString());
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
