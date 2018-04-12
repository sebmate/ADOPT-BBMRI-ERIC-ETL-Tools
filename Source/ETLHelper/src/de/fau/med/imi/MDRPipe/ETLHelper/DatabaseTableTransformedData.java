package de.fau.med.imi.MDRPipe.ETLHelper;

import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseTableTransformedData extends DatabaseTable {
	
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
	
	public static String FINAL_VALUE = "FINAL_VALUE";
	public static String TRANSFORMATION_RULE = "TRANSFORMATION_RULE";
	public static String TRANSFORMATION_SUCCESS = "TRANSFORMATION_SUCCESS";
	
	public DatabaseTableTransformedData(DatabaseStorage databaseStorage) {
		
		super(databaseStorage);
		
		ArrayList<DatabaseAttribute> databaseTableAttributes = new ArrayList<DatabaseAttribute>();

		DatabaseAttribute databaseAttribute1 = new DatabaseAttribute(DatabaseTableTransformedData.MATCHED_DATA_DATA_CASEID, "string");
		DatabaseAttribute databaseAttribute2 = new DatabaseAttribute(DatabaseTableTransformedData.MATCHED_DATA_DATA_CONCEPT, "string");
		DatabaseAttribute databaseAttribute3 = new DatabaseAttribute(DatabaseTableTransformedData.MATCHED_DATA_DATA_VALUE, "string");
		DatabaseAttribute databaseAttribute4 = new DatabaseAttribute(DatabaseTableTransformedData.MATCHED_DATA_DATA_TIMESTAMP, "string");
		DatabaseAttribute databaseAttribute5 = new DatabaseAttribute(DatabaseTableTransformedData.MATCHED_DATA_DATA_INSTANCE, "string");

		DatabaseAttribute databaseAttribute6 = new DatabaseAttribute(DatabaseTableTransformedData.MATCHED_DATA_SOURCE_URN, "string");
		DatabaseAttribute databaseAttribute7 = new DatabaseAttribute(DatabaseTableTransformedData.MATCHED_DATA_SOURCE_SOURCE_SLOT, "string");
		DatabaseAttribute databaseAttribute8 = new DatabaseAttribute(DatabaseTableTransformedData.MATCHED_DATA_SOURCE_DATA_TYPE, "string");
		DatabaseAttribute databaseAttribute9 = new DatabaseAttribute(DatabaseTableTransformedData.MATCHED_DATA_SOURCE_PERM_VALUE, "string");
		DatabaseAttribute databaseAttribute10 = new DatabaseAttribute(DatabaseTableTransformedData.MATCHED_DATA_SOURCE_PATH, "string");
		DatabaseAttribute databaseAttribute11 = new DatabaseAttribute(DatabaseTableTransformedData.MATCHED_DATA_SOURCE_MATCHING_INPUT, "string");

		DatabaseAttribute databaseAttribute12 = new DatabaseAttribute(DatabaseTableTransformedData.MATCHED_DATA_TARGET_URN, "string");
		DatabaseAttribute databaseAttribute13 = new DatabaseAttribute(DatabaseTableTransformedData.MATCHED_DATA_TARGET_SOURCE_SLOT, "string");
		DatabaseAttribute databaseAttribute14 = new DatabaseAttribute(DatabaseTableTransformedData.MATCHED_DATA_TARGET_DATA_TYPE, "string");
		DatabaseAttribute databaseAttribute15 = new DatabaseAttribute(DatabaseTableTransformedData.MATCHED_DATA_TARGET_PERM_VALUE, "string");
		DatabaseAttribute databaseAttribute16 = new DatabaseAttribute(DatabaseTableTransformedData.MATCHED_DATA_TARGET_PATH, "string");
		DatabaseAttribute databaseAttribute17 = new DatabaseAttribute(DatabaseTableTransformedData.MATCHED_DATA_TARGET_MATCHING_INPUT, "string");

		DatabaseAttribute databaseAttribute18 = new DatabaseAttribute(DatabaseTableTransformedData.MATCHED_DATA_MAPPING_SOURCE_STRING, "string");
		DatabaseAttribute databaseAttribute19 = new DatabaseAttribute(DatabaseTableTransformedData.MATCHED_DATA_MAPPING_SCORE, "string");
		DatabaseAttribute databaseAttribute20 = new DatabaseAttribute(DatabaseTableTransformedData.MATCHED_DATA_MAPPING_DO_MAP, "string");
		DatabaseAttribute databaseAttribute21 = new DatabaseAttribute(DatabaseTableTransformedData.MATCHED_DATA_MAPPING_TARGET_STRING, "string");
		
		DatabaseAttribute databaseAttribute22 = new DatabaseAttribute(DatabaseTableTransformedData.FINAL_VALUE, "string");
		DatabaseAttribute databaseAttribute23 = new DatabaseAttribute(DatabaseTableTransformedData.TRANSFORMATION_RULE, "string");
		DatabaseAttribute databaseAttribute24 = new DatabaseAttribute(DatabaseTableTransformedData.TRANSFORMATION_SUCCESS, "boolean");
		
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
		
		databaseTableAttributes.add(databaseAttribute22);
		databaseTableAttributes.add(databaseAttribute23);
		databaseTableAttributes.add(databaseAttribute24);
		
		this.setName("transformedData");
		this.setAttributes(databaseTableAttributes);
		
	}
	
	public void showDatabaseTableSummary() throws SQLException {
		System.out.println("=== Informations about the transformation process ===");
	}

}