package com.malosilefau.jobtracker.repository;

import com.malosilefau.jobtracker.domain.JobApplication;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JobApplicationRepository
    extends JpaRepository<JobApplication, Long>, JpaSpecificationExecutor<JobApplication> {

  List<JobApplication> findAllByOrderByUpdatedAtDesc();
}
