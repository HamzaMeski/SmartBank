package com.smartbank.dao;

import com.smartbank.model.Status;

public interface StatusDAO {
    Status findByName(String name);
    void save(Status status);
}
