package org.cbhs.bank;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class App {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public", Location.CLASSPATH);
            config.showJavalinBanner = false;
        });

        app.get("/", ctx -> ctx.html(loadHome()));

        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8082"));
        app.start(port);
        System.out.println("CBHS Bank running at http://localhost:" + port);
    }

    private static String loadHome() {
        try (InputStream in = App.class.getResourceAsStream("/public/pages/home.html")) {
            if (in == null) {
                return "<h1>CBHS Bank</h1><p>Under development.</p>";
            }
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "<h1>CBHS Bank</h1><p>Could not load page.</p>";
        }
    }
}
