package filter;

import model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.stubbing.BaseStubbing;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AdminFilterTest{

    @InjectMocks
    AdminFilter adminFilter;

    @Mock
    ServletRequest request;

    @Mock
    HttpServletRequest httpServletRequest;

    @Mock
    ServletResponse response;

    @Mock
    HttpSession session;

    @Mock
    FilterChain chain;

    @Mock
    User user;


    @Test
    public void doFilterWhenUserIsNull() throws IOException, ServletException {

        //prepare
        when(httpServletRequest.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);

        //act
        adminFilter.doFilter(request,response,chain);

        //assert
        //verify(response,times(1)).sendRedirect("/FinalProjectJavaEE");
    }
}