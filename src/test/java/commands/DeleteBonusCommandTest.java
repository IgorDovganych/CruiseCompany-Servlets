package commands;

import dao.TicketDao;
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
public class DeleteBonusCommandTest {

    @InjectMocks
    DeleteBonusCommand deleteBonusCommand;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    DaoFactory daoFactory;

    @Mock
    TicketDao ticketDao;

    @Test
    public void executeTest() throws ServletException, DaoException, IOException {

        String bonusName = "name of the bonus";
        when(request.getParameter("bonusName")).thenReturn(bonusName);
        mockStatic(DaoFactoryInstance.class);
        when(DaoFactoryInstance.getFactory()).thenReturn(daoFactory);
        when(daoFactory.getTicketDao()).thenReturn(ticketDao);
        String expected = "/FinalProjectJavaEE/admin/tickets";
        //act
        String result = deleteBonusCommand.execute(request,response);

        verify(ticketDao,times(1)).deleteBonusFromTicket(bonusName);
        assertEquals(expected,result);
    }
}