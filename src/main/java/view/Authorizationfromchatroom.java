package view;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;

import org.json.simple.JSONObject;

import common.ApplicationVariables;

/**
 * Servlet Filter implementation class Authorizationfromchatroom
 */
public class Authorizationfromchatroom extends HttpFilter implements Filter {
       
    private static final long serialVersionUID = -1124272684786486912L;

	/**
     * @see HttpFilter#HttpFilter()
     */
    public Authorizationfromchatroom() {
        super();
        //  Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		//  Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		JSONObject obj = new JSONObject();
		String user = (String) request.getAttribute("user");
		int id = 0;
		try {
			id = Integer.parseInt(""+request.getParameter("id"));
		}catch (Exception e) {
			e.printStackTrace();
			obj.put("status", 400);
			obj.put("message", "Bad request");
		}
		if(id>0){
			boolean valid= ApplicationVariables.users.get(user).getChatIDs().containsValue(id);
			if(valid) {
				chain.doFilter(request, response);
			}else {
				obj.put("status", 400);
				obj.put("message", "Don't access");
				response.getWriter().append(obj.toString());
			}
		}else {
			response.getWriter().append(obj.toString());
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		//  Auto-generated method stub
	}

}
