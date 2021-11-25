package application.controllers;

import java.net.URL;
import java.net.UnknownHostException;
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

import application.EnrolmentController;
import application.Models.DomesticStaff;
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
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class EditDomesticStaffController implements Initializable {

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
	private TextField tfHomeAddress;
	@FXML
	private TextField tfDesignation;
	@FXML
	private RadioButton rbGenderMale;
	@FXML
	private RadioButton rbGenderFemale;
	@FXML
	private ToggleGroup rbGender;
	@FXML
	private Button btnSaveRecord;
	
	@FXML
	private CheckBox leftThumbCheckbox;
	@FXML
	private CheckBox leftIndexCheckbox;
	@FXML
	private CheckBox rightThumbCheckbox;
	@FXML
	private CheckBox rightIndexCheckbox;
	@FXML
	private GridPane gridPaneFingerPrints;
	
	@FXML
	private ImageView pictureImageView;
	
	private String webServiceAccessToken;
	
	private DomesticStaff domesticStaff;
	
	private byte[] left_thumb;
	
	private byte[] left_index;
	
	private byte[] right_thumb;
	
	private byte[] right_index;
	
	static EnumMap<DPFPFingerIndex, DPFPTemplate> templates = new EnumMap<DPFPFingerIndex, DPFPTemplate>(DPFPFingerIndex.class);
	
	// Reference to the parent Controller
	private DomesticStaffsController parentController ;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	public void initData(DomesticStaff domesticStaff, String token, DomesticStaffsController parentController) {
		this.webServiceAccessToken = token;
		this.domesticStaff = domesticStaff;
		this.parentController = parentController;
		
		
		tfFirstName.setText(domesticStaff.getFirstName());
		tfMiddleName.setText(domesticStaff.getMiddleName());
		tfLastName.setText(domesticStaff.getLastName());
		tfPhoneNumber.setText(domesticStaff.getPhoneNumber());
		tfEmailAddress.setText(domesticStaff.getEmailAddress());
		tfDesignation.setText(domesticStaff.getDesignation());
		tfHomeAddress.setText(domesticStaff.getHomeAddress());
		
		if (rbGenderMale.getText().equalsIgnoreCase(domesticStaff.getGender())) {
			rbGenderMale.setSelected(true);
		}
		
		if (rbGenderFemale.getText().equalsIgnoreCase(domesticStaff.getGender())) {
			rbGenderFemale.setSelected(true);
		}
		
		leftThumbCheckbox.setSelected((domesticStaff.getLeftThumbFingerPrintTemplate() != null &&  domesticStaff.getLeftThumbFingerPrintTemplate().length > 0) ? true : false);
		leftIndexCheckbox.setSelected((domesticStaff.getLeftIndexFingerPrintTemplate() != null && domesticStaff.getLeftIndexFingerPrintTemplate().length > 0) ? true : false);
		rightThumbCheckbox.setSelected((domesticStaff.getRightThumbFingerPrintTemplate() != null && domesticStaff.getRightThumbFingerPrintTemplate().length > 0) ? true : false);
		rightIndexCheckbox.setSelected((domesticStaff.getRightIndexFingerPrintTemplate() != null && domesticStaff.getRightIndexFingerPrintTemplate().length > 0) ? true : false);
		
		// Reassigning the previous fingerprint details.
		left_thumb = domesticStaff.getLeftThumbFingerPrintTemplate();
		left_index = domesticStaff.getLeftIndexFingerPrintTemplate();
		right_thumb = domesticStaff.getRightThumbFingerPrintTemplate();
		right_index = domesticStaff.getRightIndexFingerPrintTemplate();
	}

	
	// Open the window for fingerprint enrollment
	public void openFingerPrintEnrollment(String fingerName, Integer fingerIndex) {
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
				
				//System.out.println(templates.size());
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
		/*leftThumbCheckbox.setSelected(templates.containsKey(DPFPFingerIndex.values()[4]));
		leftIndexCheckbox.setSelected(templates.containsKey(DPFPFingerIndex.values()[3]));
		rightThumbCheckbox.setSelected(templates.containsKey(DPFPFingerIndex.values()[9]));
		rightIndexCheckbox.setSelected(templates.containsKey(DPFPFingerIndex.values()[8]));*/
		this.reAssignFingerprintTemplates();
		
		// Still Update if it already exists
		leftThumbCheckbox.setSelected((left_thumb != null &&  left_thumb.length > 0) ? true : false);
		leftIndexCheckbox.setSelected((left_index != null && left_index.length > 0) ? true : false);
		rightThumbCheckbox.setSelected((right_thumb != null && right_thumb.length > 0) ? true : false);
		rightIndexCheckbox.setSelected((right_index != null && right_index.length > 0) ? true : false);
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
					
			if (event.getSource() == btnSaveRecord) {
					// get all field details and save the resident.
					String firstName = tfFirstName.getText();
					String lastName = tfLastName.getText();
					String middleName = tfMiddleName.getText();
					String phoneNumber = tfPhoneNumber.getText();
					String emailAddress = tfEmailAddress.getText();
					String homeAddress = tfHomeAddress.getText();
					String designation = tfDesignation.getText();
					
					String gender = "";
					try {
						RadioButton selectedGenderRadioButton = (RadioButton) rbGender.getSelectedToggle();
						gender = selectedGenderRadioButton.getText().toLowerCase();
					} catch (Exception e) {
						// suppress error exception
					}
						
					if (firstName.trim().length() > 1 && phoneNumber.trim().length() > 1 && emailAddress.trim().length() > 1
							&& homeAddress.trim().length() > 1 && designation.trim().length() > 1 && gender.trim().length() > 1) {
							
							
						JsonObject value = Json.createObjectBuilder()
							.add("data", Json.createObjectBuilder()
							.add("type", "domestic_staffs")
							.add("attributes", Json.createObjectBuilder()
							    .add("first_name", firstName)
							    .add("last_name", lastName)
							    .add("middle_name", middleName)
							    .add("mobile_number_1", phoneNumber)
							    .add("email_address", emailAddress)
							    .add("home_address", homeAddress)
							    .add("gender", gender.toLowerCase())
							    .add("designation", designation)
							    .add("left_thumb_fingerprint_template",(left_thumb != null) ? Base64.getEncoder().encodeToString(left_thumb) : "")
							    .add("left_index_fingerprint_template", (left_index != null) ? Base64.getEncoder().encodeToString(left_index) : "")
							    .add("right_thumb_fingerprint_template", (right_thumb != null) ? Base64.getEncoder().encodeToString(right_thumb) : "")
							    .add("right_index_fingerprint_template", (right_index != null) ? Base64.getEncoder().encodeToString(right_index) : "")
							 )
							 )
							 .build();
							 
						Header header2 = new BasicHeader("Authorization", "Bearer " + this.webServiceAccessToken);
								
						List<Header> headers = new ArrayList<>();
						headers.add(header2);
							 
						CloseableHttpClient client = HttpClients.custom().setDefaultHeaders(headers).build();
						HttpPut httpPut = new HttpPut("http://griffontech-estate-manager.herokuapp.com/api/v1/domestic-staffs/" + domesticStaff.getId());
							
						try {
							StringEntity entity = new StringEntity(value.toString());
							httpPut.setEntity(entity);
								
							httpPut.setHeader("Accept", "application/json");
							httpPut.setHeader("Content-type", "application/json");
								
							CloseableHttpResponse response = client.execute(httpPut);
							
						    HttpEntity entity2 = response.getEntity();
						       
						    String responseString = EntityUtils.toString(entity2);
						    					    
						    if (responseString.contains("data")) {
						    	
						    	// Reload Data in the parent controller;
						    	this.parentController.displayData();
						    	
						    	Alert alert = new Alert(AlertType.INFORMATION);
								alert.setContentText("Record was successully updated.");
								alert.showAndWait();
						    } else {
						    	Alert alert = new Alert(AlertType.ERROR);
								alert.setContentText("Resident record could not be updated! Please try again.");
								alert.showAndWait();
						    }
						    // do something useful with the response body
						    // and ensure it is fully consumed
						    EntityUtils.consume(entity2);
						} catch (UnknownHostException unknownHostException) {
							Alert alert = new Alert(AlertType.WARNING);
							alert.setContentText("No Internet Connection!.");
							alert.showAndWait();
						}catch (Exception e) {
							e.printStackTrace();
						}	
					} else {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setContentText("All Input fields are required!.");
						alert.showAndWait();
					}	
				}
	}

	
}
