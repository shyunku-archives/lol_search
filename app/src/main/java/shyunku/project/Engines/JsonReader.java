package shyunku.project.Engines;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class JsonReader {
    private static String readAllData(Reader reader) throws IOException {
        StringBuilder builder = new StringBuilder();
        int cp;
        while((cp = reader.read()) != -1){
            builder.append((char) cp);
        }
        return builder.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream inputStream = new URL(url).openStream();
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String jsonText = readAllData(reader);
            JSONObject jsonObject = new JSONObject(jsonText);
            return jsonObject;
        }finally {
            inputStream.close();
        }
    }
}
