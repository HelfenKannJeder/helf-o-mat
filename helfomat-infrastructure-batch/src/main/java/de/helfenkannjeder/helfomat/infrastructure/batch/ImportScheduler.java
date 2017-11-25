package de.helfenkannjeder.helfomat.infrastructure.batch;

import de.helfenkannjeder.helfomat.api.HelfomatConfiguration;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ImportScheduler {
    private static final Logger LOGGER = Logger.getLogger(ImportScheduler.class);

    private final ImportJobRunnerService importJobRunnerService;
    private final HelfomatConfiguration helfomatConfiguration;

    public ImportScheduler(ImportJobRunnerService importJobRunnerService, HelfomatConfiguration helfomatConfiguration) {
        this.importJobRunnerService = importJobRunnerService;
        this.helfomatConfiguration = helfomatConfiguration;
    }

    @Scheduled(cron = "#{@helfomatConfiguration.autoImport.schedule}")
    public void scheduledImport() {
        if(helfomatConfiguration.getAutoImport().isEnabled()) {
            LOGGER.info("Scheduled import started");
            importJobRunnerService.run();
        }
    }
}
