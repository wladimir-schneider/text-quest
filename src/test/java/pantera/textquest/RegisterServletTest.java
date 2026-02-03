package pantera.textquest;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import pantera.textquest.dao.InMemoryUserDao;
import pantera.textquest.model.User;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RegisterServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    @Mock
    private ServletContext servletContext;
    @Mock
    private ServletConfig servletConfig;
    @Mock
    private RequestDispatcher dispatcher;

    private RegisterServlet servlet;
    private InMemoryUserDao dao;

    @BeforeEach
    public void setup() throws ServletException {
        servlet = new RegisterServlet();
        dao = new InMemoryUserDao();
        
        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("userDao")).thenReturn(dao);
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);
        
        servlet.init(servletConfig);
    }

    @Test
    public void testDoGetForwardToRegisterJsp() throws IOException, ServletException {
        servlet.doGet(request, response);
        
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPostSuccessfulRegistration() throws IOException, ServletException {
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn("/text-quest");
        when(request.getParameter("username")).thenReturn("newuser");
        when(request.getParameter("password")).thenReturn("password123");
        
        servlet.doPost(request, response);
        
        assertTrue(dao.findByUsername("newuser").isPresent());
        User u = dao.findByUsername("newuser").get();
        assertEquals("newuser", u.getUsername());
        assertEquals("password123", u.getPassword());
        verify(session).setAttribute(eq("currentUser"), any(User.class));
        verify(response).sendRedirect("/text-quest/game");
    }

    @Test
    public void testDoPostDuplicateUsernameFails() throws IOException, ServletException {
        User existing = new User();
        existing.setUsername("existing");
        existing.setPassword("pass");
        dao.add(existing);
        
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("username")).thenReturn("existing");
        when(request.getParameter("password")).thenReturn("newpass");
        
        servlet.doPost(request, response);
        
        verify(request).setAttribute(eq("error"), contains("already exists"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPostEmptyUsernameFails() throws IOException, ServletException {
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("username")).thenReturn("");
        when(request.getParameter("password")).thenReturn("password");
        
        servlet.doPost(request, response);
        
        verify(request).setAttribute(eq("error"), contains("Invalid"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPostNullParametersFails() throws IOException, ServletException {
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("username")).thenReturn(null);
        when(request.getParameter("password")).thenReturn("password");
        
        servlet.doPost(request, response);
        
        verify(request).setAttribute(eq("error"), contains("Invalid"));
    }
}
