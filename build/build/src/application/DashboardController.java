package application;


/*import java.io.IOException;
import java.io.InputStream;
import java.nio.CharBuffer;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;*/
import java.net.URL;
import java.util.ResourceBundle;



/*import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;*/

/*import org.apache.hc.client5.http.async.methods.AbstractCharResponseConsumer;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequests;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.message.BasicHeader;
import org.apache.hc.core5.http.nio.AsyncRequestProducer;
import org.apache.hc.core5.http.nio.support.AsyncRequestBuilder;*/

import application.Models.Resident;
import application.controllers.EditResidentController;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class DashboardController implements Initializable {

	@FXML
	private MenuItem menuItemAddNewResident;
	@FXML
	private MenuItem menuItemAllResidents;
	@FXML
	private MenuItem menuItemResidentIdentification;
	@FXML
	private TableView<Resident> tvResidents;
	
	@FXML
	private TableColumn<Resident, String> colFullName;
	@FXML
	private TableColumn<Resident, String> colPhoneNumber;
	@FXML
	private TableColumn<Resident, String> colEmailAddress;
	@FXML
	private TableColumn<Resident, String> colPropertyUnitName;
	@FXML
	private TableColumn<Resident, String> colDesignation;
	@FXML
	private TableColumn<Resident, String> colGender;
	
	@FXML
	private Text networkStatusTextDisplay;
	
	@FXML
	private Button btnReloadResidentsData;
	
	@FXML
	private Button btnOpenEstateStaffs;
	
	@FXML
	private Button btnOpenDomesticStaffs;

	//private String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiM2JhNWQ5OWQxNjRiNGUxYzI5M2E1YTdhYjE5YTY4MmViZGQzYzE2NjJkMTJkYjkyYTYzNDhlMWRhOGNhNTFhN2NjNmEwOWQzN2VhNzY4MWEiLCJpYXQiOjE2MDQwNjUxOTcsIm5iZiI6MTYwNDA2NTE5NywiZXhwIjoxNjM1NjAxMTk3LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.Sueyi8iqhp3Y5dX1S7XxkAcROJHSh9lexOJQbevUcORmsnaoig4LfZiHpjX-20BxvrWHsEpiX0CEsltuwTrVC4m1VgZR0PDA13HEs3C5W0Hhn9zZzIvvQKo7vvcvCZ8ncZMQMqWI0UdANF52M-G3QH5jUhGp6Q697_zzXDVizpbxwQLHBxtrk9Ad3GBx4ONSjN562uy2Ja6x-KNlhMZ1egvC1b1EK2NtUB8ZU31IBiXeBu79FFDKaQg5i14TBVIqEfAw4gN-ge0-9j4T4KoFzGVloGAFuxqbn12o0VWourJUgFkR-Zbsd0Iii5BaGUOhXWtFv-Kg6rLkLOY3L4lkdPDn_shiFc6UBc9xPsaW7HTvnjJhG9fioYk9X3_ItKBZ2t2smF9zrmfOorHleQo3pCr7nFpy9rhzKnCy8xWDNmSDMjCZHJdNGoEElewHS2KCoodb8qI1k3n9nAZKX9PThos4cyfCO4oxXdeg8WDWxPB_Thnp2bEt4sy-mX9EuzMvakPy1_JJkXIl-fWaDbjIKNQIigLszeD80sfxCsGB4W9kp4wrDzOzfhQwoZKvxGxIFzktUa7wCJEOq37D2Bi7RayN2Ot26R_mEDKMFYqQK5vDP2wIhxCO4S_hN7fwxdDODg7Y1Wm4U5IPcM8t-lyNYhkuL6Uc0JQ-cg5CsjtKXFY";
	
	public void initialize(URL url, ResourceBundle rb)
	{			
		
        /*logger.setLevel(Level.INFO);
        
        try {
			fileTxt = new FileHandler("Logging.txt");
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        // create a TXT formatter
        formatterTxt = new SimpleFormatter();
        fileTxt.setFormatter(formatterTxt);
        logger.addHandler(fileTxt);*/
        
		showResidents();
	}
	
	
	@FXML
	public void handleMenuBarButtonAction(ActionEvent event) {
		
		if ((MenuItem) event.getSource() == menuItemAddNewResident) {
			Stage primaryStage = new Stage();
			try {
				Parent root = FXMLLoader.load(getClass().getResource("/application/AddResident.fxml"));
				Scene scene = new Scene(root);
				primaryStage.setScene(scene);
				primaryStage.setTitle("Add New Resident");
				primaryStage.initModality(Modality.APPLICATION_MODAL);
				primaryStage.show();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else if ((MenuItem) event.getSource() == menuItemResidentIdentification) {
			FXMLLoader loader = new FXMLLoader(
				    getClass().getResource(
				      "/application/Identification.fxml"
				    )
				  );
			try {
				Stage stage = new Stage();
				  stage.setScene(
				    new Scene(loader.load())
				  );

				  IdentificationController controller = loader.getController();
				  controller.initData();
				  stage.setTitle("Resident Identification - Griffon Technologies Nig");
				  stage.show();
				  
				  stage.setOnCloseRequest((WindowEvent we) -> {
						controller.stop();
					});
				  
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@FXML
	public void handleMouseAction(MouseEvent event) {
		if (event.getSource() == tvResidents) {
			
			ContextMenu contextMenu = new ContextMenu();
		    Resident selectedResident = tvResidents.getSelectionModel().getSelectedItem();
		    
		    if (selectedResident != null) {
		    	MenuItem mi1 = new MenuItem("Edit Resident");
				mi1.setOnAction((ActionEvent actionEvent) -> {
				    
				    FXMLLoader fxmlLoader = new FXMLLoader();
				    fxmlLoader.setLocation(getClass().getResource("/application/fxml/EditResident.fxml"));
				    
				    // open the resident edit.
					try {
						Parent root = fxmlLoader.load();
					    EditResidentController controller = fxmlLoader.getController();
					    
					    controller.initData(selectedResident, this);
						
					    Scene scene = new Scene(root);

					    Stage primaryStage = new Stage();
						primaryStage.setScene(scene);
						primaryStage.setTitle("Edit Resident");
						primaryStage.initModality(Modality.APPLICATION_MODAL);
						primaryStage.show();
						
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				});
				
				MenuItem mi2 = new MenuItem("View Resident");
				
				contextMenu.getItems().addAll(mi1, mi2);
				
				tvResidents.setContextMenu(contextMenu);
		    }
		}
	}
	
	@FXML
	public void handleButtonAction(ActionEvent event) {
		if (event.getSource() == btnReloadResidentsData) {
			showResidents();
			
			
		} else if (event.getSource() == btnOpenEstateStaffs) {
			FXMLLoader fxmlLoader = new FXMLLoader();
		    fxmlLoader.setLocation(getClass().getResource("/application/fxml/EstateStaffs.fxml"));
		    
		    // open the resident edit.
			try {
				Parent root = fxmlLoader.load();				

			    Scene scene = new Scene(root);

			    // Assign the stage an owner
			    Stage primaryStage = new Stage();
				primaryStage.setScene(scene);
				primaryStage.setTitle("Estate Staffs");
				
				primaryStage.show();
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else if (event.getSource() == btnOpenDomesticStaffs) {
			FXMLLoader fxmlLoader = new FXMLLoader();
		    fxmlLoader.setLocation(getClass().getResource("/application/fxml/DomesticStaffs.fxml"));
		    
		    // open the resident edit.
			try {
				Parent root = fxmlLoader.load();				

			    Scene scene = new Scene(root);

			    // Assign the stage an owner
			    Stage primaryStage = new Stage();
				primaryStage.setScene(scene);
				primaryStage.setTitle("Domestic Staffs");
				
				primaryStage.show();
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	
	
	public ObservableList<Resident> getResidentsList() {
		
		ObservableList<Resident> residentsList = FXCollections.observableArrayList();
		
		String url1 = "http://griffontech-estate-manager.herokuapp.com/api/v1/residents";
		
		Service<ObservableList<Resident>> service = new ResidentRetrievalService(url1);
		
		service.stateProperty().addListener(new InvalidationListener() {
			
			@Override
			public void invalidated(Observable observable) {
				//System.out.println("value is now "+service.getState());
				if (service.getState().equals(Worker.State.SUCCEEDED)) {
					residentsList.addAll(service.getValue());
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
		
		
		return residentsList;
	}
	
	public void showResidents()
	{
		ObservableList<Resident> list = getResidentsList();
		
		colFullName.setCellValueFactory(new PropertyValueFactory<Resident, String>("fullName"));
		colPhoneNumber.setCellValueFactory(new PropertyValueFactory<Resident, String>("phoneNumber"));
		colEmailAddress.setCellValueFactory(new PropertyValueFactory<Resident, String>("emailAddress"));
		colPropertyUnitName.setCellValueFactory(new PropertyValueFactory<Resident, String>("propertyUnitName"));
		colDesignation.setCellValueFactory(new PropertyValueFactory<Resident, String>("designation"));
		colGender.setCellValueFactory(new PropertyValueFactory<Resident, String>("gender"));

		tvResidents.setItems(list);
	}
	
	public void displayData() {
		this.showResidents();
	}
}
