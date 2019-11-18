package net.devstudy.ishop.servlet.page;

import net.devstudy.ishop.servlet.AbstractController;
import net.devstudy.ishop.util.RoutingUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/products/*")// вместо * указывается та категория в качестве параметра по которой будет выполняться поиск и фильтрация данных из БД
public class ProductsByCategoryController  extends AbstractController {// отображения продуктов по категориям
    private static final int SUBSTRING_INDEX =  "/products".length();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String categoryUrl = req.getRequestURI().substring(SUBSTRING_INDEX);// чтобы получить текущую категорию
        RoutingUtils.forwardToPage("products.jsp", req,resp);
    }
}
