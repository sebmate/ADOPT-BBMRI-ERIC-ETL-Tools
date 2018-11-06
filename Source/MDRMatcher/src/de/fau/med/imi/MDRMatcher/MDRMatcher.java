package de.fau.med.imi.MDRMatcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.joda.time.Duration;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.eclipse.collections.api.bag.Bag;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;

class MatchingThread extends Thread {

	private MetadataDefinition source;
	private MetadataDefinition target;
	private String knownWords = "";
	private String mappingLog = "";
	private int dataElementsCnt;
	private int subMatch = 0;
	private int previouslyDone;

	private String threadName = "";
	private boolean pleaseHalt;
	private double meanFrequency = 0;

	private Map<String, Long> sortedWordFrequencies = new HashMap<String, Long>();
	private Map<Integer, Double> freqPosition = new HashMap<Integer, Double>();

	// private ArrayList<ArrayList<MatchResult>> threadMatches = new
	// ArrayList<ArrayList<MatchResult>>();

	MatchingThread(MetadataDefinition source, MetadataDefinition target, String knownWords, String threadName) {
		this.source = source;
		this.target = target;
		this.threadName = threadName;

		sortedWordFrequencies = target.getSortedWordFrequencies();
		freqPosition = new HashMap<Integer, Double>(); // <How often the word occurs> <weight 0...1 which is
														// rare...frequent>

		this.meanFrequency = 0;
		int wordCnt = 0;
		int aggOcc = 0;
		double percentage = 0;
		long numOccurrances = 1, lastNumOccurrances = -1;

		long totalSize = 0;
		for (Entry<String, Long> entry : sortedWordFrequencies.entrySet()) {
			totalSize += entry.getValue().longValue();
		}

		for (Entry<String, Long> entry : sortedWordFrequencies.entrySet()) {

			numOccurrances = entry.getValue().longValue();
			aggOcc += numOccurrances;
			percentage = (-((double) aggOcc / (double) totalSize) + 1.0) * 100.0 + 1.0;

			if (numOccurrances != lastNumOccurrances) {
				freqPosition.put((int) numOccurrances, percentage);
				// System.out.println(numOccurrances + ": " + percentage);
			}
			// System.out.println(wordCnt + ": " + entry.getKey() + ": " + numOccurrances +
			// "x");
			wordCnt++;
			lastNumOccurrances = numOccurrances;
		}

		// meanFrequency = totFrequency / wordCnt;
		// System.out.println(meanFrequency);
		// System.exit(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {

		// for (int a = 0; a < source.getTermsCount(); a++) {
		// System.out.println(source.getTerm(a).getSimplifiedTermString());
		// }

		/*
		 * System.out.println("Thread " + threadName +
		 * ": Loading complete target terminology."); String completeTargetTerminology =
		 * ""; int size = target.getTermsCount(); for (int a = 0; a < size; a++) {
		 * //completeTargetTerminology += target.getTerm(a).getSimplifiedTermString() +
		 * " ; "; } System.out.println("Thread " + threadName +
		 * ": Done loading complete target terminology.");
		 */

		for (int a = 0; a < source.getTermsCount(); a++) {

			ArrayList<MatchResult> matchResults = new ArrayList<MatchResult>();
			String sourceTerm = source.getTerm(a).getSimplifiedTermString();

			if (sourceTerm != null) {
				String cachefile = DigestUtils.md5Hex(source.getTerm(a).getInfoCode()) + ".obj";
				File f = new File("cache/" + cachefile);

				if (!f.exists()) {

					double maxScore = 0;

					for (int b = 0; b < target.getTermsCount(); b++) {
						subMatch = b;

						BagOfWordsMatcher matcher = new BagOfWordsMatcher(source.getTerm(a), target.getTerm(b),
								knownWords, sortedWordFrequencies, freqPosition);

						// System.out.println(source.getTerm(a).getInfoCode() + " <=> " +
						// target.getTerm(b).getInfoCode());
						MatchResult matchResult = matcher.match();

						double score = matchResult.getMatchScore();
						if (score > maxScore)
							maxScore = score;
						if (score >= maxScore * .11) {
							// matchResult.setMatchLog("");
							matchResults.add(matchResult);
						}

						if (pleaseHalt) {
							return;
						}
					}

					Collections.sort(matchResults, new MatchResultsComparator());

					for (int c = matchResults.size() - 1; c >= 0; c--) {
						if (matchResults.get(c).getMatchScore() < maxScore * 0.11) {
							matchResults.remove(c);
						}
					}

					try {
						FileOutputStream fileOut = new FileOutputStream("cache/" + cachefile);
						ObjectOutputStream out = new ObjectOutputStream(fileOut);
						out.writeObject(matchResults);
						out.close();
						fileOut.close();
					} catch (IOException i2) {
						i2.printStackTrace();
					}
				} else {

					previouslyDone++;
				}

				// threadMatches.add(matchResults);
				dataElementsCnt++;
				subMatch = target.getTermsCount();
				matchResults = null;
			}
			subMatch = -1;
		}

	}

	int getTodo() {
		return source.getTermsCount();
	}

	int getDone() {
		return dataElementsCnt;
	}

	public double getDoneFract() {
		return (double) dataElementsCnt + ((double) subMatch / (double) target.getTermsCount());
	}

	// ArrayList<ArrayList<MatchResult>> getThreadMatches() {
	// return threadMatches;
	// }

	public String getSubMatch() {
		if (subMatch > 0) {
			return (int) (((double) subMatch / (double) target.getTermsCount()) * 100.0) + "%";
		} else if (subMatch == 0) {
			return "0%";
		} else {
			return "---";
		}
	}

	public void requestStop() {
		pleaseHalt = true;
	}

	int getPreviouslyDone() {
		return previouslyDone;
	}

	void setPreviouslyDone(int previouslyDone) {
		this.previouslyDone = previouslyDone;
	}
}

public class MDRMatcher {

	private static int dataElementsCnt;
	private static String mappingLog = "";
	private static String knownWords;

	private static int cntMatchQuality0 = 0;
	private static int cntMatchQuality1 = 0;
	private static int cntMatchQuality2 = 0;
	private static int cntMatchQuality3 = 0;

	public static void main(String[] args) {

		/*
		 * MutableList<String> l = Lists.mutable.empty(); l.add("ciao"); l.add("ciao");
		 * l.add("ciao"); Bag<String> words = l.toBag();
		 * System.out.println(words.occurrencesOf("ciao")); System.exit(0);
		 */

		System.out.println("\nWelcome to the Samply MDR Matcher 2.0!\n");

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

		final String sourceNamespaceFile = cmd.getOptionValue("source");
		final String targetNamespaceFile = cmd.getOptionValue("target");
		final String outputMappingFile = cmd.getOptionValue("output");

		System.out.print("Loading source terminology and processing synonyms ...");
		MetadataDefinition sourceTemp = new MetadataDefinition();

		File f = new File("cache/sourceterms.obj");
		if (f.exists() && !f.isDirectory()) {
			System.out.println(" using cache ...");
			try {
				FileInputStream fileIn = new FileInputStream("cache/sourceterms.obj");
				ObjectInputStream in = new ObjectInputStream(fileIn);
				sourceTemp = (MetadataDefinition) in.readObject();
				in.close();
				fileIn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println(" processing from file ...");
			sourceTemp = new MetadataDefinition(sourceNamespaceFile);
			try {
				FileOutputStream fileOut = new FileOutputStream("cache/sourceterms.obj");
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(sourceTemp);
				out.close();
				fileOut.close();
			} catch (IOException i2) {
				i2.printStackTrace();
			}
		}

		final MetadataDefinition source = sourceTemp;

		System.out.print("Loading target terminology and processing synonyms ...");

		MetadataDefinition targetTemp = new MetadataDefinition();
		File f2 = new File("cache/targetterms.obj");
		if (f2.exists() && !f2.isDirectory()) {
			System.out.println(" using cache ...");
			try {
				FileInputStream fileIn = new FileInputStream("cache/sourceterms.obj");
				ObjectInputStream in = new ObjectInputStream(fileIn);
				targetTemp = (MetadataDefinition) in.readObject();
				in.close();
				fileIn.close();
			} catch (Exception i2) {
				i2.printStackTrace();

			}
		} else {
			System.out.println(" processing from file ...");
			targetTemp = new MetadataDefinition(targetNamespaceFile);
			try {
				FileOutputStream fileOut = new FileOutputStream("cache/targetterms.obj");
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(targetTemp);
				out.close();
				fileOut.close();
			} catch (IOException i2) {
				i2.printStackTrace();
			}
		}

		final MetadataDefinition target = targetTemp;

		System.out.println("Loading Ressources/Top10000.txt to treat them as known words ...");
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

		System.out.println("Loading Ressources/Synonyms.txt to treat them as known words ...");
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

		System.out.println("\nMatching \"" + sourceNamespaceFile + "\" to \"" + targetNamespaceFile
				+ "\". This will take a while ...");

		System.out.println("Will match " + (source.getTermsCount()) + " source items to " + (target.getTermsCount())
				+ " target items.\n");

		new Thread(new Runnable() {
			private boolean someThreadIsRunning;
			private boolean gotData = true;

			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				List<MatchingThread> threads = new ArrayList<>();

				int cores = Runtime.getRuntime().availableProcessors();
				int numThreads = cores;
				System.out.println("Multi-threading: Using " + cores + " CPU cores.\n");

				for (int th = 0; th < numThreads; th++) {
					MetadataDefinition partialSource = source.getPartial(th, numThreads);

					// System.out.println(partialSource.getMappingTerms().size());
					// System.exit(0);

					MatchingThread t = new MatchingThread(partialSource, target, knownWords, "" + th);
					threads.add(t);
					t.start();
				}

				double startTime = System.currentTimeMillis() / 1000.0;

				someThreadIsRunning = true;

				int totalTodo = 0;
				double totalDoneFract = 0;
				int totalTodo2 = 0;
				double totalDoneFract2 = 0;
				int totalDone = 0;
				String processSummary = "Nothing finished yet.";

				NonblockingBufferedReader reader = new NonblockingBufferedReader(System.in);
				String line = null;

				try {

					while (someThreadIsRunning) {

						if ((line = reader.readLine()) != null) {
							System.out.println("Keystroke detected, gracefully stopping threads ...\n");
							for (MatchingThread thr : threads) {
								thr.requestStop();
							}
							// System.exit(0);
						}

						totalTodo = 0;
						totalDoneFract = 0;
						totalTodo2 = 0;
						totalDoneFract2 = 0;
						totalDone = 0;

						String subProcess = "";

						someThreadIsRunning = false;
						for (MatchingThread thr : threads) {
							someThreadIsRunning = thr.isAlive() || someThreadIsRunning;
							totalDoneFract += thr.getDoneFract() - thr.getPreviouslyDone();
							;
							totalDone += thr.getDone();
							totalTodo += thr.getTodo() - thr.getPreviouslyDone();
							;
							totalDoneFract2 += thr.getDoneFract();
							totalTodo2 += thr.getTodo();
							subProcess += thr.getSubMatch() + " ";
						}
						subProcess = "Threads: " + subProcess.trim() + ". ";

						int percCompleted = (int) ((double) totalDoneFract2 / (double) totalTodo2 * 100.0);
						double runningTime = (System.currentTimeMillis() / 1000.0) - startTime;

						double donePerSec = totalDoneFract / runningTime;
						double remainingSecs = (totalTodo - totalDoneFract) / donePerSec;

						Duration duration = new Duration((int) (remainingSecs * 1000.0)); // in milliseconds
						PeriodFormatter formatter = new PeriodFormatterBuilder().appendDays().appendSuffix("d")
								.appendHours().appendSuffix("h").appendMinutes().appendSuffix("m").appendSeconds()
								.appendSuffix("s").toFormatter();
						String formattedRemainingTime = formatter.print(duration.toPeriod());

						java.util.Date finishesTime = new java.util.Date(
								(long) (System.currentTimeMillis() + (remainingSecs * 1000)));

						String finishesTimeString = finishesTime + "";

						if (totalDoneFract == 0) {
							formattedRemainingTime = "unknown";
							finishesTimeString = "unknown";
						}

						if (formattedRemainingTime.equals(""))
							formattedRemainingTime = "0s";

						processSummary = "Total: " + percCompleted + "% done, " + totalDone + "/" + totalTodo
								+ " source items matched. Time remaining: " + formattedRemainingTime + ", finishes: "
								+ finishesTimeString;

						System.out.println(subProcess + processSummary);

						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					System.out.println("\nMatching done. Compiling results into mapping file ...\n");

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				reader.close();

				Boolean abort = false;

				try {

					PrintWriter writer2 = new PrintWriter(outputMappingFile, "Cp1252");
					PrintWriter writer3 = new PrintWriter(outputMappingFile + ".log", "Cp1252");

					writer2.print("SourceString\tScore\tMap\tTargetString\n\n");

					for (int a = 0; a < source.getTermsCount(); a++) {

						if (!abort) {

							// try {
							// if ((line = reader1.readLine()) != null) {
							// System.out.println("Keystroke detected, finishing ...\n");
							// abort = true;
							// }
							// } catch (IOException e) {
							// // TODO Auto-generated catch block
							// e.printStackTrace();
							// }

							String sourceTerm = source.getTerm(a).getInfoCode();
							if (sourceTerm != null) {
								String cachefile = DigestUtils.md5Hex(sourceTerm) + ".obj";
								File f = new File("cache/" + cachefile);
								if (f.exists() && !f.isDirectory()) {

									System.out.print("Processing " + (a + 1) + "/" + source.getTermsCount() + ": cache/"
											+ cachefile + " ");

									try {

										FileInputStream fileIn = new FileInputStream("cache/" + cachefile);
										ObjectInputStream in = new ObjectInputStream(fileIn);
										ArrayList<MatchResult> matchResults = new ArrayList<MatchResult>();
										System.out.print(".");
										matchResults = (ArrayList<MatchResult>) in.readObject();
										in.close();
										fileIn.close();
										System.out.print(".");

										double matchScore = 0;

										if (matchResults.size() == 0) {
											cntMatchQuality0++;
											writer3.print(matchResults.get(0).getTerm1().getInfoCode() + "\t" + 0
													+ "\t\t" + "NO MATCH FOUND!" + "\n");

										} else {

											writer3.print(
													"\n############################################################################################################################################################\n\n");

											double cutOff = 0;
											System.out.print(".");

											int matchResultsSize = matchResults.size();
											// System.out.print(matchResultsSize);
											boolean cont = true;

											for (int c = 0; c < matchResultsSize && c <= 200; c++) {

												/*
												 * if (c == 0 && matchResults.get(c).getMatchScore() >= 30) {
												 * cntMatchQuality3++; } else if (c == 0 &&
												 * matchResults.get(c).getMatchScore() >= 20) { cntMatchQuality2++; }
												 * else if (c == 0 && matchResults.get(c).getMatchScore() >= 10) {
												 * cntMatchQuality1++; } else if (c == 0 &&
												 * matchResults.get(c).getMatchScore() < 10) { cntMatchQuality0++; }
												 */

												if (cont) {

													MatchResult o = matchResults.get(c);
													String sources = o.getTerm1().getInfoCode();
													String targets = o.getTerm2().getInfoCode();
													matchScore = o.getMatchScore();
													String doMap = "";

													if (c == 0 && matchResultsSize > 1) {
														if (o.getMatchScore() > matchResults.get(c + 1).getMatchScore()
																&& !sources.contains("FALSE")) {
															doMap = "X";
														}
														cutOff = matchScore * .11;
													}

													if (matchScore > cutOff) {

														writer2.print(sources + "\t" + round(matchScore, 2) + "\t"
																+ doMap + "\t" + targets + "\n");

														writer3.print(
																"\n------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n");
														writer3.print(sources + "\t" + round(matchScore, 2) + "\t"
																+ doMap + "\t" + targets + "\n");
														writer3.print("\n" + o.getMatchLog());

													} else {
														cont = false;
													}

												}

											}

											System.out.println(".");
										}
										writer2.print("\n");

									} catch (IOException i) {
										i.printStackTrace();
										return;
									} catch (ClassNotFoundException c) {
										c.printStackTrace();
										return;
									}
								}
							}
						}
					}

					writer2.close();
					writer3.close();

				} catch (Exception e) {
					e.printStackTrace();
				}

				System.out.println("\nDone!");

				/*
				 * System.out.
				 * println("\nDone! Estimated probabilities for data elements to have matches:"
				 * ); System.out.println( "  None (00-09): " + (int) ((double) cntMatchQuality0
				 * / source.getTermsCount() * 100.0) + "%"); System.out.println(
				 * "   Low (10-19): " + (int) ((double) cntMatchQuality1 /
				 * source.getTermsCount() * 100.0) + "%"); System.out.println(
				 * "Medium (20-29): " + (int) ((double) cntMatchQuality2 /
				 * source.getTermsCount() * 100.0) + "%"); System.out.println(
				 * "  High (>= 30): " + (int) ((double) cntMatchQuality3 /
				 * source.getTermsCount() * 100.0) + "%");
				 */

			}

			// https://stackoverflow.com/a/22186845
			public double round(double value, int precision) {
				int scale = (int) Math.pow(10, precision);
				return (double) Math.round(value * scale) / scale;
			}

		}).start();
	}
}
