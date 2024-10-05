package com.smartbank.util;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerFactorySingleton {
    private static EntityManagerFactory emfInstance;

    private EntityManagerFactorySingleton() {}

    public static EntityManagerFactory getInstance() {
        if (emfInstance == null) {
            synchronized (EntityManagerFactorySingleton.class) {
                if (emfInstance == null) {
                    try {
                        emfInstance = Persistence.createEntityManagerFactory("SmartBankPU");
                    } catch (Exception e) {
                        throw new RuntimeException("Error initializing EntityManagerFactory: " + e.getMessage(), e);
                    }
                }
            }
        }
        return emfInstance;
    }

    public static void closeEntityManagerFactory() {
        if (emfInstance != null && emfInstance.isOpen()) {
            emfInstance.close();
        }
    }
}
