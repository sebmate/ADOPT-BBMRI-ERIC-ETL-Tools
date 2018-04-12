package de.fau.med.imi.MDRPipe.ETLHelper;

import java.util.ArrayList;

public interface XMLFormat {
	
	public void createXMLFromETLResultEntries(ArrayList<ETLResultEntry> etlResultEntries);
	
	public ArrayList<XMLEntry> convertETLResultEntriesToXMLEntries(ArrayList<ETLResultEntry> etlResultEntries);
	
	public XMLEntry convertETLResultEntryToXMLEntry(ETLResultEntry etlResultEntry);
	
}