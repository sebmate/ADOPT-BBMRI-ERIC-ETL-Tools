package de.fau.med.imi.MDRMatcher;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

public class MetadataDefinition  implements Serializable {

	private static final long serialVersionUID = 1L;
	String[] fileContent;
	private List<MappingTerm> mappingTerms = new ArrayList<MappingTerm>();

	private int numberOfTerms = 0;

	private String[] allTerms;
	private String allTermsStr = ""; // contains all words, for word frequency
	Map<String, Long> wordFrequencies = null;

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

	public MetadataDefinition() {

	}

	// Reads a metadata definition file
	public MetadataDefinition(String filename) {
		loadFile(filename);
	}

	void loadFile(String filename) {
		//List<String> synonyms = new ArrayList<String>();    // faster for hacking
        List<String> synonyms = loadSynonyms(); // the correct method

		try {
			fileContent = readLines(filename);
			
			// setTermsCount(fileContent.length);
			for (int a = 0; a < fileContent.length; a++) {
				if (!fileContent[a].trim().equals("") && a > 0) {
					MappingTerm temp = new MappingTerm(fileContent[a], synonyms);
					mappingTerms.add(temp);
					// System.out.println(temp.getSimplifiedTermString());
					allTermsStr = allTermsStr + temp.getSimplifiedTermString() + " ";
				}
			}
			
			System.out.print("-> Computing word frequencies ...");
			
			// https://stackoverflow.com/a/33703958
			Stream<String> stream = Stream.of(allTermsStr.split("\\W+")).parallel();
			wordFrequencies = stream.collect(Collectors.groupingBy(String::toString,Collectors.counting()));
			System.out.println(" OK");
		    
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private List<String> loadSynonyms() {
		FileReader fileReader;
		List<String> synonyms = new ArrayList<String>();

		try {
			fileReader = new FileReader("Ressources/Synonyms.txt");
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				synonyms.add(line);
			}
			bufferedReader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return synonyms;
	}

	public int getTermsCount() {
		// return numberOfTerms;
		return mappingTerms.size();
	}

	void setTermsCount(int numberOfTerms) {
		// this.numberOfTerms = numberOfTerms;
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

	List<MappingTerm> getMappingTerms() {
		return mappingTerms;
	}

	void setMappingTerms(List<MappingTerm> mappingTerms) {
		this.mappingTerms = mappingTerms;
	}

	// Return a part of this class' metadata
	public MetadataDefinition getPartial(int offset, int step) {
		MetadataDefinition partial = new MetadataDefinition();
		List<MappingTerm> partialMappingTerms = new ArrayList<MappingTerm>();

		for (int a = offset; a < mappingTerms.size(); a = a + step) {
			partialMappingTerms.add(mappingTerms.get(a));
		}

		partial.setMappingTerms(partialMappingTerms);
		return partial;
	}

	Map<String, Long> getSortedWordFrequencies() {
		return MapUtil.sortByValue(wordFrequencies);
	}
	
	Map<String, Long> getWordFrequencies() {
		return wordFrequencies;
	}

}
