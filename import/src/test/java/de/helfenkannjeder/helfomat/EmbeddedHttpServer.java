package de.helfenkannjeder.helfomat;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.SocketUtils;

public class EmbeddedHttpServer {

	public static final int PORT = SocketUtils.findAvailableTcpPort();
	private static HttpServer server;

	public static void start() throws IOException {
		server = HttpServer.create(new InetSocketAddress(PORT), 0);
		server.setExecutor(null); // creates a default executor
		server.start();
	}

	public static void setContent(String url, String resource) {
		server.createContext(url, new FileContentHandler(new ClassPathResource(resource)));
	}

	private static class FileContentHandler implements HttpHandler {
		private Resource resource;

		FileContentHandler(Resource resource) {
			this.resource = resource;
		}

		@Override
		public void handle(HttpExchange t) throws IOException {
			t.sendResponseHeaders(200, resource.contentLength());
			OutputStream outputStream = t.getResponseBody();
			FileCopyUtils.copy(resource.getInputStream(), outputStream);
			outputStream.close();
		}
	}
}
