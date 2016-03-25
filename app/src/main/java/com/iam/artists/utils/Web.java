package com.iam.artists.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * util class Web contains data and functions for simplify web operations (on app increase we can store here domains, functions for server requests and other)
 */

public class Web {

    public static String domain = "http://cache-default03d.cdn.yandex.net/download.cdn.yandex.net/mobilization-2016/artists.json";

    public static String httpGet(String sUrl) {
        try {
            StringBuilder content = new StringBuilder();

            URL url = new URL(sUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line);
            }
            bufferedReader.close();
            return content.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
