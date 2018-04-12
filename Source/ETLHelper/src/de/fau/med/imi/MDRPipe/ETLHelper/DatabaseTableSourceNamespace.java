package de.fau.med.imi.MDRPipe.ETLHelper;

import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseTableSourceNamespace extends DatabaseTable {
	
	public static String SOURCE_URN = "URN (do not edit)";
	public static String SOURCE_SOURCE_SLOT = "SOURCE Slot (do not edit)";
	public static String SOURCE_DATA_TYPE = "Data Type";
	public static String SOURCE_PERM_VALUE = "Permitted Value";
	public static String SOURCE_PATH = "MDR Path (do not edit)";
	public static String SOURCE_MATCHING_INPUT = "Matching Input (can be edited)";
	
	public DatabaseTableSourceNamespace(DatabaseStorage databaseStorage) {
		
		super(databaseStorage);
		
		ArrayList<DatabaseAttribute> databaseTableAttributes = new ArrayList<DatabaseAttribute>();

		DatabaseAttribute databaseAttribute1 = new DatabaseAttribute(DatabaseTableSourceNamespace.SOURCE_URN, "string");
		DatabaseAttribute databaseAttribute2 = new DatabaseAttribute(DatabaseTableSourceNamespace.SOURCE_SOURCE_SLOT, "string");
		DatabaseAttribute databaseAttribute3 = new DatabaseAttribute(DatabaseTableSourceNamespace.SOURCE_DATA_TYPE, "string");
		DatabaseAttribute databaseAttribute4 = new DatabaseAttribute(DatabaseTableSourceNamespace.SOURCE_PERM_VALUE, "string");
		DatabaseAttribute databaseAttribute5 = new DatabaseAttribute(DatabaseTableSourceNamespace.SOURCE_PATH, "string");
		DatabaseAttribute databaseAttribute6 = new DatabaseAttribute(DatabaseTableSourceNamespace.SOURCE_MATCHING_INPUT, "string");
		
		databaseTableAttributes.add(databaseAttribute1);
		databaseTableAttributes.add(databaseAttribute2);
		databaseTableAttributes.add(databaseAttribute3);
		databaseTableAttributes.add(databaseAttribute4);
		databaseTableAttributes.add(databaseAttribute5);
		databaseTableAttributes.add(databaseAttribute6);

		this.setName("sourceNamespace");
		this.setAttributes(databaseTableAttributes);
		
	}
	
	public void showDatabaseTableSummary() throws SQLException {
		System.out.println("=== Summary of the source namespace ===");
		int distinctValuesInColumnURN = this.countDistinctValuesInColumn(DatabaseTableSourceNamespace.SOURCE_URN);
		int distinctValuesinColumnSourceSlots = this.countDistinctValuesInColumn(DatabaseTableSourceNamespace.SOURCE_SOURCE_SLOT);
		int emptySourceSlots = this.countEmptyValuesInColumn(DatabaseTableSourceNamespace.SOURCE_SOURCE_SLOT);
		System.out.println("Number of different dataelements: " + distinctValuesInColumnURN);
		System.out.println("Number of different concepts (SOURCE_SLOT): " + distinctValuesinColumnSourceSlots);
		System.out.println("Number of empty SOURCE_SLOT: " + emptySourceSlots);
	}

}
