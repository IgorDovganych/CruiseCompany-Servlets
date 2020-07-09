package dao.jdbc;

import exception.DaoException;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionManager {
    private static final Logger LOGGER = Logger.getLogger(TransactionManager.class);

    public static void doInTransaction(InsideTransactionProcessor instance) {
        try (Connection connection = ConnectionPool.getConnection()){
            connection.setAutoCommit(false);
            try {
                instance.process(connection);
                connection.commit();
            } catch (DaoException e) {
                connection.rollback();
                LOGGER.warn("exception", e);
            }
        } catch (SQLException e) {
            LOGGER.warn("exception", e);
        }

    }

}
