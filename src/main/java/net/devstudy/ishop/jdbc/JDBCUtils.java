package net.devstudy.ishop.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public final class JDBCUtils {//выполняет select запрос

    public static <T> T select(Connection c, String sql, ResultSetHandler<T> resultSetHandler, Object... parameters) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            populatePreparedStatement(ps, parameters);
            ResultSet rs = ps.executeQuery();
            return resultSetHandler.handle(rs);
        }
    }

    public static void populateSqlAndParams(StringBuilder sql, List<Object> params, List<Integer> list, String expression) {// проходимся по коллекции параметров
        if (list != null && !list.isEmpty()) {
            sql.append(" and (");// добавляем в наш sql
            for (int i = 0; i < list.size(); i++) {
                sql.append(expression); // передаем впоросительный знак
                params.add(list.get(i));// и дописываем в параметры параметр который нужен  list
                if (i != list.size() - 1) {
                    sql.append(" or ");// конкатенируюся с помощью ключевого слова or
                }
            }
            sql.append(")");
        }
    }

    private static void populatePreparedStatement(PreparedStatement ps, Object... parameters) throws SQLException { // заполнение параметрами запроса
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                ps.setObject(i + 1, parameters[i]);
            }
        }
    }

    private JDBCUtils() {
    }
}
