package de.helfenkannjeder.helfomat;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import java.util.Date;

@SpringBootApplication
@EnableBatchProcessing
@EnableJpaRepositories
public class HelfomatImportApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelfomatImportApplication.class, args);
    }

    @Component
    @Profile("!test")
    static class JobRunner implements CommandLineRunner {
        private JobLauncher jobLauncher;
        private Job job;

        @Autowired
        public JobRunner(JobLauncher jobLauncher, Job job) {
            this.jobLauncher = jobLauncher;
            this.job = job;
        }

        @Override
        public void run(String... strings) throws Exception {
            JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
            jobParametersBuilder.addDate("date", new Date());
            jobLauncher.run(job, jobParametersBuilder.toJobParameters());
        }
    }
}
