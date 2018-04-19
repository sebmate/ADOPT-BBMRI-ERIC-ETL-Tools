package de.fau.med.imi.MDRPipe.ETLHelper;

public class XMLEntryForGBA implements XMLEntry {
	
	String caseId;
	String instance;
	String mdrkey;
	String value;
	String timeStamp;
	String dataelementType;
	
	public XMLEntryForGBA(String caseId, String instance, String mdrkey, String value, String timestamp, String dataelementType) {
		this.setCaseId(caseId);
		this.setInstance(instance);
		this.setMdrkey(mdrkey);
		this.setValue(value);
		this.setTimeStamp(timestamp);
		this.setDataelementType(dataelementType);
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
	
}
