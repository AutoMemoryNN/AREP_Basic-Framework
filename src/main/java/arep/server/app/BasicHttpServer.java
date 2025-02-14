package arep.server.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.impl.bootstrap.HttpServer;
import org.apache.hc.core5.http.impl.bootstrap.ServerBootstrap;
import org.apache.hc.core5.http.io.entity.InputStreamEntity;
import org.apache.hc.core5.http.protocol.HttpContext;

public class BasicHttpServer {

    private static final int PORT = 8080;
    private HttpServer server;
    private BasicWebFramework webFramework;

    public BasicHttpServer(String[] args) {
        BasicWebFramework.initailize(args);
        this.webFramework = BasicWebFramework.getInstance();
    }

    public void start() {
        try {
            server = ServerBootstrap.bootstrap()
                    .setListenerPort(PORT)
                    .register("*", this::handleRequest)
                    .create();

            server.start();
            System.out.println("Apache HTTP Server started on port " + PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleRequest(ClassicHttpRequest request, ClassicHttpResponse response, HttpContext context) {
        String method = request.getMethod();
        if (method.equals("GET")) {
            getRequestHandlers(request, response, context);
        } else if (method.equals("POST")) {
            postRequestHandlers(request, response, context);

        } else {
            response.setCode(404);
        }

    }

    private void postRequestHandlers(ClassicHttpRequest request, ClassicHttpResponse response, HttpContext context) {
        Map<String, String> queryParams = getQueryParams(request);
        String path = "";
        try {
            path = request.getUri().getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        this.webFramework.invokeControllerMethod("POST", path, queryParams, response);
    }

    private void getRequestHandlers(ClassicHttpRequest request, ClassicHttpResponse response, HttpContext context) {
        Map<String, String> queryParams = getQueryParams(request);
        String path = "";
        try {
            path = request.getUri().getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        if (path.startsWith("/static/")) {
            System.out.println("Serving static file: " + path);
            serveStaticFile(path, response);
        } else {
            this.webFramework.invokeControllerMethod("GET", path, queryParams, response);
        }
    }

    private void serveStaticFile(String path, ClassicHttpResponse response) {
        File file = new File("src/main/resources" + path);
        if (!file.exists()) {
            System.out.println("File not found: " + file.getAbsolutePath());
            response.setCode(404);
            return;
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            response.setCode(200);
            response.setEntity(new InputStreamEntity(new FileInputStream(file), file.length(),
                    ContentType.create(getMimeType(path))));
        } catch (IOException e) {
            e.printStackTrace();
            response.setCode(500);
        }
    }

    private String getMimeType(String path) {
        if (path.endsWith(".png"))
            return "image/png";
        if (path.endsWith(".jpg") || path.endsWith(".jpeg"))
            return "image/jpeg";
        if (path.endsWith(".html"))
            return "text/html";
        if (path.endsWith(".css"))
            return "text/css";
        if (path.endsWith(".js"))
            return "application/javascript";
        return "application/octet-stream"; // Tipo por defecto
    }

    private Map<String, String> getQueryParams(ClassicHttpRequest request) {
        Map<String, String> queryParams = new HashMap<>();
        try {
            final String URIQqueryParams = request.getUri().getQuery();
            if (URIQqueryParams != null) {
                for (String param : URIQqueryParams.split("&")) {
                    String[] entry = param.split("=");
                    queryParams.put(entry[0], entry[1]);
                }
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return queryParams;
    }
}