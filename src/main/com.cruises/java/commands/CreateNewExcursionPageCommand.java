package commands;

import dao.PortDao;
import dao.factory.DaoFactoryInstance;
import exception.DaoException;
import model.Port;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public class CreateNewExcursionPageCommand implements Command{
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DaoException, ParseException {
        PortDao portDao = DaoFactoryInstance.getFactory().getPortDao();
        List<Port> ports = portDao.getAllPorts();
        request.setAttribute("ports", ports);
        return "/WEB-INF/pages/create_excursion.jsp" ;
    }
}
