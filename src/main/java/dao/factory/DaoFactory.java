package dao.factory;

import dao.CruiseDao;
import dao.ExcursionDao;
import dao.TicketDao;
import dao.UserDao;

public interface DaoFactory {
    CruiseDao getCruiseDao();
    ExcursionDao getExcursionDao();
    TicketDao getTicketDao();
    UserDao getUserDao();
}
