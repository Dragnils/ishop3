package net.devstudy.ishop.util;

import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public final class RoutingUtils {
    public static void forwardToFragment(String jspFragment, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/JSP/fragment/" + jspFragment).forward(req, resp);
    }

    public static void forwardToPage(String jspPage, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("currentPage", "page/" + jspPage);// в атрибут currentPage указываем путь в page/" + jspPage
        req.getRequestDispatcher("/WEB-INF/JSP/page-template.jsp").forward(req, resp);// затем атрибут currentPage динамичеки вставляется с помощью Expression Language в page-template.jsp
    }

    public static void sendHTMLFragment(String text, HttpServletRequest req, HttpServletResponse resp) throws IOException{// для того чтобы с какого нибудь контроллера отправить HTMLFragment
        resp.setContentType("text/html");
        resp.getWriter().println(text);
        resp.getWriter().close();
    }

    public static void sendJSON(JSONObject json, HttpServletRequest req, HttpServletResponse resp) throws IOException{// для responce JSON щбъекта
        resp.setContentType("application/json");
        resp.getWriter().println(json.toString());
        resp.getWriter().close();
    }
    public static void redirect(String url, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect(url);
    }
}


