package dao.jdbc;

import dao.ExcursionDao;
import exception.DaoException;
import model.Excursion;
import model.ExcursionsInPortContainer;
import model.User;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;

public class ExcursionDaoJdbc implements ExcursionDao {
    private static ExcursionDaoJdbc excursionDaoJdbc = new ExcursionDaoJdbc();
    private static final Logger LOGGER = Logger.getLogger(ExcursionDaoJdbc.class);
    final static String SELECT_EXCURSIONS_BY_PORT_NAME_0 = "SELECT * FROM excursions where port_id = (select id from ports where name='";

    final static String SELECT_EXCURSIONS_BY_PORT_NAME = "SELECT * FROM  ports\n" +
            "inner join  excursions ON port_id = ports.id\n" +
            "where ports.name='";

    private final static String INSERT_PURCHASED_EXCURSIONS = "insert into purchased_excursions (excursion_id, purchased_ticket_id) values ";

    private static final String SELECT_EXCURSIONS_BY_CRUISE_ID =
            "select excursions.id, excursions.name, ports.name, price, description, isActive " +
                    "from excursions inner join ports " +
                    "on excursions.port_id = ports.id where port_id in " +
                    "(select port_id from route_points where route_id = (select route_id from cruises where id = ?))";
    private static final String SELECT_ALL_EXCURSIONS_IN_ALL_PORTS = "SELECT * FROM ports\n" +
            "left join excursions on ports.id=excursions.port_id";
    private static final String DEACTIVATE_EXCURSION_BY_ID = "update excursions set isActive=0 where id=?";

    private static final String ACTIVATE_EXCURSION_BY_ID = "update excursions set isActive=1 where id=?";

    private static final String INSERT_EXCURSION = "INSERT INTO excursions(name, port_id, price, description, isActive) values(?,?,?,?, true)";

    public static ExcursionDaoJdbc getExcursionDaoJdbc() {
        return excursionDaoJdbc;
    }


    @Override
    public List<ExcursionsInPortContainer> getAllExcursionsInAllPorts() throws DaoException {
        try (Connection connection = ConnectionPool.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SELECT_ALL_EXCURSIONS_IN_ALL_PORTS);

            List<ExcursionsInPortContainer> excursionsInPortContainers = new ArrayList<>();
            while (resultSet.next()) {
                int portId = resultSet.getInt("ports.id");
                String portName = resultSet.getString("ports.name");

                List<Excursion> excursions = new ArrayList<>();
                int excursionId = resultSet.getInt("excursions.id");
                String excursionName = resultSet.getString("excursions.name");
                int price = resultSet.getInt("price");
                String description = resultSet.getString("description");
                boolean isActive = resultSet.getBoolean("isActive");
                Excursion excursion = new Excursion(excursionId, excursionName, portName, price, description, isActive);
                excursions.add(excursion);
                while (resultSet.next()) {
                    excursionId = resultSet.getInt("excursions.id");
                    if (excursionId != 0 && resultSet.getInt("ports.id") == portId) {
                        excursions.add(new Excursion(
                                excursionId,
                                resultSet.getString("excursions.name"),
                                resultSet.getString("ports.name"),
                                resultSet.getInt("price"),
                                resultSet.getString("description"),
                                resultSet.getBoolean("isActive")
                        ));
                    } else {
                        break;
                    }
                }
                resultSet.previous();
                excursionsInPortContainers.add(new ExcursionsInPortContainer(portId, portName, excursions));
            }
            LOGGER.info("all excursions : " + excursionsInPortContainers.toString());
            return excursionsInPortContainers;
        } catch (SQLException e) {
            LOGGER.warn(e);
            throw new DaoException("Error while getting excursions", e);
        }
    }

    @Override
    public List<Excursion> getExcursionsByCruiseId(int cruiseId) throws DaoException {

        try (Connection connection = ConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_EXCURSIONS_BY_CRUISE_ID);
            statement.setInt(1, cruiseId);
            ResultSet resultSet = statement.executeQuery();
            List<Excursion> excursions = new ArrayList<>();
            while (resultSet.next()) {
                Excursion excursion = new Excursion(resultSet.getInt("excursions.id"),
                        resultSet.getString("excursions.name"),
                        resultSet.getString("ports.name"),
                        resultSet.getInt("price"),
                        resultSet.getString("description"),
                        resultSet.getBoolean("isActive")
                );
                excursions.add(excursion);
            }
            return excursions;
        } catch (SQLException e) {
            LOGGER.warn(e);
            throw new DaoException("Error while getting excursions by cruiseId", e);
        }
    }

    @Override
    public void purchaseExcursions(Connection connection, List<Integer> excursionIds, int purchasedTicketId) throws DaoException {
        if (excursionIds.isEmpty()) {
            return;
        }
        String query = INSERT_PURCHASED_EXCURSIONS +
                excursionIds.stream().map(excursionId -> "(" + excursionId + "," + purchasedTicketId + ")").collect(joining(","));
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            LOGGER.warn(e);
            throw new DaoException("Error occured while inserting purchased excursions", e);
        }
    }

    @Override
    public void deactivateExcursion(int excursionId) throws DaoException {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(DEACTIVATE_EXCURSION_BY_ID)) {
            LOGGER.info("deactivateExcursion method started");
            statement.setInt(1, excursionId);
            statement.executeUpdate();
            LOGGER.info("Excursion with id " + excursionId + " was deactivated");
        } catch (SQLException e) {
            LOGGER.warn(e);
            throw new DaoException("An exception occurred while deactivating excursion", e);
        }
    }

    @Override
    public void activateExcursion(int excursionId) throws DaoException {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(ACTIVATE_EXCURSION_BY_ID)) {
            LOGGER.info("activateExcursion method started");
            statement.setInt(1, excursionId);
            statement.executeUpdate();
            LOGGER.info("Excursion with id " + excursionId + " was activated");
        } catch (SQLException e) {
            LOGGER.warn(e);
            throw new DaoException("An exception occurred while activating excursion", e);
        }
    }

    /**
     * inserts new excursion into the database
     * @param name - excursion name
     * @param description - some details of excursion
     * @param portId - id of the port where excursion takes place
     * @param price - excursion price
     * @throws DaoException  when error inside connection occurs
     */
    @Override
    public void createExcursion(String name, int portId, String description, int price) {
        LOGGER.info("method create excursion started");
        try(Connection connection = ConnectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement(INSERT_EXCURSION)){
            statement.setString(1, name);
            statement.setInt(2, portId);
            statement.setInt(3, price);
            statement.setString(4, description);
            statement.executeUpdate();
            LOGGER.info("excursion "+ name + " is created");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
