package de.fau.med.imi.MDRPipe.MDRExtractor;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

import javax.ws.rs.client.ClientBuilder;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import de.fau.med.imi.MDRPipe.MDRPipeConfiguration;

import de.samply.auth.client.AuthClient;
import de.samply.auth.client.InvalidKeyException;
import de.samply.auth.client.InvalidTokenException;
import de.samply.common.mdrclient.MdrClient;
import de.samply.common.mdrclient.*;
import de.samply.common.mdrclient.domain.EnumElementType;
import de.samply.common.mdrclient.domain.PermissibleValue;
import de.samply.common.mdrclient.domain.Result;
import de.samply.common.mdrclient.domain.Slot;

public class MDRExtractor {
	
	/* Environment */
	private static Options commandLineOptions;
	
	/* Configurations */
	private static String configId;
	private static MDRPipeConfiguration MdrPipeConfiguration;
	private static String extractedMdr;

	private static AuthClient authClient;
	private static MdrClient mdrClient;
	
	private static String result = "URN (do not edit)\tSOURCE Slot (do not edit)\tData Type\tPermitted Value\tMDR Path (do not edit)\tMatching Input (can be edited)\n";
	private static int dataElementsCnt = 0;
	private static int valuesCnt = 0;

	public static void main(String[] args) throws IOException, Exception, Throwable {
		
		System.out.println("Welcome to the Samply MDR Extractor Utility 1.02!\n");
		
		LogManager.getLogger("org.apache.commons.beanutils.converters").setLevel(org.apache.log4j.Level.ERROR);
		LogManager.getLogger("de.samply.common.mdrclient").setLevel(org.apache.log4j.Level.ERROR);
		
		// Read Command Line
		readCommandLine(args);
		
		// Load and set configurations
		initConfiguration();
		
		// Build Environment
		MDRExtractor.buildEnvironment();
		
		if(MDRExtractor.getExtractedMdr().equals("target")) {
			System.out.println("Extracting metadata from namespace \"" + MDRPipeConfiguration.getMdrTargetNamespace() + "\". This will take a while (program may look stuck).\n");
			if(MDRPipeConfiguration.getMdrTargetAuthenticate()) {
				MDRExtractor.setAuthClient(new AuthClient(MDRPipeConfiguration.getMdrTargetAuthUrl(), MDRPipeConfiguration.getMdrTargetPublicKey(), MDRPipeConfiguration.getMdrTargetPrivateKey(), ClientBuilder.newClient()));
			}
			extractNamespace(MDRPipeConfiguration.getMdrTargetUrl(), MDRPipeConfiguration.getMdrTargetNamespace());
		} else {
			System.out.println("Extracting metadata from namespace \"" + MDRPipeConfiguration.getMdrSourceNamespace() + "\". This will take a while (program may look stuck).\n");
			if(MDRPipeConfiguration.getMdrSourceAuthenticate()) {
				MDRExtractor.setAuthClient(new AuthClient(MDRPipeConfiguration.getMdrSourceAuthUrl(), MDRPipeConfiguration.getMdrSourcePublicKey(), MDRPipeConfiguration.getMdrSourcePrivateKey(), ClientBuilder.newClient()));
			}
			extractNamespace(MDRPipeConfiguration.getMdrSourceUrl(), MDRPipeConfiguration.getMdrSourceNamespace());
		}
		
		System.out.println("\n\nDone. Extracted " + dataElementsCnt + " data elements with " + valuesCnt + " values.");

		// Write result to file:
		if(MDRExtractor.getExtractedMdr().equals("target")) {
			PrintWriter writer = new PrintWriter(MDRPipeConfiguration.getMetadataFolder() + MDRPipeConfiguration.getMdrTargetFileName(), "Cp1252");
			writer.println(result.substring(0, result.length() - 1));
			writer.close();
			System.out.println("Extracted metadata has been written to file \"" + MDRPipeConfiguration.getMdrTargetFileName() + "\".");
		} else {
			PrintWriter writer = new PrintWriter(MDRPipeConfiguration.getMetadataFolder() + MDRPipeConfiguration.getMdrSourceFileName(), "Cp1252");
			writer.println(result.substring(0, result.length() - 1));
			writer.close();
			System.out.println("Extracted metadata has been written to file \"" + MDRPipeConfiguration.getMdrSourceFileName() + "\".");
		}

	}
	
	private static void readCommandLine(String[] args) {
		
		MDRExtractor.setCommandLineOptions(new Options());
		
		Option configurationId = new Option("configId", "configurationId", true, "ID of the configuration that should be used");
		configurationId.setRequired(true);
		MDRExtractor.getCommandLineOptions().addOption(configurationId);
		
		Option extractedMdr = new Option("extractedMdr", "extractedMdr", true, "which defined mdr should be extracted (source/target)");
		extractedMdr.setRequired(true);
		MDRExtractor.getCommandLineOptions().addOption(extractedMdr);
		
		CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;
        
        try {
            cmd = parser.parse(MDRExtractor.getCommandLineOptions(), args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("MDRExtractor.jar", MDRExtractor.getCommandLineOptions());
            System.exit(1);
            return;
        }

        MDRExtractor.setConfigId(cmd.getOptionValue("configurationId"));
        MDRExtractor.setExtractedMdr(cmd.getOptionValue("extractedMdr"));
		
	}
	
	private static void initConfiguration() {
		MDRExtractor.setMdrPipeConfiguration(new MDRPipeConfiguration(MDRExtractor.getConfigId()));
	}
	
	private static void buildEnvironment() {
		File metadataFolder = new File(MDRPipeConfiguration.getMetadataFolder());
		metadataFolder.mkdir();
	}

	private static void extractNamespace(String URL, String NameSpace) throws MdrConnectionException, ExecutionException, MdrInvalidResponseException, InvalidTokenException, InvalidKeyException {
		MDRExtractor.setMdrClient(new MdrClient(URL));
		MDRExtractor.getMdrClient().cleanCache();
		List<Result> result = null;
		try {
			if(MDRExtractor.getExtractedMdr().equals("target")) {
				if(MDRPipeConfiguration.getMdrTargetAuthenticate()) {
					result = MDRExtractor.getMdrClient().getNamespaceMembers(MDRPipeConfiguration.getLanguage(), MDRExtractor.getAuthClient().getAccessToken().getSerialized(), null, MDRPipeConfiguration.getMdrTargetNamespace());
				} else {
					result = MDRExtractor.getMdrClient().getNamespaceMembers(MDRPipeConfiguration.getLanguage(), MDRPipeConfiguration.getMdrTargetNamespace());
				}
			} else {
				if(MDRPipeConfiguration.getMdrSourceAuthenticate()) {
					result = MDRExtractor.getMdrClient().getNamespaceMembers(MDRPipeConfiguration.getLanguage(), MDRExtractor.getAuthClient().getAccessToken().getSerialized(), null, MDRPipeConfiguration.getMdrSourceNamespace());
				} else {
					result = MDRExtractor.getMdrClient().getNamespaceMembers(MDRPipeConfiguration.getLanguage(), MDRPipeConfiguration.getMdrSourceNamespace());
				}
			}
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		List<Result> subgroups = getSubgroups(result);
		processSubgroups(subgroups, 0, "");
		List<Result> result2 = null;
		try {
			if(MDRExtractor.getExtractedMdr().equals("target")) {
				if(MDRPipeConfiguration.getMdrTargetAuthenticate()) {
					result2 = MDRExtractor.getMdrClient().getNamespaceMembers(MDRPipeConfiguration.getLanguage(), MDRExtractor.getAuthClient().getAccessToken().getSerialized(), null, MDRPipeConfiguration.getMdrTargetNamespace());
				} else {
					result2 = MDRExtractor.getMdrClient().getNamespaceMembers(MDRPipeConfiguration.getLanguage(), MDRPipeConfiguration.getMdrTargetNamespace());
				}
			} else {
				if(MDRPipeConfiguration.getMdrSourceAuthenticate()) {
					result2 = MDRExtractor.getMdrClient().getNamespaceMembers(MDRPipeConfiguration.getLanguage(), MDRExtractor.getAuthClient().getAccessToken().getSerialized(), null, MDRPipeConfiguration.getMdrSourceNamespace());
				} else {
					result2 = MDRExtractor.getMdrClient().getNamespaceMembers(MDRPipeConfiguration.getLanguage(), MDRPipeConfiguration.getMdrSourceNamespace());
				}
			}
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		List<Result> dataelements = getDataElements(result2);
		processDataelements(dataelements, 0, "");
	}

	private static List<Result> getDataElements(List<Result> members) {
		List<Result> dataelements = MDRExtractor.getMdrClient().filterMembers(members, EnumElementType.DATAELEMENT);
		return dataelements;
	}

	private static List<Result> getSubgroups(List<Result> members) {
		List<Result> subgroups = MDRExtractor.getMdrClient().filterMembers(members, EnumElementType.DATAELEMENTGROUP);
		return subgroups;
	}

	private static void processDataelements(List<Result> dataelements, int level, String groupString)
			throws MdrConnectionException, MdrInvalidResponseException, ExecutionException, InvalidTokenException,
			InvalidKeyException {

		for (int b = 0; b < dataelements.size(); b++) {

			dataElementsCnt++;

			System.out.print(".");
			if (dataElementsCnt % 10 == 0)
				System.out.println(" " + dataElementsCnt + " data elements");

			String designation = groupString + dataelements.get(b).getDesignations().get(0).getDesignation();
			String ConceptString = designation.trim() + " = ";

			String urn = dataelements.get(b).getId();
			String slotVal = "";

			try {

				ArrayList<Slot> slots = new ArrayList<Slot>();

				if(MDRExtractor.getExtractedMdr().equals("target")) {
					if(MDRPipeConfiguration.getMdrTargetAuthenticate()) {
						slots = MDRExtractor.getMdrClient().getDataElementSlots(urn, MDRExtractor.getAuthClient().getAccessToken().getSerialized(), null);
					} else {
						slots = MDRExtractor.getMdrClient().getDataElementSlots(urn);
					}					
				} else {
					if(MDRPipeConfiguration.getMdrSourceAuthenticate()) {
						slots = MDRExtractor.getMdrClient().getDataElementSlots(urn, MDRExtractor.getAuthClient().getAccessToken().getSerialized(), null);
					} else {
						slots = MDRExtractor.getMdrClient().getDataElementSlots(urn);
					}	
				}
				
				for (int c = 0; c < slots.size(); c++) {
					String sname = slots.get(c).getSlotName();
					String svalu = slots.get(c).getSlotValue();
					if (sname.equalsIgnoreCase("SOURCE"))
						slotVal = svalu;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			String mdrDeDatatype = "";
			if(MDRExtractor.getExtractedMdr().equals("target")) {
				if (MDRPipeConfiguration.getMdrTargetAuthenticate()) {
					mdrDeDatatype = MDRExtractor.getMdrClient().getDataElementValidations(dataelements.get(b).getId(), MDRPipeConfiguration.getLanguage(), MDRExtractor.getAuthClient().getAccessToken().getSerialized(), null).getDatatype();
				} else {
					mdrDeDatatype = MDRExtractor.getMdrClient().getDataElementValidations(dataelements.get(b).getId(), MDRPipeConfiguration.getLanguage()).getDatatype();
				}
			} else {
				if (MDRPipeConfiguration.getMdrSourceAuthenticate()) {
					mdrDeDatatype = MDRExtractor.getMdrClient().getDataElementValidations(dataelements.get(b).getId(), MDRPipeConfiguration.getLanguage(), MDRExtractor.getAuthClient().getAccessToken().getSerialized(), null).getDatatype();
				} else {
					mdrDeDatatype = MDRExtractor.getMdrClient().getDataElementValidations(dataelements.get(b).getId(), MDRPipeConfiguration.getLanguage()).getDatatype();
				}
			}

			String datatype = mdrDeDatatype;

			String thisLine = "";
			switch (datatype) {
			case "enumerated":
				List<PermissibleValue> permvals = MDRExtractor.getMdrClient().getDataElementValidations(dataelements.get(b).getId(), MDRPipeConfiguration.getLanguage()).getPermissibleValues();
				for (int c = 0; c < permvals.size(); c++) {

					String permValue = permvals.get(c).getValue();
					
					//System.out.println(permValue);
										
					String desig = permvals.get(c).getMeanings().get(0).getDesignation();
					processLine(ConceptString + desig, ConceptString + desig, urn, slotVal,
							datatype, permValue);
				}
				break;
			case "INTEGER":
				thisLine = ConceptString + "INTEGER";
				processLine(thisLine, thisLine, urn, slotVal, datatype, "");
				break;
			case "FLOAT":
				thisLine = ConceptString + "FLOAT";
				processLine(thisLine, thisLine, urn, slotVal, datatype, "");
				break;
			case "BOOLEAN":
				thisLine = ConceptString;
				processLine(thisLine + "TRUE", thisLine + "TRUE", urn, slotVal, datatype, "TRUE");
				processLine(thisLine + "FALSE", thisLine + "FALSE", urn, slotVal, datatype, "FALSE");
				break;
			case "STRING":
				thisLine = ConceptString + "STRING";
				processLine(thisLine, thisLine, urn, slotVal, datatype, "");
				break;
			case "DATE":
				thisLine = ConceptString + "DATE";
				processLine(thisLine, thisLine, urn, slotVal, datatype, "");
				break;
			case "DATETIME":
				thisLine = ConceptString + "DATETIME";
				processLine(thisLine, thisLine, urn, slotVal, datatype, "");
				break;
			default:
			}
		}
	}

	private static void processLine(String MDRPath, String MatchingInput, String urn, String slotVal, String dataType, String permValue) {
		valuesCnt++;
		if(MDRPipeConfiguration.getDebug()) {
			System.out.println(MDRPath);
		}
		result += urn + "\t" + slotVal + "\t" + dataType.toUpperCase() + "\t" + permValue + "\t" + MDRPath + "\t" + MatchingInput + "\n";
	}

	private static void processSubgroups(List<Result> subgroups, int level, String groupString)
			throws MdrConnectionException, ExecutionException, MdrInvalidResponseException, InvalidTokenException,
			InvalidKeyException {
		if (subgroups.size() != 0) {

			for (int i = 0; i < subgroups.size(); i++) {

				String subGroup = subgroups.get(i).getId();
				if (MDRPipeConfiguration.getDebug()) {
					System.out.println(subGroup);
				}

				try {

					List<Result> result3 = MDRExtractor.getMdrClient().getMembers(subgroups.get(i).getId(), MDRPipeConfiguration.getLanguage());
					List<Result> subgroups1 = getSubgroups(result3);
					processSubgroups(subgroups1, level + 1,
							groupString + subgroups.get(i).getDesignations().get(0).getDesignation() + " / ");
					List<Result> result2 = MDRExtractor.getMdrClient().getMembers(subgroups.get(i).getId(), MDRPipeConfiguration.getLanguage());
					List<Result> dataelements = getDataElements(result2);
					if(MDRExtractor.getExtractedMdr().equals("target")) {
						result3 = MDRExtractor.getMdrClient().getNamespaceMembers(MDRPipeConfiguration.getLanguage(), MDRPipeConfiguration.getMdrTargetNamespace());
					} else {
						result3 = MDRExtractor.getMdrClient().getNamespaceMembers(MDRPipeConfiguration.getLanguage(), MDRPipeConfiguration.getMdrSourceNamespace());
					}
					
					processDataelements(dataelements, level + 1,
							groupString + subgroups.get(i).getDesignations().get(0).getDesignation().trim() + " / ");

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static MdrClient getMdrClient() {
		return mdrClient;
	}

	public static void setMdrClient(MdrClient mdrClient) {
		MDRExtractor.mdrClient = mdrClient;
	}

	public static AuthClient getAuthClient() {
		return authClient;
	}

	public static void setAuthClient(AuthClient authClient) {
		MDRExtractor.authClient = authClient;
	}

	public static Options getCommandLineOptions() {
		return commandLineOptions;
	}

	public static void setCommandLineOptions(Options commandLineOptions) {
		MDRExtractor.commandLineOptions = commandLineOptions;
	}

	public static String getConfigId() {
		return configId;
	}

	public static void setConfigId(String configId) {
		MDRExtractor.configId = configId;
	}

	public static MDRPipeConfiguration getMdrPipeConfiguration() {
		return MdrPipeConfiguration;
	}

	public static void setMdrPipeConfiguration(MDRPipeConfiguration mdrPipeConfiguration) {
		MdrPipeConfiguration = mdrPipeConfiguration;
	}

	public static String getExtractedMdr() {
		return extractedMdr;
	}

	public static void setExtractedMdr(String extractedMdr) {
		MDRExtractor.extractedMdr = extractedMdr;
	}

}
