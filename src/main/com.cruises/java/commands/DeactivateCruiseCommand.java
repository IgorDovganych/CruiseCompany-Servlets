package commands;

import dao.CruiseDao;
import dao.factory.DaoFactoryInstance;
import exception.DaoException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

public class DeactivateCruiseCommand implements Command {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DaoException, ParseException {
        int cruiseId = Integer.parseInt(request.getParameter("id"));
        CruiseDao cruiseDao = DaoFactoryInstance.getFactory().getCruiseDao();
        cruiseDao.deactivateCruise(cruiseId);
        return "/FinalProjectJavaEE/cruises";
    }
}
