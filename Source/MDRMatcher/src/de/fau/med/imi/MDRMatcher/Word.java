package de.fau.med.imi.MDRMatcher;

import java.io.Serializable;

public class Word  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String word = "";
	int weight = 0;

	public Word(String word) {
		this.word = word;
	}

	public String toString() {
		return word;
	}

}
