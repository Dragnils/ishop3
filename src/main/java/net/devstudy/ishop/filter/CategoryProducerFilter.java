package net.devstudy.ishop.filter;

import net.devstudy.ishop.Constants;
import net.devstudy.ishop.service.ProductService;
import net.devstudy.ishop.service.impl.ServiceManager;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*Example !!!! This filter is not working, because @WebFilter annotation is absent  */

public class CategoryProducerFilter extends AbstractFilter {

    private ProductService productService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        productService = ServiceManager.getInstance(filterConfig.getServletContext()).getProductService();// получаем ссылку productService
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setAttribute(Constants.CATEGORY_LIST, productService.listAllCategories());
        request.setAttribute(Constants.PRODUCER_LIST, productService.listAllProducers());
        chain.doFilter(request, response);
    }
}
