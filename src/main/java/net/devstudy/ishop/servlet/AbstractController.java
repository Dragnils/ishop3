package net.devstudy.ishop.servlet;

import net.devstudy.ishop.service.OrderService;
import net.devstudy.ishop.service.ProductService;
import net.devstudy.ishop.service.impl.ServiceManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractController extends HttpServlet {

    //Доступ из сервлета к  бизнес сервисам
    private ProductService productService;
    private OrderService orderService;

    @Override
    public final void init() throws ServletException {
        productService = ServiceManager.getInstance(getServletContext()).getProductService(); // ссылка на один и тот же объект ServiceManager созданного в IShopApplicationListener
        orderService = ServiceManager.getInstance(getServletContext()).getOrderService();
    }

    public final ProductService getProductService() {
        return productService;
    }

    public final OrderService getOrderService() {
        return orderService;
    }

    public final int getPageCount(int totalCount, int itemsPerPage){// для преобразования общего количества страниц в зависимости от totalCount
        int result = totalCount / itemsPerPage;
        if(result * itemsPerPage!= totalCount){
            result++;
        }
        return result;
    }

    public final int getPage(HttpServletRequest req){ // считывает нашу page из app.js при нажатии кнопки Load more. и передаем в ajax обработчик AllProductsMoreController
                                                      // List<Product> products = getProductService().listAllProducts(getPage(req) , Constants.MAX_PRODUCTS_PER_HTML_PAGE);
        try {
            return Integer.parseInt(req.getParameter("page"));
        } catch (NumberFormatException e) {
            return 1;
        }

    }

}
