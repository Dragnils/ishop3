package net.devstudy.ishop.listener;

/* Создать AccountRequestStatisticsListener слушатель, который в
текущюю сессию будет добавлять информацию о каждом запросе
пользователя в формате:
METHOD URL?params, например:
GET /home
GET /search?name=Test&option1=true&category=1&category=2
POST /product/add?idProduct=1&count=3
POST /order */

import net.devstudy.Constants;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebListener
@SuppressWarnings("unchecked")
public class AccountRequestStatisticsListener implements ServletRequestListener {

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
    }


    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        HttpServletRequest req = ((HttpServletRequest)sre.getServletRequest());// создается объект HttpServletRequest
        List<String> actions = (List<String>) req.getSession().getAttribute(Constants.ACCOUNT_ACTIONS_HISTORY);// с текущей сесии запрашиваем коллекцию действий которые были сделаны, которые сохраняются в ACCOUNT_ACTIONS_HISTORY
        if(actions == null) {// если это первый запрос с текущей сессии
            actions = new ArrayList<>(); // создаем объект ArrayList и помещаем
            req.getSession().setAttribute(Constants.ACCOUNT_ACTIONS_HISTORY, actions); // и помещаем
        }
        actions.add(getCurrentAction(req));// в actions добавляем текущий actions
    }

    private String getCurrentAction(HttpServletRequest req) { // текущий actions
        StringBuilder sb = new StringBuilder(req.getMethod()).append(" ").append(req.getRequestURI());// с помощью StringBuilder записываем текущий метод, пробел, текущий RequestURI
        Map<String, String[]> map = req.getParameterMap(); // получаем параметр map
        if(map != null) { // проходимся по map
            boolean first = true;
            for(Map.Entry<String, String[]> entry : map.entrySet()) {
                if(first) {
                    sb.append('?');
                    first = false;
                } else {
                    sb.append('&');// с помощью
                }
                for(String value : entry.getValue()) {
                    sb.append(entry.getKey()).append('=').append(value).append('&');// дописываем в наш StringBuilder параметры с помощью "?" и "&"
                }
                sb.deleteCharAt(sb.length()-1);
            }
        }
        return sb.toString();// возвращаем StringBuilder toString
    }
}
