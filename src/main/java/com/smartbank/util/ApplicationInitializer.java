package com.smartbank.util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.util.logging.Logger;

@WebListener
public class ApplicationInitializer implements ServletContextListener {
    private static final Logger LOGGER = Logger.getLogger(ApplicationInitializer.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("Application initializing...");
        try {
            EntityManagerFactorySingleton.getInstance();
            LOGGER.info("Application initialized successfully");
        } catch (Exception e) {
            LOGGER.severe("Failed to initialize application: " + e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("Application shutting down...");
        EntityManagerFactorySingleton.closeEntityManagerFactory();
        LOGGER.info("Application shut down successfully");
    }
}
