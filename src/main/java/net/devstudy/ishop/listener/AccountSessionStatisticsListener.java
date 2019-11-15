package net.devstudy.ishop.listener;

/* Создать AccountSessionStatisticsListener слушатель, который в
момент уничтожения сессии залогирует историю посещения
пользователем сайта в консоль
 Проверить через current-cart?cmd=add, cmd=add, cmd=clear, cmd=invalidate
 */


import net.devstudy.Constants;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.List;

@WebListener
@SuppressWarnings("unchecked")
public class AccountSessionStatisticsListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) { // во время уничтожения сессии
        List<String> actions = (List<String>) se.getSession().getAttribute(Constants.ACCOUNT_ACTIONS_HISTORY); // берет из текущей сессии коллецию действий пользователя
        if (actions != null) { // если коллекция действий существует
            logCurrentActionHistory(se.getSession().getId(), actions);// логируем историю действий
        }
    }

    private void logCurrentActionHistory(String sessionId, List<String> actions) {
        System.out.println(sessionId + " ->\n\t" + String.join("\n\t", actions) + "\nAccountSessionStatisticsListener");
    }
}

