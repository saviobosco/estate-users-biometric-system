package application;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import application.Models.Resident;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ResidentRetrievalService extends Service<ObservableList<Resident>> {

	private String location;
	
	private String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiM2JhNWQ5OWQxNjRiNGUxYzI5M2E1YTdhYjE5YTY4MmViZGQzYzE2NjJkMTJkYjkyYTYzNDhlMWRhOGNhNTFhN2NjNmEwOWQzN2VhNzY4MWEiLCJpYXQiOjE2MDQwNjUxOTcsIm5iZiI6MTYwNDA2NTE5NywiZXhwIjoxNjM1NjAxMTk3LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.Sueyi8iqhp3Y5dX1S7XxkAcROJHSh9lexOJQbevUcORmsnaoig4LfZiHpjX-20BxvrWHsEpiX0CEsltuwTrVC4m1VgZR0PDA13HEs3C5W0Hhn9zZzIvvQKo7vvcvCZ8ncZMQMqWI0UdANF52M-G3QH5jUhGp6Q697_zzXDVizpbxwQLHBxtrk9Ad3GBx4ONSjN562uy2Ja6x-KNlhMZ1egvC1b1EK2NtUB8ZU31IBiXeBu79FFDKaQg5i14TBVIqEfAw4gN-ge0-9j4T4KoFzGVloGAFuxqbn12o0VWourJUgFkR-Zbsd0Iii5BaGUOhXWtFv-Kg6rLkLOY3L4lkdPDn_shiFc6UBc9xPsaW7HTvnjJhG9fioYk9X3_ItKBZ2t2smF9zrmfOorHleQo3pCr7nFpy9rhzKnCy8xWDNmSDMjCZHJdNGoEElewHS2KCoodb8qI1k3n9nAZKX9PThos4cyfCO4oxXdeg8WDWxPB_Thnp2bEt4sy-mX9EuzMvakPy1_JJkXIl-fWaDbjIKNQIigLszeD80sfxCsGB4W9kp4wrDzOzfhQwoZKvxGxIFzktUa7wCJEOq37D2Bi7RayN2Ot26R_mEDKMFYqQK5vDP2wIhxCO4S_hN7fwxdDODg7Y1Wm4U5IPcM8t-lyNYhkuL6Uc0JQ-cg5CsjtKXFY";

	
	public ResidentRetrievalService(String loc) {
		this.location = loc;
	}
	
	@Override
	protected Task<ObservableList<Resident>> createTask() {
		
		return new Task<ObservableList<Resident>>() {
			@Override
			protected ObservableList<Resident> call() throws Exception {
				URL host = new URL(location);
				HttpURLConnection connection = (HttpURLConnection) host.openConnection();
				
				connection.setRequestProperty("Authorization", "Bearer " + token);
				
				JsonReader jr = Json.createReader(connection.getInputStream());
				
				JsonObject jsonObject = jr.readObject();
				JsonArray jsonArray = jsonObject.getJsonArray("data");
				
				ObservableList<Resident> answer = FXCollections.observableArrayList();
				
				Resident resident;
        		for (JsonObject result : jsonArray.getValuesAs(JsonObject.class)) {
        			JsonObject jsonData = result.getJsonObject("attributes");
        			
        			resident = new Resident(
        					result.getString("id"),
        					jsonData.getString("full_name"),
        					jsonData.getString("phone_number"),
        					jsonData.getString("email"),
        					jsonData.getString("gender"), 
        					jsonData.getString("primary_property_unit"),
        					jsonData.getString("designation")
        					);
        			
        			resident.setFirstName(jsonData.getString("first_name"));
        			resident.setLastName(jsonData.getString("last_name"));
        			
        			if (!jsonData.isNull("middle_name")) {
            			resident.setMiddleName(jsonData.getString("middle_name"));
        			}

        			if (!jsonData.isNull("left_thumb_fingerprint_template")) {
        				resident.setLeftThumbFingerPrintTemplate(jsonData.getString("left_thumb_fingerprint_template"));
        			}
        			if (!jsonData.isNull("right_thumb_fingerprint_template")) {
        				resident.setRightThumbFingerPrintTemplate(jsonData.getString("right_thumb_fingerprint_template"));
        			}
        			
    				answer.add(resident);
        		}

				return answer;
			}
		};
	} 

}
