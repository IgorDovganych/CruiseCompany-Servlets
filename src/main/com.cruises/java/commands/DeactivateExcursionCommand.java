package commands;

import dao.ExcursionDao;
import dao.factory.DaoFactoryInstance;
import exception.DaoException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeactivateExcursionCommand implements Command {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DaoException {
        int excursionId = Integer.parseInt(request.getParameter("excursionId"));
        ExcursionDao excursionDao = DaoFactoryInstance.getFactory().getExcursionDao();
        excursionDao.deactivateExcursion(excursionId);
        return "/FinalProjectJavaEE/admin/excursions";

    }
}
