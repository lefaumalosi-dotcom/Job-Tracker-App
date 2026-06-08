package com.malosilefau.jobtracker.service;

import com.malosilefau.jobtracker.domain.JobApplication;
import com.malosilefau.jobtracker.domain.JobPriority;
import com.malosilefau.jobtracker.domain.JobStatus;
import com.malosilefau.jobtracker.dto.DashboardStats;
import com.malosilefau.jobtracker.dto.JobApplicationForm;
import com.malosilefau.jobtracker.repository.JobApplicationRepository;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class JobApplicationService {

  private final JobApplicationRepository repository;

  public JobApplicationService(JobApplicationRepository repository) {
    this.repository = repository;
  }

  @Transactional(readOnly = true)
  public List<JobApplication> findAllSorted() {
    return repository.findAll(Sort.by(Sort.Direction.DESC, "updatedAt"));
  }

  @Transactional(readOnly = true)
  public List<JobApplication> filterJobs(
      List<JobApplication> jobs, String query, JobStatus status, JobPriority priority) {
    String normalizedQuery = normalize(query);

    return jobs.stream()
        .filter(job -> matchesQuery(job, normalizedQuery))
        .filter(job -> status == null || job.getStatus() == status)
        .filter(job -> priority == null || job.getPriority() == priority)
        .toList();
  }

  @Transactional(readOnly = true)
  public Optional<JobApplication> findById(Long id) {
    return repository.findById(id);
  }

  public JobApplication save(JobApplicationForm form) {
    JobApplication job = new JobApplication();
    applyForm(job, form);
    return repository.save(job);
  }

  public JobApplication update(Long id, JobApplicationForm form) {
    JobApplication job = repository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found"));
    applyForm(job, form);
    return repository.save(job);
  }

  public void delete(Long id) {
    if (!repository.existsById(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found");
    }
    repository.deleteById(id);
  }

  public void updateStatus(Long id, JobStatus status) {
    JobApplication job = repository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found"));
    job.setStatus(status);
    repository.save(job);
  }

  @Transactional(readOnly = true)
  public DashboardStats buildStats(List<JobApplication> jobs) {
    long total = jobs.size();
    long wishlist = countByStatus(jobs, JobStatus.WISHLIST);
    long applied = countByStatus(jobs, JobStatus.APPLIED);
    long interviewing = countByStatus(jobs, JobStatus.INTERVIEW);
    long offer = countByStatus(jobs, JobStatus.OFFER);
    long rejected = countByStatus(jobs, JobStatus.REJECTED);
    long archived = countByStatus(jobs, JobStatus.ARCHIVED);
    long active = total - rejected - archived;
    long followUpsDue = jobs.stream().filter(JobApplication::isFollowUpDue).count();

    return new DashboardStats(total, active, wishlist, applied, interviewing, offer, rejected, archived, followUpsDue);
  }

  @Transactional(readOnly = true)
  public JobApplicationForm toForm(JobApplication job) {
    JobApplicationForm form = new JobApplicationForm();
    form.setId(job.getId());
    form.setCompanyName(job.getCompanyName());
    form.setPositionTitle(job.getPositionTitle());
    form.setLocation(job.getLocation());
    form.setJobUrl(job.getJobUrl());
    form.setRecruiter(job.getRecruiter());
    form.setSalaryRange(job.getSalaryRange());
    form.setStatus(job.getStatus());
    form.setPriority(job.getPriority());
    form.setAppliedDate(job.getAppliedDate());
    form.setFollowUpDate(job.getFollowUpDate());
    form.setNextStep(job.getNextStep());
    form.setNotes(job.getNotes());
    return form;
  }

  private void applyForm(JobApplication job, JobApplicationForm form) {
    job.setCompanyName(clean(form.getCompanyName()));
    job.setPositionTitle(clean(form.getPositionTitle()));
    job.setLocation(clean(form.getLocation()));
    job.setJobUrl(clean(form.getJobUrl()));
    job.setRecruiter(clean(form.getRecruiter()));
    job.setSalaryRange(clean(form.getSalaryRange()));
    job.setStatus(form.getStatus() == null ? JobStatus.WISHLIST : form.getStatus());
    job.setPriority(form.getPriority() == null ? JobPriority.MEDIUM : form.getPriority());
    job.setAppliedDate(form.getAppliedDate());
    job.setFollowUpDate(form.getFollowUpDate());
    job.setNextStep(clean(form.getNextStep()));
    job.setNotes(clean(form.getNotes()));
  }

  private boolean matchesQuery(JobApplication job, String query) {
    if (!StringUtils.hasText(query)) {
      return true;
    }

    return contains(job.getCompanyName(), query)
        || contains(job.getPositionTitle(), query)
        || contains(job.getLocation(), query)
        || contains(job.getRecruiter(), query)
        || contains(job.getSalaryRange(), query)
        || contains(job.getJobUrl(), query)
        || contains(job.getNextStep(), query)
        || contains(job.getNotes(), query);
  }

  private boolean contains(String value, String query) {
    return StringUtils.hasText(value) && value.toLowerCase(Locale.ENGLISH).contains(query);
  }

  private String normalize(String value) {
    return value == null ? "" : value.trim().toLowerCase(Locale.ENGLISH);
  }

  private String clean(String value) {
    return StringUtils.hasText(value) ? value.trim() : null;
  }

  private long countByStatus(List<JobApplication> jobs, JobStatus status) {
    return jobs.stream().filter(job -> job.getStatus() == status).count();
  }
}
