package de.fau.med.imi.MDRPipe.ETLHelper;

import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseTableMatching extends DatabaseTable {
	
	public static String MATCHING_SOURCE_STRING = "SourceString";
	public static String MATCHING_SCORE = "Score";
	public static String MATCHING_DO_MATCH_ORIGINAL = "Map (original, do not edit)";
	public static String MATCHING_DO_MATCH = "Map";
	public static String MATCHING_TARGET_STRING = "TargetString";
	public static String MATCHING_STATUS = "Status";
	
	public DatabaseTableMatching(DatabaseStorage databaseStorage) {
		
		super(databaseStorage);
		
		ArrayList<DatabaseAttribute> databaseTableAttributes = new ArrayList<DatabaseAttribute>();
		
		DatabaseAttribute databaseAttribute1 = new DatabaseAttribute(DatabaseTableMatching.MATCHING_SOURCE_STRING, "string");
		DatabaseAttribute databaseAttribute2 = new DatabaseAttribute(DatabaseTableMatching.MATCHING_SCORE, "string");
		DatabaseAttribute databaseAttribute3 = new DatabaseAttribute(DatabaseTableMatching.MATCHING_DO_MATCH_ORIGINAL, "string");
		DatabaseAttribute databaseAttribute4 = new DatabaseAttribute(DatabaseTableMatching.MATCHING_DO_MATCH, "string");
		DatabaseAttribute databaseAttribute5 = new DatabaseAttribute(DatabaseTableMatching.MATCHING_TARGET_STRING, "string");
		DatabaseAttribute databaseAttribute6 = new DatabaseAttribute(DatabaseTableMatching.MATCHING_STATUS, "string");
		
		databaseTableAttributes.add(databaseAttribute1);
		databaseTableAttributes.add(databaseAttribute2);
		databaseTableAttributes.add(databaseAttribute3);
		databaseTableAttributes.add(databaseAttribute4);
		databaseTableAttributes.add(databaseAttribute5);
		databaseTableAttributes.add(databaseAttribute6);

		this.setName("matchings");
		this.setAttributes(databaseTableAttributes);
		
	}
	
	public void showDatabaseTableSummary() throws SQLException {
		System.out.println("=== Summary of the mapping rules ===");
		System.out.println("Number of different source strings (dataelement and value): " + this.countDistinctValuesInColumn(DatabaseTableMatching.MATCHING_SOURCE_STRING));
		System.out.println("Number of found mappings between source and target strings: " + this.countSpecificValuesInColumn(DatabaseTableMatching.MATCHING_DO_MATCH, "X"));
	}

}
