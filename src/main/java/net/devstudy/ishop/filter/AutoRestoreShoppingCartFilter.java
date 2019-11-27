package net.devstudy.ishop.filter;

/* Написать фильтр, который десериализует состояние корзины, если корзина отсутствует, а cookie
присутствуют. Выполнить десериализацию для новой сессии только один раз! */

import net.devstudy.ishop.form.ProductForm;
import net.devstudy.ishop.model.ShoppingCart;
import net.devstudy.ishop.service.OrderService;
import net.devstudy.ishop.service.impl.ServiceManager;
import net.devstudy.ishop.util.SessionUtils;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "AutoRestoreShoppingCartFilter")
public class AutoRestoreShoppingCartFilter extends AbstractFilter {
    //чтобы выполнянлось проверка только один раз, будем использовать подход связанный со хранением атрибутов сессии
    private static final String SHOPPING_CARD_DESERIALIZATION_DONE = "SHOPPING_CARD_DESERIALIZATION_DONE";

    private OrderService orderService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        orderService = ServiceManager.getInstance(filterConfig.getServletContext()).getOrderService();// получаем ссылку orderService
    }


    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {

        if(req.getSession().getAttribute(SHOPPING_CARD_DESERIALIZATION_DONE) == null){ // если нет в текущей сесии данной константы, значит для текущего пользователя текущего сеанса не проверялась восстановление shoppingCart корзины покупок из куки
            if(!SessionUtils.isCurrentShoppingCartCreated(req)){ // и тогда мы проверяем если текущая корзина не существет
                Cookie cookie = SessionUtils.findShoppingCartCookie(req); // ищем куки с сотоянием корзины с текущего запроса
                if(cookie !=null){ //если найдена куки
                    ShoppingCart shoppingCart = shoppingCartFromString(cookie.getValue());// восстанавливаем корзину
                    SessionUtils.setCurrentShoppingCart(req, shoppingCart); // устанавливаем эту корзину в сессию
                }
            }
            req.getSession().setAttribute(SHOPPING_CARD_DESERIALIZATION_DONE, Boolean.TRUE);
        }
        chain.doFilter(req, resp);
    }



    //серилизация
    /*protected String shoppingCartToString(ShoppingCart shoppingCart) {
        StringBuilder res = new StringBuilder();
        for (ShopingCartItem shoppingCartItem : shoppingCart.getItems()) {
            res.append(shoppingCartItem.getIdProduct()).append("-").append(shoppingCartItem.getCount()).append("|");
        }
        if (res.length() > 0) {
            res.deleteCharAt(res.length() - 1);
        }
        return res.toString();
    }*/

    //десерилизация
    protected ShoppingCart shoppingCartFromString(String cookieValue) {
        ShoppingCart shoppingCart = new ShoppingCart();
        String[] items = cookieValue.split("\\|");
        for (String item : items) {
            String data[] = item.split("-");
            try {
                int idProduct = Integer.parseInt(data[0]);
                int count = Integer.parseInt(data[1]);
                orderService.addProductToShoppingCart(new ProductForm(idProduct, count), shoppingCart);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return shoppingCart;
    }

    /* else {// иначе если в нашей сессии есть корзина
            ShoppingCart shoppingCart = SessionUtils.getCurrentShoppingCart(req); // мы получаем нашу корзину
            String cookieValue = shoppingCartToString(shoppingCart); // сериализуем в куки стринг
            SessionUtils.updateCurrentShoppingCartCookie(cookieValue, resp);// обновляем текущую куки, через классы SessionUtils затем WebUtils
        }
        */

}

