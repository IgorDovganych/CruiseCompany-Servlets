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
import java.util.Date;

public class CruiseDaoJdbc implements CruiseDao {

    private static CruiseDaoJdbc cruiseDaoJdbc = new CruiseDaoJdbc();
    private static final org.apache.log4j.Logger LOGGER = Logger.getLogger(CruiseDaoJdbc.class);
    final static String GET_ALL_CRUISES = "SELECT * " +
            "FROM cruises\n" +
            "inner join ships on ship_id=ships.id\n" +
            "inner join route_points on cruises.route_id=route_points.route_id \n" +
            "inner join ports on route_points.port_id=ports.id\n" +
            "Order by cruises.id,port_sequence_number asc;";

    final static String GET_CRUISE_BY_ID = "SELECT * FROM cruises inner join ships on ship_id=ships.id " +
            "inner join route_points on cruises.route_id=route_points.route_id " +
            "inner join ports on route_points.port_id=ports.id " +
            "where cruises.id=? order by ships.id, port_sequence_number asc;";

    final static String GET_ALL_SHIPS = "SELECT * FROM ships";

    final static String INSERT_INTO_ROUTE_POINTS ="INSERT INTO route_points(route_id, port_id,port_sequence_number) " +
            "values(?,?,?)";
    final static String GET_MAX_VALUE_OF_ROUTE_ID = "SELECT MAX(route_id) as max_value FROM route_points";
    final static String INSERT_CRUISE = "INSERT INTO cruises(route_id, ship_id,start_date, end_date, base_price, isActive) values(?,?,?,?,?,?)";
    final static String ACTIVATE_CRUISE = "update cruises set isActive =true where id = ?";
    final static String DEACTIVATE_CRUISE = "update cruises set isActive =false where id = ?";

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
                        resultSet.getInt("base_price"),
                        resultSet.getBoolean("isActive"));
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
                boolean isActive = resultSet.getBoolean("isActive");
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
                        price,
                        isActive);
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
                        resultSet.getInt("base_price"),
                        resultSet.getBoolean("isActive"));

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


    /**
     * inserts route into the table inside the database
     * @param portIds all port id's on the route in sequence order
     * @return id of the route
     * @throws DaoException  when error inside connection occurs
     */
    @Override
    public int insertRoute(List<Integer> portIds) throws DaoException{
        LOGGER.info("insertRoute method started");
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_INTO_ROUTE_POINTS);
             Statement statement = connection.createStatement()){
            ResultSet rs = statement.executeQuery(GET_MAX_VALUE_OF_ROUTE_ID);
            int routeId = 0;
            while (rs.next()){
                routeId = rs.getInt("max_value");
            }
            routeId++;
            connection.setAutoCommit(false);
            int sequence_num = 1;
            for (int portId:portIds) {
                ps.setInt(1,routeId);
                ps.setInt(2,portId);
                ps.setInt(3,sequence_num);
                sequence_num++;
                ps.addBatch();
            }
            int[] updateCounts = ps.executeBatch();
            connection.commit();
            for (int i=0; i<updateCounts.length; i++) {
                if (updateCounts[i] >= 0) {
                    System.out.println("OK; updateCount=" + updateCounts[i]);
                }
            }
            System.out.println(updateCounts);
            return routeId;


        }catch (SQLException e){
            LOGGER.warn(e);
            throw new DaoException("An exception occured while inserting into route_points");
        }
    }


    /**
     * inserts cruise into the table inside the database
     * @param routeId - id of the route
     * @param shipId - ship id
     * @param startDate - date of start
     * @param endDate - date of the end
     * @param basePrice - base ticket price
     * @throws DaoException  when error inside connection occurs
     */
    @Override
    public void insertCruise(int routeId, int shipId, Date startDate, Date endDate, int basePrice) throws DaoException {
        LOGGER.info("insertCruise method started");
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_CRUISE)){
            ps.setInt(1,routeId);
            ps.setInt(2,shipId);
            java.sql.Date sqlStartDate = new java.sql.Date(startDate.getTime());
            ps.setDate(3, sqlStartDate);
            java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());
            ps.setDate(4, sqlEndDate);
            ps.setInt(5,basePrice);
            ps.setBoolean(6,true);
            ps.executeUpdate();

        }catch(SQLException e){
            LOGGER.warn(e);
            throw new DaoException("An exception while inserting cruise into cruise table");
        }
    }

    /**
     * activates cruise
     * @param cruiseId - id of the cruise
     */
    @Override
    public void activateCruise(int cruiseId) throws DaoException {
        LOGGER.info("method activateCruise started");
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(ACTIVATE_CRUISE)) {
            statement.setInt(1, cruiseId);
            statement.executeUpdate();
            LOGGER.info("cruise with id " + cruiseId + " was activated");
        } catch (SQLException e) {
            LOGGER.warn(e);
            throw new DaoException("An exception occurred while activating cruise", e);
        }
    }

    /**
     * deactivates cruise so end user cannot see it
     * @param cruiseId - id of the cruise
     */
    @Override
    public void deactivateCruise(int cruiseId) throws DaoException {
        LOGGER.info("method deactivateCruise started");
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(DEACTIVATE_CRUISE)) {
            statement.setInt(1, cruiseId);
            statement.executeUpdate();
            LOGGER.info("cruise with id " + cruiseId + " was deactivated");
        } catch (SQLException e) {
            LOGGER.warn(e);
            throw new DaoException("An exception occurred while deactivating cruise", e);
        }
    }
}
