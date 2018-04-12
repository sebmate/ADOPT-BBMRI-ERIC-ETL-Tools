package de.fau.med.imi.MDRMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

public class MappingTerm {

	String fullTerm;
	String term;
	String simplifiedTerm;

	String[] wordsTemp;
	List<Word> words = new ArrayList<Word>();

	private String infoCode;
	private int frequency;

	private String stopWords = " ABOUT ALSO AND AS AT BE BECAUSE BUT BY CAN COME COULD DO EVEN FIND FOR FROM GET GIVE GO HAVE HE HER HERE HIM HIS HOW IF IN INTO IT ITS JUST LIKE LOOK MAKE MAN ME MY OF ON ONLY OR OUR OUT PEOPLE SAY SEE SHE SO SOME TAKE TELL THAN THAT THE THEIR THEM THEN THERE THESE THEY THING THINK THIS THOSE TO UP WANT WAY WE WELL WHAT WHEN WHICH WILL WITH WOULD YOU YOUR ";

	public boolean isNumeric(String s) {
		return s != null && s.matches("-?\\d+(\\.\\d+)?");
	}

	String simplifyTerm(String term) {

		String temp = term.replaceAll("[^A-Za-z0-9= ]", " ");
		temp = temp.replaceAll("\\s+", " ")/* .toUpperCase() */;

		temp = temp.trim();
		// temp = temp + " " + temp.replaceAll("(?x) (?<= \\pL ) (?= \\pN ) |
		// (?<= \\pN ) (?= \\pL )", " ~ ");

		String[] w = temp.split(" ");
		String r = "";
		for (int a = 0; a < w.length; a++) {

			if (!stopWords.contains(" " + w[a] + " ")) {

				String[] k = w[a].replaceAll("(?x) (?<= \\pL ) (?= \\pN ) | (?<= \\pN ) (?= \\pL )", " ~ ").split(" ");

				if (k.length > 1) {
					r += w[a] + " ";
				}

				// Process roman numbers:

				for (int b = 0; b < k.length; b++) {
					// if (isNumeric(k[b]) && !k[b].equals("0")) {
					// int v = Integer.parseInt(k[b]);
					// if (v < 100 && v > 0)
					// r += RomanNumber.toRoman(v) + " ";
					// } else {
					r += k[b] + " ";
					// }
				}
			} else {
				// System.out.println("Removed stopword: " + w[a]);
			}
		}
		r = r.trim().replaceAll(" ~ ", " ");

		// if (term.startsWith("Sample / Preservation mode = Other")) {
		// System.out.println(term);
		// System.out.println(r);
		// System.exit(1);
		// }

		return r;
	}

	public MappingTerm(String term, List<String> synonyms) {

		fullTerm = term + " ";
		try {
			String temp[] = fullTerm.split("\t");

			// Use the 5rd column as original code/reference:
			setInfoCode(temp[4]);

			// Use the 6th column for the matching:
			String[] t = simplifyTerm(temp[5]).split(" ");
			this.simplifiedTerm = "";

			// Resolve Synonyms:
			for (int a = 0; a < t.length; a++) {
				String newWord = t[a].trim();
				for (int b = 0; b < synonyms.size(); b++) {
					String[] s = synonyms.get(b).split(";");
					if (newWord.equalsIgnoreCase(synonyms.get(b).split(";")[0].trim())) {
						// System.out.println(newWord);
						newWord = synonyms.get(b).split(";")[1].trim();
						// System.out.println(newWord);
					}
				}

				// Insert spaces in upper case words:
				// if (newWord.toUpperCase().equals(newWord)) {
				// if (!newWord.equals("ENUMERATED") &&
				// !newWord.equals("INTEGER") && !newWord.equals("FLOAT")
				// && !newWord.equals("BOOLEAN") && !newWord.equals("STRING") &&
				// !newWord.equals("DATE")
				// && !newWord.equals("DATETIME")) {
				// newWord = newWord.replaceAll(".(?!$)", "$0 ");
				// }
				// }

				simplifiedTerm += " " + newWord.toUpperCase();
				simplifiedTerm = simplifiedTerm.trim();
			}

			simplifiedTerm = simplifiedTerm.replaceAll("[^A-Za-z0-9= ]", " ").replaceAll("\\s+", " ");

			if (simplifiedTerm.contains("=")) {
				String[] sp = simplifiedTerm.split("=");

				if (sp.length == 2) {
					simplifiedTerm = deDup(sp[0]) + " " + deDup(sp[1]);
				} else if (sp.length == 1) {
					simplifiedTerm = deDup(sp[0]);
				} else {
					simplifiedTerm = deDup(simplifiedTerm);
				}
			}

			simplifiedTerm = simplifiedTerm.replaceAll("[^A-Za-z0-9 ]", " ").replaceAll("\\s+", " ");

			String[] t2 = simplifiedTerm.split(" ");
			for (int a = 0; a < t2.length; a++) {
				Word nw = new Word(t2[a]);
				words.add(nw);
			}

		} catch (Exception e) {
			System.err.println("Error: Could not split '" + term + "'. Check file formats!");
			e.printStackTrace();
		}
	}

	// https://stackoverflow.com/a/6790786
	public static String deDup(String s) {
		return new LinkedHashSet<String>(Arrays.asList(s.split(" "))).toString().replaceAll("(^\\[|\\]$)", "")
				.replace(", ", " ").trim();
	}

	public MappingTerm getTermObject() {
		return this;
	}

	public String getWordString(int i) {
		return words.get(i).toString();
	}

	public String getGramWordString(int i, int gram) {
		String gramWordString = "";

		for (int a = 0; a < gram; a++) {
			gramWordString = gramWordString + words.get(i + a).toString() + " ";
		}

		return gramWordString.trim();
	}

	public int getWordCount() {
		return words.size();
	}

	public int getGramCount(int gram) {
		return words.size() - gram + 1;
	}

	public String getTermString() {
		return term;
	}

	public String getSimplifiedTermString() {
		return simplifiedTerm;
	}

	String getInfoCode() {
		return infoCode;
	}

	void setInfoCode(String infoCode) {
		this.infoCode = infoCode;
	}

	public void setWordStringWeight(int b, int frequency) {
		this.frequency = frequency;
	}

}
