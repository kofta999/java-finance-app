package com.kofta.app.api;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class ApiServer {

    private HttpServer server;

    public ApiServer(int port) throws IOException {
        this.server = HttpServer.create(
            new InetSocketAddress("localhost", port),
            0
        );
    }

    public void registerHandler(String path, HttpHandler handler) {
        server.createContext(path, handler);
    }

    public void start() {
        server.setExecutor(null);
        server.start();
        System.out.printf(
            "API Server Started at %s%n",
            server.getAddress().toString()
        );
    }

    public void stop() {
        server.stop(0);
    }
}
