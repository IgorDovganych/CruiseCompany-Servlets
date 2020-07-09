package dao;

import model.Port;

import java.util.List;

public interface PortDao {
    /**
     * gets all ports from database
     * @return list of ports
     */
    List<Port> getAllPorts();
}
