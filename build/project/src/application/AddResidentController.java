package application;

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.Alert.AlertType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.EnumMap;
import java.util.ResourceBundle;

import com.digitalpersona.onetouch.DPFPFingerIndex;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPTemplate;
import com.digitalpersona.onetouch.readers.DPFPReaderDescription;
import com.digitalpersona.onetouch.readers.DPFPReadersCollection;

import javafx.event.ActionEvent;

public class AddResidentController implements Initializable {
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
	private Button pictureUpload;
	@FXML
	private ImageView pictureImageView;
	
	private File file;
		
	static EnumMap<DPFPFingerIndex, DPFPTemplate> templates = new EnumMap<DPFPFingerIndex, DPFPTemplate>(DPFPFingerIndex.class);
	
	
	public void initialize(URL url, ResourceBundle rb)
	{
		listReaders(); 
	}
	
	// list all fingerprint readers.
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
	
	
	// Open the window for fingerprint enrollment
	public void openFingerPrintEnrollment(String fingerName, Integer fingerIndex)
	{
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
			  controller.initData(fingerName, fingerIndex, templates);
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
	}
	
	private void removeFingerPrint(DPFPFingerIndex finger) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setContentText("Finger you sure you want to remove finger print?");
		alert.showAndWait().ifPresent((btnType) -> {
			if (btnType == ButtonType.OK) {
				templates.remove(finger);
			} else if (btnType == ButtonType.CANCEL) {
				// do nothing for now.
			}
		});
		System.out.println(templates.size());
		updateUI();
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
				removeFingerPrint(finger);
			}
		}else if ((CheckBox) event.getSource() == leftIndexCheckbox) {
			fingerIndex = 3;
			fingerName = leftIndexCheckbox.getText();
			finger = DPFPFingerIndex.values()[fingerIndex];
			
			if (leftIndexCheckbox.isSelected()) {
				openFingerPrintEnrollment(fingerName, fingerIndex);
			} else {
				removeFingerPrint(finger);
			}

		} else if ((CheckBox) event.getSource() == rightThumbCheckbox) {
			fingerIndex = 9;
			fingerName = rightThumbCheckbox.getText();
			finger = DPFPFingerIndex.values()[fingerIndex];
			
			if (rightThumbCheckbox.isSelected()) {
				openFingerPrintEnrollment(fingerName, fingerIndex);
			} else {
				removeFingerPrint(finger);
			}

		} else if ((CheckBox) event.getSource() == rightIndexCheckbox) {
			fingerIndex = 8;
			fingerName = rightIndexCheckbox.getText();
			
			finger = DPFPFingerIndex.values()[fingerIndex];
			
			if (rightIndexCheckbox.isSelected()) {
				openFingerPrintEnrollment(fingerName, fingerIndex);
			} else {
				removeFingerPrint(finger);
			}
		}
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
			
			byte[] left_thumb = null;
			byte[] left_index = null;
			byte[] right_thumb = null;
			byte[] right_index = null;
			
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
			
			if (fullName.trim().length() > 1 && phoneNumber.trim().length() > 1 && emailAddress.trim().length() > 1
					&& propertyUnitName.trim().length() > 1 && designation.trim().length() > 1 && gender.trim().length() > 1) {
				
				if (insertRecord(
						fullName, 
						phoneNumber, 
						emailAddress, 
						gender, 
						propertyUnitName, 
						designation,
						right_thumb,
						right_index,
						left_thumb,
						left_index) == 1) {
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
		} else if (event.getSource() == pictureUpload) {
			
			FileChooser fc = new FileChooser();
			file = fc.showOpenDialog(null);
			if (file != null) {
				Image image = new Image(file.toURI().toString(), 124, 124, true, true);
				pictureImageView.setImage(image);
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
			String designation,
			byte[] right_thumb,
			byte[] right_index,
			byte[] left_thumb,
			byte[] left_index)
	{
		
		/*String query = "INSERT INTO residents "
				+ " (full_name, phone_number, email_address, gender, property_unit_name, designation, right_thumb, right_index, left_thumb, left_index) "
				+ "VALUES ('" +
				fullName + "' ," +
				"'" + phoneNumber + "' ," +
				"'" + emailAddress + "' ," + 
				"'" + gender + "' ," +
				"'" + propertyUnitName + "' ," +
				"'" + designation + "' ," +
				"'" + right_thumb + "' ," +
				"'" + right_index + "' ," +
				"'" + left_thumb + "' ," +
				"'" + left_index + "'" +
				")";*/
		String query = "INSERT INTO residents "
				+ " (full_name, phone_number, email_address, gender, property_unit_name, designation, right_thumb, right_index, left_thumb, left_index, picture) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		System.out.println(query);
		PreparedStatement st;
		try {
			st = getConnection().prepareStatement(query);
			st.setString(1, fullName);
			st.setString(2, phoneNumber);
			st.setString(3, emailAddress);
			st.setString(4, gender);
			st.setString(5, propertyUnitName);
			st.setString(6, designation);
			st.setBytes(7, right_thumb);
			st.setBytes(8, right_index);
			st.setBytes(9, left_thumb);
			st.setBytes(10, left_index);
			
			if (file != null) {
				try {
					FileInputStream fis = new FileInputStream(file);
					st.setBinaryStream(11, (InputStream) fis);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			} else {
				st.setBytes(11, null);
			}
			
			return st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/*
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
	} */
	
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
    	leftThumbCheckbox.setSelected(templates.containsKey(DPFPFingerIndex.values()[4]));
    	leftIndexCheckbox.setSelected(templates.containsKey(DPFPFingerIndex.values()[3]));
    	rightThumbCheckbox.setSelected(templates.containsKey(DPFPFingerIndex.values()[9]));
    	rightIndexCheckbox.setSelected(templates.containsKey(DPFPFingerIndex.values()[8]));
    }
    
}
