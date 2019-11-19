package net.devstudy.ishop.listener;

import net.devstudy.ishop.Constants;
import net.devstudy.ishop.service.impl.ServiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


//Доступ из слушателя приложения к  бизнес сервисам

@WebListener
public class IShopApplicationListener implements ServletContextListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(IShopApplicationListener.class);// логгирует о том что проект запустился или не запустился
    private ServiceManager serviceManager; // создаем единожды (Singletone)

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            serviceManager = ServiceManager.getInstance(sce.getServletContext());
            sce.getServletContext().setAttribute(Constants.CATEGORY_LIST, serviceManager.getProductService().listAllCategories());// для отображения наших категорий на странице при нажатии на них и сохраняеим их в CATEGORY_LIST
            sce.getServletContext().setAttribute(Constants.PRODUCER_LIST, serviceManager.getProductService().listAllProducers());// для отображения наших Producers на странице при нажатии на них и сохраняеим их в PRODUCER_LIST
        }catch (RuntimeException e){
            LOGGER.error("Web application 'ishop' init failed: " + e.getMessage(), e);
            throw e;
        }
        LOGGER.info("Web application 'ishop' initialized");

    }
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        serviceManager.close();
        LOGGER.info("Web application 'ishop' destroyed");
    }
}
