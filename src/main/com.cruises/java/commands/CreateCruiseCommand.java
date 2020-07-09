package commands;

import dao.CruiseDao;
import dao.factory.DaoFactoryInstance;
import dao.jdbc.TransactionManager;
import exception.DaoException;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toList;

/**
 * Command is used for cruise creation
 * @author Igor Dovganych
 */
public class CreateCruiseCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(CreateCruiseCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DaoException, ParseException {
        LOGGER.info("CreateCruiseCommand started");
        int shipId = parseInt(request.getParameter("shipId"));
        String portIdsString = request.getParameter("portIds");
        Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("startDate"));
        Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("endDate"));
        int basePrice = parseInt(request.getParameter("basePrice"));
        List<Integer> portIds = Arrays.stream(request.getParameter("portIds").split(","))
                .filter(s -> !s.isEmpty()).map(Integer::parseInt).collect(toList());
        CruiseDao cruiseDao = DaoFactoryInstance.getFactory().getCruiseDao();
        TransactionManager.doInTransaction(connection -> {
            int routeId = cruiseDao.insertRoute(portIds);
            cruiseDao.insertCruise(routeId, shipId, startDate,endDate,basePrice);
        });
        return "/FinalProjectJavaEE/cruises";
    }
}
