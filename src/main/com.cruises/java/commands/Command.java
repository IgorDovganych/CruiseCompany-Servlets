package commands;

import exception.DaoException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

public interface Command {
    String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DaoException, ParseException;
}
