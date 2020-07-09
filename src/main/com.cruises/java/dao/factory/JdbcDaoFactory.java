package dao.factory;

import dao.*;
import dao.jdbc.*;

public class JdbcDaoFactory implements DaoFactory {
    private static JdbcDaoFactory factory = new JdbcDaoFactory();

    private CruiseDaoJdbc cruiseDaoJdbc = CruiseDaoJdbc.getCruiseDaoJdbc();
    private ExcursionDaoJdbc excursionDaoJdbc = ExcursionDaoJdbc.getExcursionDaoJdbc();
    private TicketDaoJdbc ticketDaoJdbc = TicketDaoJdbc.getTicketDaoJdbc();
    private UserDaoJdbc userDaoJdbc = UserDaoJdbc.getUserDaoJdbc();
    private PortDaoJdbc portDaoJdbc = PortDaoJdbc.getPortDaoJdbc();


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

    @Override
    public PortDao getPortDao() {
        return portDaoJdbc;
    }


}
