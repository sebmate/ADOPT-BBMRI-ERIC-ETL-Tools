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
		System.out.println("Number of different data elements: " + distinctValuesInColumnURN + "   (Number of distinct MDR URNs; only applicable if the CSV/MDR route is taken.)");
		System.out.println("Number of different SOURCE_SLOTs: " + distinctValuesinColumnSourceSlots + "   (Number of distinct references to the CSV file's attribute column entries; only applicable if the CSV/MDR route is taken.)");
		System.out.println("Number of empty SOURCE_SLOTs:     " + emptySourceSlots + "   (If > 0, this indicates that the MDR's metadata is missing the required SOURCE slot entries; only applicable if the CSV/MDR route is taken.)");
	}

}
