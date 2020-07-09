package commands;

import dao.CruiseDao;
import dao.factory.DaoFactory;
import dao.factory.DaoFactoryInstance;
import exception.DaoException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoFactoryInstance.class)
public class DeactivateCruiseCommandTest {

    @InjectMocks
    DeactivateCruiseCommand deactivateCruiseCommand;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    DaoFactory daoFactory;

    @Mock
    CruiseDao cruiseDao;

    @Test
    public void executeTest() throws DaoException, ServletException, ParseException, IOException {
        int id = 1;
        when(request.getParameter("id")).thenReturn("1");
        mockStatic(DaoFactoryInstance.class);
        when(DaoFactoryInstance.getFactory()).thenReturn(daoFactory);
        when(daoFactory.getCruiseDao()).thenReturn(cruiseDao);
        String expected = "/FinalProjectJavaEE/cruises";
        //act
        String result = deactivateCruiseCommand.execute(request,response);

        verify(cruiseDao,times(1)).deactivateCruise(id);
        assertEquals(expected,result);
    }
}