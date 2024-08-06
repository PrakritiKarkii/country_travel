package org.example.countrytravel;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CountryController {

    @FXML
    private TextField searchField; // Input field for country search

    @FXML
    private ListView<String> countryListView; // List view to display country names

    @FXML
    private ImageView flagImageView; // Image view to display the country's flag

    @FXML
    private ImageView countryImageView; // Image view to display a country image

    @FXML
    private VBox countryDetailsVBox; // VBox to show country details

    @FXML
    private Text capitalText; // Text to display the country's capital

    @FXML
    private Text regionText; // Text to display the country's region

    @FXML
    private Text subregionText; // Text to display the country's subregion

    @FXML
    private Text populationText; // Text to display the country's population

    @FXML
    private VBox rootVBox; // Root VBox for layout

    @FXML
    private void initialize() {
        // Set up a listener for selection changes in the countryListView
        countryListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                handleCountrySelection(newVal); // Handle the selected country
            }
        });
    }

    @FXML
    private void handleSearch() {
        // Fetch countries based on the text entered in the searchField
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            showAlert("Input Error", "Please enter a country name.");
            return;
        }
        fetchCountries(query); // Call method to fetch country data
    }

    private void fetchCountries(String query) {
        try {
            // Construct the URL for the REST API request
            URL url = new URL("https://restcountries.com/v3.1/name/" + query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse the JSON response and update the ListView
            Gson gson = new Gson();
            JsonArray jsonArray = JsonParser.parseString(response.toString()).getAsJsonArray();
            countryListView.getItems().clear();

            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                String name = jsonObject.get("name").getAsJsonObject().get("common").getAsString();
                countryListView.getItems().add(name);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while fetching country data.");
        }
    }

    private void handleCountrySelection(String countryName) {
        try {
            // Fetch details for the selected country
            URL url = new URL("https://restcountries.com/v3.1/name/" + countryName);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse the JSON response and update UI elements
            Gson gson = new Gson();
            JsonArray jsonArray = JsonParser.parseString(response.toString()).getAsJsonArray();
            JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();

            String name = jsonObject.get("name").getAsJsonObject().get("common").getAsString();
            String capital = jsonObject.has("capital") ? jsonObject.get("capital").getAsJsonArray().get(0).getAsString() : "N/A";
            String region = jsonObject.has("region") ? jsonObject.get("region").getAsString() : "N/A";
            String subregion = jsonObject.has("subregion") ? jsonObject.get("subregion").getAsString() : "N/A";
            int population = jsonObject.has("population") ? jsonObject.get("population").getAsInt() : 0;
            String flagUrl = jsonObject.get("flags").getAsJsonObject().get("png").getAsString();
            String imageUrl = fetchCountryImage(countryName);

            // Update UI with country details
            capitalText.setText("Capital: " + capital);
            regionText.setText("Region: " + region);
            subregionText.setText("Subregion: " + subregion);
            populationText.setText("Population: " + population);

            if (flagUrl != null) {
                flagImageView.setImage(new Image(flagUrl));
            }
            if (imageUrl != null) {
                countryImageView.setImage(new Image(imageUrl));
            }

            // Show the VBox with details
            countryDetailsVBox.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while fetching country details.");
        }
    }

    private String fetchCountryImage(String countryName) {
        try {
            // Construct the URL for Unsplash API request
            String unsplashUrl = "https://api.unsplash.com/search/photos?query=" + countryName + "&client_id=pejkxVMTZQQray2bT4-S7ajocLQjkyGMYWovDsgfibQ";
            URL url = new URL(unsplashUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse the JSON response to get the image URL
            JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
            JsonArray results = jsonObject.getAsJsonArray("results");
            if (results.size() > 0) {
                JsonObject firstResult = results.get(0).getAsJsonObject();
                return firstResult.getAsJsonObject("urls").get("regular").getAsString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Return null if no image found
    }

    private void showAlert(String title, String message) {
        // Show an alert dialog with the given title and message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
