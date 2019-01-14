package de.fau.med.imi.MDRPipe.ETLHelper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import de.fau.med.imi.MDRPipe.MDRPipeConfiguration;
import de.samply.registry.schemata.import_v2.OSSEImport;
import de.samply.registry.schemata.import_v2.ObjectFactory;
import de.samply.registry.schemata.import_v2.OSSEImport.Mdr;
import de.samply.registry.schemata.import_v2.OSSEImport.OSSEPatient;
import de.samply.registry.schemata.import_v2.OSSEImport.OSSEPatient.Identifier;
import de.samply.registry.schemata.import_v2.OSSEImport.OSSEPatient.Locations;
import de.samply.registry.schemata.import_v2.OSSEImport.OSSEPatient.Locations.Location;
import de.samply.registry.schemata.import_v2.OSSEImport.OSSEPatient.Locations.Location.BasicData;
import de.samply.registry.schemata.import_v2.OSSEImport.OSSEPatient.Locations.Location.BasicData.Dataelement;
import de.samply.registry.schemata.import_v2.OSSEImport.OSSEPatient.Locations.Location.Events;
import de.samply.registry.schemata.import_v2.OSSEImport.OSSEPatient.Locations.Location.Events.Event;

public class XMLFormatForBBMRI implements XMLFormat {

	private ArrayList<DataelementInformation> dataelementInformations;
	private ArrayList<String> missingUrns;
	private ArrayList<Integer> missingUrnsCount;

	@Override
	public void createXMLFromETLResultEntries(ArrayList<ETLResultEntry> etlResultEntries) {

		this.setDataelementInformations(this.generateDataelementInformations());
		this.setMissingUrns(new ArrayList<String>());
		this.setMissingUrnsCount(new ArrayList<Integer>());

		ArrayList<XMLEntry> xmlEntries = this.convertETLResultEntriesToXMLEntries(etlResultEntries);

		ObjectFactory factory = new ObjectFactory();

		OSSEImport osseImport = factory.createOSSEImport();

		Mdr myMDR = factory.createOSSEImportMdr();
		myMDR.setURL(MDRPipeConfiguration.getExportMdrUrl());
		myMDR.setNamespace(MDRPipeConfiguration.getExportMdrNamespace());
		osseImport.setMdr(myMDR);

		String lastCaseID = null;
		String lastEntity = null;
		String lastTimeStamp = null;

		OSSEPatient currentPatient = null;
		BasicData currentBasicData = null;
		Location currentLocation = null;
		Events currentEvents = null;
		Event currentEvent = null;
		Event lastEvent = null;

		String lastEventType = null;

		for (XMLEntry currentXmlEntry : xmlEntries) {

			XMLEntryForBBMRI xmlEntryForBBMRI = (XMLEntryForBBMRI) currentXmlEntry;
			String caseID = xmlEntryForBBMRI.getCaseId();
			String entity = xmlEntryForBBMRI.getInstance();
			String mdrkey = xmlEntryForBBMRI.getMdrkey();
			String name = xmlEntryForBBMRI.getName();
			String value = xmlEntryForBBMRI.getValue();
			String timeStamp = xmlEntryForBBMRI.getTimeStamp();
			String instance = xmlEntryForBBMRI.getInstance();
			String dataelementType = xmlEntryForBBMRI.getDataelementType();
			String eventName = xmlEntryForBBMRI.getEventName();
			String eventType = xmlEntryForBBMRI.getEventType();

			if (!caseID.equals(lastCaseID)) {

				// Petr, this was missing ...
				lastEntity = null;
				lastTimeStamp = null;
				lastEventType = null;
				lastEvent = null;

				// ---

				currentPatient = factory.createOSSEImportOSSEPatient();

				osseImport.getOSSEPatient().add(currentPatient);
				Identifier identifier1 = factory.createOSSEImportOSSEPatientIdentifier();
				identifier1.setEncrypted("false");
				identifier1.setValue(caseID);
				currentPatient.setIdentifier(identifier1);

				Locations locations = factory.createOSSEImportOSSEPatientLocations();
				currentPatient.setLocations(locations);

				currentLocation = factory.createOSSEImportOSSEPatientLocationsLocation();
				currentLocation.setName(MDRPipeConfiguration.getExportLocation());
				locations.getLocation().add(currentLocation);

				currentBasicData = factory.createOSSEImportOSSEPatientLocationsLocationBasicData();
				currentLocation.setBasicData(currentBasicData);

				currentEvents = factory.createOSSEImportOSSEPatientLocationsLocationEvents();
				currentLocation.setEvents(currentEvents);

			}

			Boolean doNotAdd = false;
			Boolean doNotAddToEventsList = false;

			switch (dataelementType) {
			case "BASIC":

				de.samply.registry.schemata.import_v2.OSSEImport.OSSEPatient.Locations.Location.BasicData.Dataelement dataelementForBasicData = createDataelementForBasicData(
						mdrkey, name, value);

				List<Dataelement> dataElements = currentBasicData.getDataelement();
				doNotAdd = false;
				for (Dataelement dataEl : dataElements) {
					if (dataEl.getName().equals(name) && dataEl.getValue().equals(value)) {
						System.out.println(
								"Warning: Entry \"" + dataEl.getName() + " = " + dataEl.getValue() + "\" for patient "
										+ currentPatient.getIdentifier().getValue() + " was already defined! Ignored.");
						doNotAdd = true;
					} else if (dataEl.getName().equals(name) && !dataEl.getValue().equals(value)) {

						System.out.println("Error: The new entry \"" + name + " = " + value
								+ "\" is contradicting the already processed entry \"" + dataEl.getName() + " = "
								+ dataEl.getValue() + "\" for patient " + currentPatient.getIdentifier().getValue()
								+ ". Ignored the new entry.");

						doNotAdd = true;
					}
				}

				if (!doNotAdd) {
					currentBasicData.getDataelement().add(dataelementForBasicData);
				}

				break;

			case "EVENTS":

				if (!entity.equals(lastEntity) || !timeStamp.equals(lastTimeStamp)
						|| !eventType.equals(lastEventType)) {

					// currentEvent = createEvent(eventName, eventType);

					// Petr, this looks whether an event with the same instance already exists and if so, reuses it.
					
					currentEvent = null;
					doNotAddToEventsList = false;

					// Look if there's an event already for the instance, if so, reuse this:
					for (Event ev : currentEvents.getEvent()) {
						if (ev.getEventtype().equals(eventType)) {
							if (ev.getName().equals(eventName + "-" + instance)) {
								currentEvent = ev;
								//System.out.println("Reusing event");
								doNotAddToEventsList = true;
							}
						}
					}
					if (currentEvent == null) { // There was none, so create a new one:
						currentEvent = createEvent(eventName + "-" + instance, eventType);
					}
				}

				de.samply.registry.schemata.import_v2.OSSEImport.OSSEPatient.Locations.Location.Events.Event.Dataelement dataelementForEventData = createDataelementForEventData(
						mdrkey, name, value);

				List<de.samply.registry.schemata.import_v2.OSSEImport.OSSEPatient.Locations.Location.Events.Event.Dataelement> dataElements2 = /* currentBasicData */currentEvent
						.getDataelement();

				doNotAdd = false;

				for (de.samply.registry.schemata.import_v2.OSSEImport.OSSEPatient.Locations.Location.Events.Event.Dataelement dataEl : dataElements2) {
					if (dataEl.getName().equals(name) && dataEl.getValue().equals(value)) {
						System.out.println(
								"Warning: Entry \"" + dataEl.getName() + " = " + dataEl.getValue() + "\" for patient "
										+ currentPatient.getIdentifier().getValue() + " was already defined! Ignored.");
						doNotAdd = true;
					} else if (dataEl.getName().equals(name) && !dataEl.getValue().equals(value)) {

						System.out.println("Error: The new entry \"" + name + " = " + value
								+ "\" is contradicting the already processed entry \"" + dataEl.getName() + " = "
								+ dataEl.getValue() + "\" for patient " + currentPatient.getIdentifier().getValue()
								+ ". Ignored the new entry.");

						doNotAdd = true;
					}
				}

				if (!doNotAdd) {
					currentEvent.getDataelement().add(dataelementForEventData);
				}

				if (!currentEvent.equals(lastEvent) && !doNotAddToEventsList) {
					currentEvents.getEvent().add(currentEvent);
				}

				// Petr, here is it where the last entry is remembered. Of
				// course this need to be reset when switching to another
				// patient.
				lastEntity = entity;
				lastTimeStamp = timeStamp;
				lastEventType = eventType;
				lastEvent = currentEvent;

				break;
			default:
				if (mdrkey != null) {
					if (!this.getMissingUrns().contains(mdrkey)) {
						this.getMissingUrns().add(mdrkey);
						int indexOfMdrKey = this.getMissingUrns().indexOf(mdrkey);
						this.getMissingUrnsCount().add(indexOfMdrKey, 1);
					} else {
						int indexOfMdrKey = this.getMissingUrns().indexOf(mdrkey);
						this.getMissingUrnsCount().set(indexOfMdrKey,
								this.getMissingUrnsCount().get(indexOfMdrKey) + 1);
					}
				}
				break;
			}

			lastCaseID = caseID;

		}

		if (this.getMissingUrns().size() > 0) {

			System.out.println("The following data records could not be inserted into the xml file:");

			for (int l = 0; l < this.getMissingUrns().size(); l++) {
				System.out.println("\t" + this.getMissingUrns().get(l) + ": " + this.getMissingUrnsCount().get(l));
			}

			System.out.println("");

		}

		String xmlResult = jaxbObjectToXML(osseImport);
		BufferedWriter out;
		try {
			out = new BufferedWriter(
					new FileWriter(MDRPipeConfiguration.getXmlFolder() + MDRPipeConfiguration.getXmlFileName()));
			out.write(xmlResult);
			out.close();
			System.out.println("\nThe XML file " + MDRPipeConfiguration.getXmlFileName()
					+ " has been created successfully in the directory " + MDRPipeConfiguration.getXmlFolder() + ".");
		} catch (IOException e) {
			System.out.println("\nThe XML file " + MDRPipeConfiguration.getXmlFileName()
					+ " has NOT been created successfully in the directory " + MDRPipeConfiguration.getXmlFolder()
					+ ".");
			if (MDRPipeConfiguration.getDebug()) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public ArrayList<XMLEntry> convertETLResultEntriesToXMLEntries(ArrayList<ETLResultEntry> etlResultEntries) {
		ArrayList<XMLEntry> xmlEntries = new ArrayList<XMLEntry>();
		for (int i = 0; i < etlResultEntries.size(); i++) {
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
		String name = etlResultEntry.getConcept(etlResultEntry.getTargetPath());
		String value = etlResultEntry.getFinalValue();
		String timeStamp = etlResultEntry.getDataTimestamp();
		String dataelementType = "";
		String eventName = "";
		String eventType = "";

		for (DataelementInformation dataelementInformation : this.getDataelementInformations()) {
			String urn = dataelementInformation.getUrn();
			String[] urnSplitted = urn.split(":");
			StringBuilder urnIdbuilder = new StringBuilder();
			for (int j = 0; j < urnSplitted.length - 1; j++) {
				urnIdbuilder.append(urnSplitted[j]);
				if (j < urnSplitted.length - 2) {
					urnIdbuilder.append(":");
				}
			}
			String urnId = urnIdbuilder.toString();
			if (mdrkey != null) {
				String[] mdrkeySplitted = mdrkey.split(":");
				StringBuilder mdrkeyIdBuilder = new StringBuilder();
				for (int k = 0; k < mdrkeySplitted.length - 1; k++) {
					mdrkeyIdBuilder.append(mdrkeySplitted[k]);
					if (k < mdrkeySplitted.length - 2) {
						mdrkeyIdBuilder.append(":");
					}
				}
				String mdrkeyId = mdrkeyIdBuilder.toString();
				if (mdrkeyId.equals(urnId)) {
					dataelementType = dataelementInformation.getDataelementType();
					if (!timeStamp.equals("NULL")) {
						LocalDateTime datetime = LocalDateTime.parse(timeStamp,
								DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
						eventName = datetime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
					} else {
						DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
						LocalDate localDate = LocalDate.now();
						eventName = dtf.format(localDate);
					}
					eventType = dataelementInformation.getEventType();
				}
			}
		}
		XMLEntry xmlEntry = new XMLEntryForBBMRI(caseID, entity, mdrkey, name, value, timeStamp, dataelementType,
				eventName, eventType);
		return xmlEntry;

	}

	private ArrayList<DataelementInformation> generateDataelementInformations() {

		ArrayList<DataelementInformation> dataelementInformations = new ArrayList<DataelementInformation>();

		// Get Informations as XML-List
		Document dataelementInformationsAsXML = null;

		try {
			dataelementInformationsAsXML = getDataelementInformationFromRestService();
		} catch (Exception e) {
			e.printStackTrace();
			if (MDRPipeConfiguration.getDebug()) {
				e.printStackTrace();
			}
		}

		if (dataelementInformationsAsXML == null) {
			System.out.println("ERROR (1): dataelementInformationsAsXML == null");
		}

		// Parse Informations into correct format
		dataelementInformationsAsXML.getDocumentElement().normalize();
		NodeList nList = dataelementInformationsAsXML.getElementsByTagName("MdrKey");

		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				String urn = eElement.getElementsByTagName("urn").item(0).getTextContent();
				String type = eElement.getElementsByTagName("type").item(0).getTextContent();
				String eventType = "";
				if (eElement.getElementsByTagName("eventtype").item(0) != null) {
					eventType = eElement.getElementsByTagName("eventtype").item(0).getTextContent();
				}
				DataelementInformation dataelementInformation = new DataelementInformation(urn, type, eventType);

				dataelementInformations.add(dataelementInformation);

			}

		}

		return dataelementInformations;

	}

	private Document getDataelementInformationFromRestService() {

		System.out.print("Pulling data element information from REST service ... ");

		HttpURLConnection con = null;
		Document doc = null;
		StringBuilder content = new StringBuilder();

		try {

			System.out.println("URL is: " + MDRPipeConfiguration.getStoreUrl() + "/osseimport/mdrkeylist");
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
			System.out.println("... Done!");

			try (PrintWriter out = new PrintWriter("metadata/mdrkeylist.xml")) {
				out.println(content.toString());
			}

		} catch (Exception e) {

			System.out.println("... Failed! As a fallback, loading it from file 'metadata/mdrkeylist.xml' ... ");
			try {
				String c = new String(Files.readAllBytes(Paths.get("metadata/mdrkeylist.xml")), StandardCharsets.UTF_8);
				content = new StringBuilder();
				content.append(c);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			// e.printStackTrace();
		} finally {
			con.disconnect();
		}

		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(content.toString()));
			doc = db.parse(is);
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	private static String jaxbObjectToXML(OSSEImport osseImport) {
		String xmlString = "";
		try {
			JAXBContext context = JAXBContext.newInstance(OSSEImport.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			m.marshal(osseImport, sw);
			xmlString = sw.toString();
		} catch (JAXBException e) {
			if (MDRPipeConfiguration.getDebug()) {
				e.printStackTrace();
			}
		}
		return xmlString;
	}

	private static de.samply.registry.schemata.import_v2.OSSEImport.OSSEPatient.Locations.Location.BasicData.Dataelement createDataelementForBasicData(
			String mdrkey, String name, String value) {
		de.samply.registry.schemata.import_v2.OSSEImport.OSSEPatient.Locations.Location.BasicData.Dataelement dataelement = new de.samply.registry.schemata.import_v2.OSSEImport.OSSEPatient.Locations.Location.BasicData.Dataelement();
		dataelement.setMdrkey(mdrkey);
		dataelement.setName(name);
		dataelement.setValue(value);
		return dataelement;
	}

	private static de.samply.registry.schemata.import_v2.OSSEImport.OSSEPatient.Locations.Location.Events.Event.Dataelement createDataelementForEventData(
			String mdrkey, String name, String value) {
		de.samply.registry.schemata.import_v2.OSSEImport.OSSEPatient.Locations.Location.Events.Event.Dataelement dataelement = new de.samply.registry.schemata.import_v2.OSSEImport.OSSEPatient.Locations.Location.Events.Event.Dataelement();
		dataelement.setMdrkey(mdrkey);
		dataelement.setName(name);
		dataelement.setValue(value);
		return dataelement;
	}

	private static de.samply.registry.schemata.import_v2.OSSEImport.OSSEPatient.Locations.Location.Events.Event createEvent(
			String name, String eventType) {
		de.samply.registry.schemata.import_v2.OSSEImport.OSSEPatient.Locations.Location.Events.Event event = new de.samply.registry.schemata.import_v2.OSSEImport.OSSEPatient.Locations.Location.Events.Event();
		event.setName(name);
		event.setEventtype(eventType);
		return event;
	}

	public ArrayList<DataelementInformation> getDataelementInformations() {
		return dataelementInformations;
	}

	public void setDataelementInformations(ArrayList<DataelementInformation> dataelementInformations) {
		this.dataelementInformations = dataelementInformations;
	}

	public ArrayList<String> getMissingUrns() {
		return missingUrns;
	}

	public void setMissingUrns(ArrayList<String> missingUrns) {
		this.missingUrns = missingUrns;
	}

	public ArrayList<Integer> getMissingUrnsCount() {
		return missingUrnsCount;
	}

	public void setMissingUrnsCount(ArrayList<Integer> missingUrnsCount) {
		this.missingUrnsCount = missingUrnsCount;
	}

}
