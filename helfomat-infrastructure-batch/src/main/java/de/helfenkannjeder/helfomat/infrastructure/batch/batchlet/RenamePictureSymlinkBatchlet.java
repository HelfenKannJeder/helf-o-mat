package de.helfenkannjeder.helfomat.infrastructure.batch.batchlet;

import de.helfekannjeder.helfomat.core.IndexManager;
import de.helfenkannjeder.helfomat.api.HelfomatConfiguration;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.stereotype.Component;

import javax.batch.api.AbstractBatchlet;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.batch.core.ExitStatus.COMPLETED;

/**
 * @author Valentin Zickner
 */
@Component
@JobScope
public class RenamePictureSymlinkBatchlet extends AbstractBatchlet {

    private final HelfomatConfiguration helfomatConfiguration;
    private final IndexManager indexManager;

    public RenamePictureSymlinkBatchlet(HelfomatConfiguration helfomatConfiguration, IndexManager indexManager) {
        this.helfomatConfiguration = helfomatConfiguration;
        this.indexManager = indexManager;
    }

    @Override
    public String process() throws Exception {
        Path alias = Paths.get(helfomatConfiguration.getPictureFolder(), indexManager.getAlias());
        if (Files.isSymbolicLink(alias)) {
            Files.delete(alias);
        }

        Files.createSymbolicLink(alias, Paths.get(indexManager.getCurrentIndex()));
        return COMPLETED.toString();
    }

}
