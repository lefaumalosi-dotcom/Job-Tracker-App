package com.malosilefau.jobtracker.dto;

import com.malosilefau.jobtracker.domain.JobPriority;
import com.malosilefau.jobtracker.domain.JobStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public class JobApplicationForm {

  private Long id;

  @NotBlank(message = "Company name is required")
  @Size(max = 140)
  private String companyName;

  @NotBlank(message = "Job title is required")
  @Size(max = 180)
  private String positionTitle;

  @Size(max = 140)
  private String location;

  @Size(max = 255)
  private String jobUrl;

  @Size(max = 140)
  private String recruiter;

  @Size(max = 120)
  private String salaryRange;

  @NotNull(message = "Status is required")
  private JobStatus status = JobStatus.WISHLIST;

  @NotNull(message = "Priority is required")
  private JobPriority priority = JobPriority.MEDIUM;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate appliedDate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate followUpDate;

  @Size(max = 180)
  private String nextStep;

  @Size(max = 4000)
  private String notes;

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
}
