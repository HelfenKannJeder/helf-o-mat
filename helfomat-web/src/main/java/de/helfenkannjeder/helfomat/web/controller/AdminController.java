package de.helfenkannjeder.helfomat.web.controller;

import de.helfenkannjeder.helfomat.infrastructure.batch.ImportJobRunnerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Valentin Zickner
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final ImportJobRunnerService importJobRunnerService;

    public AdminController(ImportJobRunnerService importJobRunnerService) {
        this.importJobRunnerService = importJobRunnerService;
    }

    @PostMapping("/import")
    public void startImport() {
        this.importJobRunnerService.run();
    }

}
