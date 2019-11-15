package net.devstudy.ishop.servlet;

import net.devstudy.ishop.service.BusinessService;
import net.devstudy.ishop.service.ServiceManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public abstract class AbstractController extends HttpServlet {

    //Доступ из сервлета к  бизнес сервисам
    private BusinessService businessService;

    @Override
    public final void init() throws ServletException {
       businessService = ServiceManager.getInstance(getServletContext()).getBusinessService(); // ссылка на один и тот же объект ServiceManager созданного в IShopApplicationListener
    }

    public final BusinessService getBusinessService() {
        return businessService;
    }
}
