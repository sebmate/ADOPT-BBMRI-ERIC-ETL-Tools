package de.fau.med.imi.MDRPipe.ETLHelper;

import java.util.List;

public class DatabaseAttribute {

	private String attributeName;
	private String attributeType;
	private List<String> attributeProperties;
	
	public DatabaseAttribute(String attributeName, String attributeType) {
		this.setAttributeName(attributeName);
		this.setAttributeType(attributeType);
	}
	
	public DatabaseAttribute(String attributeName, String attributeType, List<String> attributeProperties) {
		this.setAttributeName(attributeName);
		this.setAttributeType(attributeType);
		this.setAttributeProperties(attributeProperties);
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(String attributeType) {
		this.attributeType = attributeType;
	}

	public List<String> getAttributeProperties() {
		return attributeProperties;
	}

	public void setAttributeProperties(List<String> attributeProperties) {
		this.attributeProperties = attributeProperties;
	}
	
}
