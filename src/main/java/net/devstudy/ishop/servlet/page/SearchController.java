package net.devstudy.ishop.servlet.page;

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

@WebServlet("/search")
public class SearchController extends AbstractController {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SearchForm searchForm = createSearchForm(req);// наш поисковой запрос

        List<Product> products = getProductService().listProductsBySearchForm(searchForm, 1, Constants.MAX_PRODUCTS_PER_HTML_PAGE); //запрашиваем все продукты которые удовлетворят нашему searchForm
        req.setAttribute("products", products);// сохраняем в products
        int totalCount = getProductService().countProductsBySearchForm(searchForm);// для отображения кнопки
        req.setAttribute("pageCount", getPageCount(totalCount, Constants.MAX_PRODUCTS_PER_HTML_PAGE));
        req.setAttribute("productCount", totalCount);// устанавливает какое-то кол-ыо найденных результатов
        req.setAttribute("searchForm", searchForm);// для того чтобы в поисковике сохранялось наши прошлые поиски m
        RoutingUtils.forwardToPage("search-result.jsp", req, resp);// переходит на страницу
    }
}
