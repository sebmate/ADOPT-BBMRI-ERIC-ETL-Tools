package de.fau.med.imi.MDRMatcher;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class MappingContentFile {

	String[] fileContent;
	List<MappingTerm> mappingTerms = new ArrayList<MappingTerm>();

	private int numberOfTerms = 0;

	// Simple file reader
	public String[] readLines(String filename) throws IOException {
		
		
		FileInputStream is = new FileInputStream(filename);
		InputStreamReader isr = new InputStreamReader(is, "UTF-8");
		BufferedReader bufferedReader = new BufferedReader(isr);
		
		List<String> lines = new ArrayList<String>();
		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			lines.add(line);
		}
		bufferedReader.close();
		return lines.toArray(new String[lines.size()]);
	}

	public MappingContentFile(String filename) {
		
		
		FileReader fileReader;
		List<String> synonyms = new ArrayList<String>();

		try {
		
			FileInputStream is = new FileInputStream("Ressources/Synonyms.txt");
			InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(isr);

			List<String> lines = new ArrayList<String>();
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				synonyms.add(line);
			}
			bufferedReader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		try {
			fileContent = readLines(filename);
			setTermsCount(fileContent.length);
			for (int a = 0; a < fileContent.length; a++) {

				if (!fileContent[a].trim().equals("")) {
					MappingTerm temp = new MappingTerm(fileContent[a], synonyms);
					mappingTerms.add(temp);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public int getTermsCount() {
		return numberOfTerms;
	}

	void setTermsCount(int numberOfTerms) {
		this.numberOfTerms = numberOfTerms;
	}

	public MappingTerm getTerm(int i) {
		return mappingTerms.get(i);
	}

	public void setTerm(int a, MappingTerm term) {
		mappingTerms.set(a, term);
	}

	public String getTermString(int i) {
		return mappingTerms.get(i).getTermString();
	}

	public String getSimplifiedTermString(int i) {
		return mappingTerms.get(i).getSimplifiedTermString();
	}

}
