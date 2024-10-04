package com.smartbank.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "request_statuses")
public class RequestStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_request_id", nullable = false)
    private CreditRequest creditRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modification_date", nullable = false)
    private Date modificationDate;

    @Column(length = 1000)
    private String explanation;

    // Constructors
    public RequestStatus() {
    }

    public RequestStatus(CreditRequest creditRequest, Status status, Date modificationDate, String explanation) {
        this.creditRequest = creditRequest;
        this.status = status;
        this.modificationDate = modificationDate;
        this.explanation = explanation;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CreditRequest getCreditRequest() {
        return creditRequest;
    }

    public void setCreditRequest(CreditRequest creditRequest) {
        this.creditRequest = creditRequest;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    // toString method
    @Override
    public String toString() {
        return "RequestStatus{" +
                "id=" + id +
                ", creditRequest=" + (creditRequest != null ? creditRequest.getId() : null) +
                ", status=" + (status != null ? status.getName() : null) +
                ", modificationDate=" + modificationDate +
                ", explanation='" + explanation + '\'' +
                '}';
    }

    // equals and hashCode methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestStatus that = (RequestStatus) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(creditRequest, that.creditRequest) &&
                Objects.equals(status, that.status) &&
                Objects.equals(modificationDate, that.modificationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creditRequest, status, modificationDate);
    }
}
