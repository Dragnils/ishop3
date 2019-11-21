package net.devstudy.ishop.servlet.ajax;

import net.devstudy.ishop.Constants;
import net.devstudy.ishop.entity.Product;
import net.devstudy.ishop.form.SearchForm;
import net.devstudy.ishop.servlet.AbstractController;
import net.devstudy.ishop.util.RoutingUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/ajax/html/more/search")
public class SearchResultsMoreController extends AbstractController {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SearchForm searchForm = createSearchForm(req);// наш поисковой запрос

        List<Product> products = getProductService().listProductsBySearchForm(searchForm, getPage(req), Constants.MAX_PRODUCTS_PER_HTML_PAGE); //запрашиваем все продукты которые удовлетворят нашему searchForm
        req.setAttribute("products", products);// сохраняем в products

        RoutingUtils.forwardToFragment("product-list.jsp", req, resp);
    }
}
