package de.fau.med.imi.MDRPipe.ETLHelper;

import java.sql.SQLException;

import de.fau.med.imi.MDRPipe.MDRPipeConfiguration;

public class FileUploader {

	public DatabaseStorage database;
	public CSVLoader loader;

	private String data;
	private String sourceNamespace;
	private String targetNamespace;
	private String matching;

	public FileUploader(String data, String sourceNamespace, String targetNamespace, String matching) throws SQLException {
		this.setDatabase(new DatabaseStorage(MDRPipeConfiguration.getDatabaseName()));
		this.setLoader(new CSVLoader(this.getDatabase().getConnection()));
		this.setData(data);
		this.setSourceNamespace(sourceNamespace);
		this.setTargetNamespace(targetNamespace);
		this.setMatching(matching);
	}

	public void uploadFiles() throws SQLException {

		System.out.println("Transforming data to comply with the definition of namespace \"" + MDRPipeConfiguration.getMdrTargetNamespace() + "\".\n");

		try {
			System.out.println("=== Uploading files into database ===");
			UploadFileToDatabase("data");
			UploadFileToDatabase("sourceNamespace");
			UploadFileToDatabase("targetNamespace");
			UploadFileToDatabase("matching");
			System.out.println("Uploading files into database completed.\n");
		} catch (Exception e) {
			if(MDRPipeConfiguration.getDebug()) {
				e.printStackTrace();
			}
		}

		System.out.println("========= SUMMARY OF INPUT DATA =========");

		System.out.println("");

		// SHOW INFORMATIONS ABOUT INPUT DATA RECORDS
		this.getDatabase().getDatabaseTableData().showDatabaseTableSummary();

		System.out.println("");

		// SHOW INFORMATIONS ABOUT SOURCE NAMESPACE
		this.getDatabase().getDatabaseTableSourceNamespace().showDatabaseTableSummary();

		System.out.println("");

		// SHOW INFORMATIONS ABOUT TARGET NAMESPACE
		this.getDatabase().getDatabaseTableTargetNamespace().showDatabaseTableSummary();

		System.out.println("");

		// SHOW INFORMATIONS ABOUT THE MATCHING RULES
		this.getDatabase().getDatabaseTableMatching().showDatabaseTableSummary();

		System.out.println("");

		this.getDatabase().destroyDatabaseStorage();
		
	}

	private void UploadFileToDatabase(String fileType) throws Exception {
		if (fileType.equals("data") || fileType.equals("sourceNamespace") || fileType.equals("targetNamespace") || fileType.equals("matching")) {
			this.getDatabase().setStatement(this.getDatabase().getConnection().createStatement());
			this.getDatabase().getStatement().setQueryTimeout(30);
			this.setLoader(new CSVLoader(this.getDatabase().getConnection()));
			switch (fileType) {
				case "data": uploadDataToDatabase(); break;
				case "sourceNamespace": uploadSourceNamespaceToDatabase(); break;
				case "targetNamespace": uploadTargetNamespaceToDatabase(); break;
				case "matching": uploadMatchingToDatabase(); break;
			}
		}
	}

	private void uploadDataToDatabase() {
		System.out.print("Uploading data (" + this.getData() + ") ... ");
		try {
			this.getLoader().loadCSV(MDRPipeConfiguration.getDataFolder() + this.getData(), this.getDatabase().getDatabaseTableData().getName(), true);
			System.out.println("OK");
		} catch (Exception e) {
			System.out.println("ERROR");
			if(MDRPipeConfiguration.getDebug()) {
				e.printStackTrace();
			}
		}
	}

	private void uploadSourceNamespaceToDatabase() {
		System.out.print("Uploading source namespace (" + this.getSourceNamespace() + ") ... ");
		try {
			this.getLoader().loadCSV(MDRPipeConfiguration.getMetadataFolder() + this.getSourceNamespace(), this.getDatabase().getDatabaseTableSourceNamespace().getName(), true);
			System.out.println("OK");
		} catch (Exception e) {
			System.out.println("ERROR");

			if(MDRPipeConfiguration.getDebug()) {
				e.printStackTrace();
			}
		}
	}

	private void uploadTargetNamespaceToDatabase() {
		System.out.print("Uploading target namespace (" + this.getTargetNamespace() + ") ... ");
		try {
			this.getLoader().loadCSV(MDRPipeConfiguration.getMetadataFolder() + this.getTargetNamespace(), this.getDatabase().getDatabaseTableTargetNamespace().getName(), true);
			System.out.println("OK");
		} catch (Exception e) {
			System.out.println("ERROR");

			if(MDRPipeConfiguration.getDebug()) {
				e.printStackTrace();
			}
		}
	}

	private void uploadMatchingToDatabase() {
		System.out.print("Uploading matching (" + this.getMatching() + ") ... ");
		try {
			this.getLoader().loadCSV(MDRPipeConfiguration.getMappingsFolder() + this.getMatching(), this.getDatabase().getDatabaseTableMatching().getName(), true);
			System.out.println("OK");
		} catch (Exception e) {
			System.out.println("ERROR");

			if(MDRPipeConfiguration.getDebug()) {
				e.printStackTrace();
			}
		}
	}

	private String getSourceNamespace() {
		return sourceNamespace;
	}

	private void setSourceNamespace(String sourceNamespace) {
		this.sourceNamespace = sourceNamespace;
	}

	private String getTargetNamespace() {
		return targetNamespace;
	}

	private void setTargetNamespace(String targetNamespace) {
		this.targetNamespace = targetNamespace;
	}

	private String getData() {
		return data;
	}

	private void setData(String data) {
		this.data = data;
	}

	public String getMatching() {
		return matching;
	}

	public void setMatching(String matching) {
		this.matching = matching;
	}

	public CSVLoader getLoader() {
		return loader;
	}

	public void setLoader(CSVLoader loader) {
		this.loader = loader;
	}

	public DatabaseStorage getDatabase() {
		return database;
	}

	public void setDatabase(DatabaseStorage database) {
		this.database = database;
	}

}
