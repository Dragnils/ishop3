package net.devstudy.ishop.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class JDBCUtils {//выполняет select запрос

    public static <T> T select(Connection c, String sql, ResultSetHandler<T> resultSetHandler, Object... parameters) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            populatePreparedStatement(ps, parameters);
            ResultSet rs = ps.executeQuery();
            return resultSetHandler.handle(rs);
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
