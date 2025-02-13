package arep.server.app;

import java.io.InputStreamReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.impl.bootstrap.HttpServer;
import org.apache.hc.core5.http.impl.bootstrap.ServerBootstrap;
import org.apache.hc.core5.http.protocol.HttpContext;

public class BasicHttpServer {

    private static final int PORT = 8080;
    private boolean running = false;
    private HttpServer server;

    public BasicHttpServer(String[] args) {
        start();
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

    }
}