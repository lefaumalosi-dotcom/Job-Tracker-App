package com.malosilefau.jobtracker.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_applications")
public class JobApplication {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 140)
  private String companyName;

  @Column(nullable = false, length = 180)
  private String positionTitle;

  @Column(length = 140)
  private String location;

  @Column(length = 255)
  private String jobUrl;

  @Column(length = 140)
  private String recruiter;

  @Column(length = 120)
  private String salaryRange;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 24)
  private JobStatus status = JobStatus.WISHLIST;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 24)
  private JobPriority priority = JobPriority.MEDIUM;

  private LocalDate appliedDate;

  private LocalDate followUpDate;

  @Column(length = 180)
  private String nextStep;

  @Lob
  private String notes;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime updatedAt;

  @PrePersist
  void onCreate() {
    LocalDateTime now = LocalDateTime.now();
    createdAt = now;
    updatedAt = now;
  }

  @PreUpdate
  void onUpdate() {
    updatedAt = LocalDateTime.now();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public String getPositionTitle() {
    return positionTitle;
  }

  public void setPositionTitle(String positionTitle) {
    this.positionTitle = positionTitle;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getJobUrl() {
    return jobUrl;
  }

  public void setJobUrl(String jobUrl) {
    this.jobUrl = jobUrl;
  }

  public String getRecruiter() {
    return recruiter;
  }

  public void setRecruiter(String recruiter) {
    this.recruiter = recruiter;
  }

  public String getSalaryRange() {
    return salaryRange;
  }

  public void setSalaryRange(String salaryRange) {
    this.salaryRange = salaryRange;
  }

  public JobStatus getStatus() {
    return status;
  }

  public void setStatus(JobStatus status) {
    this.status = status;
  }

  public JobPriority getPriority() {
    return priority;
  }

  public void setPriority(JobPriority priority) {
    this.priority = priority;
  }

  public LocalDate getAppliedDate() {
    return appliedDate;
  }

  public void setAppliedDate(LocalDate appliedDate) {
    this.appliedDate = appliedDate;
  }

  public LocalDate getFollowUpDate() {
    return followUpDate;
  }

  public void setFollowUpDate(LocalDate followUpDate) {
    this.followUpDate = followUpDate;
  }

  public String getNextStep() {
    return nextStep;
  }

  public void setNextStep(String nextStep) {
    this.nextStep = nextStep;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public boolean isFollowUpDue() {
    return followUpDate != null && !followUpDate.isAfter(LocalDate.now()) && !status.isClosed();
  }
}
