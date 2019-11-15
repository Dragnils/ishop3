package net.devstudy.ishop.service;

import javax.servlet.ServletContext;

//Доступ к бизнес сервисам

public class ServiceManager {
    public static ServiceManager getInstance(ServletContext context) { // чтобы получить доступ к ServiceManager мы используем шаблон проектирования Singletone
        ServiceManager instance = (ServiceManager) context.getAttribute("SERVICE_MANAGER");// запрос из сервлет контекста по атрибуту SERVICE_MANAGER объект ServiceManager
        if (instance == null) {
            instance = new ServiceManager(context);
            context.setAttribute("SERVICE_MANAGER", instance);// instance будем созранять в аттрибуте context
        }
        return instance;
    }
    public void close() {

    }
        private BusinessService businessService;
        public BusinessService getBusinessService(){
            return businessService;
        }

        private ServiceManager(ServletContext context) {// будут созданы бизнес сервисы
            //init services
        }
    }