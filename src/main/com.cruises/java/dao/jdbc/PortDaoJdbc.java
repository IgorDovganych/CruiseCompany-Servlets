package dao.jdbc;

import dao.PortDao;
import model.Port;
import model.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class PortDaoJdbc implements PortDao {

    public static PortDaoJdbc portDaoJdbc = new PortDaoJdbc();
    private static final String GET_ALL_PORTS = "Select * from ports";
    public static PortDaoJdbc getPortDaoJdbc() {
        return portDaoJdbc;
    }

    /**
     * gets all ports from database
     * @return list of ports
     */
    @Override
    public List<Port> getAllPorts() {
        try(Connection connection = ConnectionPool.getConnection();
            Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(GET_ALL_PORTS);
            List<Port> ports = new ArrayList<>();
            while (resultSet.next()) {
                Port port = new Port(resultSet.getInt("id"),
                        resultSet.getString("name"));
                ports.add(port);
            }
            return ports;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
