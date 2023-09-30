import Manager.AdminManager;
import Manager.PredictionManager;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class MyServletContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        // Create an instance of your Manager class here
        PredictionManager manager = new PredictionManager();
        AdminManager adminManger = new AdminManager();
        // You can now use the manager instance as needed.
        // For example, you can store it in the servlet context for access throughout your application.
        servletContextEvent.getServletContext().setAttribute("manager", manager);
        servletContextEvent.getServletContext().setAttribute("adminManager", adminManger);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        // Cleanup code when the server is shutting down, if needed.
    }
}