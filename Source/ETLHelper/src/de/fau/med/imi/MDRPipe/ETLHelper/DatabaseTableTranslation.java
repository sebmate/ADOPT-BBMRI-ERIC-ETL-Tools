package de.fau.med.imi.MDRPipe.ETLHelper;

import java.util.ArrayList;

public class DatabaseTableTranslation extends DatabaseTable {
	
	public static String TRANSLATION_SOURCE_URN = "SOURCE_URN";
	public static String TRANSLATION_SOURCE_SOURCE_SLOT = "SOURCE_SOURCE_SLOT";
	public static String TRANSLATION_SOURCE_DATA_TYPE = "SOURCE_DATA_TYPE";
	public static String TRANSLATION_SOURCE_PERM_VALUE = "SOURCE_PERM_VALUE";
	public static String TRANSLATION_SOURCE_PATH = "SOURCE_PATH";
	public static String TRANSLATION_SOURCE_MATCHING_INPUT = "SOURCE_MATCHING_INPUT";
	
	public static String TRANSLATION_TARGET_URN = "TARGET_URN";
	public static String TRANSLATION_TARGET_SOURCE_SLOT = "TARGET_SOURCE_SLOT";
	public static String TRANSLATION_TARGET_DATA_TYPE = "TARGET_DATA_TYPE";
	public static String TRANSLATION_TARGET_PERM_VALUE = "TARGET_PERM_VALUE";
	public static String TRANSLATION_TARGET_PATH = "TARGET_PATH";
	public static String TRANSLATION_TARGET_MATCHING_INPUT = "TARGET_MATCHING_INPUT";
	
	public static String TRANSLATION_MAPPING_SOURCE_STRING = "MAPPING_SOURCE_STRING";
	public static String TRANSLATION_MAPPING_SCORE = "MAPPING_SCORE";
	public static String TRANSLATION_MAPPING_DO_MAP = "MAPPING_DO_MAP";
	public static String TRANSLATION_MAPPING_TARGET_STRING = "MAPPING_TARGET_STRING";
	
	public DatabaseTableTranslation(DatabaseStorage databaseStorage) {
		
		super(databaseStorage);
		
		ArrayList<DatabaseAttribute> databaseTableAttributes = new ArrayList<DatabaseAttribute>();
		
		this.getDatabaseStorage().getDatabaseTableSourceNamespace();
		DatabaseAttribute databaseAttribute1 = new DatabaseAttribute(DatabaseTableTranslation.TRANSLATION_SOURCE_URN, "string");
		DatabaseAttribute databaseAttribute2 = new DatabaseAttribute(DatabaseTableTranslation.TRANSLATION_SOURCE_SOURCE_SLOT, "string");
		DatabaseAttribute databaseAttribute3 = new DatabaseAttribute(DatabaseTableTranslation.TRANSLATION_SOURCE_DATA_TYPE, "string");
		DatabaseAttribute databaseAttribute4 = new DatabaseAttribute(DatabaseTableTranslation.TRANSLATION_SOURCE_PERM_VALUE, "string");
		DatabaseAttribute databaseAttribute5 = new DatabaseAttribute(DatabaseTableTranslation.TRANSLATION_SOURCE_PATH, "string");
		DatabaseAttribute databaseAttribute6 = new DatabaseAttribute(DatabaseTableTranslation.TRANSLATION_SOURCE_MATCHING_INPUT, "string");
		DatabaseAttribute databaseAttribute7 = new DatabaseAttribute(DatabaseTableTranslation.TRANSLATION_TARGET_URN, "string");
		DatabaseAttribute databaseAttribute8 = new DatabaseAttribute(DatabaseTableTranslation.TRANSLATION_TARGET_SOURCE_SLOT, "string");
		DatabaseAttribute databaseAttribute9 = new DatabaseAttribute(DatabaseTableTranslation.TRANSLATION_TARGET_DATA_TYPE, "string");
		DatabaseAttribute databaseAttribute10 = new DatabaseAttribute(DatabaseTableTranslation.TRANSLATION_TARGET_PERM_VALUE, "string");
		DatabaseAttribute databaseAttribute11 = new DatabaseAttribute(DatabaseTableTranslation.TRANSLATION_TARGET_PATH, "string");
		DatabaseAttribute databaseAttribute12 = new DatabaseAttribute(DatabaseTableTranslation.TRANSLATION_TARGET_MATCHING_INPUT, "string");
		DatabaseAttribute databaseAttribute13 = new DatabaseAttribute(DatabaseTableTranslation.TRANSLATION_MAPPING_SOURCE_STRING, "string");
		DatabaseAttribute databaseAttribute14 = new DatabaseAttribute(DatabaseTableTranslation.TRANSLATION_MAPPING_SCORE, "string");
		DatabaseAttribute databaseAttribute15 = new DatabaseAttribute(DatabaseTableTranslation.TRANSLATION_MAPPING_DO_MAP, "string");
		DatabaseAttribute databaseAttribute16 = new DatabaseAttribute(DatabaseTableTranslation.TRANSLATION_MAPPING_TARGET_STRING, "string");
		
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
		
		this.setName("translation");
		this.setAttributes(databaseTableAttributes);
		
	}
	
	public String buildDatabaseTableTranslation() {
		String query = "INSERT INTO " + this.getName() + " ";
		query += "SELECT ";
			query += "s.\"" + DatabaseTableSourceNamespace.SOURCE_URN + "\", ";
			query += "s.\"" + DatabaseTableSourceNamespace.SOURCE_SOURCE_SLOT + "\", ";
			query += "s.\"" + DatabaseTableSourceNamespace.SOURCE_DATA_TYPE + "\", ";
			query += "s.\"" + DatabaseTableSourceNamespace.SOURCE_PERM_VALUE + "\", ";
			query += "s.\"" + DatabaseTableSourceNamespace.SOURCE_PATH + "\", ";
			query += "s.\"" + DatabaseTableSourceNamespace.SOURCE_MATCHING_INPUT + "\", ";
			query += "t.\"" + DatabaseTableTargetNamespace.TARGET_URN + "\", ";
			query += "t.\"" + DatabaseTableTargetNamespace.TARGET_SOURCE_SLOT + "\", ";
			query += "t.\"" + DatabaseTableTargetNamespace.TARGET_DATA_TYPE + "\", ";
			query += "t.\"" + DatabaseTableTargetNamespace.TARGET_PERM_VALUE + "\", ";
			query += "t.\"" + DatabaseTableTargetNamespace.TARGET_PATH + "\", ";
			query += "t.\"" + DatabaseTableTargetNamespace.TARGET_MATCHING_INPUT + "\", ";
			query += "m.\"" + DatabaseTableMatching.MATCHING_SOURCE_STRING + "\", ";
			query += "m.\"" + DatabaseTableMatching.MATCHING_SCORE + "\", ";
			query += "m.\"" + DatabaseTableMatching.MATCHING_DO_MATCH + "\", ";
			query += "m.\"" + DatabaseTableMatching.MATCHING_TARGET_STRING + "\" ";
		query += "FROM ";
			query += this.getDatabaseStorage().getDatabaseTableSourceNamespace().getName() + " s, ";
			query += this.getDatabaseStorage().getDatabaseTableTargetNamespace().getName() + " t, ";
			query += this.getDatabaseStorage().getDatabaseTableMatching().getName() + " m ";
		query += "WHERE ";
			query += "s.\"" + DatabaseTableSourceNamespace.SOURCE_PATH + "\" = m.\"" + DatabaseTableMatching.MATCHING_SOURCE_STRING + "\" ";
			query += "AND ";
			query += "m.\"" + DatabaseTableMatching.MATCHING_DO_MATCH + "\" = \"X\" ";
			query += "AND ";
			query += "t.\"" + DatabaseTableTargetNamespace.TARGET_PATH + "\" = m.\"" + DatabaseTableMatching.MATCHING_TARGET_STRING + "\" ";
			query += "AND ";
			query += "s.\"" + DatabaseTableSourceNamespace.SOURCE_SOURCE_SLOT +"\" != \"\"";
		return query;
	}

}