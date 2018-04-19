package de.fau.med.imi.MDRPipe.ETLHelper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import de.fau.med.imi.MDRPipe.MDRPipeConfiguration;

import de.samply.schema.store.BiobankType;
import de.samply.schema.store.CollectionType;
import de.samply.schema.store.DonorType;
import de.samply.schema.store.EventType;
import de.samply.schema.store.MdrKeyValueType;
import de.samply.schema.store.ObjectFactory;
import de.samply.schema.store.SampleContextType;
import de.samply.schema.store.SampleType;
import de.samply.schema.store.Store;

public class XMLFormatForGBA implements XMLFormat {
	
	private ArrayList<DataelementInformation> dataelementInformations;

	@Override
	public void createXMLFromETLResultEntries(ArrayList<ETLResultEntry> etlResultEntries) {
		
		this.setDataelementInformations(this.generateDataelementInformations());
			
		ArrayList<XMLEntry> xmlEntries = this.convertETLResultEntriesToXMLEntries(etlResultEntries);
		
		ObjectFactory factory = new ObjectFactory();
		
		Store store = factory.createStore();
		
		BiobankType currentBiobank = factory.createBiobankType();
		CollectionType currentCollection = null;
		DonorType currentDonor = null;
		EventType currentEvent = null;
		SampleContextType currentSampleContext = null;
		SampleType currentSample = null;
		
		int lastBiobankIdInt = 1;
		int lastCollectionIdInt = 0;
		int lastEventIdInt = 0;
		int lastSampleContextIdInt = 0;
		int lastSampleIdInt = 0;
		
		String lastBiobankId = "Bio" + lastBiobankIdInt;
		String lastCollectionId = "Col" + lastCollectionIdInt;
		String lastEventId = "Ev" + lastEventIdInt;
		String lastSampleContextId = "CON" + lastSampleContextIdInt;
		String lastSampleId = "S" + lastSampleIdInt;
		
		String lastCaseId = "-1";
		String lastInstance = "-1";
		String lastTimeStamp = "-1";
		
		currentBiobank.setId(lastBiobankId);
		
		for (XMLEntry currentXmlEntry : xmlEntries) {
			
			XMLEntryForGBA xmlEntryForGBA = (XMLEntryForGBA) currentXmlEntry;
			
			String caseId = xmlEntryForGBA.getCaseId();
			String instance = xmlEntryForGBA.getInstance();
			String mdrkey = xmlEntryForGBA.getMdrkey();
			String value = xmlEntryForGBA.getValue();
			String timeStamp = xmlEntryForGBA.getTimeStamp();
			String dataelementType = xmlEntryForGBA.getDataelementType();
			
			if(!lastCaseId.equals(caseId) || !lastInstance.equals(instance) || !lastTimeStamp.equals(timeStamp)) {
					
				if(!lastCaseId.equals(caseId)) {
					if(currentDonor != null) {
						store.getDonor().add(currentDonor);
					}
					currentDonor = factory.createDonorType();
					currentDonor.setId(caseId);
				}
				
			}
			
			switch(dataelementType) {
				case "Biobank":
					if(!value.isEmpty()) {
						currentBiobank.getGenericAttribute().add(this.createMdrKeyValueType(mdrkey, value));
					}
					break;
				case "Collection":
					if(!lastCaseId.equals(caseId) || !lastInstance.equals(instance) || !lastTimeStamp.equals(timeStamp)) {
						if(currentCollection != null) {
							store.getCollection().add(currentCollection);
							lastCollectionIdInt++;
							lastCollectionId = "Col" + lastCollectionIdInt;
						}
						currentCollection = factory.createCollectionType();
						currentCollection.setId(lastCollectionId);
						currentCollection.setBiobankId(lastBiobankId);
					}
					if(!value.isEmpty()) {
						currentCollection.getGenericAttribute().add(this.createMdrKeyValueType(mdrkey, value));
					}
					break;
				case "Donor":
					if(!value.isEmpty()) {
						currentDonor.getGenericAttribute().add(this.createMdrKeyValueType(mdrkey, value));
					}
					break;
				case "Event":
					if(!lastCaseId.equals(caseId) || !lastInstance.equals(instance) || !lastTimeStamp.equals(timeStamp)) {
						if(!lastTimeStamp.equals(timeStamp)) {
							if(currentEvent != null) {
								store.getEvent().add(currentEvent);
								lastEventIdInt++;
								lastEventId = "Ev" + lastEventIdInt;
							}
							currentEvent = factory.createEventType();
							currentEvent.setId(lastEventId);
							currentEvent.setDonorId(caseId);
						}
					}
					if(!value.isEmpty()) {
						currentEvent.getGenericAttribute().add(this.createMdrKeyValueType(mdrkey, value));
					}
					break;
				case "Sample":
					if(!lastCaseId.equals(caseId) || !lastInstance.equals(instance) || !lastTimeStamp.equals(timeStamp)) {
						if(currentSample != null) {
							store.getSample().add(currentSample);
							lastSampleIdInt++;
							lastSampleId = "S" + lastSampleIdInt;
						}
						currentSample = factory.createSampleType();
						currentSample.setId(lastSampleId);
						currentSample.setCollectionId(lastCollectionId);
						currentSample.setSampleContextId(lastSampleContextId);
					}
					if(!value.isEmpty()) {
						currentSample.getGenericAttribute().add(this.createMdrKeyValueType(mdrkey, value));
					}
					break;
				case "SampleContext":
					if(!lastCaseId.equals(caseId) || !lastInstance.equals(instance) || !lastTimeStamp.equals(timeStamp)) {
						if(currentSampleContext != null) {
							store.getSampleContext().add(currentSampleContext);
							lastSampleContextIdInt++;
							lastSampleContextId = "CON" + lastSampleContextIdInt;
						}
						currentSampleContext = factory.createSampleContextType();
						currentSampleContext.setId(lastSampleContextId);
						
					}
					if(!value.isEmpty()) {
						currentSampleContext.getGenericAttribute().add(this.createMdrKeyValueType(mdrkey, value));
					}
					break;
			}
			
			lastCaseId = caseId;
			lastInstance = instance;
			lastTimeStamp = timeStamp;
			
		}
		
		store.getBiobank().add(currentBiobank);
		
		String xmlResult = jaxbObjectToXML(store);
		BufferedWriter out;
		try {
			out = new BufferedWriter(new FileWriter(MDRPipeConfiguration.getXmlFolder() + MDRPipeConfiguration.getXmlFileName()));
			out.write(xmlResult);
			out.close();
			System.out.println("The XML file " + MDRPipeConfiguration.getXmlFileName() + " has been created successfully in the directory " + MDRPipeConfiguration.getXmlFolder() + ".");
		} catch (IOException e) {
			System.out.println("The XML file " + MDRPipeConfiguration.getXmlFileName() + " has NOT been created successfully in the directory " + MDRPipeConfiguration.getXmlFolder() + ".");
			if(MDRPipeConfiguration.getDebug()) {
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public ArrayList<XMLEntry> convertETLResultEntriesToXMLEntries(ArrayList<ETLResultEntry> etlResultEntries) {
		ArrayList<XMLEntry> xmlEntries = new ArrayList<XMLEntry>();
		for(int i = 0; i < etlResultEntries.size(); i++) {
			XMLEntry xmlEntry = this.convertETLResultEntryToXMLEntry(etlResultEntries.get(i));
			xmlEntries.add(xmlEntry);
		}
		return xmlEntries;
	}

	@Override
	public XMLEntry convertETLResultEntryToXMLEntry(ETLResultEntry etlResultEntry) {
		
		String caseID = etlResultEntry.getDataCaseID();
		String entity = etlResultEntry.getDataInstance();
		String mdrkey = etlResultEntry.getTargetURN();
		String value = etlResultEntry.getFinalValue();
		String timeStamp = etlResultEntry.getDataTimestamp();
		String dataelementType = "";
		
		for (DataelementInformation dataelementInformation : this.getDataelementInformations()) {
			String urn = dataelementInformation.getUrn();
			if(mdrkey != null && mdrkey.equals(urn)) {
				dataelementType = dataelementInformation.getDataelementType();
			}
		}
		
		XMLEntry xmlEntry = new XMLEntryForGBA(caseID, entity, mdrkey, value, timeStamp, dataelementType);
		return xmlEntry;
		
	}
	
	private MdrKeyValueType createMdrKeyValueType(String mdrKey, String value) {
		MdrKeyValueType mdrKeyValueType = new MdrKeyValueType();
		mdrKeyValueType.setKey(mdrKey);
		mdrKeyValueType.setValue(value);
		return mdrKeyValueType;
	}

	// TODO: generic way
	private ArrayList<DataelementInformation> generateDataelementInformations() {

		ArrayList<DataelementInformation> dataelementInformations = new ArrayList<DataelementInformation>();

		DataelementInformation dataelementInformation1 = new DataelementInformation("urn:gba:dataelement:10:5", "Biobank", "");
		dataelementInformations.add(dataelementInformation1);

		DataelementInformation dataelementInformation2 = new DataelementInformation("urn:gba:dataelement:20:1", "Collection", "");
		dataelementInformations.add(dataelementInformation2);

		DataelementInformation dataelementInformation3 = new DataelementInformation("urn:gba:dataelement:22:1", "Sample", "");
		dataelementInformations.add(dataelementInformation3);

		DataelementInformation dataelementInformation4 = new DataelementInformation("urn:gba:dataelement:25:1", "Donor", "");
		dataelementInformations.add(dataelementInformation4);

		DataelementInformation dataelementInformation5 = new DataelementInformation("urn:gba:dataelement:27:1", "Event", "");
		dataelementInformations.add(dataelementInformation5);

		DataelementInformation dataelementInformation6 = new DataelementInformation("urn:gba:dataelement:28:2", "Event", "");
		dataelementInformations.add(dataelementInformation6);

		DataelementInformation dataelementInformation7 = new DataelementInformation("urn:gba:dataelement:29:3", "Donor", "");
		dataelementInformations.add(dataelementInformation7);

		DataelementInformation dataelementInformation8 = new DataelementInformation("urn:gba:dataelement:30:1", "Biobank", "");
		dataelementInformations.add(dataelementInformation8);

		DataelementInformation dataelementInformation9 = new DataelementInformation("urn:gba:dataelement:32:1", "SampleContext", "");
		dataelementInformations.add(dataelementInformation9);
		
		return dataelementInformations;

	}

	// TODO: generic way
	private Document getDataelementInformationFromRestService() throws IOException, ParserConfigurationException, SAXException {

		HttpURLConnection con = null;
		Document doc = null;
		StringBuilder content = new StringBuilder();

		try {
			URL myurl = new URL(MDRPipeConfiguration.getStoreUrl() + "/osseimport/mdrkeylist");
			con = (HttpURLConnection) myurl.openConnection();
			con.setRequestMethod("GET");
			try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
				String line;
				while ((line = in.readLine()) != null) {
					content.append(line);
					content.append(System.lineSeparator());
				}
			}
		} finally {
			con.disconnect();
		}

		DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(content.toString()));
		doc = db.parse(is);

		return doc;

	}

	private static String jaxbObjectToXML(Store store) {
		String xmlString = "";
		try {
			JAXBContext context = JAXBContext.newInstance(Store.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			m.marshal(store, sw);
			xmlString = sw.toString();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return xmlString;
	}

	public ArrayList<DataelementInformation> getDataelementInformations() {
		return dataelementInformations;
	}

	public void setDataelementInformations(ArrayList<DataelementInformation> dataelementInformations) {
		this.dataelementInformations = dataelementInformations;
	}

}
