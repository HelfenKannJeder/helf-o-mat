package de.helfenkannjeder.helfomat;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.SocketUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class EmbeddedHttpServer {

	public static final int PORT = SocketUtils.findAvailableTcpPort();
	private static HttpServer server;
	private static Map<String, FileContentHandler> RESOURCES = new HashMap<>();

	public static void start() throws IOException {
		server = HttpServer.create(new InetSocketAddress(PORT), 0);
		server.setExecutor(null); // creates a default executor
		server.start();
	}

    public static void stop() throws IOException {
        server.stop(0);
        server = null;
    }

	public static void setContent(String url, String parameters, String resource) {
        ClassPathResource classPathResource = new ClassPathResource(resource);
        if (!RESOURCES.containsKey(url)) {
            FileContentHandler httpHandler = new FileContentHandler(classPathResource);
            RESOURCES.put(url, httpHandler);
            server.createContext(url, httpHandler);
		}

		RESOURCES.get(url).addParameters(parameters, classPathResource);
	}

	private static class FileContentHandler implements HttpHandler {

        private Map<String, ClassPathResource> resources = new HashMap<>();
        private ClassPathResource defaultResource;

        public FileContentHandler(ClassPathResource defaultResource) {
            this.defaultResource = defaultResource;
        }


        @Override
		public void handle(HttpExchange httpExchange) throws IOException {
            ClassPathResource resource = this.defaultResource;
            String query = httpExchange.getRequestURI().getQuery();
            if (resources.containsKey(query)) {
                resource = resources.get(query);
            }

            httpExchange.sendResponseHeaders(200, resource.contentLength());

			OutputStream outputStream = httpExchange.getResponseBody();
			FileCopyUtils.copy(resource.getInputStream(), outputStream);
			outputStream.close();
		}


        public void addParameters(String parameters, ClassPathResource classPathResource) {
            this.resources.put(parameters, classPathResource);
        }
    }
}
