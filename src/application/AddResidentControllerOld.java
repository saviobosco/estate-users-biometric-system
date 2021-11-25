package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.Alert.AlertType;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.EnumMap;
import java.util.ResourceBundle;

import com.digitalpersona.onetouch.DPFPFingerIndex;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPTemplate;
import com.digitalpersona.onetouch.readers.DPFPReaderDescription;
import com.digitalpersona.onetouch.readers.DPFPReadersCollection;

import javafx.event.ActionEvent;

public class AddResidentControllerOld implements Initializable {
	@FXML
	private TextField tfFullName;
	@FXML
	private TextField tfPhoneNumber;
	@FXML
	private TextField tfEmailAddress;
	@FXML
	private TextField tfPropertyUnitName;
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
	private GridPane gridPaneFingerPrints;

	static EnumMap<DPFPFingerIndex, DPFPTemplate> templates = new EnumMap<DPFPFingerIndex, DPFPTemplate>(DPFPFingerIndex.class);
    private EnumMap<DPFPFingerIndex, CheckBox> checkBoxes = new EnumMap<DPFPFingerIndex, CheckBox>(DPFPFingerIndex.class);
	
	public void initialize(URL url, ResourceBundle rb)
	{
        for (DPFPFingerIndex finger : DPFPFingerIndex.values())
        {
            CheckBox CheckBox = new CheckBox(fingerName(finger));
            final int rows = DPFPFingerIndex.values().length / 2;
            int gridX = finger.ordinal() / rows;
            int gridY = rows - 1 - Math.abs(rows - 1 - finger.ordinal()) + gridX;            
            gridPaneFingerPrints.add(CheckBox, gridX, gridY);
            checkBoxes.put(finger, CheckBox);
            
            CheckBox.setOnAction((event) -> {
            		if (CheckBox.isSelected()) {
            			System.out.println(CheckBox.getText() + " was selected");
            			FXMLLoader loader = new FXMLLoader(
            				    getClass().getResource(
            				      "Enrolment.fxml"
            				    )
            				  );
            			try {
            				Stage stage = new Stage(StageStyle.DECORATED);
            				  stage.setScene(
            				    new Scene(loader.load())
            				  );

            				  EnrolmentController controller = loader.getController();
            				  controller.initData(CheckBox.getText(), finger.ordinal(), templates);
            				  stage.initModality(Modality.APPLICATION_MODAL);
            				  stage.setTitle("Capture FingerPrint");
            				  stage.showAndWait();
            				  controller.stop();
            				  // update the user interface
            				  updateUI();
            				  System.out.println(templates.size());
            			} catch (Exception e) {
            				e.printStackTrace();
            			}
            		} else {
            			templates.remove(finger);
      				  System.out.println(templates.size());
            		}
            	}
            );
        }
		listReaders(); 
	} 
	
	public void listReaders() {
		DPFPReadersCollection readers = DPFPGlobal.getReadersFactory().getReaders();
		if (readers == null || readers.size() == 0) {
			System.out.println("There are no readers available. \n");
			return;
		}
		for (DPFPReaderDescription readerDescription : readers) {
			System.out.println(readerDescription.getSerialNumber());
		}
	}
	
	@FXML
	public void handleFingerPrintCheckbox(ActionEvent event) {
		//Stage enrolmentStage = new Stage();
		
		//new EnrolmentController(enrolmentStage);
		/*FXMLLoader loader = new FXMLLoader(
			    getClass().getResource(
			      "Enrolment.fxml"
			    )
			  );
		try {
			Stage stage = new Stage(StageStyle.DECORATED);
			  stage.setScene(
			    new Scene(loader.load())
			  );

			  EnrolmentController controller = loader.getController();
			  controller.initData("hello world");
			  stage.initModality(Modality.APPLICATION_MODAL);
			  stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.print("Window closed");*/
	}
	
	
	
	// Event Listener on Button[#btnSaveResident].onAction
	@FXML
	public void handleButtonAction(ActionEvent event) {
		if (event.getSource() == btnSaveResident) {
			// get all field details and save the resident.
			String fullName = tfFullName.getText();
			String phoneNumber = tfPhoneNumber.getText();
			String emailAddress = tfEmailAddress.getText();
			String propertyUnitName = tfPropertyUnitName.getText();
			String designation = tfDesignation.getText();
			String gender = "";
			try {
				RadioButton selectedGenderRadioButton = (RadioButton) rbGender.getSelectedToggle();
				gender = selectedGenderRadioButton.getText();
			} catch (Exception e) {
				// suppress error exception
			}
			
			
			
			if (fullName.trim().length() > 1 && phoneNumber.trim().length() > 1 && emailAddress.trim().length() > 1
					&& propertyUnitName.trim().length() > 1 && designation.trim().length() > 1 && gender.trim().length() > 1) {
				
				if (insertRecord(fullName, phoneNumber, emailAddress, gender, propertyUnitName, designation) == 1) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setContentText("Resident was successully added.");
					alert.showAndWait();
					clearInputFields();
				} else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setContentText("Resident record could not be created!");
					alert.showAndWait();
				}
				
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("All Input fields are required!.");
				alert.showAndWait();
			}	
		}
	}
	
	public Connection getConnection() {
		Connection conn;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/estate-manager?serverTimezone=UTC", "root", "");
			return conn;
		} catch (Exception ex) {
			System.out.println("Error " + ex.getMessage());
			return null;
		}
	}
	
	public int insertRecord(
			String fullName,
			String phoneNumber,
			String emailAddress,
			String gender,
			String propertyUnitName,
			String designation)
	{
		String query = "INSERT INTO residents (full_name, phone_number, email_address, gender, property_unit_name, designation) "
				+ "VALUES ('" +
				fullName + "' ," +
				"'" + phoneNumber + "' ," +
				"'" + emailAddress + "' ," + 
				"'" + gender + "' ," +
				"'" + propertyUnitName + "' ," +
				"'" + designation + "'" +
				")";
		
		return executeQuery(query);
	}
	
	private int executeQuery(String query) {
		Connection conn = getConnection();
		Statement st;
		try {
			st = conn.createStatement();
			Integer returnValue = st.executeUpdate(query);
			
			return returnValue;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}
	
	public void clearInputFields()
	{
		tfFullName.setText("");
		tfPhoneNumber.setText("");
		tfEmailAddress.setText("");
		tfPropertyUnitName.setText("");
		tfDesignation.setText("");
		rbGenderMale.setSelected(false);
		rbGenderFemale.setSelected(false);
	}
	
	/*
	 * @FXML public void handleGenderSelectionAction(ActionEvent event) { String
	 * message = ""; if (rbGenderMale.isSelected()) { message +=
	 * rbGenderMale.getText() + "\n"; } if (rbGenderFemale.isSelected()) { message
	 * += rbGenderFemale.getText() + "\n"; } System.out.println(message); }
	 */
	
	public static final EnumMap<DPFPFingerIndex, String> fingerNames;
    static { 
    	fingerNames = new EnumMap<DPFPFingerIndex, String>(DPFPFingerIndex.class);
    	fingerNames.put(DPFPFingerIndex.LEFT_PINKY,	  "left pinky");
    	fingerNames.put(DPFPFingerIndex.LEFT_RING,    "left ring");
    	fingerNames.put(DPFPFingerIndex.LEFT_MIDDLE,  "left middle");
    	fingerNames.put(DPFPFingerIndex.LEFT_INDEX,   "left index");
    	fingerNames.put(DPFPFingerIndex.LEFT_THUMB,   "left thumb");
    	fingerNames.put(DPFPFingerIndex.RIGHT_PINKY,  "right pinky");
    	fingerNames.put(DPFPFingerIndex.RIGHT_RING,   "right ring");
    	fingerNames.put(DPFPFingerIndex.RIGHT_MIDDLE, "right middle");
    	fingerNames.put(DPFPFingerIndex.RIGHT_INDEX,  "right index");
    	fingerNames.put(DPFPFingerIndex.RIGHT_THUMB,  "right thumb");
    }
    
    public String fingerName(DPFPFingerIndex finger) {
    	return fingerNames.get(finger); 
    }
    
    
    private void updateUI() {
    	// update enrolled fingers checkboxes
        for (DPFPFingerIndex finger : DPFPFingerIndex.values())
            checkBoxes.get(finger).setSelected(templates.containsKey(finger));
        // update verification button
    }
    
}
