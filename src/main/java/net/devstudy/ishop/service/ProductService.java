package net.devstudy.ishop.service;

import net.devstudy.ishop.entity.Category;
import net.devstudy.ishop.entity.Producer;
import net.devstudy.ishop.entity.Product;

import java.util.List;

public interface ProductService {

    List<Product> listAllProducts(int page, int limit);

    List<Product> listProductsByCategory(String categoryUrl, int page, int limit);

    List<Category> listAllCategory();
    List<Producer> listAllProducer();

}
