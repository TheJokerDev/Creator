package me.thejokerdev.creator.utils;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Hastebin {
    public String paste(String content){
        final HttpClient client = HttpClient.newHttpClient();
        final HttpRequest request = HttpRequest.newBuilder(URI.create("https://www.toptal.com/developers/hastebin/documents"))
                .POST(HttpRequest.BodyPublishers.ofString(content)).build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        final String responseContent = response.body();
        final JSONObject responseJson = new JSONObject(responseContent);
        final String key = responseJson.getString("key");
        return "https://www.toptal.com/developers/hastebin/" + key;
    }
}