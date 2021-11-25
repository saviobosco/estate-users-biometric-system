package application.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import application.Models.Resident;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ResidentsController implements Initializable {

	@FXML
	private ObservableList<Resident> residentsList;
	
	@FXML
	private TextField searchField;
	
	@FXML
	private Button btnSearch;
	
	@FXML
	private TableView<Resident> residentsTableView;
	
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
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
	}
	
	@FXML
	public void handleButtonAction(ActionEvent event) {
		if (event.getSource() == btnSearch) {
			
		}
	}
	
	
	
	

}
