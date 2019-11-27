package net.devstudy.ishop.jdbc;

import net.devstudy.ishop.entity.Category;
import net.devstudy.ishop.entity.Producer;
import net.devstudy.ishop.entity.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class ResultSetHandlerFactory {

    public final static ResultSetHandler<Product> PRODUCT_RESULT_SET_HANDLER = new ResultSetHandler<Product>() { // анонимный класс который реализует  интерфейс ResultSetHandler<Product>()
        @Override
        public Product handle(ResultSet rs) throws SQLException {// определение правила формирования продуктов из ResultSetHandler, обработчик строк
            Product p = new Product();
            p.setCategory(rs.getString("category")); // устанавливаем категорию
            p.setDescription(rs.getString("description"));
            p.setId(rs.getInt("id"));
            p.setImageLink(rs.getString("image_link"));
            p.setName(rs.getString("name"));
            p.setPrice(rs.getBigDecimal("price"));
            p.setProducer(rs.getString("producer"));
            return p;
        }
    };


    public final static ResultSetHandler<Category> CATEGORY_RESULT_SET_HANDLER = new ResultSetHandler<Category>() {
        @Override
        public Category handle(ResultSet rs) throws SQLException {//перобразование из текущей строки ResultSet в объект Category
            Category c = new Category();
            c.setId(rs.getInt("id"));
            c.setName(rs.getString("name"));
            c.setUrl(rs.getString("url"));
            c.setProductCount(rs.getInt("produc_count"));
            return c;
        }
    };

    public final static ResultSetHandler<Producer> PRODUCER_RESULT_SET_HANDLER = new ResultSetHandler<Producer>() {
        @Override
        public Producer handle(ResultSet rs) throws SQLException { //перобразование из текущей строки ResultSet в объект Producer
            Producer p = new Producer();
            p.setId(rs.getInt("id"));
            p.setName(rs.getString("name"));
            p.setProductCount(rs.getInt("produc_count"));
            return p;
        }
    };

    public final static ResultSetHandler<Integer> getCountResultSetHandler() {/* Integer. передаем на countAllProducts класса ProductServiceImpl*/
        return new ResultSetHandler<Integer>() {
            @Override
            public Integer handle(ResultSet rs) throws SQLException {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    return 0;
                }
            }
        };
    }



    public final static <T> ResultSetHandler<T> getSingleResultSetHandler(final ResultSetHandler<T> oneRowResultSetHandler) { // запрос для одного продукта
        return new ResultSetHandler<T>() {
            @Override
            public T handle(ResultSet rs) throws SQLException {
                if (rs.next()) { // если у нас по текущему select-у существует какие-то данные то мы
                    return oneRowResultSetHandler.handle(rs);// обрабатываем эти данные и возвращаем единичный вариянт
                } else {// если select запрос не вернул никаких данных, то мы возвращаем null
                    return null;
                }
            }
        };
    }

    public final static <T> ResultSetHandler<List<T>> getListResultSetHandler(final ResultSetHandler<T> oneRowResultSetHandler) {//запрос для коллекции продуктов
        return new ResultSetHandler<List<T>>() {
            @Override
            public List<T> handle(ResultSet rs) throws SQLException {
                List<T> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(oneRowResultSetHandler.handle(rs));// добавляем в каждую коллекцию обработчик каждой строчки, и обработчик handle преобразует эту строчку к элементу коллекции list наших продуктов
                }
                return list;// если не найдется не один продукт, то нам вернется пустая коллекция
            }
        };
    }

    private ResultSetHandlerFactory() {
    }
}
