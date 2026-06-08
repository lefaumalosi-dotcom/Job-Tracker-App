package com.malosilefau.jobtracker.config;

import com.malosilefau.jobtracker.domain.JobApplication;
import com.malosilefau.jobtracker.domain.JobPriority;
import com.malosilefau.jobtracker.domain.JobStatus;
import com.malosilefau.jobtracker.repository.JobApplicationRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

  @Bean
  CommandLineRunner seedDatabase(JobApplicationRepository repository) {
    return args -> {
      if (repository.count() > 0) {
        return;
      }

      repository.saveAll(
          List.of(
              job("Northstar Health", "Product Manager", "Remote", "https://northstar.example/jobs/pm",
                  "Alicia", "$135k-$155k", JobStatus.APPLIED, JobPriority.HIGH,
                  LocalDate.now().minusDays(8), LocalDate.now().plusDays(2),
                  "Follow up with hiring manager", "Tailor resume toward health-tech workflow ownership."),
              job("Lantern AI", "Senior Backend Engineer", "Austin, TX", "https://lantern.example/jobs/backend",
                  "Marcus", "$150k-$180k", JobStatus.INTERVIEW, JobPriority.HIGH,
                  LocalDate.now().minusDays(16), LocalDate.now().plusDays(1),
                  "Prepare architecture stories", "Strong fit for Java and service design."),
              job("Koa Logistics", "Operations Analyst", "Honolulu, HI", "https://koa.example/jobs/analyst",
                  "Nina", "$82k-$95k", JobStatus.WISHLIST, JobPriority.MEDIUM,
                  null, null, "Research the team and company metrics", "Keep on radar for next hiring cycle."),
              job("Harbor Creative", "Frontend Developer", "San Diego, CA", "https://harbor.example/jobs/frontend",
                  "Jordan", "$118k-$140k", JobStatus.OFFER, JobPriority.HIGH,
                  LocalDate.now().minusDays(24), null,
                  "Review offer details", "Current top opportunity. Compare against other interviews."),
              job("Summit Labs", "Data Scientist", "Seattle, WA", "https://summit.example/jobs/data-scientist",
                  "Rhea", "$145k-$170k", JobStatus.REJECTED, JobPriority.LOW,
                  LocalDate.now().minusDays(30), null,
                  "Archive and note feedback", "Good experience, but not moving forward."),
              job("Makai Systems", "Java Engineer", "Remote", "https://makai.example/jobs/java",
                  "Tane", "$125k-$150k", JobStatus.APPLIED, JobPriority.MEDIUM,
                  LocalDate.now().minusDays(3), LocalDate.now().plusDays(4),
                  "Share Spring Boot project example", "Strong alignment with backend work and API design.")));
    };
  }

  private JobApplication job(
      String company,
      String title,
      String location,
      String url,
      String recruiter,
      String salary,
      JobStatus status,
      JobPriority priority,
      LocalDate appliedDate,
      LocalDate followUpDate,
      String nextStep,
      String notes) {
    JobApplication job = new JobApplication();
    job.setCompanyName(company);
    job.setPositionTitle(title);
    job.setLocation(location);
    job.setJobUrl(url);
    job.setRecruiter(recruiter);
    job.setSalaryRange(salary);
    job.setStatus(status);
    job.setPriority(priority);
    job.setAppliedDate(appliedDate);
    job.setFollowUpDate(followUpDate);
    job.setNextStep(nextStep);
    job.setNotes(notes);
    return job;
  }
}
