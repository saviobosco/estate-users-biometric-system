package application.Retrievals;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import application.Models.FingerprintTemplate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class FingerprintTemplatesRetrievalService extends Service<ObservableList<FingerprintTemplate>> {

	private String location;
	
	private String token;
	
	public FingerprintTemplatesRetrievalService(String loc, String token) {
		this.location = loc;
		this.token = token;
	}
	
	
	@Override
	protected Task<ObservableList<FingerprintTemplate>> createTask() {
		
		return new Task<ObservableList<FingerprintTemplate>>() {
			
			@Override
			protected ObservableList<FingerprintTemplate> call() throws Exception {
				
				URL host = new URL(location);
				HttpURLConnection connection = (HttpURLConnection) host.openConnection();
				
				connection.setRequestProperty("Accept", "application/json");
				connection.setRequestProperty("Content-type", "application/json");
				connection.setRequestProperty("Authorization", "Bearer " + token);
				
				JsonReader jr = Json.createReader(connection.getInputStream());
				
				JsonObject jsonObject = jr.readObject();
				JsonArray jsonArray = jsonObject.getJsonArray("data");
								
				ObservableList<FingerprintTemplate> answer = FXCollections.observableArrayList();
				
				FingerprintTemplate fingerprintTemplate;
				
				 try {
					 for (JsonObject result : jsonArray.getValuesAs(JsonObject.class)) {
						 try {
							 JsonObject jsonData = result.getJsonObject("attributes");
			        			
			        			fingerprintTemplate = new FingerprintTemplate();
			        			
			        			// setting others
			        			System.out.println(jsonData);
			        			
			        			if (!jsonData.isNull("full_name")) {
			        				fingerprintTemplate.setFullName(jsonData.getString("full_name"));
			        			}
			        			if (!jsonData.isNull("phone_number")) {
			        				fingerprintTemplate.setPhoneNumber(jsonData.getString("phone_number"));
			        			}
			        			
			        			if (!jsonData.isNull("designation")) {
			        				fingerprintTemplate.setDesignation(jsonData.getString("designation"));
			        			}
			        			
			        			if (!jsonData.isNull("type")) {
			        				fingerprintTemplate.setType(jsonData.getString("type"));
			        			}
			        			
			        			if (!jsonData.isNull("property_address")) {
			        				fingerprintTemplate.setPropertyAddress(jsonData.getString("property_address"));
			        			}
			        			
			        			if (!jsonData.isNull("profile_picture")) {
			        				fingerprintTemplate.setProfilePicture(jsonData.getString("profile_picture"));
			        			}
			        			
			        			if (!jsonData.isNull("left_thumb_fingerprint_template")) {
			        				fingerprintTemplate.setLeftThumbFingerprintTemplate(jsonData.getString("left_thumb_fingerprint_template"));
			        			}
			        			
			        			if (!jsonData.isNull("left_index_fingerprint_template")) {
			        				fingerprintTemplate.setLeftIndexFingerprintTemplate(jsonData.getString("left_index_fingerprint_template"));
			        			}
			        			
			        			if (!jsonData.isNull("right_thumb_fingerprint_template")) {
			        				fingerprintTemplate.setRightThumbFingerprintTemplate(jsonData.getString("right_thumb_fingerprint_template"));
			        			}
			        			
			        			if (!jsonData.isNull("right_index_fingerprint_template")) {
			        				fingerprintTemplate.setRightIndexFingerprintTemplate(jsonData.getString("right_index_fingerprint_template"));
			        			}
			        			
			    				answer.add(fingerprintTemplate);

						 } catch (Exception ex) {
							 continue;
						 }
		        					        		}
				 } catch (Exception ex) {
					 ex.printStackTrace();
				 }
        		

				return answer;
			}
		};
	}
}
