package net.devstudy;

public final class Constants {
    public static final String CURRENT_SHOPPING_CART = "CURRENT_SHOPPING_CART";
    public static final int MAX_PRODUCT_COUNT_PER_SHOPPING_CART = 10; // максимальное количество одного и того же продукта на одну корзину
    public  static final int MAX_PRODUCTS_PER_SHOPPING_CART = 20; // максимальное количество продуктов в корзине

    public static final String ACCOUNT_ACTIONS_HISTORY = "ACCOUNT_ACTIONS_HISTORY";

    public enum Cookie {
        SHOPPING_CART("iSCC", 60 * 60 * 24 * 365);

        private final String name;
        private final int ttl;

        Cookie(String name, int ttl) {
            //1 year ttl
            this.name = name;
            this.ttl = ttl;
        }

        public String getName() {
            return name;
        }

        public int getTtl() {
            return ttl;
        }
    }
}
