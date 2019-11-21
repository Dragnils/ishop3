package net.devstudy.ishop.service;

import net.devstudy.ishop.entity.Category;
import net.devstudy.ishop.entity.Producer;
import net.devstudy.ishop.entity.Product;
import net.devstudy.ishop.form.SearchForm;

import java.util.List;

public interface ProductService {

    List<Product> listAllProducts(int page, int limit);

    int countAllProducts();// возвращать количество продуктов, для того чтобы показывать кнопку "Load More" или нет


    List<Product> listProductsByCategory(String categoryUrl, int page, int limit);

    int countProductsByCategory(String categoryUrl);// будет возвращать количество продуктов по категориям

    List<Category> listAllCategories();
    List<Producer> listAllProducers();

    List<Product> listProductsBySearchForm(SearchForm searchForm, int page, int limit);

    int countProductsBySearchForm(SearchForm searchForm);


}
