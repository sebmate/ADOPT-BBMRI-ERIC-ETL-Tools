package de.fau.med.imi.MDRPipe.ETLHelper;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import de.fau.med.imi.MDRPipe.MDRPipeConfiguration;

public class DatabaseStorage {

	private String databaseName = "";
	private String connectionString = "";

	Connection connection;
	Statement statement;

	DatabaseTableData databaseTableData;
	DatabaseTableSourceNamespace databaseTableSourceNamespace;
	DatabaseTableTargetNamespace databaseTableTargetNamespace;
	DatabaseTableMatching databaseTableMatching;
	DatabaseTableTranslation databaseTableTranslation;
	DatabaseTableMatchedData databaseTableMatchedData;
	DatabaseTableTransformedData databaseTableTransformedData;

	public DatabaseStorage(String databaseName) throws SQLException {

		this.setDatabaseName(databaseName);

		System.out.print("Deleting old SQLite database ... ");
		File file = new File(MDRPipeConfiguration.getLogFolder() + this.getDatabaseName());
		if(file.delete()){
			System.out.println("OK");
		}else{
			System.out.println("ERROR, aborting.");
			System.exit(0);
		}
		
		this.setConnectionString("jdbc:sqlite:" + MDRPipeConfiguration.getLogFolder() + this.getDatabaseName());
		this.setConnection(DriverManager.getConnection(this.getConnectionString()));
		this.setStatement(this.getConnection().createStatement());
		this.getStatement().setQueryTimeout(30);

		// DatabaseTable Data
		this.setDatabaseTableData(new DatabaseTableData(this));
		this.getStatement().executeUpdate(this.getDatabaseTableData().dropDatabaseTable());
		this.getStatement().executeUpdate(this.getDatabaseTableData().createDatabaseTable());

		// DatabaseTable SourceNamespace
		this.setDatabaseTableSourceNamespace(new DatabaseTableSourceNamespace(this));
		this.getStatement().executeUpdate(this.getDatabaseTableSourceNamespace().dropDatabaseTable());
		this.getStatement().executeUpdate(this.getDatabaseTableSourceNamespace().createDatabaseTable());

		// DatabaseTable TargetNamespace
		this.setDatabaseTableTargetNamespace(new DatabaseTableTargetNamespace(this));
		this.getStatement().executeUpdate(this.getDatabaseTableTargetNamespace().dropDatabaseTable());
		this.getStatement().executeUpdate(this.getDatabaseTableTargetNamespace().createDatabaseTable());

		// DatabaseTable Mapping
		this.setDatabaseTableMatching(new DatabaseTableMatching(this));
		this.getStatement().executeUpdate(this.getDatabaseTableMatching().dropDatabaseTable());
		this.getStatement().executeUpdate(this.getDatabaseTableMatching().createDatabaseTable());

		// DatabaseTable Translation
		this.setDatabaseTableTranslation(new DatabaseTableTranslation(this));
		this.getStatement().executeUpdate(this.getDatabaseTableTranslation().dropDatabaseTable());
		this.getStatement().executeUpdate(this.getDatabaseTableTranslation().createDatabaseTable());

		// DatabaseTable MappedData
		this.setDatabaseTableMatchedData(new DatabaseTableMatchedData(this));
		this.getStatement().executeUpdate(this.getDatabaseTableMatchedData().dropDatabaseTable());
		this.getStatement().executeUpdate(this.getDatabaseTableMatchedData().createDatabaseTable());

		// DatabaseTable TransformedData
		this.setDatabaseTableTransformedData(new DatabaseTableTransformedData(this));
		this.getStatement().executeUpdate(this.getDatabaseTableTransformedData().dropDatabaseTable());
		this.getStatement().executeUpdate(this.getDatabaseTableTransformedData().createDatabaseTable());

	}

	public void destroyDatabaseStorage() throws SQLException {
		this.getConnection().close();
	}

	public void exportAllDatabaseTablesToFiles() {
		this.exportDatabaseTableToFile(this.getDatabaseTableData());
		this.exportDatabaseTableToFile(this.getDatabaseTableMatchedData());
		this.exportDatabaseTableToFile(this.getDatabaseTableSourceNamespace());
		this.exportDatabaseTableToFile(this.getDatabaseTableTargetNamespace());
	}

	// TODO
	public void exportDatabaseTableToFile(DatabaseTable databaseTable) {

	}

	// TODO
	public void insertETLResultEntryIntoDatabaseTableMatchedData(ETLResultEntry entry) throws SQLException {
		String query = "INSERT INTO " + this.getDatabaseTableMatchedData().getName() + " ";
		query += "VALUES (";
		query += "\"" + entry.getDataCaseID() + "\", ";
		query += "\"" + entry.getDataConcept() + "\", ";
		query += "\"" + entry.getDataValue() + "\", ";
		query += "\"" + entry.getDataTimestamp() + "\", ";
		query += "\"" + entry.getDataInstance() + "\", ";
		query += "\"" + entry.getSourceURN() + "\", ";
		query += "\"" + entry.getSourceSourceSlot() + "\", ";
		query += "\"" + entry.getSourceDataType() + "\", ";
		query += "\"" + entry.getSourcePermValue() + "\", ";
		query += "\"" + entry.getSourcePath() + "\", ";
		query += "\"" + entry.getSourceMatchingInput() + "\", ";
		query += "\"" + entry.getTargetURN() + "\", ";
		query += "\"" + entry.getTargetSourceSlot() + "\", ";
		query += "\"" + entry.getTargetDataType() + "\", ";
		query += "\"" + entry.getTargetPermValue() + "\", ";
		query += "\"" + entry.getTargetPath() + "\", ";
		query += "\"" + entry.getTargetMatchingInput() + "\", ";
		query += "\"" + entry.getMappingSourceString() + "\", ";
		query += "\"" + entry.getMappingScore() + "\", ";
		query += "\"" + entry.getMappingDoMap() + "\", ";
		query += "\"" + entry.getMappingTargetString() + "\");";

		this.getStatement().setQueryTimeout(30);
		this.getStatement().execute(query);

	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Statement getStatement() {
		return statement;
	}

	public void setStatement(Statement statement) {
		this.statement = statement;
	}

	public DatabaseTableData getDatabaseTableData() {
		return databaseTableData;
	}

	public DatabaseTableSourceNamespace getDatabaseTableSourceNamespace() {
		return databaseTableSourceNamespace;
	}

	public void setDatabaseTableSourceNamespace(DatabaseTableSourceNamespace databaseTableSourceNamespace) {
		this.databaseTableSourceNamespace = databaseTableSourceNamespace;
	}

	public DatabaseTableTargetNamespace getDatabaseTableTargetNamespace() {
		return databaseTableTargetNamespace;
	}

	public void setDatabaseTableTargetNamespace(DatabaseTableTargetNamespace databaseTableTargetNamespace) {
		this.databaseTableTargetNamespace = databaseTableTargetNamespace;
	}

	public DatabaseTableMatching getDatabaseTableMatching() {
		return databaseTableMatching;
	}

	public void setDatabaseTableMatching(DatabaseTableMatching databaseTableMatching) {
		this.databaseTableMatching = databaseTableMatching;
	}

	public DatabaseTableTranslation getDatabaseTableTranslation() {
		return databaseTableTranslation;
	}

	public void setDatabaseTableTranslation(DatabaseTableTranslation databaseTableTranslation) {
		this.databaseTableTranslation = databaseTableTranslation;
	}

	public void setDatabaseTableData(DatabaseTableData databaseTableData) {
		this.databaseTableData = databaseTableData;
	}

	public DatabaseTableMatchedData getDatabaseTableMatchedData() {
		return databaseTableMatchedData;
	}

	public void setDatabaseTableMatchedData(DatabaseTableMatchedData databaseTableMatchedData) {
		this.databaseTableMatchedData = databaseTableMatchedData;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getConnectionString() {
		return connectionString;
	}

	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}

	public DatabaseTableTransformedData getDatabaseTableTransformedData() {
		return databaseTableTransformedData;
	}

	public void setDatabaseTableTransformedData(DatabaseTableTransformedData databaseTableTransformedData) {
		this.databaseTableTransformedData = databaseTableTransformedData;
	}

}
