package dao;

import model.User;

import java.util.List;


public interface UserDao {
    /**
     * gets a user searching by email from database
     * @param email - email
     * @return user
     */
    User findUserByEmail(String email);

    /**
     * inserts new user into the database
     * @param email - user email
     * @param name - name of the user
     * @param password - hashed password
     * @return user
     */
    User createUser(String name, String email, String password);

    /**
     * gets all users from database
     * @return list of users
     */
    List<User> getAllUsers();


    /**
     * updates user and stores in database without updating a password
     * @param id - user id
     * @param name - user name
     * @param email - user email
     * @param role - user role
     * @return updated user
     */
    User updateUserWithoutPasswordUpdate(int id, String name, String email, String role);

    /**
     * updates user and stores in database with password update
     * @param id - user id
     * @param name - user name
     * @param email - user email
     * @param password - hashed password
     * @param role - user role
     * @return updated user
     */
    User updateUser(int id, String name, String email, String password, String role);


}
