package de.fau.med.imi.TablePreprocessor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TableHelper {

	List<String> lines = new ArrayList<String>();

	public TableHelper(String file) {

		try {
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				lines.add(line);
			}
			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getLine(int i) {
		if (i > lines.size())
			return null;
		return (lines.get(i));
	}

	public int getLineCount() {
		return lines.size();
	}

	public int getColumnCount(int i) {
		String[] l = getLine(i).split("\t");
		return l.length;
	}

	public String getCell(int line, int column) {
		String[] l = getLine(line).split("\t");

		if (column < l.length) {
			return l[column];
		} else {
			return "";
		}
	}

	public HashSet<String> getAggregate(int column, int fromLine, int toLine) {

		HashSet<String> set = new HashSet<String>();

		for (int line = fromLine; line < toLine; line++) {
			String cellEntry = getCell(line, column);
			set.add(cellEntry);
		}

		return set;

	}

}
