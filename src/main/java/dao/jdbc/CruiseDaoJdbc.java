package dao.jdbc;

import dao.CruiseDao;
import exception.DaoException;
import model.Port;
import model.Cruise;
import model.Ship;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CruiseDaoJdbc implements CruiseDao {

    private static CruiseDaoJdbc cruiseDaoJdbc = new CruiseDaoJdbc();
    private static final org.apache.log4j.Logger LOGGER = Logger.getLogger(CruiseDaoJdbc.class);
    final static String GET_ALL_CRUISES = "SELECT * " +
            "FROM cruises\n" +
            "inner join ships on ship_id=ships.id\n" +
            "inner join route_points on cruises.route_id=route_points.route_id \n" +
            "inner join ports on route_points.port_id=ports.id\n" +
            "Order by ships.id,port_sequence_number asc;";

    final static String GET_CRUISE_BY_ID = "SELECT * FROM cruises inner join ships on ship_id=ships.id " +
            "inner join route_points on cruises.route_id=route_points.route_id " +
            "inner join ports on route_points.port_id=ports.id " +
            "where cruises.id=? order by ships.id, port_sequence_number asc;";

    final static String GET_CRUISES_BY_IDS = "SELECT * FROM cruises inner join ships on ship_id=ships.id \n" +
            "inner join route_points on cruises.route_id=route_points.route_id " +
            "inner join ports on route_points.port_id=ports.id " +
            "where cruises.id in(?) order by ships.id, port_sequence_number asc;";
    final static String GET_ALL_SHIPS = "SELECT * FROM ships";
    final static String GET_ALL_PORTS = "SELECT * FROM ports";

    public static CruiseDaoJdbc getCruiseDaoJdbc() {
        return cruiseDaoJdbc;
    }


    public List<Cruise> getAllCruises() throws DaoException {
        try (Connection connection = ConnectionPool.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(GET_ALL_CRUISES);
            List<Cruise> cruises = new ArrayList<>();
            while (resultSet.next()) {
                Ship ship = new Ship(resultSet.getInt("ships.id"),
                        resultSet.getInt("ships.capacity"),
                        resultSet.getString("ships.model"));
                Port port = new Port(resultSet.getInt("ports.id"),
                        resultSet.getString("name"));
                List<String> route = new ArrayList<>();
                String routePoint = resultSet.getString("ports.name");
                route.add(routePoint);
                int currentRouteSequenceNumber = resultSet.getInt("port_sequence_number");
                int portsInCruise = 1;
                while (resultSet.next()) {
                    if (resultSet.getInt("port_sequence_number") > currentRouteSequenceNumber) {
                      route.add(resultSet.getString("ports.name"));
                    } else {
                        break;
                    }
                }
                resultSet.previous();
                Cruise cruise = new Cruise(resultSet.getInt("id"),
                        ship,
                        route,
                        resultSet.getDate("start_date"),
                        resultSet.getDate("end_date"),
                        resultSet.getInt("base_price"));
                cruises.add(cruise);
            }
            return cruises;
        } catch (SQLException e) {
            LOGGER.warn(e);
            throw new DaoException("An exception occurred while getting cruises", e);
        }
    }


    public Cruise getCruiseById(int cruiseId) {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_CRUISE_BY_ID)) {
            statement.setInt(1, cruiseId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Ship ship = new Ship(resultSet.getInt("ships.id"),
                        resultSet.getInt("ships.capacity"),
                        resultSet.getString("ships.model"));
                Port port = new Port(resultSet.getInt("ports.id"),
                        resultSet.getString("name"));

                Date startDate = resultSet.getDate("start_date");
                Date endDate = resultSet.getDate("end_date");
                int price = resultSet.getInt("base_price");

                List<String> route = new ArrayList<>();
                String routePoint = resultSet.getString("ports.name");
                route.add(routePoint);
                while (resultSet.next()) {
                    route.add(resultSet.getString("ports.name"));

                }
                Cruise cruise = new Cruise(cruiseId,
                        ship,
                        route,
                        startDate,
                        endDate,
                        price);
                LOGGER.info(cruise.toString());
                return cruise;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HashMap<Integer, Cruise> getCruisesByIdsInHashMap() throws DaoException {
        try (Connection connection = ConnectionPool.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(GET_ALL_CRUISES);
            HashMap<Integer, Cruise> cruises = new HashMap<>();
            while (resultSet.next()) {
                Ship ship = new Ship(resultSet.getInt("ships.id"),
                        resultSet.getInt("ships.capacity"),
                        resultSet.getString("ships.model"));
                List<String> route = new ArrayList<>();
                String routePoint = resultSet.getString("ports.name");
                route.add(routePoint);
                int currentRouteSequenceNumber = resultSet.getInt("port_sequence_number");
                while (resultSet.next()) {
                    if (resultSet.getInt("port_sequence_number") > currentRouteSequenceNumber) {
                        route.add(resultSet.getString("ports.name"));
                    } else {
                        break;
                    }
                }
                resultSet.previous();
                int cruiseId = resultSet.getInt("id");
                Cruise cruise = new Cruise(cruiseId,
                        ship,
                        route,
                        resultSet.getDate("start_date"),
                        resultSet.getDate("end_date"),
                        resultSet.getInt("base_price"));

                cruises.put(cruiseId,cruise);
            }
            return cruises;
        } catch (SQLException e) {
            LOGGER.warn(e);
            throw new DaoException("An exception occurred while counting tickets", e);
        }
    }

    @Override
    public List<Ship> getAllShips() throws DaoException {
        LOGGER.info("getAllShips method started");
        try (Connection connection = ConnectionPool.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(GET_ALL_SHIPS);
            List<Ship> ships = new ArrayList<>();
            while (resultSet.next()) {
                Ship ship = new Ship(
                        resultSet.getInt("id"),
                        resultSet.getInt("capacity"),
                        resultSet.getString("model")
                );
                ships.add(ship);
            }
            return ships;
        } catch (SQLException e) {
            LOGGER.warn(e);
            throw new DaoException("An exception occurred while getting ships", e);
        }
    }

    @Override
    public List<Port> getAllPorts() throws DaoException {
        LOGGER.info("getAllPorts method started");
        try (Connection connection = ConnectionPool.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(GET_ALL_PORTS);
            List<Port> ports = new ArrayList<>();
            while (resultSet.next()) {
                Port port = new Port(
                        resultSet.getInt("id"),
                        resultSet.getString("name"));
                ports.add(port);
            }
            return ports;
        } catch (SQLException e) {
            LOGGER.warn(e);
            throw new DaoException("An exception occurred while getting ports", e);
        }
    }
}
