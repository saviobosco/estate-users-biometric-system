package application.Retrievals;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import application.Models.EstateStaff;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class EstateStaffsRetrievalService extends Service<ObservableList<EstateStaff>> {

	private String location;
	
	private String token;

	public EstateStaffsRetrievalService(String loc, String token) {
		this.location = loc;
		this.token = token;
	}
	
	@Override
	protected Task<ObservableList<EstateStaff>> createTask() {
		
		return new Task<ObservableList<EstateStaff>>() {
			
			@Override
			protected ObservableList<EstateStaff> call() throws Exception {
				
				URL host = new URL(location);
				HttpURLConnection connection = (HttpURLConnection) host.openConnection();
				
				connection.setRequestProperty("Accept", "application/json");
				connection.setRequestProperty("Content-type", "application/json");
				connection.setRequestProperty("Authorization", "Bearer " + token);
				
				JsonReader jr = Json.createReader(connection.getInputStream());
				
				JsonObject jsonObject = jr.readObject();
				JsonArray jsonArray = jsonObject.getJsonArray("data");
				
				
				ObservableList<EstateStaff> answer = FXCollections.observableArrayList();
				
				EstateStaff estateStaff;
				
        		for (JsonObject result : jsonArray.getValuesAs(JsonObject.class)) {
        			JsonObject jsonData = result.getJsonObject("attributes");
        			
        			
        			estateStaff = new EstateStaff(
        					result.getString("id"),
        					jsonData.getString("full_name"),
        					jsonData.getString("mobile_number_1"),
        					jsonData.getString("email_address"),
        					jsonData.getString("gender"), 
        					jsonData.getString("home_address"),
        					jsonData.getString("designation")
        					);
        			
        			// setting others 
        			estateStaff.setFirstName(jsonData.getString("first_name"));
        			estateStaff.setLastName(jsonData.getString("last_name"));

        			if (!jsonData.isNull("middle_name")) {
            			estateStaff.setMiddleName(jsonData.getString("middle_name"));
        			} else {
        				estateStaff.setMiddleName("");
        			}
        			
        			
        			if (!jsonData.isNull("left_thumb_fingerprint_template")) {
        				estateStaff.setLeftThumbFingerPrintTemplate(jsonData.getString("left_thumb_fingerprint_template"));
        			}
        			
        			if (!jsonData.isNull("left_index_fingerprint_template")) {
        				estateStaff.setLeftIndexFingerPrintTemplate(jsonData.getString("left_index_fingerprint_template"));
        			}
        			
        			if (!jsonData.isNull("right_thumb_fingerprint_template")) {
        				estateStaff.setRightThumbFingerPrintTemplate(jsonData.getString("right_thumb_fingerprint_template"));
        			}
        			
        			if (!jsonData.isNull("right_index_fingerprint_template")) {
        				estateStaff.setRightIndexFingerPrintTemplate(jsonData.getString("right_index_fingerprint_template"));
        			}        			
    				answer.add(estateStaff);
        		}

				return answer;
			}
		};
	}
	
	
}
