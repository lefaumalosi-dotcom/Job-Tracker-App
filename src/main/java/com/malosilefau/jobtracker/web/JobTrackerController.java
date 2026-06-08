package com.malosilefau.jobtracker.web;

import com.malosilefau.jobtracker.domain.JobApplication;
import com.malosilefau.jobtracker.domain.JobPriority;
import com.malosilefau.jobtracker.domain.JobStatus;
import com.malosilefau.jobtracker.dto.JobApplicationForm;
import com.malosilefau.jobtracker.service.JobApplicationService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping
public class JobTrackerController {

  private final JobApplicationService service;

  public JobTrackerController(JobApplicationService service) {
    this.service = service;
  }

  @ModelAttribute("statuses")
  public JobStatus[] statuses() {
    return JobStatus.values();
  }

  @ModelAttribute("priorities")
  public JobPriority[] priorities() {
    return JobPriority.values();
  }

  @GetMapping({"/", "/jobs"})
  public String dashboard(
      @RequestParam(required = false) String q,
      @RequestParam(required = false) String status,
      @RequestParam(required = false) String priority,
      Model model) {
    List<JobApplication> allJobs = service.findAllSorted();
    JobStatus statusFilter = parseStatus(status);
    JobPriority priorityFilter = parsePriority(priority);
    List<JobApplication> jobs = service.filterJobs(allJobs, q, statusFilter, priorityFilter);

    model.addAttribute("jobs", jobs);
    model.addAttribute("stats", service.buildStats(allJobs));
    model.addAttribute("query", q == null ? "" : q);
    model.addAttribute("selectedStatus", statusFilter);
    model.addAttribute("selectedPriority", priorityFilter);
    model.addAttribute("resultsLabel", jobs.size() + " tracked role" + (jobs.size() == 1 ? "" : "s"));
    model.addAttribute("hasFilters", hasFilters(q, statusFilter, priorityFilter));
    return "dashboard";
  }

  @GetMapping("/jobs/new")
  public String newJob(Model model) {
    model.addAttribute("form", new JobApplicationForm());
    model.addAttribute("pageTitle", "Add Job");
    model.addAttribute("formSubtitle", "Capture a new opportunity before it gets lost in your inbox.");
    model.addAttribute("submitLabel", "Save Job");
    return "job-form";
  }

  @GetMapping("/jobs/{id}/edit")
  public String editJob(@PathVariable Long id, Model model) {
    JobApplication job = service.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found"));
    model.addAttribute("form", service.toForm(job));
    model.addAttribute("pageTitle", "Edit Job");
    model.addAttribute("formSubtitle", "Update the details, timing, and next step for this role.");
    model.addAttribute("submitLabel", "Update Job");
    return "job-form";
  }

  @PostMapping("/jobs")
  public String createJob(
      @Valid @ModelAttribute("form") JobApplicationForm form,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("pageTitle", "Add Job");
      model.addAttribute("formSubtitle", "Capture a new opportunity before it gets lost in your inbox.");
      model.addAttribute("submitLabel", "Save Job");
      return "job-form";
    }

    service.save(form);
    redirectAttributes.addFlashAttribute("flashMessage", "Job saved successfully.");
    return "redirect:/jobs";
  }

  @PostMapping("/jobs/{id}")
  public String updateJob(
      @PathVariable Long id,
      @Valid @ModelAttribute("form") JobApplicationForm form,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("pageTitle", "Edit Job");
      model.addAttribute("formSubtitle", "Update the details, timing, and next step for this role.");
      model.addAttribute("submitLabel", "Update Job");
      return "job-form";
    }

    service.update(id, form);
    redirectAttributes.addFlashAttribute("flashMessage", "Job updated successfully.");
    return "redirect:/jobs";
  }

  @PostMapping("/jobs/{id}/delete")
  public String deleteJob(@PathVariable Long id, RedirectAttributes redirectAttributes) {
    service.delete(id);
    redirectAttributes.addFlashAttribute("flashMessage", "Job removed from the tracker.");
    return "redirect:/jobs";
  }

  @PostMapping("/jobs/{id}/status")
  public String updateStatus(
      @PathVariable Long id,
      @RequestParam JobStatus status,
      RedirectAttributes redirectAttributes) {
    service.updateStatus(id, status);
    redirectAttributes.addFlashAttribute("flashMessage", "Job status updated.");
    return "redirect:/jobs";
  }

  private JobStatus parseStatus(String value) {
    if (value == null || value.isBlank()) {
      return null;
    }

    try {
      return JobStatus.valueOf(value);
    } catch (IllegalArgumentException ex) {
      return null;
    }
  }

  private JobPriority parsePriority(String value) {
    if (value == null || value.isBlank()) {
      return null;
    }

    try {
      return JobPriority.valueOf(value);
    } catch (IllegalArgumentException ex) {
      return null;
    }
  }

  private boolean hasFilters(String query, JobStatus status, JobPriority priority) {
    return (query != null && !query.isBlank()) || status != null || priority != null;
  }
}
