package servlet;

import commands.Command;
import exception.DaoException;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/")
public class Controller extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(Controller.class);

    // TODO ERROR PAGE!!!!!!!!!!!
    private static final String ERROR_PAGE = "/WEB-INF/pages/error.jsp";
    private ControllerHelper controllerHelper = ControllerHelper.getInstance();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            Command command = controllerHelper.getCommand(request);
            String page = command.execute(request, response);
            if (page.endsWith(".jsp")) {
                request.getRequestDispatcher(page).forward(request, response);
            } else {
                response.sendRedirect(page);
            }
        } catch (DaoException | ServletException | IOException e) {
            LOGGER.warn("Warning!", e);
            try {
                request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
            } catch (ServletException | IOException e1) {
                LOGGER.warn("Warning!", e1);
                //nothing to do here
            }
        } catch (Exception e) {
            LOGGER.error("Global error!", e);
            try {
                request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
            } catch (ServletException | IOException e1) {
                LOGGER.warn("Warning!", e1);
                //nothing to do here
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        processRequest(request, response);
    }
}
