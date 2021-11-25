package application.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.EnumMap;
import java.util.List;
import java.util.ResourceBundle;

import javax.json.Json;
import javax.json.JsonObject;

import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicHeader;

import com.digitalpersona.onetouch.DPFPFingerIndex;
import com.digitalpersona.onetouch.DPFPTemplate;

import application.DashboardController;
import application.EnrolmentController;
import application.Models.Resident;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class EditResidentController implements Initializable {
	
	@FXML
	private TextField tfFirstName;
	@FXML
	private TextField tfMiddleName;
	@FXML
	private TextField tfLastName;
	@FXML
	private TextField tfPhoneNumber;
	@FXML
	private TextField tfEmailAddress;
	
	@FXML
	private TextField tfDesignation;
	@FXML
	private RadioButton rbGenderMale;
	@FXML
	private RadioButton rbGenderFemale;
	@FXML
	private ToggleGroup rbGender;
	@FXML
	private Button btnSaveResident;
	@FXML
	private CheckBox leftThumbCheckbox;
	@FXML
	private CheckBox leftIndexCheckbox;
	@FXML
	private CheckBox rightThumbCheckbox;
	@FXML
	private CheckBox rightIndexCheckbox;
	
	@FXML
	private ImageView pictureImageView;
	
	private Resident resident;
	
	private DashboardController parentController;
	
	private String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiM2JhNWQ5OWQxNjRiNGUxYzI5M2E1YTdhYjE5YTY4MmViZGQzYzE2NjJkMTJkYjkyYTYzNDhlMWRhOGNhNTFhN2NjNmEwOWQzN2VhNzY4MWEiLCJpYXQiOjE2MDQwNjUxOTcsIm5iZiI6MTYwNDA2NTE5NywiZXhwIjoxNjM1NjAxMTk3LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.Sueyi8iqhp3Y5dX1S7XxkAcROJHSh9lexOJQbevUcORmsnaoig4LfZiHpjX-20BxvrWHsEpiX0CEsltuwTrVC4m1VgZR0PDA13HEs3C5W0Hhn9zZzIvvQKo7vvcvCZ8ncZMQMqWI0UdANF52M-G3QH5jUhGp6Q697_zzXDVizpbxwQLHBxtrk9Ad3GBx4ONSjN562uy2Ja6x-KNlhMZ1egvC1b1EK2NtUB8ZU31IBiXeBu79FFDKaQg5i14TBVIqEfAw4gN-ge0-9j4T4KoFzGVloGAFuxqbn12o0VWourJUgFkR-Zbsd0Iii5BaGUOhXWtFv-Kg6rLkLOY3L4lkdPDn_shiFc6UBc9xPsaW7HTvnjJhG9fioYk9X3_ItKBZ2t2smF9zrmfOorHleQo3pCr7nFpy9rhzKnCy8xWDNmSDMjCZHJdNGoEElewHS2KCoodb8qI1k3n9nAZKX9PThos4cyfCO4oxXdeg8WDWxPB_Thnp2bEt4sy-mX9EuzMvakPy1_JJkXIl-fWaDbjIKNQIigLszeD80sfxCsGB4W9kp4wrDzOzfhQwoZKvxGxIFzktUa7wCJEOq37D2Bi7RayN2Ot26R_mEDKMFYqQK5vDP2wIhxCO4S_hN7fwxdDODg7Y1Wm4U5IPcM8t-lyNYhkuL6Uc0JQ-cg5CsjtKXFY";

	private byte[] left_thumb;
	
	private byte[] left_index;
	
	private byte[] right_thumb;
	
	private byte[] right_index;
	
	static EnumMap<DPFPFingerIndex, DPFPTemplate> templates = new EnumMap<DPFPFingerIndex, DPFPTemplate>(DPFPFingerIndex.class);
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub	
	}
	
	
	public void initData(Resident resident, DashboardController parentController) throws IOException {
		   		
		this.resident = resident;
		this.parentController = parentController;
		
		tfFirstName.setText(resident.getFirstName());
		tfMiddleName.setText(resident.getMiddleName());
		tfLastName.setText(resident.getLastName());
		tfPhoneNumber.setText(resident.getPhoneNumber());
		tfDesignation.setText(resident.getDesignation());
		tfEmailAddress.setText(resident.getEmailAddress());
		
		
		if (rbGenderMale.getText().equalsIgnoreCase(resident.getGender())) {
			rbGenderMale.setSelected(true);
		}
		
		if (rbGenderFemale.getText().equalsIgnoreCase(resident.getGender())) {
			rbGenderFemale.setSelected(true);
		}
		
		leftThumbCheckbox.setSelected((resident.getLeftThumbFingerPrintTemplate() != null &&  resident.getLeftThumbFingerPrintTemplate().length > 0) ? true : false);
		leftIndexCheckbox.setSelected((resident.getLeftIndexFingerPrintTemplate() != null && resident.getLeftIndexFingerPrintTemplate().length > 0) ? true : false);
		rightThumbCheckbox.setSelected((resident.getRightThumbFingerPrintTemplate() != null && resident.getRightThumbFingerPrintTemplate().length > 0) ? true : false);
		rightIndexCheckbox.setSelected((resident.getRightIndexFingerPrintTemplate() != null && resident.getRightIndexFingerPrintTemplate().length > 0) ? true : false);
		
		// Reassigning the previous fingerprint details.
		left_thumb = resident.getLeftThumbFingerPrintTemplate();
		left_index = resident.getLeftIndexFingerPrintTemplate();
		right_thumb = resident.getRightThumbFingerPrintTemplate();
		right_index = resident.getRightIndexFingerPrintTemplate();
	}
	
	
	@FXML
	public void handleFingerPrintCheckbox(ActionEvent event) {
		DPFPFingerIndex finger;
		int fingerIndex;
		String fingerName = "";
		if ((CheckBox) event.getSource() == leftThumbCheckbox) {
			fingerIndex = 4;
			fingerName = leftThumbCheckbox.getText();
			finger = DPFPFingerIndex.values()[fingerIndex];
			if (leftThumbCheckbox.isSelected()) {
				openFingerPrintEnrollment(fingerName, fingerIndex);
			} else {
				removeFingerPrint(finger, fingerIndex);
			}
		}else if ((CheckBox) event.getSource() == leftIndexCheckbox) {
			fingerIndex = 3;
			fingerName = leftIndexCheckbox.getText();
			finger = DPFPFingerIndex.values()[fingerIndex];
			
			if (leftIndexCheckbox.isSelected()) {
				openFingerPrintEnrollment(fingerName, fingerIndex);
			} else {
				removeFingerPrint(finger, fingerIndex);
			}

		} else if ((CheckBox) event.getSource() == rightThumbCheckbox) {
			fingerIndex = 9;
			fingerName = rightThumbCheckbox.getText();
			finger = DPFPFingerIndex.values()[fingerIndex];
			
			if (rightThumbCheckbox.isSelected()) {
				openFingerPrintEnrollment(fingerName, fingerIndex);
			} else {
				removeFingerPrint(finger, fingerIndex);
			}

		} else if ((CheckBox) event.getSource() == rightIndexCheckbox) {
			fingerIndex = 8;
			fingerName = rightIndexCheckbox.getText();
			
			finger = DPFPFingerIndex.values()[fingerIndex];
			
			if (rightIndexCheckbox.isSelected()) {
				openFingerPrintEnrollment(fingerName, fingerIndex);
			} else {
				removeFingerPrint(finger, fingerIndex);
			}
		}
	}
	
	// Event Listener on Button[#btnSaveResident].onAction
		@FXML
	public void handleButtonAction(ActionEvent event) {
			
			if (event.getSource() == btnSaveResident) {
				// get all field details and save the resident.
				String firstName = tfFirstName.getText();
				String lastName = tfLastName.getText();
				String middleName = tfMiddleName.getText();
				String phoneNumber = tfPhoneNumber.getText();
				String emailAddress = tfEmailAddress.getText();
				String designation = tfDesignation.getText();
				String gender = "";
				try {
					RadioButton selectedGenderRadioButton = (RadioButton) rbGender.getSelectedToggle();
					gender = selectedGenderRadioButton.getText();
				} catch (Exception e) {
					// suppress error exception
				}
				
				if (firstName.trim().length() > 1 && lastName.trim().length() > 1 && emailAddress.trim().length() > 1
						&& designation.trim().length() > 1 && gender.trim().length() > 1) {
					
					
					 JsonObject value = Json.createObjectBuilder()
					     .add("data", Json.createObjectBuilder()
					         .add("type", "residents")
					         .add("attributes", Json.createObjectBuilder()
					        		 .add("first_name", firstName)
					        		 .add("middle_name", middleName)
					        		 .add("last_name", lastName)
					        		 .add("phone_number", phoneNumber)
					        		 .add("email_address", emailAddress)
					        		 .add("gender", gender.toLowerCase())
					        		 .add("designation", designation)
					        		 .add("left_thumb_fingerprint_template",(left_thumb != null) ? Base64.getEncoder().encodeToString(left_thumb) : "")
					        		 .add("left_index_fingerprint_template", (left_index != null) ? Base64.getEncoder().encodeToString(left_index) : "")
					        		 .add("right_thumb_fingerprint_template", (right_thumb != null) ? Base64.getEncoder().encodeToString(right_thumb) : "")
					        		 .add("right_index_fingerprint_template", (right_index != null) ? Base64.getEncoder().encodeToString(right_index) : "")
					        		 )
					         )
					     .build();
					 
					 Header header2 = new BasicHeader("Authorization", "Bearer " + token);
						
						List<Header> headers = new ArrayList<>();
						headers.add(header2);
					 
					CloseableHttpClient client = HttpClients.custom().setDefaultHeaders(headers).build();
					HttpPut httpPut = new HttpPut("http://griffontech-estate-manager.herokuapp.com/api/v1/residents/" + resident.getId());
					
					try {
						StringEntity entity = new StringEntity(value.toString());
						httpPut.setEntity(entity);
						
						httpPut.setHeader("Accept", "application/json");
						httpPut.setHeader("Content-type", "application/json");
						
						CloseableHttpResponse response = client.execute(httpPut);
				        HttpEntity entity2 = response.getEntity();
					    String responseString = EntityUtils.toString(entity2);
				        EntityUtils.consume(entity2);				        
				       
				        int responseCode = response.getCode();
				        
				        if (responseCode == 200) {
					    	// Reload Data in the parent controller;
					    	this.parentController.displayData();
					    	
					    	Alert alert = new Alert(AlertType.INFORMATION);
							alert.setContentText("Record was successully updated.");
							alert.showAndWait();
					    } else {
					    	Alert alert = new Alert(AlertType.ERROR);
							alert.setContentText("Resident record could not be updated! Please try again. " + responseString);
							alert.showAndWait();
					    }

				        // do something useful with the response body
				        // and ensure it is fully consumed
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				} else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setContentText("All Input fields are required!.");
					alert.showAndWait();
				}	
			}
	}
	
	
	// Open the window for fingerprint enrollment
		public void openFingerPrintEnrollment(String fingerName, Integer fingerIndex)
		{
			FXMLLoader loader = new FXMLLoader(
				    getClass().getResource(
				      "/application/Enrolment.fxml"
				    )
				  );
			try {
				Stage stage = new Stage(StageStyle.DECORATED);
				  stage.setScene(
				    new Scene(loader.load())
				  );

				  EnrolmentController controller = loader.getController();
				  controller.initData(fingerName, fingerIndex, templates);
				  stage.initModality(Modality.APPLICATION_MODAL);
				  stage.setTitle("Capture FingerPrint");
				  stage.showAndWait();
				  controller.stop();
				  // update the user interface
				  updateUI();
				  
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		private void removeFingerPrint(DPFPFingerIndex finger,int fingerIndex) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setContentText("Finger you sure you want to remove finger print?");
			alert.showAndWait().ifPresent((btnType) -> {
				if (btnType == ButtonType.OK) {
						templates.remove(finger);
						switch(fingerIndex) {
							case(4):
								left_thumb = null;
								break;
							case(3):
								left_index = null;
								break;
							case(8):
								right_index = null;
								break;
							case(9):
								right_thumb = null;
						}
					} else if (btnType == ButtonType.CANCEL) {
						// do nothing for now.
					}
			});
			updateUI();
		}
		
		public void reAssignFingerprintTemplates() {
			if (templates.containsKey(DPFPFingerIndex.values()[9])) {
				right_thumb = templates.get(DPFPFingerIndex.values()[9]).serialize();
			}
			
			if (templates.containsKey(DPFPFingerIndex.values()[8])) {
				right_index = templates.get(DPFPFingerIndex.values()[8]).serialize();
			}
			
			if (templates.containsKey(DPFPFingerIndex.values()[4])) {
				left_thumb = templates.get(DPFPFingerIndex.values()[4]).serialize();
			}
			
			if (templates.containsKey(DPFPFingerIndex.values()[3])) {
				left_index = templates.get(DPFPFingerIndex.values()[3]).serialize();
			}
		}
		
		private void updateUI() {    	
	    	this.reAssignFingerprintTemplates();
			
			// Still Update if it already exists
			leftThumbCheckbox.setSelected((left_thumb != null &&  left_thumb.length > 0) ? true : false);
			leftIndexCheckbox.setSelected((left_index != null && left_index.length > 0) ? true : false);
			rightThumbCheckbox.setSelected((right_thumb != null && right_thumb.length > 0) ? true : false);
			rightIndexCheckbox.setSelected((right_index != null && right_index.length > 0) ? true : false);
	    }

}
