package application.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import application.Models.EstateStaff;
import application.Retrievals.EstateStaffsRetrievalService;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EstateStaffsController implements Initializable {

	
	@FXML
	private TextField searchField;
	
	@FXML
	private Button btnSearch;
	
	@FXML
	private Button btnReloadData;
	
	@FXML
	private TableView<EstateStaff> estateStaffsTableView;
	
	@FXML
	private TableColumn<EstateStaff, String> colFullName;
	
	@FXML
	private TableColumn<EstateStaff, String> colPhoneNumber;
	
	@FXML
	private TableColumn<EstateStaff, String> colEmailAddress;
	
	@FXML
	private TableColumn<EstateStaff, String> colPropertyUnitName;
	
	@FXML
	private TableColumn<EstateStaff, String> colDesignation;
	
	@FXML
	private TableColumn<EstateStaff, String> colGender;
	
	@FXML
	private Text networkStatusTextDisplay;
	
	private String webServiceAccessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiM2JhNWQ5OWQxNjRiNGUxYzI5M2E1YTdhYjE5YTY4MmViZGQzYzE2NjJkMTJkYjkyYTYzNDhlMWRhOGNhNTFhN2NjNmEwOWQzN2VhNzY4MWEiLCJpYXQiOjE2MDQwNjUxOTcsIm5iZiI6MTYwNDA2NTE5NywiZXhwIjoxNjM1NjAxMTk3LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.Sueyi8iqhp3Y5dX1S7XxkAcROJHSh9lexOJQbevUcORmsnaoig4LfZiHpjX-20BxvrWHsEpiX0CEsltuwTrVC4m1VgZR0PDA13HEs3C5W0Hhn9zZzIvvQKo7vvcvCZ8ncZMQMqWI0UdANF52M-G3QH5jUhGp6Q697_zzXDVizpbxwQLHBxtrk9Ad3GBx4ONSjN562uy2Ja6x-KNlhMZ1egvC1b1EK2NtUB8ZU31IBiXeBu79FFDKaQg5i14TBVIqEfAw4gN-ge0-9j4T4KoFzGVloGAFuxqbn12o0VWourJUgFkR-Zbsd0Iii5BaGUOhXWtFv-Kg6rLkLOY3L4lkdPDn_shiFc6UBc9xPsaW7HTvnjJhG9fioYk9X3_ItKBZ2t2smF9zrmfOorHleQo3pCr7nFpy9rhzKnCy8xWDNmSDMjCZHJdNGoEElewHS2KCoodb8qI1k3n9nAZKX9PThos4cyfCO4oxXdeg8WDWxPB_Thnp2bEt4sy-mX9EuzMvakPy1_JJkXIl-fWaDbjIKNQIigLszeD80sfxCsGB4W9kp4wrDzOzfhQwoZKvxGxIFzktUa7wCJEOq37D2Bi7RayN2Ot26R_mEDKMFYqQK5vDP2wIhxCO4S_hN7fwxdDODg7Y1Wm4U5IPcM8t-lyNYhkuL6Uc0JQ-cg5CsjtKXFY";
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		displayData();
	}
	
	@FXML
	public void handleButtonAction(ActionEvent event) {
		if (event.getSource() == btnSearch) {
			
		} else if (event.getSource() == btnReloadData) {
			this.displayData();
		}
	}
	
	@FXML
	public void handleMouseAction(MouseEvent event) {
		if (event.getSource() == estateStaffsTableView) {
			
		    EstateStaff selectedRecord = estateStaffsTableView.getSelectionModel().getSelectedItem();

		    
		    if (selectedRecord != null) {
				ContextMenu contextMenu = new ContextMenu();

		    	MenuItem mi1 = new MenuItem("Edit Record");
				mi1.setOnAction((ActionEvent actionEvent) -> {
				    
				    FXMLLoader fxmlLoader = new FXMLLoader();
				    fxmlLoader.setLocation(getClass().getResource("/application/fxml/EditEstateStaff.fxml"));
				    
				    // open the resident edit.
					try {
						Parent root = fxmlLoader.load();
					    EditEstateStaffController controller = fxmlLoader.getController();
					    
					    controller.initData(selectedRecord, this.webServiceAccessToken, this);
						
					    Scene scene = new Scene(root);

					    Stage primaryStage = new Stage();
						primaryStage.setScene(scene);
						primaryStage.setTitle("Edit Estate Staff");
						primaryStage.initModality(Modality.APPLICATION_MODAL);
						
						primaryStage.show();
						
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				});
				
				MenuItem mi2 = new MenuItem("View Record");
				
				contextMenu.getItems().addAll(mi1, mi2);
				
				estateStaffsTableView.setContextMenu(contextMenu);
		    }
		}
	}
	
	
	public ObservableList<EstateStaff> getEstateStaffsList() {
		
		ObservableList<EstateStaff> estateStaffsList = FXCollections.observableArrayList();
		
		String url1 = "http://griffontech-estate-manager.herokuapp.com/api/v1/estate-staffs";
		
		Service<ObservableList<EstateStaff>> service = new EstateStaffsRetrievalService(url1, this.webServiceAccessToken);
		
		service.stateProperty().addListener(new InvalidationListener() {
			
			@Override
			public void invalidated(Observable observable) {
				//System.out.println("value is now "+service.getState());
				if (service.getState().equals(Worker.State.SUCCEEDED)) {
					
					estateStaffsList.addAll(service.getValue());
					
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
		
		
		return estateStaffsList;
	}
	
	public void displayData()
	{
		ObservableList<EstateStaff> list = getEstateStaffsList();
		
		colFullName.setCellValueFactory(new PropertyValueFactory<EstateStaff, String>("fullName"));
		colPhoneNumber.setCellValueFactory(new PropertyValueFactory<EstateStaff, String>("phoneNumber"));
		colEmailAddress.setCellValueFactory(new PropertyValueFactory<EstateStaff, String>("emailAddress"));
		colPropertyUnitName.setCellValueFactory(new PropertyValueFactory<EstateStaff, String>("homeAddress"));
		colDesignation.setCellValueFactory(new PropertyValueFactory<EstateStaff, String>("designation"));
		colGender.setCellValueFactory(new PropertyValueFactory<EstateStaff, String>("gender"));

		estateStaffsTableView.setItems(list);
	}


}
