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
import java.util.Collections;
import java.util.List;



@WebServlet("/products")
public class AllProductsController extends AbstractController {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {// наш контроллер отвечает за отображение всех продуктов
                           //Доступ из сервлета к  бизнес сервисам
        List<Product> products = getProductService().listAllProducts(1, Constants.MAX_PRODUCTS_PER_HTML_PAGE); //get Products from database, мы должны получить из БД список всех продуктов с помощью класса Product
        req.setAttribute("products", products);// устанавливаем в атрибуты коллекцию продуктов которые нужно отобразить
        int totalCount = getProductService().countAllProducts();// для отображения кнопки
        req.setAttribute("pageCount", getPageCount(totalCount, Constants.MAX_PRODUCTS_PER_HTML_PAGE));
        RoutingUtils.forwardToPage("products.jsp", req, resp);// передаем управление на страницу с помощью метода forwardToPage()
    }
}
