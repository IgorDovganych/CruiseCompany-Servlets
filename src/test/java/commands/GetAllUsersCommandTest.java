package commands;

import dao.CruiseDao;
import dao.UserDao;
import dao.factory.DaoFactory;
import dao.factory.DaoFactoryInstance;
import exception.DaoException;
import model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoFactoryInstance.class)
public class GetAllUsersCommandTest {

    @InjectMocks
    GetAllUsersCommand getAllUsersCommand;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    DaoFactory daoFactory;

    @Mock
    UserDao userDao;


    @Test
    public void GetAllUsersTest() throws ServletException, DaoException, IOException {
        //prepare
        List<User> users = null;
        mockStatic(DaoFactoryInstance.class);
        when(DaoFactoryInstance.getFactory()).thenReturn(daoFactory);
        when(daoFactory.getUserDao()).thenReturn(userDao);
        when(userDao.getAllUsers()).thenReturn(users);
        String expectedResult = "/WEB-INF/pages/users.jsp";
        //act
        String actual = getAllUsersCommand.execute(request,response);

        //assert
        assertEquals(expectedResult,actual);
        verify(request).setAttribute("users", users);

    }
}