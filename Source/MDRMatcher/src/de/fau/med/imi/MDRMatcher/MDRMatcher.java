package de.fau.med.imi.MDRMatcher;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class MDRMatcher {

	private static int dataElementsCnt;
	private static String mappingLog = "";
	private static String knownWords;

	// https://stackoverflow.com/a/22186845
	public static double round(double value, int precision) {
		int scale = (int) Math.pow(10, precision);
		return (double) Math.round(value * scale) / scale;
	}

	public static void main(String[] args) {

		System.out.println("\nWelcome to the Samply MDR Matcher Utility 1.0!\n");

		// Parse command line options:

		Options options = new Options();

		Option optNamespace1 = new Option("s", "source", true, "the source namespace TSV file");
		optNamespace1.setRequired(true);
		options.addOption(optNamespace1);

		Option optNamespace2 = new Option("t", "target", true, "the target namespace TSV file");
		optNamespace2.setRequired(true);
		options.addOption(optNamespace2);
		
		Option optOutput = new Option("o", "output", true, "the output mapping file");
		optOutput.setRequired(true);
		options.addOption(optOutput);


		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd;

		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			formatter.printHelp("MDRExtractor", options);
			System.exit(1);
			return;
		}

		String sourceNamespaceFile = cmd.getOptionValue("source");
		String targetNamespaceFile = cmd.getOptionValue("target");
		String outputMappingFile = cmd.getOptionValue("output");

		MappingContentFile source = new MappingContentFile(sourceNamespaceFile);
		MappingContentFile target = new MappingContentFile(targetNamespaceFile);

		// https://raw.githubusercontent.com/first20hours/google-10000-english/master/google-10000-english-usa.txt
		try (BufferedReader br = new BufferedReader(new FileReader("Ressources/Top10000.txt"))) {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			knownWords = sb.toString().toUpperCase();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// also add synonyms to known words:
		try (BufferedReader br = new BufferedReader(new FileReader("Ressources/Synonyms.txt"))) {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			knownWords += " "
					+ sb.toString().replaceAll(";", " ").replaceAll("\n", " ").replaceAll("\r", " ").toUpperCase()
					+ " ";
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Matching \"" + sourceNamespaceFile + "\" to \"" + targetNamespaceFile
				+ "\". This will take a while (program may look stuck).");

		System.out.println("Will match " + (source.getTermsCount() -1) + " source items to " + (target.getTermsCount() -1)
				+ " target items.\n");

		String result = "SourceString\tScore\tMap\tTargetString\n\n";

		int cntMatchQuality0 = 0;
		int cntMatchQuality1 = 0;
		int cntMatchQuality2 = 0;
		int cntMatchQuality3 = 0;

		for (int a = 1; a < source.getTermsCount(); a++) {
			ArrayList<MatchResult> matchResults = new ArrayList<MatchResult>();

			for (int b = 0; b < target.getTermsCount(); b++) {
				BagOfWordsMatcher matcher = new BagOfWordsMatcher(source.getTerm(a), target.getTerm(b), knownWords);

				MatchResult matchResult = matcher.match();
				if (matchResult.getMatchScore() > 0) {
					matchResults.add(matchResult);
				}

			}

			Collections.sort(matchResults, new MatchResultsComparator());

			double matchScore = 0;
			int lastMatchScore = 0;
			if (matchResults.size() == 0) {
				cntMatchQuality0++;
			}

			if (matchResults.size() == 0) {
				result += source.getTerm(a).getInfoCode() + "\t" + 0 + "\t\t" + "NO MATCH FOUND!" + "\n";
			} else {

				mappingLog += "\n############################################################################################################################################################\n\n";

				double highestScore = 0;
				for (int c = 0; c < matchResults.size() && c <= 200; c++) {

				
					if (c == 0 && matchResults.get(c).getMatchScore() >= 30) {
						cntMatchQuality3++;
					} else if (c == 0 && matchResults.get(c).getMatchScore() >= 20) {
						cntMatchQuality2++;
					} else if (c == 0 && matchResults.get(c).getMatchScore() >= 10) {
						cntMatchQuality1++;
					} else if (c == 0 && matchResults.get(c).getMatchScore() < 10) {
						cntMatchQuality0++;
					}

					String sources = matchResults.get(c).getTerm1().getInfoCode();
					String targets = matchResults.get(c).getTerm2().getInfoCode();
					matchScore = matchResults.get(c).getMatchScore();

					String doMap = "";

					if (c == 0 && matchResults.size() > 1) {
						if (matchResults.get(c).getMatchScore() > matchResults.get(c + 1).getMatchScore()
								&& !sources.contains("FALSE")) {
							doMap = "X";
						}
						highestScore = matchScore;
					}

					if (matchScore > highestScore * 0.33) {
						mappingLog += "\n------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n";

						result += sources + "\t" + round(matchScore, 2) + "\t" + doMap + "\t" + targets + "\n";

						mappingLog += sources + "\t" + round(matchScore, 2) + "\t" + doMap + "\t" + targets + "\n";
						mappingLog += "\n" + matchResults.get(c).getMatchLog();
					}
				}
			}
			result += "\n";

			dataElementsCnt++;
			System.out.print(".");
			if (dataElementsCnt % 10 == 0)
				System.out.println(" " + dataElementsCnt + " items");

		}

		System.out.println("\nDone! Estimated probabilities for data elements to have matches:");
		System.out
				.println("    None (0-9): " + (int) ((double) cntMatchQuality0 / source.getTermsCount() * 100.0) + "%");
		System.out
				.println("   Low (10-19): " + (int) ((double) cntMatchQuality1 / source.getTermsCount() * 100.0) + "%");
		System.out
				.println("Medium (20-29): " + (int) ((double) cntMatchQuality2 / source.getTermsCount() * 100.0) + "%");
		System.out
				.println("  High (>= 30): " + (int) ((double) cntMatchQuality3 / source.getTermsCount() * 100.0) + "%");

		// Write result to file:

		PrintWriter writer2;
		try {
			writer2 = new PrintWriter(outputMappingFile, "UTF-8");
			writer2.println(result.substring(0, result.length() - 1));
			writer2.close();

			System.out.println(
					"\nA mapping file \"" + outputMappingFile + "\" has been created.");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			PrintWriter writer3 = new PrintWriter(outputMappingFile + ".log", "UTF-8");
			writer3.print(mappingLog);
			writer3.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}
}
