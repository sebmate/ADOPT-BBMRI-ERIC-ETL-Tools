package de.fau.med.imi.MDRPipe.ETLHelper;

import java.math.BigDecimal;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;

import de.fau.med.imi.MDRPipe.MDRPipeConfiguration;

public class ElementMatcher {

	private DatabaseStorage database;

	HashSet<String> missingMatchings = new HashSet<String>();
	HashSet<String> badEntries = new HashSet<String>();

	public ElementMatcher(DatabaseStorage database) throws SQLException {
		this.setDatabase(database);
		this.getDatabase().setConnection(DriverManager.getConnection("jdbc:sqlite:" + MDRPipeConfiguration.getLogFolder() + this.getDatabase().getDatabaseName()));
		this.getDatabase().setStatement(this.getDatabase().getConnection().createStatement());
		this.getDatabase().getStatement().setQueryTimeout(30);
	}

	public static double round(double d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(Double.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.doubleValue();
	}

	public void startMatching() throws SQLException {
		
		System.out.println("");
		
		System.out.println("========= COMPILING INFORMATION INTO SINGLE TABLE =========");
		
		System.out.println("");
		
		try {
			System.out.println("=== Performing actions on data tables ===");
			buildTranslationTable();
			buildMatchedDataTable();
			System.out.println("Performing actions on data tables successful!\n");
		} catch (Exception e) {
			if(MDRPipeConfiguration.getDebug()) {
				e.printStackTrace();
			}
		}
		
		this.getDatabase().getDatabaseTableMatchedData().showDatabaseTableSummary();
		
		// Export all database tables to files
		// TODO
		// this.getDatabase().exportAllDatabaseTablesToFiles();
		
		this.getDatabase().destroyDatabaseStorage();
		
	}

	private void buildTranslationTable() {
		System.out.print("Building translation table ... ");
		try {
			this.getDatabase().setStatement(this.getDatabase().getConnection().createStatement());
			this.getDatabase().getStatement().setQueryTimeout(30);
			this.getDatabase().getStatement().executeUpdate(this.getDatabase().getDatabaseTableTranslation().buildDatabaseTableTranslation());
			System.out.println("OK");
		} catch (Exception e) {
			System.out.println("ERROR");

			if(MDRPipeConfiguration.getDebug()) {
				e.printStackTrace();
			}
		}
	}

	private void buildMatchedDataTable() {
		System.out.print("Merging tables into single data table ... ");
		try {
			this.getDatabase().setStatement(this.getDatabase().getConnection().createStatement());
			this.getDatabase().getStatement().setQueryTimeout(30);
			this.getDatabase().getStatement().executeUpdate(this.getDatabase().getDatabaseTableMatchedData().buildDatabaseTableMatchedData());
			System.out.println("OK");
		} catch (Exception e) {
			System.out.println("ERROR");

			if(MDRPipeConfiguration.getDebug()) {
				e.printStackTrace();
			}
		}
	}

	public DatabaseStorage getDatabase() {
		return database;
	}

	public void setDatabase(DatabaseStorage database) {
		this.database = database;
	}

}
