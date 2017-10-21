package de.helfenkannjeder.helfomat.api.picture;

import de.helfenkannjeder.helfomat.core.ProfileRegistry;
import de.helfenkannjeder.helfomat.core.picture.DownloadService;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author Valentin Zickner
 */
@Service
@Profile("!" + ProfileRegistry.MOCK_DOWNLOAD)
public class RestDownloadService implements DownloadService {

    private final RestTemplate restTemplate;

    public RestDownloadService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public byte[] download(String url) {
        return this.restTemplate.getForObject(url, byte[].class);
    }

}
