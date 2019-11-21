package net.devstudy.ishop.servlet.page;

import net.devstudy.ishop.Constants;
import net.devstudy.ishop.entity.Product;
import net.devstudy.ishop.servlet.AbstractController;
import net.devstudy.ishop.util.RoutingUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/products/*")// вместо * указывается та категория в качестве параметра по которой будет выполняться поиск и фильтрация данных из БД
public class ProductsByCategoryController  extends AbstractController {// отображения продуктов по категориям
    private static final int SUBSTRING_INDEX =  "/products".length();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String categoryUrl = req.getRequestURI().substring(SUBSTRING_INDEX);// чтобы получить текущую категорию
        List<Product> products = getProductService().listProductsByCategory(categoryUrl,1 , Constants.MAX_PRODUCTS_PER_HTML_PAGE); //get Products from database, мы должны получить из БД список всех категорий
        req.setAttribute("products", products);

        int totalCount = getProductService().countProductsByCategory(categoryUrl);// возвращает кол-во продуктов по категориям
        req.setAttribute("pageCount", getPageCount(totalCount, Constants.MAX_PRODUCTS_PER_HTML_PAGE));

        req.setAttribute("selectedCategoryUrl", categoryUrl);// сохраняем текущую выбранную категорию ? для того чотбы подсвечивала синим цветом выбранную категорию
        RoutingUtils.forwardToPage("products.jsp", req,resp);
    }
}
