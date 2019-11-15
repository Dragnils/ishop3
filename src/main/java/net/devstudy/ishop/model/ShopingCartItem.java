package net.devstudy.ishop.model;

import java.io.Serializable;

public class ShopingCartItem implements Serializable {
    private int idProduct;
    private int count;

    public ShopingCartItem(){
        super();
    }

    public ShopingCartItem(int idProduct, int count) {
        super();
        this.idProduct = idProduct;
        this.count = count;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString(){
        return "ShoppingCartItem [idProduct=" + idProduct + ", count=" + count + "]";
    }
}
