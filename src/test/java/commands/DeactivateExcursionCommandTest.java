package commands;

import dao.ExcursionDao;
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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoFactoryInstance.class)
public class DeactivateExcursionCommandTest {

    @InjectMocks
    DeactivateExcursionCommand deactivateExcursionCommand;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    DaoFactory daoFactory;

    @Mock
    ExcursionDao excursionDao;

    @Test
    public void executeTest() throws ServletException, DaoException, IOException {
        int id = 1;
        when(request.getParameter("excursionId")).thenReturn("1");
        mockStatic(DaoFactoryInstance.class);

        when(DaoFactoryInstance.getFactory()).thenReturn(daoFactory);
        when(daoFactory.getExcursionDao()).thenReturn(excursionDao);
        String expected = "/FinalProjectJavaEE/admin/excursions";
        //act
        String result = deactivateExcursionCommand.execute(request,response);

        verify(excursionDao,times(1)).deactivateExcursion(id);
        assertEquals(expected,result);
    }
}