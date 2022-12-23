package com.application.base.services.internet;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import com.application.base.main.SelectionObserver;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InternetService implements SelectionObserver {

    private MyWebSocketClient myWebSocketClient;

    private static final String LINK_LIST_OF_SESSIONS = "http://159.69.196.15:8000/cars/sessions/";
    private static final String LINK_LIST_OF_CARS_IN_SESSION = "http://159.69.196.15:8000/cars/sessions/";
    private static final String LINK_LIST_FOR_SUBMITTING_COMMANDS = "http://159.69.196.15:8000/cars/sessions/";

    public InternetService() {
        this.myWebSocketClient = null;
    }

    public void connectToWebSocket(String internetAddress) {
        if (myWebSocketClient != null) {
            closeConnectionToWebSocket();
        }
        myWebSocketClient = new MyWebSocketClient();
       // myWebSocketClient.openConnection(internetAddress);
    }

    public void closeConnectionToWebSocket() {
        if (myWebSocketClient == null) {
            return;
        }
       // myWebSocketClient.closeConnection();
        myWebSocketClient = null;
    }

    public List<String> getListOfSessions() {
        try {
            String json = getGetRequestBody(LINK_LIST_OF_SESSIONS);

            ArrayList strings = jsonStringToListOfKeys(json);

            return strings;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public List<String> getListOfCarsInSession(String idString) {
        try {
            String json = getGetRequestBody(LINK_LIST_OF_CARS_IN_SESSION + idString + "/");

            ArrayList strings = pairToListOfValues(json);

            return strings;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    private static String getGetRequestBody(String internetAddress) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(internetAddress).openConnection();
        connection.setRequestMethod("GET");
        connection.addRequestProperty("Accept", "application/json");

        int responseCode = connection.getResponseCode();

        if (responseCode != 200) {
            throw new Exception("Connection to Server failed.");
        }

        String json = "";
        Scanner scanner = new Scanner(connection.getInputStream());

        while (scanner.hasNext()) {
            json += scanner.nextLine();
        }
        scanner.close();

        return json;
    }

    private ArrayList<String> jsonStringToListOfKeys(String jsonString) throws JSONException {

        ArrayList<String> listOfKeys = new ArrayList<>();

        JSONArray jsonArray = new JSONArray(jsonString);

        for (int i = 0; i < jsonArray.length(); i++) {
            listOfKeys.add(jsonArray.getJSONObject(i).getString("id"));
        }
        return listOfKeys;
    }

    private ArrayList<String> pairToListOfValues(String jsonString) throws JSONException {

        ArrayList<String> listOfValues = new ArrayList<>();

        JSONObject object = new JSONObject(jsonString);

        JSONArray jsonArray = (JSONArray) object.get("cars");

        for (int i = 0; i < jsonArray.length(); i++) {
            listOfValues.add(String.valueOf(jsonArray.getInt(i)));
        }

        return listOfValues;
    }

    public void sendCommandToServer(String command) {
        if (command.isBlank() || myWebSocketClient == null) {
            return;
        }
     //   myWebSocketClient.ensureConnection();
     //   myWebSocketClient.sendCommand(command);
    }

    @Override
    public void selectionChanged(String newValue) {
        if (newValue.isBlank()) {
            return;
        }
        connectToWebSocket(LINK_LIST_FOR_SUBMITTING_COMMANDS + newValue + "/");
    }
}
