package com.smartbank.model;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "credit_requests")
public class CreditRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "request_number", unique = true, nullable = false)
    private String requestNumber;

    @Column(nullable = false)
    private String project;

    @Column(nullable = false)
    private String employmentStatus;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private Integer duration;

    @Column(nullable = false)
    private Double monthlyPayment;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String identificationNumber;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date dateOfBirth;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date employmentStartDate;

    @Column(nullable = false)
    private Double monthlyIncome;

    @Column(nullable = false)
    private Boolean hasOngoingCredits;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "request_date", nullable = false)
    private Date requestDate;

    @OneToMany(mappedBy = "creditRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RequestStatus> requestStatuses = new HashSet<>();

    // Constructors
    public CreditRequest() {
    }

    // Getters and Setters for all fields

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequestNumber() {
        return requestNumber;
    }

    public void setRequestNumber(String requestNumber) {
        this.requestNumber = requestNumber;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Double getMonthlyPayment() {
        return monthlyPayment;
    }

    public void setMonthlyPayment(Double monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Date getEmploymentStartDate() {
        return employmentStartDate;
    }

    public void setEmploymentStartDate(Date employmentStartDate) {
        this.employmentStartDate = employmentStartDate;
    }

    public Double getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(Double monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public Boolean getHasOngoingCredits() {
        return hasOngoingCredits;
    }

    public void setHasOngoingCredits(Boolean hasOngoingCredits) {
        this.hasOngoingCredits = hasOngoingCredits;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Set<RequestStatus> getRequestStatuses() {
        return requestStatuses;
    }

    public void setRequestStatuses(Set<RequestStatus> requestStatuses) {
        this.requestStatuses = requestStatuses;
    }

    // Helper methods for managing the relationship with RequestStatus
    public void addRequestStatus(RequestStatus requestStatus) {
        requestStatuses.add(requestStatus);
        requestStatus.setCreditRequest(this);
    }

    public void removeRequestStatus(RequestStatus requestStatus) {
        requestStatuses.remove(requestStatus);
        requestStatus.setCreditRequest(null);
    }

    // toString, equals, and hashCode methods

}
