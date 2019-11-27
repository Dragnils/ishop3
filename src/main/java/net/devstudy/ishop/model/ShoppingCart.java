package net.devstudy.ishop.model;

import net.devstudy.ishop.Constants;
import net.devstudy.ishop.entity.Product;
import net.devstudy.ishop.exception.ValidationException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ShoppingCart implements Serializable {
    private Map<Integer, ShopingCartItem> products = new HashMap<>();
    private int totalCount = 0;
    private BigDecimal totalCost = BigDecimal.ZERO;

    public void addProduct(Product product, int count) { // добавление продуктов
        validateShoppingCartSize(product.getId()); // валидируем размер корзины, можно ли добавить новый продукт
        ShopingCartItem shoppingCartItem = products.get(product.getId()); // ищем  ShopingCartItem по idProduct
        if (shoppingCartItem == null) {
            validateProductCount(count); // проверяем общее количество, чтобы оно не было больше 10, если все ок, то идем дальше
            shoppingCartItem = new ShopingCartItem(product, count); // если все нормально, то создаем объект shoppingCartItem
            products.put(product.getId(), shoppingCartItem); // добавляем в Map
        } else {
            validateProductCount(count + shoppingCartItem.getCount()); // проверяем общее количество продуктов, чтобы оно не было больше 10,
            shoppingCartItem.setCount(shoppingCartItem.getCount() + count);//если корзина не пуста, то прибавляем к сущиствещим вещам, новые
        }
        refreshStatistics(); // обновляем статистику общее количество
    }

    public void removeProduct(Integer idProduct, int count) {
        ShopingCartItem shoppingCartItem = products.get(idProduct);
        if (shoppingCartItem != null) {
            if (shoppingCartItem.getCount() > count) {
                shoppingCartItem.setCount(shoppingCartItem.getCount() - count);
            } else {
                products.remove(idProduct);
            }
            refreshStatistics(); // обновляем статистику общее количество
        }
    }

    public Collection<ShopingCartItem> getItems() {
        return products.values();
    }

    public int getTotalCount() {
        return totalCount;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    private void validateProductCount(int count)  {
        if(count > Constants.MAX_PRODUCT_COUNT_PER_SHOPPING_CART){
            throw new ValidationException("Limit for product count reached: count="+count);
        }
    }

    private void validateShoppingCartSize(int idProduct) { // валидируем размер корзины, чтобы он не привышал
        if(products.size() > Constants.MAX_PRODUCTS_PER_SHOPPING_CART ||
                (products.size() == Constants.MAX_PRODUCTS_PER_SHOPPING_CART && !products.containsKey(idProduct))) {
            throw new ValidationException("Limit for ShoppingCart size reached: size="+products.size());
        }
    }

    private void refreshStatistics() {
        totalCount = 0;
        totalCost = BigDecimal.ZERO;
        for (ShopingCartItem shoppingCartItem : getItems()) {
            totalCount += shoppingCartItem.getCount();
            totalCost = totalCost.add(shoppingCartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(shoppingCartItem.getCount())));// уиножение цены на количество товаров
        }
    }

    @Override
    public String toString() {
        return String.format("ShoppingCart [products=%s, totalCount=%s, totalCost=%s]", products, totalCount, totalCost);
    }

   /* public String getView(){
        StringBuilder r = new StringBuilder();
        for(ShopingCartItem shopingCartItem: getItems()){
            r.append(shopingCartItem.getIdProduct()).append("-&gt;").append(shopingCartItem.getCount()).append("<br>"); // "-&gt;" для указания знака > на HTML Page
        }
        return r.toString();
    }*/
}

