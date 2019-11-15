package net.devstudy.ishop.filter;

/* Написать фильтр, который десериализует состояние корзины, если корзина отсутствует, а cookie
присутствуют. Выполнить десериализацию для новой сессии только один раз! */

import net.devstudy.ishop.model.ShoppingCart;
import net.devstudy.ishop.util.SessionUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class AutoRestoreShoppingCartFilter implements Filter {
    //чтобы выполнянлось проверка только один раз, будем использовать подход связанный со хранением атрибутов сессии
    private static final String SHOPPING_CARD_DESERIALIZATION_DONE = "SHOPPING_CARD_DESERIALIZATION_DONE";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request; // восстановление объектов HttpServletRequest
        HttpServletResponse resp = (HttpServletResponse) response; // восстановление объектов HttpServletResponse
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

    //десерилизация
    protected ShoppingCart shoppingCartFromString(String cookieValue) {
        ShoppingCart shoppingCart = new ShoppingCart();
        String[] items = cookieValue.split("\\|");
        for (String item : items) {
            String data[] = item.split("-");
            try {
                int idProduct = Integer.parseInt(data[0]);
                int count = Integer.parseInt(data[1]);
                shoppingCart.addProduct(idProduct, count);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return shoppingCart;
    }

    @Override
    public void destroy() {

    }
}

