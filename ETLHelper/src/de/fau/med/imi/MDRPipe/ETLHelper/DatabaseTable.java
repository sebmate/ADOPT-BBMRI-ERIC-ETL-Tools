package de.fau.med.imi.MDRPipe.ETLHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseTable {
	
	private DatabaseStorage databaseStorage;
	private String name;
	private ArrayList<DatabaseAttribute> attributes;
	
	public DatabaseTable(DatabaseStorage databaseStorage) {
		this.setDatabaseStorage(databaseStorage);
	}
	
	public String createDatabaseTable() {
		String query = "CREATE TABLE " + this.getName() + "(";
		ArrayList<DatabaseAttribute> attributes = this.getAttributes();
		for(int i = 0; i < attributes.size(); i++) {
			DatabaseAttribute attribute = attributes.get(i);
			query += "\"" + attribute.getAttributeName() + "\" " + attribute.getAttributeType();
			if(attribute.getAttributeProperties() != null) {
				for(String singleAttributeProperty : attribute.getAttributeProperties()) {
					query += " " + singleAttributeProperty;
				}
			}
			if(i != (attributes.size()-1)) {
				query += ", ";
			}
		}
		query += ")";
		return query;
	}
	
	public String dropDatabaseTable() {
		return "DROP TABLE IF EXISTS " + this.getName();
	}
	
	public int countNonSpecificValuesInColumn(String columnName, String specificValue) throws SQLException {
		return this.countNonSpecificValuesInColumn(columnName, specificValue, this.getName());
	}
	
	public int countNonSpecificValuesInColumn(String columnName, String specificValue, String tableName) throws SQLException {
		String query = "SELECT COUNT(*) AS specificValues FROM " + tableName + " WHERE \"" + columnName + "\" IS NOT \"" + specificValue + "\"";
		this.getDatabaseStorage().setStatement(this.getDatabaseStorage().getConnection().createStatement());
		this.getDatabaseStorage().getStatement().setQueryTimeout(30);
		ResultSet resultSet = this.getDatabaseStorage().getStatement().executeQuery(query);
		int specificValuesInColumn = 0;
		while(resultSet.next()) {
			specificValuesInColumn = resultSet.getInt("specificValues");
		}
		return specificValuesInColumn;
	}
	
	public int countSpecificValuesInColumn(String columnName, String specificValue) throws SQLException {
		return this.countSpecificValuesInColumn(columnName, specificValue, this.getName());
	}
	
	public int countSpecificValuesInColumn(String columnName, String specificValue, String tableName) throws SQLException {
		String query = "SELECT COUNT(\"" + columnName + "\") AS specificValues FROM " + tableName + " WHERE \"" + columnName + "\" IS \"" + specificValue + "\"";
		this.getDatabaseStorage().setStatement(this.getDatabaseStorage().getConnection().createStatement());
		this.getDatabaseStorage().getStatement().setQueryTimeout(30);
		ResultSet resultSet = this.getDatabaseStorage().getStatement().executeQuery(query);
		int specificValuesInColumn = 0;
		while(resultSet.next()) {
			specificValuesInColumn = resultSet.getInt("specificValues");
		}
		return specificValuesInColumn;
	}
	
	public int countEmptyValuesInColumn(String columnName) throws SQLException {
		return this.countEmptyValuesInColumn(columnName, this.getName());
	}
	
	public int countEmptyValuesInColumn(String columnName, String tableName) throws SQLException {
		return this.countSpecificValuesInColumn(columnName, "", tableName);
	}
	
	public int countValuesInColumn(String columnName) throws SQLException {
		return this.countValuesInColumn(columnName, this.getName());
	}
	
	public int countValuesInColumn(String columnName, String tableName) throws SQLException {
		String query = "SELECT COUNT(\"" + columnName + "\") AS countedValues FROM " + tableName;
		this.getDatabaseStorage().setStatement(this.getDatabaseStorage().getConnection().createStatement());
		this.getDatabaseStorage().getStatement().setQueryTimeout(30);
		ResultSet resultSet = this.getDatabaseStorage().getStatement().executeQuery(query);
		int distinctValuesInColumn = 0;
		while(resultSet.next()) {
			distinctValuesInColumn = resultSet.getInt("countedValues");
		}
		return distinctValuesInColumn;
	}
	
	public int countDistinctValuesInColumn(String columnName) throws SQLException {
		return this.countDistinctValuesInColumn(columnName, this.getName());
	}
	
	public int countDistinctValuesInColumn(String columnName, String tableName) throws SQLException {
		String query = "SELECT COUNT(DISTINCT \"" + columnName + "\") AS countedDistinctValues FROM " + tableName;
		this.getDatabaseStorage().setStatement(this.getDatabaseStorage().getConnection().createStatement());
		this.getDatabaseStorage().getStatement().setQueryTimeout(30);
		ResultSet resultSet = this.getDatabaseStorage().getStatement().executeQuery(query);
		int distinctValuesInColumn = 0;
		while(resultSet.next()) {
			distinctValuesInColumn = resultSet.getInt("countedDistinctValues");
		}
		return distinctValuesInColumn;
	}
	
	public ResultSet getAllDataFromDatabaseTable() throws SQLException {
		String query = "SELECT * FROM " + this.getName();
		ResultSet resultSet = this.getDatabaseStorage().getStatement().executeQuery(query);
		return resultSet;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public ArrayList<DatabaseAttribute> getAttributes() {
		return attributes;
	}
	
	public void setAttributes(ArrayList<DatabaseAttribute> attributes) {
		this.attributes = attributes;
	}

	public DatabaseStorage getDatabaseStorage() {
		return databaseStorage;
	}

	public void setDatabaseStorage(DatabaseStorage databaseStorage) {
		this.databaseStorage = databaseStorage;
	}
	
}
