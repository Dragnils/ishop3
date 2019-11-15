package net.devstudy.ishop.listener;

import net.devstudy.ishop.service.ServiceManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


//Доступ из слушателя приложения к  бизнес сервисам

@WebListener
public class IShopApplicationListener implements ServletContextListener {
    private ServiceManager serviceManager; // создаем единожды (Singletone)
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        serviceManager = ServiceManager.getInstance(sce.getServletContext());
        serviceManager.getBusinessService().doSomething();
    }
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        serviceManager.getBusinessService().doSomething();
        serviceManager.close();
    }
}
