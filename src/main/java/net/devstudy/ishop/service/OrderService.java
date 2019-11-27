package net.devstudy.ishop.service;

import net.devstudy.ishop.form.ProductForm;
import net.devstudy.ishop.model.ShoppingCart;

public interface OrderService {


    void addProductToShoppingCart(ProductForm productForm, ShoppingCart shoppingCart);// метод добавления продуктов в корзину
}
