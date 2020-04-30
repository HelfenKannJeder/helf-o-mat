package de.helfenkannjeder.helfomat.infrastructure.filesystem

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import org.springframework.core.io.ClassPathResource
import org.springframework.util.FileCopyUtils
import org.springframework.util.SocketUtils
import java.net.InetSocketAddress
import java.util.*

class EmbeddedHttpServer {

    val port = SocketUtils.findAvailableTcpPort()
    private var server: HttpServer? = null
    private val RESOURCES: MutableMap<String, FileContentHandler> = HashMap()

    fun start() {
        val newServer = HttpServer.create(InetSocketAddress(port), 0)
        newServer.setExecutor(null) // creates a default executor
        newServer.start()
        server = newServer
    }

    fun stop() {
        server?.stop(0)
        server = null
    }

    fun setContent(url: String, parameters: String?, resource: String) {
        val classPathResource = ClassPathResource(resource)
        if (!RESOURCES.containsKey(url)) {
            val httpHandler = FileContentHandler(classPathResource)
            RESOURCES[url] = httpHandler
            server?.createContext(url, httpHandler)
        }
        RESOURCES[url]?.addParameters(parameters, classPathResource)
    }

    private class FileContentHandler(private val defaultResource: ClassPathResource) : HttpHandler {
        private val resources: MutableMap<String?, ClassPathResource> = HashMap()

        override fun handle(httpExchange: HttpExchange) {
            var resource: ClassPathResource? = defaultResource
            val query = httpExchange.requestURI.query
            if (resources.containsKey(query)) {
                resource = resources[query]
            }
            httpExchange.sendResponseHeaders(200, resource?.contentLength() ?: 0)
            val outputStream = httpExchange.responseBody
            if (resource != null) {
                FileCopyUtils.copy(resource.inputStream, outputStream)
            }
            outputStream.close()
        }

        fun addParameters(parameters: String?, classPathResource: ClassPathResource) {
            resources[parameters] = classPathResource
        }

    }
}