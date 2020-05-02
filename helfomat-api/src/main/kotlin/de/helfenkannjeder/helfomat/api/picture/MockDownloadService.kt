package de.helfenkannjeder.helfomat.api.picture

import de.helfenkannjeder.helfomat.core.ProfileRegistry
import de.helfenkannjeder.helfomat.core.picture.DownloadService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

/**
 * @author Valentin Zickner
 */
@Service
@Profile(ProfileRegistry.MOCK_DOWNLOAD)
class MockDownloadService : DownloadService {

    @Value("classpath:dummy.jpg")
    private lateinit var dummyPicture: Resource

    override fun download(url: String): ByteArray {
        return try {
            Files.readAllBytes(Paths.get(dummyPicture.uri))
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

}