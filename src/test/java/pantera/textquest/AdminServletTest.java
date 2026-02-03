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
import pantera.textquest.model.Role;
import pantera.textquest.model.User;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AdminServletTest {
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

    private AdminServlet servlet;
    private InMemoryUserDao dao;

    @BeforeEach
    public void setup() throws ServletException {
        servlet = new AdminServlet();
        dao = new InMemoryUserDao();
        
        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("userDao")).thenReturn(dao);
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn("/text-quest");
        when(request.getRequestDispatcher("/admin.jsp")).thenReturn(dispatcher);
        
        servlet.init(servletConfig);
    }

    @Test
    public void testDoGetAdminAccess() throws IOException, ServletException {
        User admin = dao.findByUsername("admin").get();
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(admin);
        
        servlet.doGet(request, response);
        
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoGetNonAdminDenied() throws IOException, ServletException {
        User regularUser = new User();
        regularUser.setUsername("user");
        regularUser.setRole(Role.USER);
        
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(regularUser);
        
        servlet.doGet(request, response);
        
        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    @Test
    public void testDoGetNoLoginDenied() throws IOException, ServletException {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(null);
        
        servlet.doGet(request, response);
        
        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    @Test
    public void testDoPostDeleteUser() throws IOException, ServletException {
        User admin = dao.findByUsername("admin").get();
        User toDelete = new User();
        toDelete.setUsername("todelete");
        dao.add(toDelete);
        
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(admin);
        when(request.getParameter("action")).thenReturn("delete");
        when(request.getParameter("username")).thenReturn("todelete");
        
        servlet.doPost(request, response);
        
        assertFalse(dao.findByUsername("todelete").isPresent());
        verify(response).sendRedirect("/text-quest/admin");
    }

    @Test
    public void testDoPostNonAdminDenied() throws IOException, ServletException {
        User regularUser = new User();
        regularUser.setUsername("user");
        regularUser.setRole(Role.USER);
        
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(regularUser);
        when(request.getParameter("action")).thenReturn("delete");
        
        servlet.doPost(request, response);
        
        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    @Test
    public void testDoPostInvalidActionIgnored() throws IOException, ServletException {
        User admin = dao.findByUsername("admin").get();
        User target = new User();
        target.setUsername("target");
        dao.add(target);
        
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(admin);
        when(request.getParameter("action")).thenReturn("invalid");
        
        servlet.doPost(request, response);
        
        assertTrue(dao.findByUsername("target").isPresent());
    }
}
