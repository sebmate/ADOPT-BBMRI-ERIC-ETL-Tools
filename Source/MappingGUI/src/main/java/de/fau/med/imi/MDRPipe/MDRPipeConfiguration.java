package de.fau.med.imi.MDRPipe;

import java.security.PrivateKey;
import java.security.PublicKey;

import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.tree.xpath.XPathExpressionEngine;

import de.samply.auth.client.jwt.KeyLoader;

public class MDRPipeConfiguration {
	
	/* MDRPipeConfiguration */
	private static String configId;
	private static String configFolder = "./config/";
	private static String configFileName = "config.xml";
	private static XMLConfiguration xmlConfigurations;
	
	/* Debugging */
	private static boolean debug;
	
	/* Export */
	private static String exportFormat;
	private static String exportLocation;
	private static String exportMdrNamespace;
	private static String exportMdrUrl;
	
	/* File Structure */
	private static String dataFileName;
	private static String databaseName;
	private static String logFileName;
	private static String matchingFileName;
	private static String mdrSourceFileName;
	private static String mdrTargetFileName;
	private static String xmlFileName;
	
	/* Folder Structure */
	private static String dataFolder;
	private static String databaseFolder;
	private static String logFolder;
	private static String matchingsFolder;
	private static String metadataFolder;
	private static String xmlFolder;
	
	/* Language */
	private static String language;
	
	/* Logging */
	private static boolean loggingEnable;
	private static String loggingLevel;
	
	/* MDR - Source */
	private static boolean mdrSourceAuthenticate;
	private static PrivateKey mdrSourcePrivateKey;
	private static PublicKey mdrSourcePublicKey;
	private static String mdrSourceAuthUrl;
	private static String mdrSourceNamespace;
	private static String mdrSourceUrl;
	
	/* MDR - Target */
	private static boolean mdrTargetAuthenticate;
	private static PrivateKey mdrTargetPrivateKey;
	private static PublicKey mdrTargetPublicKey;
	private static String mdrTargetAuthUrl;
	private static String mdrTargetNamespace;
	private static String mdrTargetUrl;
	
	/* Store */
	private static String storeIp;
	private static String storePort;
	
	public MDRPipeConfiguration(String configId) {
		MDRPipeConfiguration.setConfigId(configId);
		loadConfigurationsFromXml();
		setConfigurationsFromXml();
	}
	
	private static void loadConfigurationsFromXml() {
		Configurations configurations = new Configurations();
		try {
			MDRPipeConfiguration.setXmlConfigurations(configurations.xml(MDRPipeConfiguration.getConfigFolder() + MDRPipeConfiguration.getConfigFileName()));
			MDRPipeConfiguration.getXmlConfigurations().setExpressionEngine(new XPathExpressionEngine());
		} catch(ConfigurationException cex) {
		    // something went wrong, e.g. the file was not found
			System.out.println("Die Konfigurationsdatei konnte leider nicht in " + MDRPipeConfiguration.getConfigFolder() + MDRPipeConfiguration.getConfigFileName() + " gefunden werden!");
			//System.exit(1);
		}
	}
	
	private static void setConfigurationsFromXml() {
		
		String basePath = "config[@configId = '" + MDRPipeConfiguration.getConfigId() + "']";
		// TODO: CHECK IF CONFIGURATION WITH THIS ID EVEN EXISTS
		
		/* Debugging */
		MDRPipeConfiguration.setDebug(MDRPipeConfiguration.getXmlConfigurations().getBoolean(basePath + "/debug"));
		
		/* Export */
		MDRPipeConfiguration.setExportFormat(MDRPipeConfiguration.getXmlConfigurations().getString(basePath + "/export/format"));
		MDRPipeConfiguration.setExportLocation(MDRPipeConfiguration.getXmlConfigurations().getString(basePath + "/export/location"));
		MDRPipeConfiguration.setExportMdrNamespace(MDRPipeConfiguration.getXmlConfigurations().getString(basePath + "/export/mdrNamespace"));
		MDRPipeConfiguration.setExportMdrUrl(MDRPipeConfiguration.getXmlConfigurations().getString(basePath + "/export/mdrUrl"));
		
		/* File Structure */
		MDRPipeConfiguration.setDataFileName(MDRPipeConfiguration.getXmlConfigurations().getString(basePath + "/fileStructure/dataFileName"));
		MDRPipeConfiguration.setDatabaseName(MDRPipeConfiguration.getXmlConfigurations().getString(basePath + "/fileStructure/databaseName"));
		MDRPipeConfiguration.setLogFileName(MDRPipeConfiguration.getXmlConfigurations().getString(basePath + "/fileStructure/logFileName"));
		MDRPipeConfiguration.setMatchingFileName(MDRPipeConfiguration.getXmlConfigurations().getString(basePath + "/fileStructure/mappingFileName"));
		MDRPipeConfiguration.setMdrSourceFileName(MDRPipeConfiguration.getXmlConfigurations().getString(basePath + "/fileStructure/mdrSourceFileName"));
		MDRPipeConfiguration.setMdrTargetFileName(MDRPipeConfiguration.getXmlConfigurations().getString(basePath + "/fileStructure/mdrTargetFileName"));
		MDRPipeConfiguration.setXmlFileName(MDRPipeConfiguration.getXmlConfigurations().getString(basePath + "/fileStructure/xmlFileName"));
		
		/* Folder Structure */
		MDRPipeConfiguration.setDataFolder(MDRPipeConfiguration.getXmlConfigurations().getString(basePath + "/folderStructure/dataFolder"));
		MDRPipeConfiguration.setLogFolder(MDRPipeConfiguration.getXmlConfigurations().getString(basePath + "/folderStructure/logFolder"));
		MDRPipeConfiguration.setMatchingsFolder(MDRPipeConfiguration.getXmlConfigurations().getString(basePath + "/folderStructure/mappingsFolder"));
		MDRPipeConfiguration.setMetadataFolder(MDRPipeConfiguration.getXmlConfigurations().getString(basePath + "/folderStructure/metadataFolder"));
		MDRPipeConfiguration.setXmlFolder(MDRPipeConfiguration.getXmlConfigurations().getString(basePath + "/folderStructure/xmlFolder"));
		
		/* Language */
		MDRPipeConfiguration.setLanguage(MDRPipeConfiguration.getXmlConfigurations().getString(basePath + "/language"));
		
		/* Logging */
		MDRPipeConfiguration.setLoggingEnable(MDRPipeConfiguration.getXmlConfigurations().getBoolean(basePath + "/logging/enable"));
		MDRPipeConfiguration.setLoggingLevel(MDRPipeConfiguration.getXmlConfigurations().getString(basePath + "/logging/level"));
		
		/* MDR - Source */
		MDRPipeConfiguration.setMdrSourceAuthenticate(MDRPipeConfiguration.getXmlConfigurations().getBoolean(basePath + "/mdr/source/auth/authenticate"));
		String privKeySource = cleanKey(MDRPipeConfiguration.getXmlConfigurations().getString(basePath + "/mdr/source/auth/privateKey"));
		if(!privKeySource.equals("")) {
			MDRPipeConfiguration.setMdrSourcePrivateKey(KeyLoader.loadPrivateKey(privKeySource));
		} else {
			MDRPipeConfiguration.setMdrSourcePrivateKey(null);
		}
		String pubKeySource = cleanKey(MDRPipeConfiguration.getXmlConfigurations().getString(basePath + "/mdr/source/auth/publicKey"));
		if(!pubKeySource.equals("")) {
			MDRPipeConfiguration.setMdrSourcePublicKey(KeyLoader.loadKey(pubKeySource));
		} else {
			MDRPipeConfiguration.setMdrSourcePublicKey(null);
		}
		MDRPipeConfiguration.setMdrSourceAuthUrl(MDRPipeConfiguration.getXmlConfigurations().getString(basePath + "/mdr/source/auth/url"));
		MDRPipeConfiguration.setMdrSourceNamespace(MDRPipeConfiguration.getXmlConfigurations().getString(basePath + "/mdr/source/namespace"));
		MDRPipeConfiguration.setMdrSourceUrl(MDRPipeConfiguration.getXmlConfigurations().getString(basePath + "/mdr/source/url"));
		
		/* MDR - Target */
		MDRPipeConfiguration.setMdrTargetAuthenticate(MDRPipeConfiguration.getXmlConfigurations().getBoolean(basePath + "/mdr/target/auth/authenticate"));
		String privKeyTarget = cleanKey(MDRPipeConfiguration.getXmlConfigurations().getString(basePath + "/mdr/target/auth/privateKey"));
		if(!privKeyTarget.equals("")) {
			MDRPipeConfiguration.setMdrTargetPrivateKey(KeyLoader.loadPrivateKey(privKeyTarget));
		} else {
			MDRPipeConfiguration.setMdrTargetPrivateKey(null);
		}
		String pubKeyTarget = cleanKey(MDRPipeConfiguration.getXmlConfigurations().getString(basePath + "/mdr/target/auth/publicKey"));
		if(!pubKeyTarget.equals("")) {
			MDRPipeConfiguration.setMdrTargetPublicKey(KeyLoader.loadKey(pubKeyTarget));
		} else {
			MDRPipeConfiguration.setMdrTargetPublicKey(null);
		}
		MDRPipeConfiguration.setMdrTargetPublicKey(KeyLoader.loadKey(cleanKey(MDRPipeConfiguration.getXmlConfigurations().getString(basePath + "/mdr/target/auth/publicKey"))));
		MDRPipeConfiguration.setMdrTargetAuthUrl(MDRPipeConfiguration.getXmlConfigurations().getString(basePath + "/mdr/target/auth/url"));
		MDRPipeConfiguration.setMdrTargetNamespace(MDRPipeConfiguration.getXmlConfigurations().getString(basePath + "/mdr/target/namespace"));
		MDRPipeConfiguration.setMdrTargetUrl(MDRPipeConfiguration.getXmlConfigurations().getString(basePath + "/mdr/target/url"));
		
		/* Store */
		MDRPipeConfiguration.setStoreIp(MDRPipeConfiguration.getXmlConfigurations().getString(basePath + "/store/ip"));
		MDRPipeConfiguration.setStorePort(MDRPipeConfiguration.getXmlConfigurations().getString(basePath + "/store/port"));
		
	}

	private static String cleanKey(String string) {
		return string.replaceAll("-----BEGIN PUBLIC KEY-----", "").replaceAll("-----END PUBLIC KEY-----", "")
				.replaceAll("-----BEGIN RSA PRIVATE KEY-----", "").replaceAll("-----END RSA PRIVATE KEY-----", "")
				.replaceAll("\n", "");
	}
	
	public static String getDataFileName() {
		return dataFileName;
	}
	
	public static void setDataFileName(String dataFileName) {
		MDRPipeConfiguration.dataFileName = dataFileName;
	}
	
	public static String getDatabaseName() {
		return databaseName;
	}
	
	public static void setDatabaseName(String databaseName) {
		MDRPipeConfiguration.databaseName = databaseName;
	}
	
	public static String getLogFileName() {
		return logFileName;
	}
	
	public static void setLogFileName(String logFileName) {
		MDRPipeConfiguration.logFileName = logFileName;
	}
	
	public static String getXmlFileName() {
		return xmlFileName;
	}
	
	public static void setXmlFileName(String xmlFileName) {
		MDRPipeConfiguration.xmlFileName = xmlFileName;
	}
	
	public static String getDataFolder() {
		return dataFolder;
	}
	
	public static void setDataFolder(String dataFolder) {
		MDRPipeConfiguration.dataFolder = dataFolder;
	}
	
	public static String getDatabaseFolder() {
		return databaseFolder;
	}
	
	public static void setDatabaseFolder(String databaseFolder) {
		MDRPipeConfiguration.databaseFolder = databaseFolder;
	}
	
	public static String getLogFolder() {
		return logFolder;
	}
	
	public static void setLogFolder(String logFolder) {
		MDRPipeConfiguration.logFolder = logFolder;
	}
	
	public static String getXmlFolder() {
		return xmlFolder;
	}
	
	public static void setXmlFolder(String xmlFolder) {
		MDRPipeConfiguration.xmlFolder = xmlFolder;
	}
	
	public static String getLanguage() {
		return language;
	}
	
	public static void setLanguage(String language) {
		MDRPipeConfiguration.language = language;
	}
	
	public static Boolean getLoggingEnable() {
		return loggingEnable;
	}
	
	public static void setLoggingEnable(Boolean loggingEnable) {
		MDRPipeConfiguration.loggingEnable = loggingEnable;
	}
	
	public static String getLoggingLevel() {
		return loggingLevel;
	}
	
	public static void setLoggingLevel(String loggingLevel) {
		MDRPipeConfiguration.loggingLevel = loggingLevel;
	}
	
	public static Boolean getMdrSourceAuthenticate() {
		return mdrSourceAuthenticate;
	}
	
	public static void setMdrSourceAuthenticate(Boolean mdrSourceAuthenticate) {
		MDRPipeConfiguration.mdrSourceAuthenticate = mdrSourceAuthenticate;
	}
	
	public static PrivateKey getMdrSourcePrivateKey() {
		return mdrSourcePrivateKey;
	}
	
	public static void setMdrSourcePrivateKey(PrivateKey mdrSourcePrivateKey) {
		MDRPipeConfiguration.mdrSourcePrivateKey = mdrSourcePrivateKey;
	}
	
	public static PublicKey getMdrSourcePublicKey() {
		return mdrSourcePublicKey;
	}
	
	public static void setMdrSourcePublicKey(PublicKey mdrSourcePublicKey) {
		MDRPipeConfiguration.mdrSourcePublicKey = mdrSourcePublicKey;
	}
	
	public static String getMdrSourceAuthUrl() {
		return mdrSourceAuthUrl;
	}
	
	public static void setMdrSourceAuthUrl(String mdrSourceAuthUrl) {
		MDRPipeConfiguration.mdrSourceAuthUrl = mdrSourceAuthUrl;
	}
	
	public static String getMdrSourceNamespace() {
		return mdrSourceNamespace;
	}
	
	public static void setMdrSourceNamespace(String mdrSourceNamespace) {
		MDRPipeConfiguration.mdrSourceNamespace = mdrSourceNamespace;
	}
	
	public static String getMdrSourceUrl() {
		return mdrSourceUrl;
	}
	
	public static void setMdrSourceUrl(String mdrSourceUrl) {
		MDRPipeConfiguration.mdrSourceUrl = mdrSourceUrl;
	}
	
	public static Boolean getMdrTargetAuthenticate() {
		return mdrTargetAuthenticate;
	}
	
	public static void setMdrTargetAuthenticate(Boolean mdrTargetAuthenticate) {
		MDRPipeConfiguration.mdrTargetAuthenticate = mdrTargetAuthenticate;
	}
	
	public static PrivateKey getMdrTargetPrivateKey() {
		return mdrTargetPrivateKey;
	}
	
	public static void setMdrTargetPrivateKey(PrivateKey mdrTargetPrivateKey) {
		MDRPipeConfiguration.mdrTargetPrivateKey = mdrTargetPrivateKey;
	}
	
	public static PublicKey getMdrTargetPublicKey() {
		return mdrTargetPublicKey;
	}
	
	public static void setMdrTargetPublicKey(PublicKey mdrTargetPublicKey) {
		MDRPipeConfiguration.mdrTargetPublicKey = mdrTargetPublicKey;
	}
	
	public static String getMdrTargetAuthUrl() {
		return mdrTargetAuthUrl;
	}
	
	public static void setMdrTargetAuthUrl(String mdrTargetAuthUrl) {
		MDRPipeConfiguration.mdrTargetAuthUrl = mdrTargetAuthUrl;
	}
	
	public static String getMdrTargetNamespace() {
		return mdrTargetNamespace;
	}
	
	public static void setMdrTargetNamespace(String mdrTargetNamespace) {
		MDRPipeConfiguration.mdrTargetNamespace = mdrTargetNamespace;
	}
	
	public static String getMdrTargetUrl() {
		return mdrTargetUrl;
	}
	
	public static void setMdrTargetUrl(String mdrTargetUrl) {
		MDRPipeConfiguration.mdrTargetUrl = mdrTargetUrl;
	}
	
	public static String getStoreIp() {
		return storeIp;
	}
	
	public static void setStoreIp(String storeIp) {
		MDRPipeConfiguration.storeIp = storeIp;
	}
	
	public static String getStorePort() {
		return storePort;
	}
	
	public static void setStorePort(String storePort) {
		MDRPipeConfiguration.storePort = storePort;
	}

	public static String getConfigFolder() {
		return configFolder;
	}

	public static void setConfigFolder(String configFolder) {
		MDRPipeConfiguration.configFolder = configFolder;
	}

	public static String getConfigFileName() {
		return configFileName;
	}

	public static void setConfigFileName(String configFileName) {
		MDRPipeConfiguration.configFileName = configFileName;
	}

	public static XMLConfiguration getXmlConfigurations() {
		return xmlConfigurations;
	}

	public static void setXmlConfigurations(XMLConfiguration xmlConfigurations) {
		MDRPipeConfiguration.xmlConfigurations = xmlConfigurations;
	}

	public static String getConfigId() {
		return configId;
	}

	public static void setConfigId(String configId) {
		MDRPipeConfiguration.configId = configId;
	}

	public static String getMatchingsFolder() {
		return matchingsFolder;
	}

	public static void setMatchingsFolder(String matchingsFolder) {
		MDRPipeConfiguration.matchingsFolder = matchingsFolder;
	}

	public static String getMetadataFolder() {
		return metadataFolder;
	}

	public static void setMetadataFolder(String metadataFolder) {
		MDRPipeConfiguration.metadataFolder = metadataFolder;
	}

	public static String getMdrSourceFileName() {
		return mdrSourceFileName;
	}

	public static void setMdrSourceFileName(String mdrSourceFileName) {
		MDRPipeConfiguration.mdrSourceFileName = mdrSourceFileName;
	}

	public static String getMdrTargetFileName() {
		return mdrTargetFileName;
	}

	public static void setMdrTargetFileName(String mdrTargetFileName) {
		MDRPipeConfiguration.mdrTargetFileName = mdrTargetFileName;
	}

	public static String getMatchingFileName() {
		return matchingFileName;
	}

	public static void setMatchingFileName(String matchingFileName) {
		MDRPipeConfiguration.matchingFileName = matchingFileName;
	}

	public static boolean getDebug() {
		return debug;
	}

	public static void setDebug(boolean debug) {
		MDRPipeConfiguration.debug = debug;
	}

	public static String getExportFormat() {
		return exportFormat;
	}

	public static void setExportFormat(String exportFormat) {
		MDRPipeConfiguration.exportFormat = exportFormat;
	}

	public static String getExportLocation() {
		return exportLocation;
	}

	public static void setExportLocation(String exportLocation) {
		MDRPipeConfiguration.exportLocation = exportLocation;
	}

	public static String getExportMdrNamespace() {
		return exportMdrNamespace;
	}

	public static void setExportMdrNamespace(String exportMdrNamespace) {
		MDRPipeConfiguration.exportMdrNamespace = exportMdrNamespace;
	}

	public static String getExportMdrUrl() {
		return exportMdrUrl;
	}

	public static void setExportMdrUrl(String exportMdrUrl) {
		MDRPipeConfiguration.exportMdrUrl = exportMdrUrl;
	}
	
}
