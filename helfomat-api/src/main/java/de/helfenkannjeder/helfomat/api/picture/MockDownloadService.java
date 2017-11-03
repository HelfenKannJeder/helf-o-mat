package de.helfenkannjeder.helfomat.api.picture;

import de.helfenkannjeder.helfomat.core.ProfileRegistry;
import de.helfenkannjeder.helfomat.core.picture.DownloadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Valentin Zickner
 */
@Service
@Profile(ProfileRegistry.MOCK_DOWNLOAD)
public class MockDownloadService implements DownloadService {

    @Value("classpath:dummy.jpg")
    private Resource dummyPicture;

    @Override
    public byte[] download(String url) {
        try {
            return Files.readAllBytes(Paths.get(dummyPicture.getURI()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
