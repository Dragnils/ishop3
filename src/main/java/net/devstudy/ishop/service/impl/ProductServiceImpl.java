package net.devstudy.ishop.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.devstudy.ishop.entity.Category;
import net.devstudy.ishop.entity.Producer;
import net.devstudy.ishop.entity.Product;
import net.devstudy.ishop.exception.InternalServerErrorException;
import net.devstudy.ishop.form.SearchForm;
import net.devstudy.ishop.jdbc.JDBCUtils;
import net.devstudy.ishop.jdbc.ResultSetHandler;
import net.devstudy.ishop.jdbc.ResultSetHandlerFactory;
import net.devstudy.ishop.jdbc.SearchQuery;
import net.devstudy.ishop.service.ProductService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class ProductServiceImpl implements ProductService { // реализация

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

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
            return JDBCUtils.select(c, "select * from producer order by name", producerListResultSetHandler);
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
        try(Connection c = dataSource.getConnection()){
            return JDBCUtils.select(c, "select count(p.*) from product p, category c where c.id=p.id_category and c.url=?", countResultSetHandler, categoryUrl);// countResultSetHandler - преобразует наш результат запроса в integer
        }catch (SQLException e){
            throw new InternalServerErrorException("Can't execute sql query: " + e.getMessage(), e);// создали свой класс исключений для того чтобы оно пробрасывалось в наш фильтр ErrorHandlerFilter и логгировалось
        }
    }

    /*@Override
    public List<Product> listProductsBySearchForm(SearchForm searchForm, int page, int limit) {
        try(Connection c = dataSource.getConnection()){
            // запрос на получение продуктов
            int offset = (page - 1) * limit;                          // as category и as producer чтобы совпадало с названиями колонок в обработчике строк(handle) класса ResultSetHandlerFactory
            return JDBCUtils.select(c, "select p.*, c.name as category, pr.name as producer from product p, producer pr, category c "
                    + "where (p.name ilike ? or p.description ilike ?) and c.id=p.id_category and pr.id=p.id_producer limit ? offset ?",
                    productsResultSetHandler, "%"+searchForm.getQuery()+"%", "%"+searchForm.getQuery()+"%", limit, offset);// productsResultSetHandler - преобразует наш результат запроса в коллекцию listAllProducts
        }catch (SQLException e){
            throw new InternalServerErrorException("Can't execute sql query: " + e.getMessage(), e);// создали свой класс исключений для того чтобы оно пробрасывалось в наш фильтр ErrorHandlerFilter и логгировалось
        }
    }*/

    @Override
    public List<Product> listProductsBySearchForm(SearchForm form, int page, int limit) {
        try (Connection c = dataSource.getConnection()) {
            int offset = (page - 1) * limit;
            SearchQuery sq = buildSearchQuery("p.*, c.name as category, pr.name as producer", form);// строится наш SearchQuery
            sq.getSql().append(" order by p.id limit ? offset ?");// если это есть то
            sq.getParams().add(limit);// добавляются эти параметры
            sq.getParams().add(offset);// добавляются эти параметры
            LOGGER.debug("search query={} with params={}", sq.getSql(), sq.getParams()); //для того чтобы узнать какой запрос генерируется
            return JDBCUtils.select(c, sq.getSql().toString(), productsResultSetHandler, sq.getParams().toArray()); // и выполняется запрос
        } catch (SQLException e) {
            throw new InternalServerErrorException("Can't execute SQL request: " + e.getMessage(), e);
        }
    }

    protected SearchQuery buildSearchQuery(String selectFields, SearchForm form) {
        List<Object> params = new ArrayList<>(); // создается коллекция параметров
        StringBuilder sql = new StringBuilder("select ");
        //добавляются поля
        sql.append(selectFields).append(" from product p, category c, producer pr where pr.id=p.id_producer and c.id=p.id_category and (p.name ilike ? or p.description ilike ?)");//извлекаем данные из таблиц
        params.add("%" + form.getQuery() + "%");//добавляем в парамтры обязательно сравнение по Query
        params.add("%" + form.getQuery() + "%");
        JDBCUtils.populateSqlAndParams(sql, params, form.getCategory(), "c.id = ?"); //считываем из формы категории getCategory()
        JDBCUtils.populateSqlAndParams(sql, params, form.getProducer(), "pr.id = ?");//считываем из формы producer getProducer()
        return new SearchQuery(sql, params);// получаем наш SearchQuery
    }

    @Override
    public int countProductsBySearchForm(SearchForm searchForm) {
        try(Connection c = dataSource.getConnection()){

            SearchQuery sq = buildSearchQuery("count(*)", searchForm);
            LOGGER.debug("search query={} with params={}", sq.getSql(), sq.getParams());//для того чтобы узнать какой запрос генерируется
            return JDBCUtils.select(c, sq.getSql().toString(), countResultSetHandler, sq.getParams().toArray());  // запрос на получение продуктов

            // запрос на получение продуктов
           /* return JDBCUtils.select(c, "select count(*) from product p, producer pr, category c "
                            + "where (p.name ilike ? or p.description ilike ?) and c.id=p.id_category and pr.id=p.id_producer",
                    countResultSetHandler, "%"+searchForm.getQuery()+"%", "%"+searchForm.getQuery()+"%"); */ // countResultSetHandler -  преобразует наш результат запроса в integer

        }catch (SQLException e){
            throw new InternalServerErrorException("Can't execute sql query: " + e.getMessage(), e);// создали свой класс исключений для того чтобы оно пробрасывалось в наш фильтр ErrorHandlerFilter и логгировалось
        }
    }
}
