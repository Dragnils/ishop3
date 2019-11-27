package net.devstudy.ishop.service.impl;

import net.devstudy.ishop.service.OrderService;
import net.devstudy.ishop.service.ProductService;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

//Доступ к бизнес сервисам

public class ServiceManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceManager.class); //

    public static ServiceManager getInstance(ServletContext context) { // чтобы получить доступ к ServiceManager мы используем шаблон проектирования Singletone
        ServiceManager instance = (ServiceManager) context.getAttribute("SERVICE_MANAGER");// запрос из сервлет контекста по атрибуту SERVICE_MANAGER объект ServiceManager
        if (instance == null) {
            instance = new ServiceManager(context);
            context.setAttribute("SERVICE_MANAGER", instance);// instance будем сохранять в аттрибуте context
        }
        return instance;
    }

    public ProductService getProductService() {
        return productService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public String getApplicationProperty(String key){
        return applicationProperties.getProperty(key);
    }

    public void close() {
        try {
            dataSource.close();
        } catch (SQLException e) {
            LOGGER.error("Close dataSource failed: " + e.getMessage(), e);
        }
    }

    private final Properties applicationProperties = new Properties();// сохраняем в файл resources/application.properties глобальные настройки проекта
    private final BasicDataSource dataSource;//--
    private final ProductService productService; // создается только единожды в ServiceManager
    private final OrderService orderService;
    private ServiceManager(ServletContext context) {// будут созданы бизнес сервисы
            loadApplicationProperties();
            dataSource = createDataSource();//-- должен быть всегда после  loadApplicationProperties();
            productService = new ProductServiceImpl(dataSource);//--
            orderService = new OrderServiceImpl(dataSource);
    }

    private BasicDataSource createDataSource(){
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDefaultAutoCommit(false);// чтобы все соединения которые будут получены из данного dataSource с AutoCommit false флагом
        dataSource.setRollbackOnReturn(true);// когда будет выполнятся возвпат конектиона в пул соединений то автоматически вызывается Rollback если мы не вызвали Commit
        dataSource.setDriverClassName(getApplicationProperty("db.driver"));// устанавливаем
        dataSource.setUrl(getApplicationProperty("db.url"));
        dataSource.setUsername(getApplicationProperty("db.username"));
        dataSource.setPassword(getApplicationProperty("db.password"));
        dataSource.setInitialSize(Integer.parseInt(getApplicationProperty("db.pool.initSize")));
        dataSource.setMaxTotal(Integer.parseInt(getApplicationProperty("db.pool.maxSize")));

        return dataSource;
    }

    private void loadApplicationProperties(){
        try(InputStream in = ServiceManager.class.getClassLoader().getResourceAsStream("application.properties")){
            applicationProperties.load(in);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}