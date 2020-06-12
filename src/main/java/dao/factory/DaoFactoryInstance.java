package dao.factory;

public class DaoFactoryInstance {
    private static DaoFactory factory = JdbcDaoFactory.getFactory();

    private DaoFactoryInstance() {

    }

    public static DaoFactory getFactory() {
        return factory;
    }

}
