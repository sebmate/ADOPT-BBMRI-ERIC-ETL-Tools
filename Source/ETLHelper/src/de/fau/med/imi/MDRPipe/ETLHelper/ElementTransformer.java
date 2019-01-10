package de.fau.med.imi.MDRPipe.ETLHelper;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import de.fau.med.imi.MDRPipe.MDRPipeConfiguration;

public class ElementTransformer {
	
	private DatabaseStorage database;
	private DataTypeTransformations dataTypeTransformations;

	public ElementTransformer(DatabaseStorage database) throws SQLException {
		this.setDatabase(database);
		this.getDatabase().setConnection(DriverManager.getConnection("jdbc:sqlite:" + MDRPipeConfiguration.getLogFolder() + this.getDatabase().getDatabaseName()));
		this.getDatabase().setStatement(this.getDatabase().getConnection().createStatement());
		this.getDatabase().getStatement().setQueryTimeout(30);
		this.setDataTypeTransformations(new DataTypeTransformations());
	}
	
	public ArrayList<ETLResultEntry> startTransformation() {
		
		System.out.println("");
		
		System.out.println("");
		
		System.out.println("========= FACTS DATA TRANSFORMATION PROCESS =========");
		
		System.out.println("");

		System.out.println("=== Performing actions on data tables ===");
		
		System.out.print("Performing datatype transformations (casting) ... ");
		
		ArrayList<ETLResultEntry> etlResultEntries = new ArrayList<ETLResultEntry>();
		
		try {
			
			ResultSet matchedData = this.getDatabase().getDatabaseTableMatchedData().getAllDataFromDatabaseTable();
			
			while(matchedData.next()) {
				
				ETLResultEntry etlResultEntry = new ETLResultEntry();
				
				etlResultEntry.setDataCaseID(matchedData.getString(DatabaseTableMatchedData.MATCHED_DATA_DATA_CASEID));
				etlResultEntry.setDataConcept(matchedData.getString(DatabaseTableMatchedData.MATCHED_DATA_DATA_CONCEPT));
				etlResultEntry.setDataValue(matchedData.getString(DatabaseTableMatchedData.MATCHED_DATA_DATA_VALUE));
				etlResultEntry.setDataTimestamp(matchedData.getString(DatabaseTableMatchedData.MATCHED_DATA_DATA_TIMESTAMP));
				etlResultEntry.setDataInstance(matchedData.getString(DatabaseTableMatchedData.MATCHED_DATA_DATA_INSTANCE));
				
				etlResultEntry.setSourceURN(matchedData.getString(DatabaseTableMatchedData.MATCHED_DATA_SOURCE_URN));
				etlResultEntry.setSourceSourceSlot(matchedData.getString(DatabaseTableMatchedData.MATCHED_DATA_SOURCE_SOURCE_SLOT));
				etlResultEntry.setSourceDataType(matchedData.getString(DatabaseTableMatchedData.MATCHED_DATA_SOURCE_DATA_TYPE));
				etlResultEntry.setSourcePermValue(matchedData.getString(DatabaseTableMatchedData.MATCHED_DATA_SOURCE_PERM_VALUE));
				etlResultEntry.setSourcePath(matchedData.getString(DatabaseTableMatchedData.MATCHED_DATA_SOURCE_PATH));
				etlResultEntry.setSourceMatchingInput(matchedData.getString(DatabaseTableMatchedData.MATCHED_DATA_SOURCE_MATCHING_INPUT));
				
				etlResultEntry.setTargetURN(matchedData.getString(DatabaseTableMatchedData.MATCHED_DATA_TARGET_URN));
				etlResultEntry.setTargetSourceSlot(matchedData.getString(DatabaseTableMatchedData.MATCHED_DATA_TARGET_SOURCE_SLOT));
				etlResultEntry.setTargetDataType(matchedData.getString(DatabaseTableMatchedData.MATCHED_DATA_TARGET_DATA_TYPE));
				etlResultEntry.setTargetPermValue(matchedData.getString(DatabaseTableMatchedData.MATCHED_DATA_TARGET_PERM_VALUE));
				etlResultEntry.setTargetPath(matchedData.getString(DatabaseTableMatchedData.MATCHED_DATA_TARGET_PATH));
				etlResultEntry.setTargetMatchingInput(matchedData.getString(DatabaseTableMatchedData.MATCHED_DATA_TARGET_MATCHING_INPUT));

				etlResultEntry.setMappingSourceString(matchedData.getString(DatabaseTableMatchedData.MATCHED_DATA_MAPPING_SOURCE_STRING));
				etlResultEntry.setMappingScore(matchedData.getString(DatabaseTableMatchedData.MATCHED_DATA_MAPPING_SCORE));
				etlResultEntry.setMappingDoMap(matchedData.getString(DatabaseTableMatchedData.MATCHED_DATA_MAPPING_DO_MAP));
				etlResultEntry.setMappingTargetString(matchedData.getString(DatabaseTableMatchedData.MATCHED_DATA_MAPPING_TARGET_STRING));
				
				// For the further Transformation Process we need a defined source and target data type
				if(etlResultEntry.getSourceDataType() == null || etlResultEntry.getSourceDataType().equals("")) {
					etlResultEntry.setSourceDataType("(unknown)");
				}
				if(etlResultEntry.getTargetDataType() == null || etlResultEntry.getTargetDataType().equals("")) {
					etlResultEntry.setTargetDataType("(unknown)");
				}
				
				this.getDataTypeTransformations().performTransformationOnETLResultEntry(etlResultEntry);
				
				etlResultEntries.add(etlResultEntry);
				
			}
			
			System.out.println("OK");
			
		} catch (SQLException e) {
			if(MDRPipeConfiguration.getDebug()) {
				e.printStackTrace();
			}
			e.printStackTrace();
			System.out.println("ERROR");
		}
		
		/*
		
		// TODO
		
		System.out.print("Building transformed data table ... ");
		
		try {
			
			for(int i = 0; i < etlResultEntries.size(); i++) {
				insertETLResultEntryIntoDatabaseTableTransformedData(etlResultEntries.get(i));
			}
			
			insertETLResultEntriesIntoDatabaseTableTransformedData(etlResultEntries);
			System.out.println("successful.");
		} catch (SQLException e) {
			if(MDRPipeConfiguration.getDebug()) {
				e.printStackTrace();
			}
			System.out.println("not successful.");
		}
		
		*/
		
		this.getDataTypeTransformations().showDataTypeTransformations();
		return etlResultEntries;
	
	}
	
	public void insertETLResultEntriesIntoDatabaseTableTransformedData(ArrayList<ETLResultEntry> etlResultEntries) throws SQLException {
		
		String query = "INSERT INTO " + this.getDatabase().getDatabaseTableTransformedData().getName();
			query += "(";
				query += "\"" + DatabaseTableTransformedData.MATCHED_DATA_DATA_CASEID + "\", ";
				query += "\"" + DatabaseTableTransformedData.MATCHED_DATA_DATA_CONCEPT + "\", ";
				query += "\"" + DatabaseTableTransformedData.MATCHED_DATA_DATA_VALUE + "\", ";
				query += "\"" + DatabaseTableTransformedData.MATCHED_DATA_DATA_TIMESTAMP + "\", ";
				query += "\"" + DatabaseTableTransformedData.MATCHED_DATA_DATA_INSTANCE + "\", ";
				query += "\"" + DatabaseTableTransformedData.MATCHED_DATA_SOURCE_URN + "\", ";
				query += "\"" + DatabaseTableTransformedData.MATCHED_DATA_SOURCE_SOURCE_SLOT + "\", ";
				query += "\"" + DatabaseTableTransformedData.MATCHED_DATA_SOURCE_DATA_TYPE + "\", ";
				query += "\"" + DatabaseTableTransformedData.MATCHED_DATA_SOURCE_PERM_VALUE + "\", ";
				query += "\"" + DatabaseTableTransformedData.MATCHED_DATA_SOURCE_PATH + "\", ";
				query += "\"" + DatabaseTableTransformedData.MATCHED_DATA_SOURCE_MATCHING_INPUT + "\", ";
				query += "\"" + DatabaseTableTransformedData.MATCHED_DATA_TARGET_URN + "\", ";
				query += "\"" + DatabaseTableTransformedData.MATCHED_DATA_TARGET_SOURCE_SLOT + "\", ";
				query += "\"" + DatabaseTableTransformedData.MATCHED_DATA_TARGET_DATA_TYPE + "\", ";
				query += "\"" + DatabaseTableTransformedData.MATCHED_DATA_TARGET_PERM_VALUE + "\", ";
				query += "\"" + DatabaseTableTransformedData.MATCHED_DATA_TARGET_PATH + "\", ";
				query += "\"" + DatabaseTableTransformedData.MATCHED_DATA_TARGET_MATCHING_INPUT + "\", ";
				query += "\"" + DatabaseTableTransformedData.MATCHED_DATA_MAPPING_SOURCE_STRING + "\", ";
				query += "\"" + DatabaseTableTransformedData.MATCHED_DATA_MAPPING_SCORE + "\", ";
				query += "\"" + DatabaseTableTransformedData.MATCHED_DATA_MAPPING_DO_MAP + "\", ";
				query += "\"" + DatabaseTableTransformedData.MATCHED_DATA_MAPPING_TARGET_STRING + "\", ";
				query += "\"" + DatabaseTableTransformedData.FINAL_VALUE + "\", ";
				query += "\"" + DatabaseTableTransformedData.TRANSFORMATION_RULE + "\", ";
				query += "\"" + DatabaseTableTransformedData.TRANSFORMATION_SUCCESS + "\"";
			query += ") ";
		query += "VALUES ";
		
		for(int i = 0; i < etlResultEntries.size(); i++) {
			ETLResultEntry etlResultEntry = etlResultEntries.get(i);
			query += "(";
			query += "\"" + etlResultEntry.getDataCaseID() + "\", ";
			query += "\"" + etlResultEntry.getDataConcept() + "\", ";
			query += "\"" + etlResultEntry.getDataValue() + "\", ";
			query += "\"" + etlResultEntry.getDataTimestamp() + "\", ";
			query += "\"" + etlResultEntry.getDataInstance() + "\", ";
			query += "\"" + etlResultEntry.getSourceURN() + "\", ";
			query += "\"" + etlResultEntry.getSourceSourceSlot() + "\", ";
			query += "\"" + etlResultEntry.getSourceDataType() + "\", ";
			query += "\"" + etlResultEntry.getSourcePermValue() + "\", ";
			query += "\"" + etlResultEntry.getSourcePath() + "\", ";
			query += "\"" + etlResultEntry.getSourceMatchingInput() + "\", ";
			query += "\"" + etlResultEntry.getTargetURN() + "\", ";
			query += "\"" + etlResultEntry.getTargetSourceSlot() + "\", ";
			query += "\"" + etlResultEntry.getTargetDataType() + "\", ";
			query += "\"" + etlResultEntry.getTargetPermValue() + "\", ";
			query += "\"" + etlResultEntry.getTargetPath() + "\", ";
			query += "\"" + etlResultEntry.getTargetMatchingInput() + "\", ";
			query += "\"" + etlResultEntry.getMappingSourceString() + "\", ";
			query += "\"" + etlResultEntry.getMappingScore() + "\", ";
			query += "\"" + etlResultEntry.getMappingDoMap() + "\", ";
			query += "\"" + etlResultEntry.getMappingTargetString() + "\", ";
			query += "\"" + etlResultEntry.getFinalValue() + "\", ";
			query += "\"" + etlResultEntry.getTransformationRule() + "\", ";
			query += "\"" + etlResultEntry.getTransformationSuccess() + "\"";
			if(i == etlResultEntries.size()-1) {
				query += ")";
			} else {
				query += "), ";
			}
		}
		
		try {
			this.getDatabase().setStatement(this.getDatabase().getConnection().createStatement());
			this.getDatabase().getStatement().setQueryTimeout(30);
			this.getDatabase().getStatement().executeUpdate(query);
		} catch (SQLException e) {
			if(MDRPipeConfiguration.getDebug()) {
				e.printStackTrace();
			}
			e.printStackTrace();
		}
		
	}
	
	public ArrayList<ETLResultEntry> getTransformedElements() throws SQLException {
		
		ArrayList<ETLResultEntry> etlResultEntries = new ArrayList<ETLResultEntry>();
		/*
		String queryDataFromDatabase = "SELECT DISTINCT ";
			queryDataFromDatabase += DatabaseTableData.DATA_CASEID + ", ";
			queryDataFromDatabase += DatabaseTableData.DATA_CONCEPT + ", ";
			queryDataFromDatabase += DatabaseTableData.DATA_VALUE + ", ";
			queryDataFromDatabase += DatabaseTableData.DATA_TIMESTAMP + ", ";
			queryDataFromDatabase += DatabaseTableData.DATA_INSTANCE + " ";
		queryDataFromDatabase += "FROM " + this.getDatabase().getDatabaseTableData().getName() + " ";
		queryDataFromDatabase += "ORDER BY ";
			queryDataFromDatabase += DatabaseTableData.DATA_CASEID + " ASC" + ", ";
			queryDataFromDatabase += DatabaseTableData.DATA_INSTANCE + " ASC" + ", ";
			queryDataFromDatabase += DatabaseTableData.DATA_TIMESTAMP + " ASC";
	
		ResultSet rs = this.getDatabase().getStatement().executeQuery(queryDataFromDatabase);
	
		int ProcessOK = 0;
		int ProcessWARN = 0;
		int ProcessBAD = 0;
		int dataRecordsCnt = 0;
	
		while (rs.next()) {
	
			String CaseID = rs.getString("CaseID");
			String Concept = rs.getString("Concept");
			String Value = rs.getString("Value");
			String TimeStamp = rs.getString("TimeStamp");
			String Instance = rs.getString("Instance");
	
			String FinalValue = "";
			String MappingTargetString = "";
			String SourceDataType = "";
			String TargetDataType = "";
	
			Connection connection2 = DriverManager.getConnection(this.getDatabase().getConnectionString());
			Statement statement2 = connection2.createStatement();
			statement2.setQueryTimeout(30);
	
			String sql2 = "SELECT SOURCE_URN, SOURCE_SOURCE_SLOT, SOURCE_DATA_TYPE, SOURCE_PERM_VALUE, SOURCE_PATH, SOURCE_MATCHING_INPUT, TARGET_URN, TARGET_SOURCE_SLOT,\r\nTARGET_DATA_TYPE, TARGET_PERM_VALUE, TARGET_PATH, TARGET_MATCHING_INPUT, MAPPING_SOURCE_STRING, MAPPING_SCORE, MAPPING_DO_MAP, MAPPING_TARGET_STRING\r\nFROM translation\r\n"
					+ "WHERE SOURCE_SOURCE_SLOT = \"" + Concept
					+ "\"\r\n  AND ((SOURCE_DATA_TYPE == \"ENUMERATED\" AND \"" + Value
					+ "\" = SOURCE_PERM_VALUE) OR \r\n       (SOURCE_DATA_TYPE != \"ENUMERATED\")\r\n       )\r\n;";
	
			ResultSet rs2 = statement2.executeQuery(sql2);
	
			// START Änderungen Seb am 15.03.2018
	
			int mappings = 0;
			int success = 0;
			boolean foundSomething = false;
			
			while (rs2.next()) {
	
				foundSomething = true;
	
				dataRecordsCnt++;
				if (dataRecordsCnt % 20 == 0)
					//System.out.print(".");
				if (dataRecordsCnt % 200 == 0)
					//System.out.println(" " + dataRecordsCnt + " items");
	
				mappings++;
				ETLResultEntry res = new ETLResultEntry();
	
				res.setDataCaseID(CaseID);
				res.setDataConcept(Concept);
				res.setDataValue(Value);
				res.setDataTimestamp(TimeStamp);
				res.setDataInstance(Instance);
	
				res.setSourceURN(rs2.getString("SOURCE_URN"));
				res.setSourceSourceSlot(rs2.getString("SOURCE_SOURCE_SLOT"));
				res.setSourceDataType(rs2.getString("SOURCE_DATA_TYPE"));
				res.setSourcePermValue(rs2.getString("SOURCE_PERM_VALUE"));
				res.setSourcePath(rs2.getString("SOURCE_PATH"));
				res.setSourceMatchingInput(rs2.getString("SOURCE_MATCHING_INPUT"));
	
				res.setTargetURN(rs2.getString("TARGET_URN"));
				res.setTargetSourceSlot(rs2.getString("TARGET_SOURCE_SLOT"));
				res.setTargetDataType(rs2.getString("TARGET_DATA_TYPE"));
				res.setTargetPermValue(rs2.getString("TARGET_PERM_VALUE"));
				res.setTargetPath(rs2.getString("TARGET_PATH"));
				res.setTargetMatchingInput(rs2.getString("TARGET_MATCHING_INPUT"));
	
				res.setMappingSourceString(rs2.getString("MAPPING_SOURCE_STRING"));
				res.setMappingScore(rs2.getString("MAPPING_SCORE"));
				res.setMappingDoMap(rs2.getString("MAPPING_DO_MAP"));
				res.setMappingTargetString(rs2.getString("MAPPING_TARGET_STRING"));
	
				success = res.process();
	
				FinalValue = res.getFinalValue();
				MappingTargetString = res.getMappingTargetString();
				SourceDataType = res.getSourceDataType();
				TargetDataType = res.getTargetDataType();
	
				if (success != 0) {
					etlResultEntries.add(res);
				}
				
			}
	
			if (success < 2) {
				if (foundSomething == false) {
					//missingMatchings.add(Concept);
				} else {
					//badEntries.add(Concept + " = " + Value);
				}
			}
	
			String OK = "";
			if (success == 0) {
				OK = "ERROR";
			}
			if (success == 1)
				OK = "WARNING";
			if (success == 2)
				OK = "OK";
	
			//elementMatcherLog.write(OK + "\t" + CaseID + "\t" + Concept + "\t" + Value + "\t" + FinalValue + "\t" + MappingTargetString + "\t" + SourceDataType + "=>" + TargetDataType + "\t" + Instance + "\n");
	
			if (mappings == 0) {
				success = 0;
			}
			if (mappings > 1) {
				success = 2;
			}
	
			// ENDE Änderungen Seb am 15.03.2018
	
			switch (success) {
			case 0:
				ProcessBAD++;
				break;
			case 1:
				ProcessWARN++;
				break;
			case 2:
				ProcessOK++;
				break;
			}
	
		}
		
		
		*/
		
		return etlResultEntries;
	}
	
	public DatabaseStorage getDatabase() {
		return database;
	}

	public void setDatabase(DatabaseStorage database) {
		this.database = database;
	}

	public DataTypeTransformations getDataTypeTransformations() {
		return dataTypeTransformations;
	}

	public void setDataTypeTransformations(DataTypeTransformations dataTypeTransformations) {
		this.dataTypeTransformations = dataTypeTransformations;
	}
	
}
