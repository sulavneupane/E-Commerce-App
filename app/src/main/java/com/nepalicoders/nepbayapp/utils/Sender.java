package com.nepalicoders.nepbayapp.utils;

/**
 * Created by sulav on 12/28/15.
 */
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * An Example Class to use for the submission using HTTP API You can perform * your own validations into this Class For username, password,destination,
 * source, dlr, type, message, server and port
 **/

public class Sender {

    // Username that is to be used for submission
    String username;
    // password that is to be used along with username
    String password;
    // Message content that is to be transmitted
    String message;
    /**
     * What type of the message that is to be sent
     * <ul>
     * <li>0:means plain text</li>
     * <li>1:means flash</li>
     * <li>2:means Unicode (Message content should be in Hex)</li>
     * <li>6:means Unicode Flash (Message content should be in Hex)</li>
     * </ul>
     */
    String type;
    /**
     * Require DLR or not
     * <ul>
     * <li>0:means DLR is not Required</li>
     * <li>1:means DLR is Required</li>
     * </ul>
     */
    String dlr;
    /**
     * Destinations to which message is to be sent For submitting more than one * destination at once destinations should be comma separated Like
     * 91999000123,91999000124
     */
    String destination;

    // Sender Id to be used for submitting the message
    String source;
    // To what server you need to connect to for submission
    String server;
    // Port that is to be used like 8080 or 8000
    int port;

    public Sender(String server, int port, String username, String password, String message, String dlr, String type, String destination,
                  String source) {
        this.username = username;
        this.password = password;
        this.message = message;
        this.dlr = dlr;
        this.type = type;
        this.destination = destination;
        this.source = source;
        this.server = server;
        this.port = port;
    }

    public void submitMessage() {
        try {
            Log.d("RegistrationStatus", "Message Submitted!");
            // Url that will be called to submit the message
            URL sendUrl = new URL("http://" + this.server + ":" + this.port + "/bulksms/bulksms");
            HttpURLConnection httpConnection = (HttpURLConnection) sendUrl.openConnection();

            Log.d("RegistrationStatus", "Connection Established!");

            // This method sets the method type to POST so that
            // will be send as a POST request
            httpConnection.setRequestMethod("POST");

            // This method is set as true wince we intend to send
            // input to the server
            httpConnection.setDoInput(true);

            // This method implies that we intend to receive data from server.
            httpConnection.setDoOutput(true);

            // Implies do not use cached data
            httpConnection.setUseCaches(false);

            // Data that will be sent over the stream to the server.
            Log.d("RegistrationStatus", "Set Do Input & Output!");

            // Data that will be sent over the stream to the server.
            DataOutputStream dataStreamToServer = new DataOutputStream(httpConnection.getOutputStream());

            Log.d("RegistrationStatus", "DataOutputStream created!");

            dataStreamToServer.writeBytes("username="
                    + Strings.urlEncode(this.username) + "&password="
                    + Strings.urlEncode(this.password) + "&type="
                    + Strings.urlEncode(this.type) + "&dlr="
                    + Strings.urlEncode(this.dlr) + "&destination="
                    + Strings.urlEncode(this.destination) + "&source="
                    + Strings.urlEncode(this.source) + "&message="
                    + Strings.urlEncode(this.message));

            Log.d("RegistrationStatus", "DataOutputStream writeBytes!");

            dataStreamToServer.flush();

            dataStreamToServer.close();

            Log.d("RegistrationStatus", "Data Output Stream Configured!");

            // Here take the output value of the server.
            BufferedReader dataStreamFromUrl = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
            String dataFromUrl = "", dataBuffer = "";

            Log.d("RegistrationStatus", "Buffered Read");
            // Writing information from the stream to the buffer
            while ((dataBuffer = dataStreamFromUrl.readLine()) != null) {
                dataFromUrl += dataBuffer;
            }

            /**
             * Now dataFromUrl variable contains the Response received from the * server so we can parse the response and process it accordingly.
             */
            dataStreamFromUrl.close();
            //System.out.println("Response: " + dataFromUrl);
            Log.d("RegistrationStatus", dataFromUrl);
        } catch (Exception ex) {
            Log.d("RegistrationStatus", ex.getMessage());
        }
    }

}
