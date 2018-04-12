package de.fau.med.imi.MDRPipe.ETLHelper;

public class XMLEntryForBBMRI implements XMLEntry {
	
	String caseId;
	String instance;
	String mdrkey;
	String name;
	String value;
	String timeStamp;
	String dataelementType;
	String eventName;
	String eventType;
	
	public XMLEntryForBBMRI(String caseId, String instance, String mdrkey, String name, String value, String timestamp, String dataelementType, String eventName, String eventType) {
		this.setCaseId(caseId);
		this.setInstance(instance);
		this.setMdrkey(mdrkey);
		this.setName(name);
		this.setValue(value);
		this.setTimeStamp(timestamp);
		this.setDataelementType(dataelementType);
		this.setEventName(eventName);
		this.setEventType(eventType);
	}
	
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	public String getInstance() {
		return instance;
	}
	public void setInstance(String instance) {
		this.instance = instance;
	}
	public String getMdrkey() {
		return mdrkey;
	}
	public void setMdrkey(String mdrkey) {
		this.mdrkey = mdrkey;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getDataelementType() {
		return dataelementType;
	}
	public void setDataelementType(String dataelementType) {
		this.dataelementType = dataelementType;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	
}
