package de.fau.med.imi.MDRPipe.ETLHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class DatabaseTableData extends DatabaseTable {
	
	public static String DATA_CASEID = "CASEID";
	public static String DATA_CONCEPT = "CONCEPT";
	public static String DATA_VALUE = "VALUE";
	public static String DATA_TIMESTAMP = "TIMESTAMP";
	public static String DATA_INSTANCE = "INSTANCE";
	public static String DATA_ID = "ID";

	public DatabaseTableData(DatabaseStorage databaseStorage) {
		
		super(databaseStorage);
		
		ArrayList<DatabaseAttribute> databaseTableAttributes = new ArrayList<DatabaseAttribute>();

		DatabaseAttribute databaseAttribute1 = new DatabaseAttribute(DatabaseTableData.DATA_CASEID, "string");
		DatabaseAttribute databaseAttribute2 = new DatabaseAttribute(DatabaseTableData.DATA_CONCEPT, "string");
		DatabaseAttribute databaseAttribute3 = new DatabaseAttribute(DatabaseTableData.DATA_VALUE, "string");
		DatabaseAttribute databaseAttribute4 = new DatabaseAttribute(DatabaseTableData.DATA_TIMESTAMP, "string");
		DatabaseAttribute databaseAttribute5 = new DatabaseAttribute(DatabaseTableData.DATA_INSTANCE, "number");
		DatabaseAttribute databaseAttribute6 = new DatabaseAttribute(DatabaseTableData.DATA_ID, "INTEGER", Arrays.asList("PRIMARY KEY", "AUTOINCREMENT"));
		
		databaseTableAttributes.add(databaseAttribute1);
		databaseTableAttributes.add(databaseAttribute2);
		databaseTableAttributes.add(databaseAttribute3);
		databaseTableAttributes.add(databaseAttribute4);
		databaseTableAttributes.add(databaseAttribute5);
		databaseTableAttributes.add(databaseAttribute6);
		
		this.setName("data");
		this.setAttributes(databaseTableAttributes);
		
	}
	
	public void showDatabaseTableSummary() throws SQLException {
		System.out.println("=== Summary of the biobank data (from Excel/CSV file) ===");
		System.out.println("Number of patients: " + this.countDistinctValuesInColumn(DatabaseTableData.DATA_CASEID));
		System.out.println("Number of data records: " + this.countValuesInColumn(DatabaseTableData.DATA_VALUE));
		System.out.println("Number of different concepts: " + this.countDistinctValuesInColumn(DatabaseTableData.DATA_CONCEPT));
		System.out.println("Number of empty concepts: " + this.countEmptyValuesInColumn(DatabaseTableData.DATA_CONCEPT));
	}

}
