package dao.jdbc;

import dao.UserDao;
import dao.factory.DaoFactoryInstance;
import model.User;
import org.apache.log4j.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJdbc implements UserDao {
    private static UserDaoJdbc userDaoJdbc = new UserDaoJdbc();
    private static final Logger LOGGER = Logger.getLogger(UserDaoJdbc.class);

    private static final String SELECT_BY_EMAIL = "select * from users where email = '";
    private static final String SELECT_USER_BY_ID = "select * from users where id = ";
    private static final String INSERT_USER = "insert into users(name, email, password, role) values(?, ?,?,'user')";
    private static final String SELECT_ALL_FROM_USERS = "select * from users";
    private static final String UPDATE_USER = "update users set name=?,email=?,role=?,password=? where id=";
    private static final String UPDATE_USER_WITHOUT_PASSWORD_UPDATE = "update users set name=?,email=?,role=? where id=";

    public static UserDaoJdbc getUserDaoJdbc() {
        return userDaoJdbc;
    }


    @Override
    public User findUserByEmail(String email) {
        try {
            Connection connection = ConnectionPool.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_BY_EMAIL + email + "'");
            if (resultSet.next()) {
                User user = new User((long) resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("email"), resultSet.getString("password"), resultSet.getString("role"));
                connection.close();
                System.out.println(user.toString());
                return user;
            } else return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User createUser(String name, String email, String password) {
        try {
            Connection connection = ConnectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();            //getting auto-generated id for user
            long id;
            if (generatedKeys.next()) {
                System.out.println(generatedKeys);
                id = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Id is missing");
            }
            User user = new User(id, name, email, password, "user");
            LOGGER.info(user.toString() + " is created");
            connection.close();
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        try {
            Connection connection = ConnectionPool.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_ALL_FROM_USERS);
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                User user = new User(resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("role"));
                users.add(user);
            }
            connection.close();
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User updateUser(int id, String name, String email, String password, String role) {
        try (Connection connection = ConnectionPool.getConnection()) {
            String sql = password == null ? UPDATE_USER_WITHOUT_PASSWORD_UPDATE : UPDATE_USER;
            PreparedStatement statement = connection.prepareStatement(sql + id);
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, role);
            if (password != null) {
                statement.setString(4, password);
            }
            statement.executeUpdate();
            LOGGER.info("User " + email + " was updated");
            ResultSet resultSet = statement.executeQuery(SELECT_USER_BY_ID + id);
            if (resultSet.next()) {
                User user = new User(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("role"));
                return user;
            } else return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User updateUserWithoutPasswordUpdate(int id, String name, String email, String role) {
        return updateUser(id, name, email, null, role);
    }

    public static String HashPasswordMD5(String passwordToHash) {
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(passwordToHash.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //System.out.println(generatedPassword);
        return generatedPassword;
    }
}
