package net.devstudy.ishop.util;

import net.devstudy.Constants;
import net.devstudy.ishop.model.ShoppingCart;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SessionUtils {
    public static ShoppingCart getCurrentShoppingCart(HttpServletRequest req) { // можем получить корзину пользователя, который залогинин в текущей сессии
        ShoppingCart shoppingCart = (ShoppingCart) req.getSession().getAttribute(Constants.CURRENT_SHOPPING_CART);
        if (shoppingCart == null) {
            shoppingCart = new ShoppingCart();
            setCurrentShoppingCart(req, shoppingCart);
        }
        return shoppingCart;
    }
    public static boolean isCurrentShoppingCartCreated(HttpServletRequest req) { // проверяем существует ли корзина или нет
        return req.getSession().getAttribute(Constants.CURRENT_SHOPPING_CART) != null;
    }
    public static void setCurrentShoppingCart(HttpServletRequest req, ShoppingCart shoppingCart) { // можем установить корзину
        req.getSession().setAttribute(Constants.CURRENT_SHOPPING_CART, shoppingCart);
    }
    public static void clearCurrentShoppingCart(HttpServletRequest req, HttpServletResponse resp) { // можем очисть корзину из сессии
        req.getSession().removeAttribute(Constants.CURRENT_SHOPPING_CART); // удаляем из сессии
        WebUtils.setCookie(Constants.Cookie.SHOPPING_CART.getName(), null, 0, resp); // если куки age ="0" то куки удаляется
    }
    public static Cookie findShoppingCartCookie(HttpServletRequest req){ // находим куки для текущего запроса который отвечает за состояние корзины
        return WebUtils.findCookie(req, Constants.Cookie.SHOPPING_CART.getName());
    }
    public static void updateCurrentShoppingCartCookie(String cookieValue, HttpServletResponse resp) { // сохранить состояние состояние корзины
        WebUtils.setCookie(Constants.Cookie.SHOPPING_CART.getName(), cookieValue,
                Constants.Cookie.SHOPPING_CART.getTtl(), resp);
    }
    private SessionUtils() {
    }
}
