package de.helfenkannjeder.helfomat.rest;

import de.helfenkannjeder.helfomat.config.ImporterConfiguration;
import de.helfenkannjeder.helfomat.core.picture.DownloadFailedException;
import de.helfenkannjeder.helfomat.core.picture.PictureId;
import de.helfenkannjeder.helfomat.core.picture.PictureStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * @author Valentin Zickner
 */
@Component
public class RestPictureStorageService implements PictureStorageService {

    private final RestTemplate restTemplate;
    private final ImporterConfiguration importerConfiguration;

    public RestPictureStorageService(RestTemplate restTemplate, ImporterConfiguration importerConfiguration) {
        this.restTemplate = restTemplate;
        this.importerConfiguration = importerConfiguration;
    }

    @Override
    public PictureId savePicture(String url, PictureId pictureId) throws DownloadFailedException {
        RestTemplate plainRestTemplate = new RestTemplate();
        try {
            HttpEntity<Object> httpEntity = new HttpEntity<>(new HttpHeaders());
            ResponseEntity<Resource> responseEntity = plainRestTemplate.exchange(url, HttpMethod.GET, httpEntity, Resource.class);
            Resource body = responseEntity.getBody();
            if (body == null) {
                return null;
            }
            InputStream inputStream = body.getInputStream();
            return savePicture(inputStream.readAllBytes(), pictureId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PictureId savePicture(byte[] bytes, PictureId pictureId) throws DownloadFailedException {
        // it seems to be unnecessary hard to upload a file....
        // see https://medium.com/red6-es/uploading-a-file-with-a-filename-with-spring-resttemplate-8ec5e7dc52ca for mor details
        MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
        ContentDisposition contentDisposition = ContentDisposition
            .builder("form-data")
            .name("file")
            .filename("image")
            .build();
        fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
        HttpEntity<byte[]> fileEntity = new HttpEntity<>(bytes, fileMap);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileEntity);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        restTemplate.exchange(importerConfiguration.getWebApiUrl() + "/api/picture/" + pictureId.getValue(), HttpMethod.POST, requestEntity, Void.class);
        return pictureId;
    }

    @Override
    public PictureId savePicture(PictureId pictureId, InputStream inputStream) throws DownloadFailedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean existPicture(PictureId pictureId) {
        if (pictureId == null) {
            return false;
        }
        try {
            this.restTemplate.headForHeaders(importerConfiguration.getWebApiUrl() + "/api/picture/" + pictureId.getValue());
            return true;
        } catch (HttpClientErrorException restClientException) {
            return false;
        }
    }

    @Override
    public Path getPicture(PictureId pictureId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Path getPicture(PictureId pictureId, String size) {
        throw new UnsupportedOperationException();
    }

}
