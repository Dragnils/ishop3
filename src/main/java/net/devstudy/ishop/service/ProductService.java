package net.devstudy.ishop.service;

import net.devstudy.ishop.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> listAllProducts(int page, int limit);

}
