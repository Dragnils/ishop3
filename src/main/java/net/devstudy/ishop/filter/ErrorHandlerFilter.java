package net.devstudy.ishop.filter;

import net.devstudy.ishop.util.RoutingUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "ErrorHandlerFilter")
public class ErrorHandlerFilter extends AbstractFilter {

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(req, resp);// будет передавать управление дальше
        } catch (Throwable th) { // если будет ошибка, то он ее залогирует
            String requestUrl = req.getRequestURI();
            LOGGER.error("Request " + requestUrl + " failed: " + th.getMessage(), th);
            RoutingUtils.forwardToPage("error.jsp", req,  resp); // и передаст управление на error.jsp
        }
    }
}
