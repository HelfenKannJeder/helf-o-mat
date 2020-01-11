package de.helfenkannjeder.helfomat.infrastructure.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Valentin Zickner
 */
@Service
public class ImportJobRunnerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportJobRunnerService.class);

    private final JobLauncher jobLauncher;
    private final Job job;

    public ImportJobRunnerService(JobLauncher jobLauncher, Job job) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    public void run() {
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addDate("date", new Date());
        try {
            jobLauncher.run(job, jobParametersBuilder.toJobParameters());
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            LOGGER.error("Import failed with exception", e);
            throw new ImportJobException(e);
        }
    }

}
