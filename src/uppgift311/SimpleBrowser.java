package uppgift311;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class SimpleBrowser {
    public static void main(String[] args) throws Exception {
        System.out.print("Enter URL: ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String urlString = reader.readLine();

        URL url = new URL(urlString);
        BufferedReader urlReader = new BufferedReader(new InputStreamReader(url.openStream()));

        String line = "";
        while ((line = urlReader.readLine()) != null) {
            System.out.println(line);
        }

        urlReader.close();
    }
}

