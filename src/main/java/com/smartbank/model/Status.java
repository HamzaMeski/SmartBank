package com.smartbank.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

@Entity
@Table(name = "statuses")
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "status", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RequestStatus> requestStatuses = new HashSet<>();

    // Constructors
    public Status() {
    }

    public Status(String name) {
        this.name = name;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<RequestStatus> getRequestStatuses() {
        return requestStatuses;
    }

    public void setRequestStatuses(Set<RequestStatus> requestStatuses) {
        this.requestStatuses = requestStatuses;
    }

    // Helper methods
    public void addRequestStatus(RequestStatus requestStatus) {
        requestStatuses.add(requestStatus);
        requestStatus.setStatus(this);
    }

    public void removeRequestStatus(RequestStatus requestStatus) {
        requestStatuses.remove(requestStatus);
        requestStatus.setStatus(null);
    }
}
