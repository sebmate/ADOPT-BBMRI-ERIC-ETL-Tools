package de.fau.med.imi.MDRPipe.ETLHelper;

public class DataelementInformation {
	
	private String urn = "";
	private String dataelementType = "basic";
	private String eventType = "";
	
	public DataelementInformation(String urn, String dataelementType, String eventType) {
		this.urn = urn;
		this.dataelementType = dataelementType;
		this.eventType = eventType;
	}

	public String getUrn() {
		return urn;
	}

	public void setUrn(String urn) {
		this.urn = urn;
	}

	public String getDataelementType() {
		return dataelementType;
	}

	public void setDataelementType(String dataelementType) {
		this.dataelementType = dataelementType;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	
}
