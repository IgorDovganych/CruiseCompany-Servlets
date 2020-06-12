package dao.factory;

import dao.CruiseDao;
import dao.ExcursionDao;
import dao.TicketDao;
import dao.UserDao;
import dao.jdbc.CruiseDaoJdbc;
import dao.jdbc.ExcursionDaoJdbc;
import dao.jdbc.TicketDaoJdbc;
import dao.jdbc.UserDaoJdbc;

public class JdbcDaoFactory implements DaoFactory {
    private static JdbcDaoFactory factory = new JdbcDaoFactory();

    private CruiseDaoJdbc cruiseDaoJdbc = CruiseDaoJdbc.getCruiseDaoJdbc();
    private ExcursionDaoJdbc excursionDaoJdbc = ExcursionDaoJdbc.getExcursionDaoJdbc();
    private TicketDaoJdbc ticketDaoJdbc = TicketDaoJdbc.getTicketDaoJdbc();
    private UserDaoJdbc userDaoJdbc = UserDaoJdbc.getUserDaoJdbc();


    public static JdbcDaoFactory getFactory() {
        return factory;
    }

    @Override
    public CruiseDao getCruiseDao() {
        return cruiseDaoJdbc;
    }

    @Override
    public ExcursionDao getExcursionDao() {
        return excursionDaoJdbc;
    }

    @Override
    public TicketDao getTicketDao() {
        return ticketDaoJdbc;
    }

    @Override
    public UserDao getUserDao() {
        return userDaoJdbc;
    }
}
