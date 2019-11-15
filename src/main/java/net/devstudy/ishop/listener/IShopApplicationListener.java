package net.devstudy.ishop.listener;

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
