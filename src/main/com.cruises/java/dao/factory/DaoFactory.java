package dao.factory;

import dao.*;

public interface DaoFactory {
    CruiseDao getCruiseDao();
    ExcursionDao getExcursionDao();
    TicketDao getTicketDao();
    UserDao getUserDao();
    PortDao getPortDao();
}
