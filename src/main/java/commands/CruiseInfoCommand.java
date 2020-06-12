package commands;

import dao.factory.DaoFactoryInstance;
import exception.DaoException;
import model.Cruise;
import model.Excursion;
import model.TicketType;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CruiseInfoCommand implements Command {
    private static final Logger LOGGER = Logger.getLogger(CruiseInfoCommand.class);

    //@Override
    public String execute1(HttpServletRequest request, HttpServletResponse response) {
        String route = request.getParameter("route");
        List<String> ports = Arrays.asList(route.split(" - "));
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("shipModel");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        request.setAttribute("startDate", startDate);
        request.setAttribute("endDate", endDate);

        int price = Integer.parseInt(request.getParameter("price"));
        request.setAttribute("cruiseId", id);
        request.setAttribute("shipModel", name);
        request.setAttribute("route", route);
        request.setAttribute("price", price);

        List<Excursion> excursionsInOnePort;
        List<Excursion> allExcursions = new ArrayList<>();


        request.setAttribute("ports", ports);
        request.setAttribute("excursions", allExcursions);

        List<TicketType> ticketTypes = DaoFactoryInstance.getFactory().getTicketDao().getAllTicketTypes();
        request.setAttribute("tickets", ticketTypes);
        return "cruise_info.jsp";
    }

    //@Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws DaoException {
        int cruiseId = Integer.parseInt(request.getParameter("id"));
        Cruise cruise = DaoFactoryInstance.getFactory().getCruiseDao().getCruiseById(cruiseId);

        request.setAttribute("cruise", cruise);
        List<Excursion> allExcursions = DaoFactoryInstance.getFactory().getExcursionDao().getExcursionsByCruiseId(cruiseId);
        request.setAttribute("excursions", allExcursions);
        List<TicketType> ticketTypes = DaoFactoryInstance.getFactory().getTicketDao().getAllTicketTypes();
        request.setAttribute("ticketTypes", ticketTypes);
        int amountOfPurchasedTickets = DaoFactoryInstance.getFactory().getTicketDao()
                .getPurchasedTicketsAmountByCruiseId(cruiseId);
        int availableTicketsAmount = cruise.getShip().getCapacity()-amountOfPurchasedTickets;
        request.setAttribute("availableTicketsAmount",availableTicketsAmount);
        return "/WEB-INF/pages/cruise_info.jsp";
    }

}
