package de.helfenkannjeder.helfomat.infrastructure.filesystem;

import com.google.common.base.Preconditions;
import de.helfenkannjeder.helfomat.api.picture.ResizeImageService;
import de.helfenkannjeder.helfomat.core.picture.DownloadFailedException;
import de.helfenkannjeder.helfomat.core.picture.DownloadService;
import de.helfenkannjeder.helfomat.core.picture.PictureId;
import de.helfenkannjeder.helfomat.core.picture.PictureStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

/**
 * @author Valentin Zickner
 */
@Service
@EnableConfigurationProperties(PictureConfiguration.class)
public class FileSystemPictureStorageService implements PictureStorageService {

    private static final Logger LOG = LoggerFactory.getLogger(FileSystemPictureStorageService.class);

    private final DownloadService downloadService;
    private final ResizeImageService resizeImageService;
    private final PictureConfiguration pictureConfiguration;

    public FileSystemPictureStorageService(DownloadService downloadService,
                                           ResizeImageService resizeImageService,
                                           PictureConfiguration pictureConfiguration) {
        this.downloadService = downloadService;
        this.resizeImageService = resizeImageService;
        this.pictureConfiguration = pictureConfiguration;
    }

    public PictureId savePicture(String url, PictureId pictureId) throws DownloadFailedException {
        try {
            byte[] bytes = this.downloadService.download(url);
            return savePicture(bytes, pictureId);
        } catch (DownloadFailedException | RestClientException exception) {
            LOG.error("Failed to write image to filesystem url='" + url + "' picture='" + pictureId + "'", exception);
            throw new DownloadFailedException(exception);
        }
    }

    public PictureId savePicture(byte[] bytes, PictureId pictureId) throws DownloadFailedException {
        try {
            Path path = createPath(pictureId.getValue());
            if (bytes == null) {
                throw new DownloadFailedException();
            }

            Files.write(path, bytes);
            scalePicture(pictureId, path);

            return pictureId;
        } catch (IOException | InvalidPathException | RestClientException exception) {
            LOG.error("Failed to write image to filesystem picture='" + pictureId + "'", exception);
            throw new DownloadFailedException(exception);
        }
    }

    @Override
    public PictureId savePicture(PictureId pictureId, InputStream inputStream) throws DownloadFailedException {
        try {
            Path path = createPath(pictureId.getValue());
            Files.copy(inputStream, path);
            scalePicture(pictureId, path);
            return pictureId;
        } catch (IOException exception) {
            throw new DownloadFailedException(exception);
        }
    }

    public Path getPicture(PictureId pictureId) {
        return Paths.get(this.pictureConfiguration.getPictureFolder(), pictureId.getValue());
    }

    public Path getPicture(PictureId pictureId, String size) {
        Preconditions.checkArgument(Pattern.compile("^[a-z\\-]+$").matcher(size).matches());
        return Paths.get(this.pictureConfiguration.getPictureFolder(), size, pictureId.getValue());
    }

    @Override
    public boolean existPicture(PictureId pictureId) {
        return Files.exists(getPicture(pictureId));
    }

    private void scalePicture(PictureId pictureId, Path path) throws IOException {
        for (PictureConfiguration.PictureSize pictureSize : pictureConfiguration.getPictureSizes()) {
            Path outputFile = createPath(pictureSize.getName(), pictureId.getValue());
            resizeImageService.resize(path, outputFile, pictureSize.getWidth(), pictureSize.getHeight());
        }
    }

    private Path createPath(String... folder) throws IOException {
        Path path = Paths.get(pictureConfiguration.getPictureFolder(), folder);
        if (!path.getParent().toFile().exists()) {
            Files.createDirectories(path.getParent());
        }
        return path;
    }
}
