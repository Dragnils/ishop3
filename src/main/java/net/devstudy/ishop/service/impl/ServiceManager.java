package net.devstudy.ishop.service.impl;

import net.devstudy.ishop.service.OrderService;
import net.devstudy.ishop.service.ProductService;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

//Доступ к бизнес сервисам

public class ServiceManager {
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

    }

    private final Properties applicationProperties = new Properties();// сохраняем в файл resources/application.properties
    private final ProductService productService; // создается только единожды в ServiceManager
    private final OrderService orderService;
    private ServiceManager(ServletContext context) {// будут созданы бизнес сервисы
            loadApplicationProperties();
            productService = new ProductServiceImpl();
            orderService = new OrderServiceImpl();
    }

    private void loadApplicationProperties(){
        try(InputStream in = ServiceManager.class.getClassLoader().getResourceAsStream("application.properties")){
            applicationProperties.load(in);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}