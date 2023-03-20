package view;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;

import common.ApplicationVariables;


/**
 * Servlet Filter implementation class Authentication
 */
@SuppressWarnings("serial")
public class Authentication extends HttpFilter implements Filter {
       
    /**
     * @see HttpFilter#HttpFilter()
     */
    public Authentication() {
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
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException{
		try {
			HttpServletRequest req = (HttpServletRequest) request;
			Cookie[] cook = req.getCookies();
			String value = null;
			if(cook!=null) {
				for(int i =0;i<cook.length;i++) {
					if(cook[i].getName().equals("SessionID")){
						value= cook[i].getValue();
						break;
					}
				}
				if(value!=null) {
					try {
						PreparedStatement pre = ApplicationVariables.dbConnection.prepareStatement("select * from Session where id = ? and valid = 'T'");
						pre.setString(1,value);
						ResultSet rs = pre.executeQuery();
						if(rs.next()){
//							System.out.println(rs.getString(2));
							request.setAttribute("user", rs.getString(2));
							chain.doFilter(req, response);
						}else {
							response.getWriter().append(new JSONObject().put("status", 1000).toString());
						}
					}catch (Exception e){
						response.getWriter().append(new JSONObject().put("status", 1000).toString());
					}
				}
				
			}else {
				response.getWriter().append(new JSONObject().put("status", 1000).toString());
			}
		}catch (Exception e) {
			
		}
	}

	
	public void init(FilterConfig fConfig) throws ServletException {
		super.init(fConfig);
	}

}
