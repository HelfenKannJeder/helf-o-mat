package de.helfenkannjeder.helfomat.api.picture;

import com.google.common.base.Preconditions;
import de.helfenkannjeder.helfomat.api.HelfomatConfiguration;
import de.helfenkannjeder.helfomat.core.picture.DownloadFailedException;
import de.helfenkannjeder.helfomat.core.picture.DownloadService;
import de.helfenkannjeder.helfomat.core.picture.PictureId;
import de.helfenkannjeder.helfomat.core.picture.PictureRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

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
            return savePicture(bytes, folder, pictureId);
        } catch (DownloadFailedException | RestClientException exception) {
            LOG.error("Failed to write image to filesystem url='" + url + "' picture='" + pictureId + "'", exception);
            throw new DownloadFailedException(exception);
        }
    }

    public PictureId savePicture(byte[] bytes, String folder, PictureId pictureId) throws DownloadFailedException {
        try {
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
        } catch (IOException | InvalidPathException | RestClientException exception) {
            LOG.error("Failed to write image to filesystem picture='" + pictureId + "'", exception);
            throw new DownloadFailedException(exception);
        }
    }

    public Path getPicture(PictureId pictureId) {
        return Paths.get(this.helfomatConfiguration.getPictureFolder(), "helfomat", pictureId.getValue());
    }

    public Path getPicture(PictureId pictureId, String size) {
        Preconditions.checkArgument(Pattern.compile("^[a-z\\-]+$").matcher(size).matches());
        return Paths.get(this.helfomatConfiguration.getPictureFolder(), "helfomat", size, pictureId.getValue());
    }

    @Override
    public boolean existPicture(PictureId pictureId) {
        return Files.exists(getPicture(pictureId));
    }

    private Path createPath(String... folder) throws IOException {
        Path path = Paths.get(helfomatConfiguration.getPictureFolder(), folder);
        Files.createDirectories(path.getParent());
        return path;
    }
}
