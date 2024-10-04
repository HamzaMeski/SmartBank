package com.smartbank.model;

import javax.persistence.*;
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

    // toString method
    @Override
    public String toString() {
        return "Status{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    // equals and hashCode methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Status status = (Status) o;
        return Objects.equals(id, status.id) &&
                Objects.equals(name, status.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
