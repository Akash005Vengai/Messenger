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
 * Servlet Filter implementation class AuthorizationforGroupChat
 */
public class AuthorizationforGroupChat extends HttpFilter implements Filter {
       
    private static final long serialVersionUID = 6542070938555664779L;

	/**
     * @see HttpFilter#HttpFilter()
     */
    public AuthorizationforGroupChat() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
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
		}catch (Exception e){
			e.printStackTrace();
			obj.put("status", 400);
			obj.put("message", "Bad request");
		}
		if(id>0) {
			boolean valid=ApplicationVariables.groups.get(id).getAdmins().contains(user)||ApplicationVariables.groups.get(id).getMembers().contains(user);
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
		// TODO Auto-generated method stub
	}

}
