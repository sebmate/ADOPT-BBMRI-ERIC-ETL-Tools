package de.fau.med.imi.MDRPipe.ETLHelper;

import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseTableTargetNamespace extends DatabaseTable {
	
	public static String TARGET_URN = "URN (do not edit)";
	public static String TARGET_SOURCE_SLOT = "SOURCE Slot (do not edit)";
	public static String TARGET_DATA_TYPE = "Data Type";
	public static String TARGET_PERM_VALUE = "Permitted Value";
	public static String TARGET_PATH = "MDR Path (do not edit)";
	public static String TARGET_MATCHING_INPUT = "Matching Input (can be edited)";
	
	public DatabaseTableTargetNamespace(DatabaseStorage databaseStorage) {
		
		super(databaseStorage);
		
		ArrayList<DatabaseAttribute> databaseTableAttributes = new ArrayList<DatabaseAttribute>();

		DatabaseAttribute databaseAttribute1 = new DatabaseAttribute(DatabaseTableTargetNamespace.TARGET_URN, "string");
		DatabaseAttribute databaseAttribute2 = new DatabaseAttribute(DatabaseTableTargetNamespace.TARGET_SOURCE_SLOT, "string");
		DatabaseAttribute databaseAttribute3 = new DatabaseAttribute(DatabaseTableTargetNamespace.TARGET_DATA_TYPE, "string");
		DatabaseAttribute databaseAttribute4 = new DatabaseAttribute(DatabaseTableTargetNamespace.TARGET_PERM_VALUE, "string");
		DatabaseAttribute databaseAttribute5 = new DatabaseAttribute(DatabaseTableTargetNamespace.TARGET_PATH, "string");
		DatabaseAttribute databaseAttribute6 = new DatabaseAttribute(DatabaseTableTargetNamespace.TARGET_MATCHING_INPUT, "string");
		
		databaseTableAttributes.add(databaseAttribute1);
		databaseTableAttributes.add(databaseAttribute2);
		databaseTableAttributes.add(databaseAttribute3);
		databaseTableAttributes.add(databaseAttribute4);
		databaseTableAttributes.add(databaseAttribute5);
		databaseTableAttributes.add(databaseAttribute6);

		this.setName("targetNamespace");
		this.setAttributes(databaseTableAttributes);
		
	}
	
	public void showDatabaseTableSummary() throws SQLException {
		System.out.println("=== Summary of the target namespace ===");		
		System.out.println("Number of different dataelements: " + this.countDistinctValuesInColumn(DatabaseTableTargetNamespace.TARGET_URN));
	}

}
