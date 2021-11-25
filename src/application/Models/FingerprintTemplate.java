package application.Models;

import java.util.Base64;

public class FingerprintTemplate {

	private String fullName;
	private String phoneNumber;
	private String designation;
	private String propertyAddress;
	private String profilePicture;
	private String type;
	private String leftThumbFingerprintTemplate = "";
	private String leftIndexFingerprintTemplate = "";
	private String rightThumbFingerprintTemplate = "";
	private String rightIndexFingerprintTemplate = "";
	
	
	public String getFullName() {
		return fullName;
	}
	
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	
	public String getDesignation() {
		return designation;
	}
	
	
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	
	
	public String getPropertyAddress() {
		return propertyAddress;
	}
	
	
	public void setPropertyAddress(String propertyAddress) {
		this.propertyAddress = propertyAddress;
	}
	
	public String getprofilePicture() {
		return profilePicture;
	}
	
	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}
	
	
	public byte[] getLeftThumbFingerprintTemplate() {
		byte [] backToByte = null;
		if (!leftThumbFingerprintTemplate.isEmpty()) {
			backToByte = Base64.getDecoder().decode(leftThumbFingerprintTemplate);
		}
		return backToByte;
	}
	
	
	public void setLeftThumbFingerprintTemplate(String leftThumbFingerprintTemplate) {
		this.leftThumbFingerprintTemplate = leftThumbFingerprintTemplate;
	}
	
	
	public byte[] getLeftIndexFingerprintTemplate() {
		byte [] backToByte = null;
		if (!leftIndexFingerprintTemplate.isEmpty()) {
			backToByte = Base64.getDecoder().decode(leftIndexFingerprintTemplate);
		}
		return backToByte;
	}
	
	public void setLeftIndexFingerprintTemplate(String leftIndexFingerprintTemplate) {
		this.leftIndexFingerprintTemplate = leftIndexFingerprintTemplate;
	}
	
	
	public byte[] getRightIndexFingerprintTemplate() {
		byte [] backToByte = null;
		if (!rightIndexFingerprintTemplate.isEmpty()) {
			backToByte = Base64.getDecoder().decode(rightIndexFingerprintTemplate);
		}
		return backToByte;
	}
	
	
	public void setRightIndexFingerprintTemplate(String rightIndexFingerprintTemplate) {
		this.rightIndexFingerprintTemplate = rightIndexFingerprintTemplate;
	}
	
	
	public byte[] getRightThumbFingerprintTemplate() {
		byte [] backToByte = null;
		if (!rightThumbFingerprintTemplate.isEmpty()) {
			backToByte = Base64.getDecoder().decode(rightThumbFingerprintTemplate);
		}
		return backToByte;
	}
	
	
	public void setRightThumbFingerprintTemplate(String rightThumbFingerprintTemplate) {
		this.rightThumbFingerprintTemplate = rightThumbFingerprintTemplate;
	}
}
