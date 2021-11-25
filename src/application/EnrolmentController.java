package application;


import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.EnumMap;


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
import com.digitalpersona.onetouch.processing.DPFPEnrollment;
import com.digitalpersona.onetouch.processing.DPFPFeatureExtraction;
import com.digitalpersona.onetouch.processing.DPFPImageQualityException;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class EnrolmentController {

	@FXML
	private Label fingerPrintLabel;
	@FXML
	private ImageView picture;
	@FXML
	private Label labelStatus;
	@FXML
	private TextArea tALogs;
	
    private EnumMap<DPFPFingerIndex, DPFPTemplate> templates;
	private DPFPCapture capturer = DPFPGlobal.getCaptureFactory().createCapture();
	private DPFPEnrollment enroller = DPFPGlobal.getEnrollmentFactory().createEnrollment();
	private Integer fingerIndex;
	
	/*public EnrolmentController(Stage primaryStage) {
		this.primaryStage = primaryStage;

		
		AnchorPane root = new AnchorPane();
		Label textLabel = new Label("Please place your finger here");
		root.getChildren().add(textLabel);
		
		Scene scene = new Scene(root, 300, 300);
		this.primaryStage.setScene(scene);
		this.primaryStage.showAndWait();
	}*/
	
	public void initData(String text, Integer fingerIndex, EnumMap<DPFPFingerIndex, DPFPTemplate> templates) {
		fingerPrintLabel.setText("Using the fingerprint reader, scan your " + text + " finger");
		this.templates = templates;
		this.fingerIndex = fingerIndex;
		
		capturer.addDataListener(new DPFPDataAdapter() {
			@Override public void dataAcquired(final DPFPDataEvent e) {
				Platform.runLater(new Runnable() {	public void run() {
					makeReport("The fingerprint sample was captured.");
					setPrompt("Scan the same fingerprint again.");
					process(e.getSample());
				}});
			}
		});
		
		capturer.addReaderStatusListener(new DPFPReaderStatusAdapter() {
			
			@Override public void readerConnected(final DPFPReaderStatusEvent e) {
				Platform.runLater(new Runnable() {	public void run() {
		 			makeReport("The fingerprint reader was connected.");
				}});
			}
			@Override public void readerDisconnected(final DPFPReaderStatusEvent e) {
				Platform.runLater(new Runnable() {	public void run() {
					makeReport("The fingerprint reader was disconnected.");
				}});
			}
		});
		
		capturer.addSensorListener(new DPFPSensorAdapter() {
			
			@Override public void fingerTouched(final DPFPSensorEvent e) {
				Platform.runLater(new Runnable() {	public void run() {
					makeReport("The fingerprint reader was touched.");
				}});
			}
			
			
			@Override public void fingerGone(final DPFPSensorEvent e) {
				Platform.runLater(new Runnable() {	public void run() {
					makeReport("The finger was removed from the fingerprint reader.");
				}});
			}
		});
		
		capturer.addImageQualityListener(new DPFPImageQualityAdapter() {
			@Override public void onImageQuality(final DPFPImageQualityEvent e) {
				
				Platform.runLater(new Runnable() {	public void run() {
					if (e.getFeedback().equals(DPFPCaptureFeedback.CAPTURE_FEEDBACK_GOOD))
						makeReport("The quality of the fingerprint sample is good.");
					else
						makeReport("The quality of the fingerprint sample is poor.");
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
	
	public void setStatus(String string) {
		labelStatus.setText(string);
	}
	public void setPrompt(String string) {
		fingerPrintLabel.setText(string);
	}
	
	public void makeReport(String string) {
		tALogs.appendText(string + "\n");
	}
	
	protected void process(DPFPSample sample)
	{
		// Draw fingerprint sample image.
		drawPicture(convertSampleToBitmap(sample));
		DPFPFeatureSet features = extractFeatures(sample, DPFPDataPurpose.DATA_PURPOSE_ENROLLMENT);

		// Check quality of the sample and add to enroller if it's good
		if (features != null) try
		{
			makeReport("The fingerprint feature set was created.");
			enroller.addFeatures(features);		// Add feature set to template.
		}
		catch (DPFPImageQualityException ex) { }
		finally {
			updateStatus();

			// Check if template has been created.
			switch(enroller.getTemplateStatus())
			{
				case TEMPLATE_STATUS_READY:	// report success and stop capturing
					stopCapture();
					
					DPFPTemplate template = enroller.getTemplate();

					// saving the template in an EnumpMap of finger templates.
					templates.put(DPFPFingerIndex.values()[fingerIndex], template);
					//((MainForm)getOwner()).setTemplate(enroller.getTemplate());
					setPrompt("Click Close, and then click Fingerprint Verification.");
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setContentText("Finger capture successful");
					alert.showAndWait();
					Stage currentStage = (Stage) labelStatus.getScene().getWindow();
					currentStage.close();
					break;

				case TEMPLATE_STATUS_FAILED:	// report failure and restart capturing
					enroller.clear();
					stopCapture();
					updateStatus();
					//((MainForm)getOwner()).setTemplate(null);
					//JOptionPane.showMessageDialog(EnrollmentForm.this, "The fingerprint template is not valid. Repeat fingerprint enrollment.", "Fingerprint Enrollment", JOptionPane.ERROR_MESSAGE);
					start();
					break;
			case TEMPLATE_STATUS_INSUFFICIENT:
				break;
			case TEMPLATE_STATUS_UNKNOWN:
				break;
			default:
				break;
			}
		}
	}
	
	public void drawPicture(Image image) {
		BufferedImage bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(image, 0, 0, null);
		bGr.dispose();
		
		javafx.scene.image.Image jfxImage = SwingFXUtils.toFXImage(bimage, null);
		picture.setImage(jfxImage);
		/*labelPicture. (new ImageIcon(
			image.getScaledInstance((int)labelPicture.getWidth(), (int)labelPicture.getHeight(), Image.SCALE_DEFAULT)));*/
	}
	
	protected Image convertSampleToBitmap(DPFPSample sample) {
		//return /*SwingFXUtils.toFXImage(*/(BufferedImage) DPFPGlobal.getSampleConversionFactory().createImage(sample);
		return  DPFPGlobal.getSampleConversionFactory().createImage(sample);
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
	
	private void updateStatus()
	{
		// Show number of samples needed.
		setStatus(String.format("Fingerprint samples needed: %1$s", enroller.getFeaturesNeeded()));
	}
	
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
    
    
    public void stop() {
    	stopCapture();
    }
}
