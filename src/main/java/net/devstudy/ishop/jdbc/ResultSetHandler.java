package net.devstudy.ishop.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetHandler<T> { // определяет правило преобразования ResultSet в соответствующий объект

    T handle(ResultSet rs) throws SQLException;
}
