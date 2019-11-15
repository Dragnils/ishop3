package net.devstudy.ishop.filter;

import net.devstudy.ishop.util.RoutingUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class ErrorHandlerFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(req, resp);// будет передавать управление дальше
        } catch (Throwable th) { // если будет ошибка, то он ее залогирует
            String requestUrl = ((HttpServletRequest)req).getRequestURI();
            //LOGGER.error("Request " + requestUrl + " failed: " + th.getMessage(), th);
            RoutingUtils.forwardToPage("error.jsp", ((HttpServletRequest)req), ((HttpServletResponse)resp)); // и передаст управление на error.jsp
        }
    }
    @Override
    public void destroy() {
    }
}
