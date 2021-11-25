package application.Models;

import java.util.Base64;

public class Resident {

	private String id;
	private String fullName;
	private String firstName;
	private String middleName;
	private String lastName;
	private String phoneNumber;
	private String emailAddress;
	private String gender;
	private String propertyUnitName;
	private String designation;
	private String leftThumbFingerprintTemplate = "";
	private String leftIndexFingerprintTemplate = "";
	private String rightThumbFingerprintTemplate = "";
	private String rightIndexFingerprintTemplate = "";

	
	public Resident(String id, String fullName, String phoneNumber, String emailAddress, String gender,
			String propertyUnitName, String designation) {

		this.id = id;
		this.fullName = fullName;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
		this.gender = gender;
		this.propertyUnitName = propertyUnitName;
		this.designation = designation;
	}


	public String getId() {
		return id;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	
	public String getMiddleName() {
		return middleName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getFullName() {
		return fullName;
	}


	public String getPhoneNumber() {
		return phoneNumber;
	}


	public String getEmailAddress() {
		return emailAddress;
	}


	public String getGender() {
		return gender;
	}


	public String getPropertyUnitName() {
		return propertyUnitName;
	}


	public String getDesignation() {
		return designation;
	}
	
	public void setLeftThumbFingerPrintTemplate(String template) {
		leftThumbFingerprintTemplate = template;
	}
	
	public void setLeftIndexFingerPrintTemplate(String template) {
		leftIndexFingerprintTemplate = template;
	}
	
	public void setRightThumbFingerPrintTemplate(String template) {
		rightThumbFingerprintTemplate = template;
	}
	
	public void setRightIndexFingerPrintTemplate(String template) {
		rightIndexFingerprintTemplate = template;
	}
	
	
	public byte[] getLeftThumbFingerPrintTemplate()
	{
		byte [] backToByte = null;
		if (!leftThumbFingerprintTemplate.isEmpty()) {
			backToByte = Base64.getDecoder().decode(leftThumbFingerprintTemplate);
		}
		return backToByte;
	}
	
	public byte[] getLeftIndexFingerPrintTemplate()
	{
		byte [] backToByte = null;
		if (!leftIndexFingerprintTemplate.isEmpty()) {
			backToByte = Base64.getDecoder().decode(leftIndexFingerprintTemplate);
		}
		return backToByte;
	}
	
	
	public byte[] getRightThumbFingerPrintTemplate()
	{
		byte [] backToByte = null;
		if (!rightThumbFingerprintTemplate.isEmpty()) {
			backToByte = Base64.getDecoder().decode(rightThumbFingerprintTemplate);
		}
		return backToByte;
	}
	
	public byte[] getRightIndexFingerPrintTemplate()
	{
		byte [] backToByte = null;
		if (!rightIndexFingerprintTemplate.isEmpty()) {
			backToByte = Base64.getDecoder().decode(rightIndexFingerprintTemplate);
		}
		return backToByte;
	}
	
}
