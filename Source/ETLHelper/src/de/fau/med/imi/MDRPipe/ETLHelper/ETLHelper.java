package de.fau.med.imi.MDRPipe.ETLHelper;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import de.fau.med.imi.MDRPipe.MDRPipeConfiguration;

public class ETLHelper {
	
	/* Environment */
	private static Options commandLineOptions;
	
	/* Configurations */
	private static String configId;
	private static MDRPipeConfiguration MdrPipeConfiguration;
	
	private static ElementMatcher elementMatcher;
	private static ElementTransformer elementTransformer;
	
	public static void main(String[] args) throws SQLException, IOException {
		
		System.out.println("Welcome to the BBMRI-ERIC ETL Helper Utility 1.01!\n");
		
		// Read Command Line
		readCommandLine(args);
		
		// Load and set configurations
		initConfiguration();
		
		// Build Environment
		ETLHelper.buildEnvironment();
		
		// Start the uploading of the different files
		FileUploader fileUploader = new FileUploader(MDRPipeConfiguration.getDataFileName(), MDRPipeConfiguration.getMdrSourceFileName(), MDRPipeConfiguration.getMdrTargetFileName(), MDRPipeConfiguration.getMappingFileName());
		fileUploader.uploadFiles();
		
		// Start the matching of the elements from the source namespace to the target namespace
		ETLHelper.setElementMatcher(new ElementMatcher(fileUploader.getDatabase()));
		elementMatcher.startMatching();
		
		// Start the transformation of the already matched elements
		ETLHelper.setElementTransformer(new ElementTransformer(fileUploader.getDatabase()));
		ArrayList<ETLResultEntry> transformedElements = elementTransformer.startTransformation();	
		
		// Show Summary of the overall ETL process
		ETLHelper.showSummaryOfETL();
		
		// Start the creation of the XML-file
		XMLCreator myXMLCreator = new XMLCreator(transformedElements, MDRPipeConfiguration.getExportFormat());
		myXMLCreator.createXML();
		
		System.out.println("");
		System.out.println("If you experience any problems with this program, please contact christian.knell@fau.de or sebastian.mate@fau.de.");
		
	}
	
	private static void readCommandLine(String[] args) {
		
		ETLHelper.setCommandLineOptions(new Options());
		
		Option configurationId = new Option("configId", "configurationId", true, "ID of the configuration that should be used");
		configurationId.setRequired(true);
		ETLHelper.getCommandLineOptions().addOption(configurationId);
		
		CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;
        
        try {
            cmd = parser.parse(ETLHelper.getCommandLineOptions(), args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("ETLHelper.jar", ETLHelper.getCommandLineOptions());
            System.exit(1);
            return;
        }

        ETLHelper.setConfigId(cmd.getOptionValue("configurationId"));
		
	}
	
	private static void initConfiguration() {
		ETLHelper.setMdrPipeConfiguration(new MDRPipeConfiguration(ETLHelper.getConfigId()));
	}
	
	private static void buildEnvironment() {
		File dataFolder = new File(MDRPipeConfiguration.getDataFolder());
		dataFolder.mkdir();
		File logFolder = new File(MDRPipeConfiguration.getLogFolder());
		logFolder.mkdir();
		File matchingsFolder = new File(MDRPipeConfiguration.getMappingsFolder());
		matchingsFolder.mkdir();
		File metadataFolder = new File(MDRPipeConfiguration.getMetadataFolder());
		metadataFolder.mkdir();
		File xmlFolder = new File(MDRPipeConfiguration.getXmlFolder());
		xmlFolder.mkdir();
	}
	
	public static double round(double d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(Double.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.doubleValue();
	}
	
	public static void showSummaryOfETL() throws SQLException {
		int originalNumberOfDataRecords = ETLHelper.getElementMatcher().getDatabase().getDatabaseTableData().countValuesInColumn(DatabaseTableData.DATA_VALUE);
		int numberOfDataRecordsWithMappingRuleAndSuccessfulTransformation = ETLHelper.getElementTransformer().getDataTypeTransformations().countNumberOfTranslationRulesBySuccess("OK");
		double percent = ((double) numberOfDataRecordsWithMappingRuleAndSuccessfulTransformation / (double) originalNumberOfDataRecords) * 100.0;
		System.out.println("");
		System.out.println("=== Final summary about the ETL process ===");
		System.out.println("");
		System.out.println("In total, " + ETLHelper.round(percent, 2) + "% of the input data records could be mapped and transformed.");
	}

	public static Options getCommandLineOptions() {
		return commandLineOptions;
	}

	public static void setCommandLineOptions(Options commandLineOptions) {
		ETLHelper.commandLineOptions = commandLineOptions;
	}

	public static String getConfigId() {
		return configId;
	}

	public static void setConfigId(String configId) {
		ETLHelper.configId = configId;
	}

	public static MDRPipeConfiguration getMdrPipeConfiguration() {
		return MdrPipeConfiguration;
	}

	public static void setMdrPipeConfiguration(MDRPipeConfiguration mdrPipeConfiguration) {
		MdrPipeConfiguration = mdrPipeConfiguration;
	}

	public static ElementMatcher getElementMatcher() {
		return elementMatcher;
	}

	public static void setElementMatcher(ElementMatcher elementMatcher) {
		ETLHelper.elementMatcher = elementMatcher;
	}

	public static ElementTransformer getElementTransformer() {
		return elementTransformer;
	}

	public static void setElementTransformer(ElementTransformer elementTransformer) {
		ETLHelper.elementTransformer = elementTransformer;
	}

}
