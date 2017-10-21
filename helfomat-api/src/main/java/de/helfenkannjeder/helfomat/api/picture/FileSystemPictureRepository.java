package de.helfenkannjeder.helfomat.api.picture;

import de.helfekannjeder.helfomat.core.organisation.PictureId;
import de.helfekannjeder.helfomat.core.picture.DownloadFailedException;
import de.helfekannjeder.helfomat.core.picture.DownloadService;
import de.helfekannjeder.helfomat.core.picture.PictureRepository;
import de.helfenkannjeder.helfomat.api.HelfomatConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Valentin Zickner
 */
@Service
public class FileSystemPictureRepository implements PictureRepository {

    private static final Logger LOG = LoggerFactory.getLogger(FileSystemPictureRepository.class);

    private final DownloadService downloadService;
    private final ResizeImageService resizeImageService;
    private final HelfomatConfiguration helfomatConfiguration;

    public FileSystemPictureRepository(DownloadService downloadService,
                                       ResizeImageService resizeImageService,
                                       HelfomatConfiguration helfomatConfiguration) {
        this.downloadService = downloadService;
        this.resizeImageService = resizeImageService;
        this.helfomatConfiguration = helfomatConfiguration;
    }

    public PictureId savePicture(String url, String folder, PictureId pictureId) throws DownloadFailedException {
        try {
            byte[] bytes = this.downloadService.download(url);
            Path path = createPath(folder, pictureId.getValue());
            if (bytes == null) {
                throw new DownloadFailedException();
            }

            Files.write(path, bytes);
            for (HelfomatConfiguration.PictureSize pictureSize : helfomatConfiguration.getPictureSizes()) {
                Path outputFile = createPath(folder, pictureSize.getName(), pictureId.getValue());
                resizeImageService.resize(path, outputFile, pictureSize.getWidth(), pictureSize.getHeight());
            }

            return pictureId;
        } catch (IOException | RestClientException exception) {
            LOG.error("Failed to write image to filesystem", exception);
            throw new DownloadFailedException(exception);
        }
    }

    private Path createPath(String... folder) throws IOException {
        Path path = Paths.get(helfomatConfiguration.getPictureFolder(), folder);
        Files.createDirectories(path.getParent());
        return path;
    }
}
