package de.fau.med.imi.TablePreprocessor;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class TablePreprocessor {

	static int cntENUMERATED = 0;
	static int cntEnumeratedValues= 0;
	static int cntINTEGER = 0;
	static int cntFLOAT = 0;
	static int cntBOOLEAN = 0;
	static int cntSTRING = 0;
	static int cntDATE = 0;
	static int cntDATETIME = 0;

	public static void main(String[] args) {
		System.out.println("\nWelcome to the Table Preprocessor Utility 1.0!\n");

		// Parse command line options:

		Options options = new Options();

		Option inputFileOpt = new Option("i", "input", true, "the input TSV file");
		inputFileOpt.setRequired(true);
		options.addOption(inputFileOpt);

		Option outputMetaFileOpt = new Option("m", "metadata", true, "the output metadata TSV file");
		outputMetaFileOpt.setRequired(true);
		options.addOption(outputMetaFileOpt);

		Option outputEAVFileOpt = new Option("e", "eav", true, "the output EAV format TSV file");
		outputEAVFileOpt.setRequired(true);
		options.addOption(outputEAVFileOpt);

		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd;

		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			formatter.printHelp("TablePreprocessor", options);
			System.exit(1);
			return;
		}

		String InputFile = cmd.getOptionValue("input");
		String MetadataFile = cmd.getOptionValue("metadata");
		String EAVFile = cmd.getOptionValue("eav");

		try {

			PrintWriter dataWriter = new PrintWriter(EAVFile, "UTF-8");
			PrintWriter metaWriter = new PrintWriter(MetadataFile, "UTF-8");

			dataWriter.println("CASEID\tCONCEPT\tVALUE\tTIMESTAMP\tINSTANCE");
			metaWriter.println(
					"URN (do not edit)\tSOURCE Slot (do not edit)\tData Type\tPermitted Value\tMDR Path (do not edit)\tMatching Input (can be edited)");

			TableHelper th = new TableHelper(InputFile);

			int columns = th.getColumnCount(0);
			int lines = th.getLineCount();

			System.out.println("Found " + columns + " columns in file:\n");

			for (int c = 0; c < columns; c++) {

				String cName = th.getCell(0, c);
				String cType = th.getCell(1, c).toUpperCase();

				if (c == 0) {
					System.out.println(" - Treating the first column (" + cName + ")  as column with patient ids.");
				} else {

					System.out.println(" - " + cName + " = " + cType);

					if (cType.equals("ENUMERATED"))
						cntENUMERATED++;
					if (cType.equals("INTEGER"))
						cntINTEGER++;
					if (cType.equals("FLOAT"))
						cntFLOAT++;
					if (cType.equals("BOOLEAN"))
						cntBOOLEAN++;
					if (cType.equals("STRING"))
						cntSTRING++;
					if (cType.equals("DATE"))
						cntDATE++;
					if (cType.equals("DATETIME"))
						cntDATETIME++;

					if (!cType.equals("ENUMERATED") && !cType.equals("INTEGER") && !cType.equals("FLOAT")
							&& !cType.equals("BOOLEAN") && !cType.equals("STRING") && !cType.equals("DATE")
							&& !cType.equals("DATETIME")) {
						System.out.println("\n   ERROR: Don't know how to handle datatype \"" + cType + "\"\n");

					} else {

						// Get the metadata:

						if (cType.equals("ENUMERATED")) {

							HashSet<String> distValues = th.getAggregate(c, 2, lines);
							Iterator<String> itr = distValues.iterator();

							String alreadyContains = "";

							while (itr.hasNext()) {
								String value = itr.next().trim();

								if (!value.equals("")) {

									if (value.contains(";")) {
										String[] values = value.split(";");

										for (int a = 0; a < values.length; a++) {

											String entry = "\t" + cName + "\t" + cType + "\t" + values[a].trim() + "\t"
													+ cName + " = " + values[a].trim() + "\t" + cName + " = "
													+ values[a].trim();

											if (!alreadyContains.contains("#" + values[a].trim() + "#")) {
												metaWriter.println(entry);
												System.out.println("   - " + values[a].trim());
												alreadyContains += "#" + values[a].trim() + "#";
												cntEnumeratedValues++;
											}

										}

									} else {
										String entry = "\t" + cName + "\t" + cType + "\t" + value + "\t" + cName + " = "
												+ value + "\t" + cName + " = " + value;
										if (!alreadyContains.contains("#" + value + "#")) {
											metaWriter.println(entry);
											System.out.println("   - " + value);
											alreadyContains += "#" + value + "#";
											cntEnumeratedValues++;
										}
									}
								}
							}
						} else if (cType.equals("BOOLEAN")) {
							metaWriter.println("\t" + cName + "\t" + cType + "\t" + "TRUE" + "\t" + cName + " = "
									+ "TRUE" + "\t" + cName + " = " + "TRUE");
							metaWriter.println("\t" + cName + "\t" + cType + "\t" + "FALSE" + "\t" + cName + " = "
									+ "FALSE" + "\t" + cName + " = " + "FALSE");
						} else {
							metaWriter.println("\t" + cName + "\t" + cType + "\t" + "" + "\t" + cName + " = " + cType
									+ "\t" + cName + " = " + cType);
						}

						// Rotate the data:

						for (int l = 2; l < lines; l++) {
							String Patient = th.getCell(l, 0).trim();
							String Concept = th.getCell(0, c).trim();
							String Value = th.getCell(l, c).trim();

							if (!Patient.equals("") && !Concept.equals("") && !Value.equals("")) {

								if (Value.contains(";")) {
									String[] values = Value.split(";");

									for (int a = 0; a < values.length; a++) {
										dataWriter.println(Patient + "\t" + Concept + "\t" + values[a].trim()
												+ "\tNULL\t" + (a + 1));

									}

								} else {
									dataWriter.println(Patient + "\t" + Concept + "\t" + Value + "\tNULL\t1");
								}
							}
						}
					}
				}
			}

			dataWriter.close();
			metaWriter.close();

			System.out.println("");
			System.out.println("Done. Processed " + columns + " columns:\n");

			System.out.println("Number of type ENUMERATED: " + cntENUMERATED + " with " + cntEnumeratedValues + " distinct values");
			System.out.println("Number of type INTEGER: " + cntINTEGER);
			System.out.println("Number of type FLOAT: " + cntFLOAT);
			System.out.println("Number of type BOOLEAN: " + cntBOOLEAN);
			System.out.println("Number of type STRING: " + cntSTRING);
			System.out.println("Number of type DATE: " + cntDATE);
			System.out.println("Number of type DATETIME: " + cntDATETIME);

		} catch (

		FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
