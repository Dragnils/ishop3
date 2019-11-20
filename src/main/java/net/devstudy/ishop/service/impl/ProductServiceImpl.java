package net.devstudy.ishop.service.impl;

import net.devstudy.ishop.entity.Category;
import net.devstudy.ishop.entity.Producer;
import net.devstudy.ishop.entity.Product;
import net.devstudy.ishop.exception.InternalServerErrorException;
import net.devstudy.ishop.jdbc.JDBCUtils;
import net.devstudy.ishop.jdbc.ResultSetHandler;
import net.devstudy.ishop.jdbc.ResultSetHandlerFactory;
import net.devstudy.ishop.service.ProductService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

class ProductServiceImpl implements ProductService { // реализация

    private static final ResultSetHandler<List<Product>> productsResultSetHandler =
            ResultSetHandlerFactory.getListResultSetHandler(ResultSetHandlerFactory.PRODUCT_RESULT_SET_HANDLER);// каждую строчку обрабатываем PRODUCT_RESULT_SET_HANDLER
    private final ResultSetHandler<List<Category>> categoryListResultSetHandler =
            ResultSetHandlerFactory.getListResultSetHandler(ResultSetHandlerFactory.CATEGORY_RESULT_SET_HANDLER);
    private final ResultSetHandler<List<Producer>> producerListResultSetHandler =
            ResultSetHandlerFactory.getListResultSetHandler(ResultSetHandlerFactory.PRODUCER_RESULT_SET_HANDLER);

    private final ResultSetHandler<Integer> countResultSetHandler = ResultSetHandlerFactory.getCountResultSetHandler();


    private final DataSource dataSource;// для выполнени запросов к БД нам нужен connection

    ProductServiceImpl(DataSource dataSource) {
        super();
        this.dataSource = dataSource;
    }

    @Override
    public List<Product> listAllProducts(int page, int limit) {
        try(Connection c = dataSource.getConnection()){
            // запрос на получение продуктов
            int offset = (page - 1) * limit;                          // as category и as producer чтобы совпадало с названиями колонок в обработчике строк(handle) класса ResultSetHandlerFactory
            return JDBCUtils.select(c, "select p.*, c.name as category, pr.name as producer from product p, producer pr, category c "
                    + "where c.id=p.id_category and pr.id=p.id_producer limit ? offset ?", productsResultSetHandler, limit, offset);// productsResultSetHandler - преобразует наш результат запроса в коллекцию listAllProducts
        }catch (SQLException e){
            throw new InternalServerErrorException("Can't execute sql query: " + e.getMessage(), e);// создали свой класс исключений для того чтобы оно пробрасывалось в наш фильтр ErrorHandlerFilter и логгировалось
        }
    }

    @Override
    public List<Product> listProductsByCategory(String categoryUrl, int page, int limit) {
        try (Connection c = dataSource.getConnection()) {
            int offset = (page - 1) * limit;
            return JDBCUtils.select(c,
                    "select p.*, c.name as category, pr.name as producer from product p, category c, producer pr where c.url=? and pr.id=p.id_producer and c.id=p.id_category order by p.id limit ? offset ?",
                    productsResultSetHandler, categoryUrl, limit, offset);
        } catch (SQLException e) {
            throw new InternalServerErrorException("Can't execute sql query: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Category> listAllCategories() {
        try (Connection c = dataSource.getConnection()) {
            return JDBCUtils.select(c, "select * from category order by id", categoryListResultSetHandler);
        } catch (SQLException e) {
            throw new InternalServerErrorException("Can't execute sql query: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Producer> listAllProducers() {
        try (Connection c = dataSource.getConnection()) {
            return JDBCUtils.select(c, "select * from producer order by id", producerListResultSetHandler);
        } catch (SQLException e) {
            throw new InternalServerErrorException("Can't execute sql query: " + e.getMessage(), e);
        }
    }

    @Override
    public int countAllProducts() {
        try(Connection c = dataSource.getConnection()){
            return JDBCUtils.select(c, "select count(*) from product", countResultSetHandler);// countResultSetHandler - преобразует наш результат запроса в integer
        }catch (SQLException e){
            throw new InternalServerErrorException("Can't execute sql query: " + e.getMessage(), e);// создали свой класс исключений для того чтобы оно пробрасывалось в наш фильтр ErrorHandlerFilter и логгировалось
        }
    }

    @Override
    public int countProductsByCategory(String categoryUrl) {
        return 0;
    }
}
