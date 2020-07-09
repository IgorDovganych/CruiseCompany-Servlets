package dao.jdbc;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {

    private static javax.sql.DataSource dataSourse = getDataSourse();

    private ConnectionPool() {
    }

    private static javax.sql.DataSource getDataSourse() {
        javax.sql.DataSource dataSourse = null;
        try {
            dataSourse = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/CruiseCompany");
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return dataSourse;
    }

    public static Connection getConnection() throws SQLException {
        return dataSourse.getConnection();
    }

}
