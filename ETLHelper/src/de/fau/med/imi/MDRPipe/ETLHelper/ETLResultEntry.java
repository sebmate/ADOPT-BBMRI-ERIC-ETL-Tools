package de.fau.med.imi.MDRPipe.ETLHelper;

public class ETLResultEntry {

	private String dataCaseID = "";
	private String dataConcept = "";
	private String dataValue = "";
	private String dataTimestamp = "";
	private String dataInstance = "";

	private String sourceURN = "";
	private String sourceSourceSlot = "";
	private String sourceDataType = "";
	private String sourcePermValue = "";
	private String sourcePath = "";
	private String sourceMatchingInput = "";

	private String targetURN = "";
	private String targetSourceSlot = "";
	private String targetDataType = "";
	private String targetPermValue = "";
	private String targetPath = "";
	private String targetMatchingInput = "";

	private String mappingSourceString = "";
	private String mappingScore = "";
	private String mappingDoMap = "";
	private String mappingTargetString = "";

	private String finalValue = "";
	private String transformationRule = "";
	private int transformationSuccess = 2; // 2: Success // 1: Warning // 0: Error

	private int success = 2; // 2: Success // 1: Warning // 0: Error

	public String getConcept(String input) {
		String concept = "";
		if(input != null) {
			String[] splittedString = input.split(" = ");
			String pathAndConcept = splittedString[0];
			String[] splittedPathAndConcept = pathAndConcept.split(" / ");
			concept = splittedPathAndConcept[splittedPathAndConcept.length - 1];
		}
		return concept;
	}

	public String toString() {
		return 	this.getDataCaseID() + " | " + 
				this.getDataInstance() + " | " + 
				this.getTargetURN() + " | " + 
				this.getConcept(this.getTargetPath()) + " | " + 
				this.getDataValue() + " | " + 
				this.getFinalValue() + " | " + 
				this.getDataTimestamp() + "\n";
	}

	public String getSourceURN() {
		return sourceURN;
	}

	public void setSourceURN(String sourceURN) {
		this.sourceURN = sourceURN;
	}

	public String getSourceSourceSlot() {
		return sourceSourceSlot;
	}

	public void setSourceSourceSlot(String sourceSourceSlot) {
		this.sourceSourceSlot = sourceSourceSlot;
	}

	public String getSourceDataType() {
		return sourceDataType;
	}

	public void setSourceDataType(String sourceDataType) {
		this.sourceDataType = sourceDataType;
	}

	public String getSourcePermValue() {
		return sourcePermValue;
	}

	public void setSourcePermValue(String sourcePermValue) {
		this.sourcePermValue = sourcePermValue;
	}

	public String getSourcePath() {
		return sourcePath;
	}

	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}

	public String getSourceMatchingInput() {
		return sourceMatchingInput;
	}

	public void setSourceMatchingInput(String sourceMatchingInput) {
		this.sourceMatchingInput = sourceMatchingInput;
	}

	public String getTargetURN() {
		return targetURN;
	}

	public void setTargetURN(String targetURN) {
		this.targetURN = targetURN;
	}

	public String getTargetSourceSlot() {
		return targetSourceSlot;
	}

	public void setTargetSourceSlot(String targetSourceSlot) {
		this.targetSourceSlot = targetSourceSlot;
	}

	public String getTargetDataType() {
		return targetDataType;
	}

	public void setTargetDataType(String targetDataType) {
		this.targetDataType = targetDataType;
	}

	public String getTargetPermValue() {
		return targetPermValue;
	}

	public void setTargetPermValue(String targetPermValue) {
		this.targetPermValue = targetPermValue;
	}

	public String getTargetPath() {
		return targetPath;
	}

	public void setTargetPath(String targetPath) {
		this.targetPath = targetPath;
	}

	public String getTargetMatchingInput() {
		return targetMatchingInput;
	}

	public void setTargetMatchingInput(String targetMatchingInput) {
		this.targetMatchingInput = targetMatchingInput;
	}

	public String getDataCaseID() {
		return dataCaseID;
	}

	public void setDataCaseID(String dataCaseID) {
		this.dataCaseID = dataCaseID;
	}

	public String getDataConcept() {
		return dataConcept;
	}

	public void setDataConcept(String dataConcept) {
		this.dataConcept = dataConcept;
	}

	public String getDataValue() {
		return dataValue;
	}

	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
	}

	public String getDataTimestamp() {
		return dataTimestamp;
	}

	public void setDataTimestamp(String dataTimestamp) {
		this.dataTimestamp = dataTimestamp;
	}

	public String getDataInstance() {
		return dataInstance;
	}

	public void setDataInstance(String dataInstance) {
		this.dataInstance = dataInstance;
	}

	public String getMappingSourceString() {
		return mappingSourceString;
	}

	public void setMappingSourceString(String mappingSourceString) {
		this.mappingSourceString = mappingSourceString;
	}

	public String getMappingScore() {
		return mappingScore;
	}

	public void setMappingScore(String mappingScore) {
		this.mappingScore = mappingScore;
	}

	public String getMappingDoMap() {
		return mappingDoMap;
	}

	public void setMappingDoMap(String mappingDoMap) {
		this.mappingDoMap = mappingDoMap;
	}

	public String getMappingTargetString() {
		return mappingTargetString;
	}

	public void setMappingTargetString(String mappingTargetString) {
		this.mappingTargetString = mappingTargetString;
	}

	int getSuccess() {
		return success;
	}

	void setSuccess(int success) {
		this.success = success;
	}

	public String getFinalValue() {
		return finalValue;
	}

	public void setFinalValue(String finalValue) {
		this.finalValue = finalValue;
	}

	public String getTransformationRule() {
		return transformationRule;
	}

	public void setTransformationRule(String transformationRule) {
		this.transformationRule = transformationRule;
	}

	public int getTransformationSuccess() {
		return transformationSuccess;
	}

	public void setTransformationSuccess(int transformationSuccess) {
		this.transformationSuccess = transformationSuccess;
	}

}
