package de.fau.med.imi.MDRPipe.ETLHelper;

import java.util.ArrayList;

public class XMLCreator {
	
	public ArrayList<ETLResultEntry> etlResultEntries;
	public XMLFormat xmlFormat;
	
	public XMLCreator(ArrayList<ETLResultEntry> etlResultEntries, String exportFormat) {
		this.setEtlResultEntries(etlResultEntries);
		switch(exportFormat) {
			case "bbmri": this.setXmlFormat(new XMLFormatForBBMRI()); break;
			case "gba": this.setXmlFormat(new XMLFormatForGBA()); break;
			default: this.setXmlFormat(new XMLFormatForBBMRI()); break;
		}
	}
	
	protected void createXML() {
		
		System.out.println("");
		
		System.out.println("");
		
		System.out.println("========= XML CREATION PROCESS =========");
		
		System.out.println("");
		
		this.getXmlFormat().createXMLFromETLResultEntries(this.getEtlResultEntries());
		
	}

	public XMLFormat getXmlFormat() {
		return xmlFormat;
	}

	public void setXmlFormat(XMLFormat xmlFormat) {
		this.xmlFormat = xmlFormat;
	}

	public ArrayList<ETLResultEntry> getEtlResultEntries() {
		return etlResultEntries;
	}

	public void setEtlResultEntries(ArrayList<ETLResultEntry> etlResultEntries) {
		this.etlResultEntries = etlResultEntries;
	}

}
