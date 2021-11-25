package application.Models;

import java.util.Base64;

public class Residents {

	private String id;
	private String fullName;
	private String phoneNumber;
	private String emailAddress;
	private String gender;
	private String propertyUnitName;
	private String designation;
	private String leftThumbFingerprintTemplate = "";
	private String rightThumbFingerprintTemplate = "";

	
	public Residents(String id, String fullName, String phoneNumber, String emailAddress, String gender,
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
	
	public void setRightThumbFingerPrintTemplate(String template) {
		rightThumbFingerprintTemplate = template;
	}
	
	
	public byte[] getLeftThumbFingerPrintTemplate()
	{
		byte [] backToByte = null;
		if (!leftThumbFingerprintTemplate.isEmpty()) {
			backToByte = Base64.getDecoder().decode(leftThumbFingerprintTemplate);
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
	
	
	
}
