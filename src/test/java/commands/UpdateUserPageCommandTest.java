package commands;

import dao.factory.DaoFactoryInstance;
import exception.DaoException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class UpdateUserPageCommandTest {

    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;

    @Test
    public void execute() throws ServletException, IOException, DaoException {
        String expected = "/WEB-INF/pages/update_user_page.jsp";
        assertEquals(expected,new UpdateUserPageCommand().execute(request, response));
    }
}