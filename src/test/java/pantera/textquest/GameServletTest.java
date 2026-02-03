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
public class GameServletTest {
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

    private GameServlet servlet;
    private InMemoryUserDao dao;

    @BeforeEach
    public void setup() throws ServletException {
        servlet = new GameServlet();
        dao = new InMemoryUserDao();
        
        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("userDao")).thenReturn(dao);
        when(request.getRequestDispatcher("/game.jsp")).thenReturn(dispatcher);
        
        servlet.init(servletConfig);
    }

    @Test
    public void testDoGetWithoutLogin() throws IOException, ServletException {
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn("/text-quest");
        when(session.getAttribute("currentUser")).thenReturn(null);
        
        servlet.doGet(request, response);
        
        verify(response).sendRedirect("/text-quest/login");
    }

    @Test
    public void testDoGetWithLogin() throws IOException, ServletException {
        User u = new User();
        u.setUsername("player");
        u.setProgress("start");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(u);
        
        servlet.doGet(request, response);
        
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPostStartAction() throws IOException, ServletException {
        User u = new User();
        u.setUsername("player");
        u.setProgress("start");
        dao.add(u);
        
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn("/text-quest");
        when(session.getAttribute("currentUser")).thenReturn(u);
        when(request.getParameter("action")).thenReturn("start");
        
        servlet.doPost(request, response);
        
        assertEquals("zone", u.getProgress());
        verify(response).sendRedirect("/text-quest/game");
    }

    @Test
    public void testDoPostExploreRuinsAction() throws IOException, ServletException {
        User u = new User();
        u.setUsername("player");
        u.setProgress("zone");
        dao.add(u);
        
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(u);
        when(request.getParameter("action")).thenReturn("explore_ruins");
        
        servlet.doPost(request, response);
        
        assertEquals("ruins", u.getProgress());
    }

    @Test
    public void testDoPostEnterAnomalyAction() throws IOException, ServletException {
        User u = new User();
        u.setUsername("player");
        u.setProgress("zone");
        dao.add(u);
        
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(u);
        when(request.getParameter("action")).thenReturn("enter_anomaly");
        
        servlet.doPost(request, response);
        
        assertEquals("anomaly", u.getProgress());
    }

    @Test
    public void testDoPostSearchArtifactsAction() throws IOException, ServletException {
        User u = new User();
        u.setUsername("player");
        u.setProgress("ruins");
        dao.add(u);
        
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(u);
        when(request.getParameter("action")).thenReturn("search_artifacts");
        
        servlet.doPost(request, response);
        
        assertEquals("artifact_found", u.getProgress());
    }

    @Test
    public void testDoPostResetAction() throws IOException, ServletException {
        User u = new User();
        u.setUsername("player");
        u.setProgress("treasure");
        dao.add(u);
        
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(u);
        when(request.getParameter("action")).thenReturn("reset");
        
        servlet.doPost(request, response);
        
        assertEquals("start", u.getProgress());
    }

    @Test
    public void testDoPostWithoutLogin() throws IOException, ServletException {
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn("/text-quest");
        when(session.getAttribute("currentUser")).thenReturn(null);
        when(request.getParameter("action")).thenReturn("start");
        
        servlet.doPost(request, response);
        
        verify(response).sendRedirect("/text-quest/login");
    }

    @Test
    public void testDoPostInvalidActionDoesNothing() throws IOException, ServletException {
        User u = new User();
        u.setUsername("player");
        u.setProgress("zone");
        dao.add(u);
        
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(u);
        when(request.getParameter("action")).thenReturn("invalid_action");
        
        servlet.doPost(request, response);
        
        assertEquals("zone", u.getProgress());
    }
}
