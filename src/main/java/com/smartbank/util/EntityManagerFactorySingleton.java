package com.smartbank.util;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EntityManagerFactorySingleton {
    private static final Logger LOGGER = Logger.getLogger(EntityManagerFactorySingleton.class.getName());
    private static EntityManagerFactory emfInstance;

    private EntityManagerFactorySingleton() {}

    public static EntityManagerFactory getInstance() {
        if (emfInstance == null) {
            LOGGER.info("Attempting to create EntityManagerFactory...");
            synchronized (EntityManagerFactorySingleton.class) {
                if (emfInstance == null) {
                    try {
                        LOGGER.info("Creating EntityManagerFactory with persistence unit 'SmartBankPU'");
                        emfInstance = Persistence.createEntityManagerFactory("SmartBankPU");
                        LOGGER.info("EntityManagerFactory created successfully!");
                    } catch (Exception e) {
                        LOGGER.log(Level.SEVERE, "Error initializing EntityManagerFactory", e);
                        throw new RuntimeException("Error initializing EntityManagerFactory: " + e.getMessage(), e);
                    }
                }
            }
        }
        return emfInstance;
    }

    public static void closeEntityManagerFactory() {
        if (emfInstance != null && emfInstance.isOpen()) {
            LOGGER.info("Closing EntityManagerFactory");
            emfInstance.close();
            LOGGER.info("EntityManagerFactory closed successfully");
        }
    }
}
