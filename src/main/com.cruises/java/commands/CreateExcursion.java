package commands;

import dao.ExcursionDao;
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

public class CreateExcursion implements Command {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DaoException, ParseException {
        int portId = Integer.parseInt(request.getParameter("port"));
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        int price = Integer.parseInt(request.getParameter("price"));
        PortDao portDao = DaoFactoryInstance.getFactory().getPortDao();
        if(name.equals("")){
            request.setAttribute("description", description);
            request.setAttribute("port",portId);
            request.setAttribute("price",price);
            List<Port> ports = portDao.getAllPorts();
            request.setAttribute("ports", ports);
            request.setAttribute("error_message", "errorNoName");
            return "create_excursion";
        }

        if(description.equals("")){
            request.setAttribute("name", name);
            request.setAttribute("port",portId);
            request.setAttribute("price",price);
            List<Port> ports = portDao.getAllPorts();
            request.setAttribute("ports", ports);
            request.setAttribute("error_message", "errorNoDescription");
            return "create_excursion";
        }
        ExcursionDao excursionDao = DaoFactoryInstance.getFactory().getExcursionDao();
        excursionDao.createExcursion(name,portId,description,price);
        return "/FinalProjectJavaEE/admin/excursions" ;
    }
}
