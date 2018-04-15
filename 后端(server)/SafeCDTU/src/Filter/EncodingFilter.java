package Filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


//过滤器
public class EncodingFilter implements Filter {

    
    public EncodingFilter() {
        
    }

	
	public void destroy() {
		
	}

	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8;pageEncoding=UTF-8");
        response.setCharacterEncoding("UTF-8");
        HttpServletResponse httpServletResponse=(HttpServletResponse)response;
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
		chain.doFilter(request, httpServletResponse);
		
		
	}

	
	public void init(FilterConfig fConfig) throws ServletException {
		
	}

}
