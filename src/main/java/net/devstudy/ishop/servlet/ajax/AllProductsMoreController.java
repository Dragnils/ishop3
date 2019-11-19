package net.devstudy.ishop.servlet.ajax;

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

@WebServlet("/ajax/html/more/products")
public class AllProductsMoreController extends AbstractController { // обрабатывает поведение нажатия кнопки "Load more products" в конце страницв


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Product> products = getProductService().listAllProducts(2 , Constants.MAX_PRODUCTS_PER_HTML_PAGE); //get Products from database, мы должны получить из БД список всех продуктов с помощью класса Product
        req.setAttribute("products", products);// устанавливаем в атрибуты коллекцию продуктов которые нужно отобразить
        RoutingUtils.forwardToFragment("product-list.jsp", req, resp);
    }
}
