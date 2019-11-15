package net.devstudy.ishop.filter;

import net.devstudy.ishop.service.BusinessService;
import net.devstudy.ishop.service.ServiceManager;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

//Доступ из фильтра к  бизнес сервисам

@WebFilter("/*")
public class VerificationFilter implements Filter {
    private BusinessService businessService;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        businessService = ServiceManager.getInstance(filterConfig.getServletContext()).getBusinessService(); // ссылка на один и тот же объект ServiceManager созданного в IShopApplicationListener
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        businessService.doSomething();
        chain.doFilter(request, response);
    }
    @Override
    public void destroy() {
    }
}
