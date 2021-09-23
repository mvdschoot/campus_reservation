package nl.tudelft.oopp.demo.communication;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

import nl.tudelft.oopp.demo.general.GeneralMethods;

public class GeneralCommunication {

    private static Logger logger = Logger.getLogger("GlobalLogger");
    private static HttpClient client = HttpClient.newBuilder().build();

    /**
     * Sends a HTTP POST request to the server with the specified parameters.
     *
     * @param endpoint The endpoint on the server to send the request to.
     * @param params   The parameters to add to the url.
     * @return boolean true if communication was successful, false otherwise
     */
    protected static boolean sendPost(String endpoint, String params) {
        HttpResponse<String> response = null;
        try {
            params = GeneralMethods.encodeCommunication(params);

            HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.noBody())
                    .uri(URI.create("http://localhost:8080/" + endpoint + "?" + params)).build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
            return false;
        }
        if (response.statusCode() != 200) {
            logger.log(Level.SEVERE, "Server responded with status code: " + response.statusCode());
            return false;
        }
        return true;
    }

    /**
     * Sends a HTTP GET request to the server with the specified parameters and returns the response.
     *
     * @param endpoint The endpoint on the server to send the request to.
     * @param params   The parameters to add to the url.
     * @return Returns the response
     */
    protected static String sendGet(String endpoint, String params) {
        HttpResponse<String> response = null;
        try {
            params = GeneralMethods.encodeCommunication(params);

            HttpRequest request = HttpRequest.newBuilder().GET()
                    .uri(URI.create("http://localhost:8080/" + endpoint + "?" + params)).build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
            return null;
        }
        if (response.statusCode() != 200) {
            logger.log(Level.SEVERE, "Server responded with status code: " + response.statusCode());
        }
        return response.body();
    }
}
