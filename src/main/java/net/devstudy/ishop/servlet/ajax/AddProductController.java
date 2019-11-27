package net.devstudy.ishop.servlet.ajax;

import net.devstudy.ishop.form.ProductForm;
import net.devstudy.ishop.model.ShoppingCart;
import net.devstudy.ishop.servlet.AbstractController;
import net.devstudy.ishop.util.RoutingUtils;
import net.devstudy.ishop.util.SessionUtils;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/ajax/json/product/add")
public class AddProductController extends AbstractController {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {// придобавлении нового продукта в корзину
        ProductForm productForm = createProductForm(req);// извлекаем данные
        ShoppingCart shoppingCart = SessionUtils.getCurrentShoppingCart(req);// получаем текущую корзину
        getOrderService().addProductToShoppingCart(productForm, shoppingCart);// добавляем продукт в корзину


        JSONObject r = new JSONObject();
        r.put("totalCount", shoppingCart.getTotalCount());// обновляем текущее состояние TotalCount
        r.put("totalCost", shoppingCart.getTotalCost());// обновляем текущее состояние TotalCost
        RoutingUtils.sendJSON(r, req, resp);// отправлем его respons'ом обратно текущему запросу

    }
}
