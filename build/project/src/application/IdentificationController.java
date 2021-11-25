package application;

import java.io.InputStream;
import java.util.EnumMap;
import java.util.Iterator;

import com.digitalpersona.onetouch.*;
import com.digitalpersona.onetouch.capture.DPFPCapture;
import com.digitalpersona.onetouch.capture.event.DPFPDataAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPDataEvent;
import com.digitalpersona.onetouch.capture.event.DPFPImageQualityAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPImageQualityEvent;
import com.digitalpersona.onetouch.capture.event.DPFPReaderStatusAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPReaderStatusEvent;
import com.digitalpersona.onetouch.capture.event.DPFPSensorAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPSensorEvent;
import com.digitalpersona.onetouch.processing.DPFPFeatureExtraction;
import com.digitalpersona.onetouch.processing.DPFPImageQualityException;
import com.digitalpersona.onetouch.readers.DPFPReaderDescription;
import com.digitalpersona.onetouch.readers.DPFPReadersCollection;
import com.digitalpersona.onetouch.verification.DPFPVerification;
import com.digitalpersona.onetouch.verification.DPFPVerificationResult;

import application.Models.FingerprintTemplate;
import application.Retrievals.FingerprintTemplatesRetrievalService;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class IdentificationController {
	@FXML
	private Text tfFullName;
	@FXML
	private Text tfPhoneNumber;
	@FXML
	private Text tfPropertyUnitName;
	@FXML
	private Text tfDesignation;
	@FXML
	private Text tfProfileType;
	@FXML
	private Text labelStatus;
	@FXML
	private Text networkStatusTextDisplay;
	
	@FXML
	private ImageView profilePictureImageView;

	private ObservableList<FingerprintTemplate> fingerprintTemplates;
	
	static EnumMap<DPFPFingerIndex, DPFPTemplate> templates = new EnumMap<DPFPFingerIndex, DPFPTemplate>(DPFPFingerIndex.class);
	private DPFPCapture capturer = DPFPGlobal.getCaptureFactory().createCapture();
	private DPFPVerification verificator = DPFPGlobal.getVerificationFactory().createVerification();

	private String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiM2JhNWQ5OWQxNjRiNGUxYzI5M2E1YTdhYjE5YTY4MmViZGQzYzE2NjJkMTJkYjkyYTYzNDhlMWRhOGNhNTFhN2NjNmEwOWQzN2VhNzY4MWEiLCJpYXQiOjE2MDQwNjUxOTcsIm5iZiI6MTYwNDA2NTE5NywiZXhwIjoxNjM1NjAxMTk3LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.Sueyi8iqhp3Y5dX1S7XxkAcROJHSh9lexOJQbevUcORmsnaoig4LfZiHpjX-20BxvrWHsEpiX0CEsltuwTrVC4m1VgZR0PDA13HEs3C5W0Hhn9zZzIvvQKo7vvcvCZ8ncZMQMqWI0UdANF52M-G3QH5jUhGp6Q697_zzXDVizpbxwQLHBxtrk9Ad3GBx4ONSjN562uy2Ja6x-KNlhMZ1egvC1b1EK2NtUB8ZU31IBiXeBu79FFDKaQg5i14TBVIqEfAw4gN-ge0-9j4T4KoFzGVloGAFuxqbn12o0VWourJUgFkR-Zbsd0Iii5BaGUOhXWtFv-Kg6rLkLOY3L4lkdPDn_shiFc6UBc9xPsaW7HTvnjJhG9fioYk9X3_ItKBZ2t2smF9zrmfOorHleQo3pCr7nFpy9rhzKnCy8xWDNmSDMjCZHJdNGoEElewHS2KCoodb8qI1k3n9nAZKX9PThos4cyfCO4oxXdeg8WDWxPB_Thnp2bEt4sy-mX9EuzMvakPy1_JJkXIl-fWaDbjIKNQIigLszeD80sfxCsGB4W9kp4wrDzOzfhQwoZKvxGxIFzktUa7wCJEOq37D2Bi7RayN2Ot26R_mEDKMFYqQK5vDP2wIhxCO4S_hN7fwxdDODg7Y1Wm4U5IPcM8t-lyNYhkuL6Uc0JQ-cg5CsjtKXFY";
	
	
	public void initData() {
		
		fingerprintTemplates = getFingerprintTemplatesList();
		
		capturer.addDataListener(new DPFPDataAdapter() {
			@Override public void dataAcquired(final DPFPDataEvent e) {
				Platform.runLater(new Runnable() {	public void run() {
					clearStatus();
					clearUserDisplay();
					//System.out.println("The fingerprint sample was captured.");
					process(e.getSample());
				}});
			}
		});
		
		capturer.addReaderStatusListener(new DPFPReaderStatusAdapter() {
			
			@Override public void readerConnected(final DPFPReaderStatusEvent e) {
				Platform.runLater(new Runnable() {	public void run() {
					//System.out.println("The fingerprint reader was connected.");
				}});
			}
			@Override public void readerDisconnected(final DPFPReaderStatusEvent e) {
				Platform.runLater(new Runnable() {	public void run() {
					//System.out.println("The fingerprint reader was disconnected.");
				}});
			}
		});
		
		capturer.addSensorListener(new DPFPSensorAdapter() {
			
			@Override public void fingerTouched(final DPFPSensorEvent e) {
				Platform.runLater(new Runnable() {	public void run() {
					//System.out.println("The fingerprint reader was touched.");
				}});
			}
			
			
			@Override public void fingerGone(final DPFPSensorEvent e) {
				Platform.runLater(new Runnable() {	public void run() {
					//System.out.println("The finger was removed from the fingerprint reader.");
				}});
			}
		});
		
		capturer.addImageQualityListener(new DPFPImageQualityAdapter() {
			@Override public void onImageQuality(final DPFPImageQualityEvent e) {
				
				Platform.runLater(new Runnable() {	public void run() {
					/*if (e.getFeedback().equals(DPFPCaptureFeedback.CAPTURE_FEEDBACK_GOOD))
						System.out.println("The quality of the fingerprint sample is good.");
					else
						System.out.println("The quality of the fingerprint sample is poor.");*/
				}});
			}
		});
		
		// start capture here.
		start();
	}

	protected void start()
	{
		capturer.startCapture();
	}

	protected void stopCapture()
	{
		capturer.stopCapture();
	}
	
	protected void process(DPFPSample sample) /*throws IOException*/ {
		// Process the sample and create a feature set for the enrollment purpose.
				DPFPFeatureSet features = extractFeatures(sample, DPFPDataPurpose.DATA_PURPOSE_VERIFICATION);

				// Check quality of the sample and start verification if it's good
				if (features != null)
				{
					boolean verified = false;

					if (fingerprintTemplates.size() > 0) {
						
						FingerprintTemplate currentResident = new FingerprintTemplate();
						
						Iterator<FingerprintTemplate> residentsIterator = fingerprintTemplates.iterator();
						while(residentsIterator.hasNext()) {
							
							currentResident = residentsIterator.next();
							
							byte[] right_thumb = currentResident.getRightThumbFingerprintTemplate();
							byte[] left_thumb = currentResident.getLeftThumbFingerprintTemplate();
							byte[] right_index = currentResident.getLeftIndexFingerprintTemplate();
							byte[] left_index = currentResident.getRightIndexFingerprintTemplate();
							
							if (right_thumb != null && right_thumb.length > 0) {
								if (checkVerify(features, right_thumb)) {
									verified = true;
								}
							}
							
							
							if (left_thumb != null && left_thumb.length > 0) {
								if (checkVerify(features, left_thumb)) {
									verified = true;
								}
							}
							
							if (left_index != null && left_index.length > 0) {
								if (checkVerify(features, left_index)) {
									verified = true;
								}
							}
							
							if (right_index != null && right_index.length > 0) {
								if (checkVerify(features, right_index)) {
									verified = true;
								}
							}
							
							
							if (verified) {
								break;
							}
							
						}
						
						if (verified) {
							displayUser(
								currentResident.getFullName(),
								currentResident.getPhoneNumber(),
								currentResident.getPropertyAddress(),
								currentResident.getDesignation(),
								currentResident.getType(),
								currentResident.getprofilePicture()
								);
							
							userFound();
						} else {
							userNotFound();
							clearUserDisplay();
						}
					} else {
						// Indicate that no finger template was loaded.
						//System.out.println("No resident loaded.");
					}
				}
	}
	
	
	protected boolean checkVerify(DPFPFeatureSet features, byte[] fp) {
		DPFPTemplate template = DPFPGlobal.getTemplateFactory().createTemplate();
		template.deserialize(fp);
		DPFPVerificationResult result = 
				verificator.verify(features, template);
			//System.out.println(result.getFalseAcceptRate());
			
		if (result.isVerified()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	protected void userNotFound() {
		labelStatus.setText("Profile not found!.");
		labelStatus.setFill(Color.RED);
	}
	
	protected void userFound() {
		labelStatus.setText("Profile Found");
		labelStatus.setFill(Color.GREEN);
	}
	
	protected void clearStatus() {
		labelStatus.setText("");
	}
	
	protected void clearUserDisplay() {
		tfFullName.setText("");
		tfPhoneNumber.setText("");
		tfPropertyUnitName.setText("");
		tfDesignation.setText("");
		tfProfileType.setText("");
		
		InputStream input = getClass().getResourceAsStream("/application/avatar.jpg");
		Image image = new Image(input);
		profilePictureImageView.setImage(image);
	}
	
	
	
	protected void displayUser(
			String full_name,
			String phone_number,
			String property_unit_name,
			String designation,
			String type, String profilePicture) {
		
		tfFullName.setText(full_name);
		tfPhoneNumber.setText(phone_number);
		tfPropertyUnitName.setText(property_unit_name);
		tfDesignation.setText(designation);
		tfProfileType.setText(type);
		
		if (profilePicture != null && profilePicture.length() > 0) {
			Platform.runLater(new Runnable() {	public void run() {
				Image image = new Image(profilePicture);
				profilePictureImageView.setImage(image);
			}});
		}
		
	}
	
	
	protected DPFPFeatureSet extractFeatures(DPFPSample sample, DPFPDataPurpose purpose)
	{
		DPFPFeatureExtraction extractor = DPFPGlobal.getFeatureExtractionFactory().createFeatureExtraction();
		try {
			return extractor.createFeatureSet(sample, purpose);
		} catch (DPFPImageQualityException e) {
			return null;
		}
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
	
	public void stop()
	{
		stopCapture();
	}
	
	
	public ObservableList<FingerprintTemplate> getFingerprintTemplatesList() {
		
		ObservableList<FingerprintTemplate> fingerprintTemplatesList = FXCollections.observableArrayList();
		
		String url1 = "http://griffontech-estate-manager.herokuapp.com/api/v1/fingerprint-templates";
		
		Service<ObservableList<FingerprintTemplate>> service = new FingerprintTemplatesRetrievalService(url1, this.token);
		
		service.stateProperty().addListener(new InvalidationListener() {
			
			@Override
			public void invalidated(Observable observable) {
				//System.out.println("value is now "+service.getState());
				
				if (service.getState().equals(Worker.State.SUCCEEDED)) {
					
					fingerprintTemplatesList.addAll(service.getValue());
					
					//System.out.println("Total of " + fingerprintTemplatesList.size() + " was loaded");
					networkStatusTextDisplay.setFill(Color.GREEN);
					networkStatusTextDisplay.setText("Successful");
					
				} else if (service.getState().equals(Worker.State.RUNNING)) {
					networkStatusTextDisplay.setFill(Color.BLACK);
					networkStatusTextDisplay.setText("Executing Request");
					
				} else if (service.getState().equals(Worker.State.FAILED)) {
					
					networkStatusTextDisplay.setFill(Color.RED);
					networkStatusTextDisplay.setText("Failed");
				}
			}
		});
		
		//System.out.println("START SERVICE = "+service.getTitle());
		service.start();
		
		
		return fingerprintTemplatesList;
	}
	
	
}
