package de.fau.med.imi.MDRPipe.ETLHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import de.fau.med.imi.MDRPipe.MDRPipeConfiguration;

public class DatabaseTableMatchedData extends DatabaseTable {
	
	public static String MATCHED_DATA_DATA_CASEID = "CASEID";
	public static String MATCHED_DATA_DATA_CONCEPT = "CONCEPT";
	public static String MATCHED_DATA_DATA_VALUE = "VALUE";
	public static String MATCHED_DATA_DATA_TIMESTAMP = "TIMESTAMP";
	public static String MATCHED_DATA_DATA_INSTANCE = "INSTANCE";
	
	public static String MATCHED_DATA_SOURCE_URN = "SOURCE_URN";
	public static String MATCHED_DATA_SOURCE_SOURCE_SLOT = "SOURCE_SOURCE_SLOT";
	public static String MATCHED_DATA_SOURCE_DATA_TYPE = "SOURCE_DATA_TYPE";
	public static String MATCHED_DATA_SOURCE_PERM_VALUE = "SOURCE_PERM_VALUE";
	public static String MATCHED_DATA_SOURCE_PATH = "SOURCE_PATH";
	public static String MATCHED_DATA_SOURCE_MATCHING_INPUT = "SOURCE_MATCHING_INPUT";
	
	public static String MATCHED_DATA_TARGET_URN = "TARGET_URN";
	public static String MATCHED_DATA_TARGET_SOURCE_SLOT = "TARGET_SOURCE_SLOT";
	public static String MATCHED_DATA_TARGET_DATA_TYPE = "TARGET_DATA_TYPE";
	public static String MATCHED_DATA_TARGET_PERM_VALUE = "TARGET_PERM_VALUE";
	public static String MATCHED_DATA_TARGET_PATH = "TARGET_PATH";
	public static String MATCHED_DATA_TARGET_MATCHING_INPUT = "TARGET_MATCHING_INPUT";
	
	public static String MATCHED_DATA_MAPPING_SOURCE_STRING = "MAPPING_SOURCE_STRING";
	public static String MATCHED_DATA_MAPPING_SCORE = "MAPPING_SCORE";
	public static String MATCHED_DATA_MAPPING_DO_MAP = "MAPPING_DO_MAP";
	public static String MATCHED_DATA_MAPPING_TARGET_STRING = "MAPPING_TARGET_STRING";
	
	public DatabaseTableMatchedData(DatabaseStorage databaseStorage) {
		
		super(databaseStorage);
		
		ArrayList<DatabaseAttribute> databaseTableAttributes = new ArrayList<DatabaseAttribute>();

		DatabaseAttribute databaseAttribute1 = new DatabaseAttribute(DatabaseTableMatchedData.MATCHED_DATA_DATA_CASEID, "string");
		DatabaseAttribute databaseAttribute2 = new DatabaseAttribute(DatabaseTableMatchedData.MATCHED_DATA_DATA_CONCEPT, "string");
		DatabaseAttribute databaseAttribute3 = new DatabaseAttribute(DatabaseTableMatchedData.MATCHED_DATA_DATA_VALUE, "string");
		DatabaseAttribute databaseAttribute4 = new DatabaseAttribute(DatabaseTableMatchedData.MATCHED_DATA_DATA_TIMESTAMP, "string");
		DatabaseAttribute databaseAttribute5 = new DatabaseAttribute(DatabaseTableMatchedData.MATCHED_DATA_DATA_INSTANCE, "string");

		DatabaseAttribute databaseAttribute6 = new DatabaseAttribute(DatabaseTableMatchedData.MATCHED_DATA_SOURCE_URN, "string");
		DatabaseAttribute databaseAttribute7 = new DatabaseAttribute(DatabaseTableMatchedData.MATCHED_DATA_SOURCE_SOURCE_SLOT, "string");
		DatabaseAttribute databaseAttribute8 = new DatabaseAttribute(DatabaseTableMatchedData.MATCHED_DATA_SOURCE_DATA_TYPE, "string");
		DatabaseAttribute databaseAttribute9 = new DatabaseAttribute(DatabaseTableMatchedData.MATCHED_DATA_SOURCE_PERM_VALUE, "string");
		DatabaseAttribute databaseAttribute10 = new DatabaseAttribute(DatabaseTableMatchedData.MATCHED_DATA_SOURCE_PATH, "string");
		DatabaseAttribute databaseAttribute11 = new DatabaseAttribute(DatabaseTableMatchedData.MATCHED_DATA_SOURCE_MATCHING_INPUT, "string");

		DatabaseAttribute databaseAttribute12 = new DatabaseAttribute(DatabaseTableMatchedData.MATCHED_DATA_TARGET_URN, "string");
		DatabaseAttribute databaseAttribute13 = new DatabaseAttribute(DatabaseTableMatchedData.MATCHED_DATA_TARGET_SOURCE_SLOT, "string");
		DatabaseAttribute databaseAttribute14 = new DatabaseAttribute(DatabaseTableMatchedData.MATCHED_DATA_TARGET_DATA_TYPE, "string");
		DatabaseAttribute databaseAttribute15 = new DatabaseAttribute(DatabaseTableMatchedData.MATCHED_DATA_TARGET_PERM_VALUE, "string");
		DatabaseAttribute databaseAttribute16 = new DatabaseAttribute(DatabaseTableMatchedData.MATCHED_DATA_TARGET_PATH, "string");
		DatabaseAttribute databaseAttribute17 = new DatabaseAttribute(DatabaseTableMatchedData.MATCHED_DATA_TARGET_MATCHING_INPUT, "string");

		DatabaseAttribute databaseAttribute18 = new DatabaseAttribute(DatabaseTableMatchedData.MATCHED_DATA_MAPPING_SOURCE_STRING, "string");
		DatabaseAttribute databaseAttribute19 = new DatabaseAttribute(DatabaseTableMatchedData.MATCHED_DATA_MAPPING_SCORE, "string");
		DatabaseAttribute databaseAttribute20 = new DatabaseAttribute(DatabaseTableMatchedData.MATCHED_DATA_MAPPING_DO_MAP, "string");
		DatabaseAttribute databaseAttribute21 = new DatabaseAttribute(DatabaseTableMatchedData.MATCHED_DATA_MAPPING_TARGET_STRING, "string");
		
		databaseTableAttributes.add(databaseAttribute1);
		databaseTableAttributes.add(databaseAttribute2);
		databaseTableAttributes.add(databaseAttribute3);
		databaseTableAttributes.add(databaseAttribute4);
		databaseTableAttributes.add(databaseAttribute5);

		databaseTableAttributes.add(databaseAttribute6);
		databaseTableAttributes.add(databaseAttribute7);
		databaseTableAttributes.add(databaseAttribute8);
		databaseTableAttributes.add(databaseAttribute9);
		databaseTableAttributes.add(databaseAttribute10);
		databaseTableAttributes.add(databaseAttribute11);

		databaseTableAttributes.add(databaseAttribute12);
		databaseTableAttributes.add(databaseAttribute13);
		databaseTableAttributes.add(databaseAttribute14);
		databaseTableAttributes.add(databaseAttribute15);
		databaseTableAttributes.add(databaseAttribute16);
		databaseTableAttributes.add(databaseAttribute17);

		databaseTableAttributes.add(databaseAttribute18);
		databaseTableAttributes.add(databaseAttribute19);
		databaseTableAttributes.add(databaseAttribute20);
		databaseTableAttributes.add(databaseAttribute21);
		
		this.setName("matchedData");
		this.setAttributes(databaseTableAttributes);
		
	}
	
	public String buildDatabaseTableMatchedData() {
		String query = "INSERT INTO " + this.getName() + " ";
		query += "SELECT ";
			query += "d.\"" + DatabaseTableData.DATA_CASEID + "\", ";
			query += "d.\"" + DatabaseTableData.DATA_CONCEPT + "\", ";
			query += "d.\"" + DatabaseTableData.DATA_VALUE + "\", ";
			query += "d.\"" + DatabaseTableData.DATA_TIMESTAMP + "\", ";
			query += "d.\"" + DatabaseTableData.DATA_INSTANCE + "\", ";
			query += "t.\"" + DatabaseTableTranslation.TRANSLATION_SOURCE_URN + "\", ";
			query += "t.\"" + DatabaseTableTranslation.TRANSLATION_SOURCE_SOURCE_SLOT + "\", ";
			query += "t.\"" + DatabaseTableTranslation.TRANSLATION_SOURCE_DATA_TYPE + "\", ";
			query += "t.\"" + DatabaseTableTranslation.TRANSLATION_SOURCE_PERM_VALUE + "\", ";
			query += "t.\"" + DatabaseTableTranslation.TRANSLATION_SOURCE_PATH + "\", ";
			query += "t.\"" + DatabaseTableTranslation.TRANSLATION_SOURCE_MATCHING_INPUT + "\", ";
			query += "t.\"" + DatabaseTableTranslation.TRANSLATION_TARGET_URN + "\", ";
			query += "t.\"" + DatabaseTableTranslation.TRANSLATION_TARGET_SOURCE_SLOT + "\", ";
			query += "t.\"" + DatabaseTableTranslation.TRANSLATION_TARGET_DATA_TYPE + "\", ";
			query += "t.\"" + DatabaseTableTranslation.TRANSLATION_TARGET_PERM_VALUE + "\", ";
			query += "t.\"" + DatabaseTableTranslation.TRANSLATION_TARGET_PATH + "\", ";
			query += "t.\"" + DatabaseTableTranslation.TRANSLATION_TARGET_MATCHING_INPUT + "\", ";
			query += "t.\"" + DatabaseTableTranslation.TRANSLATION_MAPPING_SOURCE_STRING + "\", ";
			query += "t.\"" + DatabaseTableTranslation.TRANSLATION_MAPPING_SCORE + "\", ";
			query += "t.\"" + DatabaseTableTranslation.TRANSLATION_MAPPING_DO_MAP + "\", ";
			query += "t.\"" + DatabaseTableTranslation.TRANSLATION_MAPPING_TARGET_STRING + "\"";
		query += "FROM ";
			query += this.getDatabaseStorage().getDatabaseTableData().getName() + " d ";
		query += "LEFT OUTER JOIN ";
			query += this.getDatabaseStorage().getDatabaseTableTranslation().getName() + " t ";
		query += "ON ";
			query += "d.\"" + DatabaseTableData.DATA_CONCEPT + "\" = t.\"" + "" + DatabaseTableTranslation.TRANSLATION_SOURCE_SOURCE_SLOT + "\" ";
			query += "AND ";
				query += "( ";
					query += "(";
						query += "(d.\"" + DatabaseTableData.DATA_VALUE + "\" = t.\"" + "" + DatabaseTableTranslation.TRANSLATION_SOURCE_PERM_VALUE + "\") AND ";
						query += "(t.\"" + DatabaseTableTranslation.TRANSLATION_SOURCE_DATA_TYPE + "\" = \"ENUMERATED\")";
					query += ") ";
					query += "OR ";
						query += "(";
						query += "t.\"" + DatabaseTableTranslation.TRANSLATION_SOURCE_DATA_TYPE + "\" != \"ENUMERATED\"";
						query += ") ";
					query += ") ";
			query += "GROUP BY ";
			query += "d.\"" + DatabaseTableData.DATA_ID + "\" ";
		query += "ORDER BY ";
			query += "d.\"" + DatabaseTableData.DATA_CASEID + "\" ASC, ";
			query += "d.\"" + DatabaseTableData.DATA_INSTANCE + "\" ASC, ";
			query += "d.\"" + DatabaseTableData.DATA_TIMESTAMP + "\" ASC";
		return query;
	}
	
	public void showDatabaseTableSummary() throws SQLException {
		
		System.out.println("=== Assessment of mapping completeness ===");
		
		int numberOfDataelements = this.countValuesInColumn(DatabaseTableMatchedData.MATCHED_DATA_DATA_CASEID);
		System.out.println("Number of data records (facts) that should have a mapping: " + numberOfDataelements);
		
		int numberOfMatchedDataelements = this.countSpecificValuesInColumn(DatabaseTableMatchedData.MATCHED_DATA_MAPPING_DO_MAP, "X");
		System.out.println("Number of data records (facts) that do have a mapping: " + numberOfMatchedDataelements);
		
		int numberOfNonMatchedDataelements = this.countNonSpecificValuesInColumn(DatabaseTableMatchedData.MATCHED_DATA_MAPPING_DO_MAP, "X");
		System.out.println("Number of data records (facts) that don't have a mapping: " + numberOfNonMatchedDataelements);
		double percent = ((double) numberOfMatchedDataelements / (double) numberOfDataelements) * 100.0;
		System.out.println("\n  => " + ETLHelper.round(percent, 2) + "% of the data records (facts) have a mapping.");
		
		if(numberOfNonMatchedDataelements > 0) {
			System.out.println("");
			System.out.println(this.showNonMappedDataElements());
			System.out.println(this.showUnprocessedData());
		}
		
		
		
	}
	
	public String showNonMappedDataElements() {
		
		String nonMatchedConcepts = "  Items (not facts) without a mapping (consider updating the mapping file):\n\n";

		/*
		String query = "SELECT ";
			query += "DISTINCT s.\"" + DatabaseTableSourceNamespace.SOURCE_PATH + "\", ";
			query += "COUNT(s.\"" + DatabaseTableSourceNamespace.SOURCE_PATH + "\") as conceptCount ";
		query += "FROM " + this.getName() + " m ";
		query += "LEFT OUTER JOIN " + this.getDatabaseStorage().getDatabaseTableSourceNamespace().getName() + " s ";
		query += "ON m.\"" + DatabaseTableMatchedData.MATCHED_DATA_DATA_CONCEPT + "\" = s.\"" + DatabaseTableSourceNamespace.SOURCE_SOURCE_SLOT + "\" ";
		query += "WHERE m.\"" + DatabaseTableMatchedData.MATCHED_DATA_MAPPING_DO_MAP + "\" IS NOT \"X\" ";
		query += "GROUP BY s.\"" + DatabaseTableSourceNamespace.SOURCE_PATH + "\"";
		
		*/

		String query = "SELECT DISTINCT s.\"MDR Path (do not edit)\" AS UNMAPPED\r\nFROM sourceNamespace s \r\nEXCEPT\r\nSELECT DISTINCT m.\"SourceString\"\r\nFROM matchings m WHERE m.\"Map\" = \"X\"\r\n";
		
		
		try {
			this.getDatabaseStorage().setStatement(this.getDatabaseStorage().getConnection().createStatement());
			this.getDatabaseStorage().getStatement().setQueryTimeout(30);
			ResultSet nonMatchedConceptsResultSet = this.getDatabaseStorage().getStatement().executeQuery(query);
			while(nonMatchedConceptsResultSet.next()) {
				//nonMatchedConcepts += "\t" + nonMatchedConceptsResultSet.getString(DatabaseTableSourceNamespace.SOURCE_PATH) + " (" + nonMatchedConceptsResultSet.getString("conceptCount") + "x) \n";
				nonMatchedConcepts += "\t" + nonMatchedConceptsResultSet.getString("UNMAPPED") + "\n";
			}
		} catch (SQLException e) {
			if(MDRPipeConfiguration.getDebug()) {
				e.printStackTrace();
			}
			System.out.println("Error when executing the query: " + query);
		}
		
		return nonMatchedConcepts;
		
	}
	
	
public String showUnprocessedData() {
		
		String nonMatchedConcepts = "  Items (not facts) for which no data was processed (because of missing mapping or missing data in the source table):\n\n";

		/*
		String query = "SELECT ";
			query += "DISTINCT s.\"" + DatabaseTableSourceNamespace.SOURCE_PATH + "\", ";
			query += "COUNT(s.\"" + DatabaseTableSourceNamespace.SOURCE_PATH + "\") as conceptCount ";
		query += "FROM " + this.getName() + " m ";
		query += "LEFT OUTER JOIN " + this.getDatabaseStorage().getDatabaseTableSourceNamespace().getName() + " s ";
		query += "ON m.\"" + DatabaseTableMatchedData.MATCHED_DATA_DATA_CONCEPT + "\" = s.\"" + DatabaseTableSourceNamespace.SOURCE_SOURCE_SLOT + "\" ";
		query += "WHERE m.\"" + DatabaseTableMatchedData.MATCHED_DATA_MAPPING_DO_MAP + "\" IS NOT \"X\" ";
		query += "GROUP BY s.\"" + DatabaseTableSourceNamespace.SOURCE_PATH + "\"";
		
		*/

		String query = "SELECT DISTINCT s.\"MDR Path (do not edit)\" AS LACKING\r\nFROM sourceNamespace s \r\nexcept\r\nSELECT DISTINCT m.\"SOURCE_PATH\"\r\nFROM matchedData m\r\nORDER BY m.\"SOURCE_PATH\"";
		
		
		try {
			this.getDatabaseStorage().setStatement(this.getDatabaseStorage().getConnection().createStatement());
			this.getDatabaseStorage().getStatement().setQueryTimeout(30);
			ResultSet nonMatchedConceptsResultSet = this.getDatabaseStorage().getStatement().executeQuery(query);
			while(nonMatchedConceptsResultSet.next()) {
				//nonMatchedConcepts += "\t" + nonMatchedConceptsResultSet.getString(DatabaseTableSourceNamespace.SOURCE_PATH) + " (" + nonMatchedConceptsResultSet.getString("conceptCount") + "x) \n";
				nonMatchedConcepts += "\t" + nonMatchedConceptsResultSet.getString("LACKING") + "\n";
			}
		} catch (SQLException e) {
			if(MDRPipeConfiguration.getDebug()) {
				e.printStackTrace();
			}
			System.out.println("Error when executing the query: " + query);
		}
		
		return nonMatchedConcepts;
		
	}
	
	
	
	

}