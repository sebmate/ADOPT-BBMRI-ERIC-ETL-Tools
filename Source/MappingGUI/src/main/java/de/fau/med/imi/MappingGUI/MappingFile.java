/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fau.med.imi.MappingGUI;

import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.script.ScriptEngine.FILENAME;
import org.apache.commons.lang3.SerializationUtils;

/**
 *
 * @author matesn
 */
class MappingFile {

    String fileContents = "";
    private String currScore;

    MappingFile(String file) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            sb.append("\t\t\t");
            sb.append(System.lineSeparator());
            fileContents = sb.toString();
            //System.out.println(fileContents);
            br.close();
        } catch (Exception ex) {
            Logger.getLogger(MappingFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    ArrayList<Mapping> getMappings() {

        ArrayList<Mapping> mappings = new ArrayList<>();

        String[] lines = fileContents.split(System.lineSeparator());

        String[] c = lines[0].split("\t");

        int colCount = 0;

        if (c.length == 6) {
            if (c[0].equals("SourceString") && c[1].equals("Score") && c[2].equals("Map (original, do not edit)")
                    && c[3].equals("Map") && c[4].equals("TargetString") && c[5].equals("Status")) {

                System.out.println("File is a six-column mapping file.");
                colCount = 6;
            }
        }
        if (c.length == 5) {
            if (c[0].equals("SourceString") && c[1].equals("Score") && c[2].equals("Map (original, do not edit)")
                    && c[3].equals("Map") && c[4].equals("TargetString")) {

                System.out.println("File is a five-column mapping file.");
                colCount = 5;
            }
        }
        if (c.length == 4) {
            if (c[0].equals("SourceString") && c[1].equals("Score") && c[2].equals("Map")
                    && c[3].equals("TargetString")) {
                System.out.println("File is a four-column mapping file.");
                colCount = 4;
            }
        }

        if (colCount > 0) {

            String SourceString = "";
            String Score = "";
            String Map = "", OriginalMap = "";
            String TargetString = "";
            String MappingTerm = "", OriginalMappingTerm = "";
            String Status = "";

            ArrayList<Match> tempMatches = new ArrayList<>();

            for (int a = 1; a < lines.length; a++) {
                String[] columns = lines[a].split("\t");

                //System.out.println(columns.length + ": " + lines[a]);
                if (columns.length == colCount) {

                    if (colCount == 4) {
                        SourceString = columns[0];
                        Score = columns[1];
                        Map = columns[2];
                        TargetString = columns[3];
                    }
                    if (colCount == 5) {
                        SourceString = columns[0];
                        Score = columns[1];
                        OriginalMap = columns[2];
                        Map = columns[3];
                        TargetString = columns[4];
                    }
                    if (colCount == 6) {
                        SourceString = columns[0];
                        Score = columns[1];
                        OriginalMap = columns[2];
                        Map = columns[3];
                        TargetString = columns[4];
                        Status = columns[5];
                    }

                    if (Map.equals("X")) {
                        MappingTerm = TargetString;
                        OriginalMappingTerm = TargetString;
                    }

                    Match m = new Match(Score, Map, OriginalMap, TargetString);

                    tempMatches.add(m);

                    currScore = Score;

                } else if (tempMatches.size() > 0) { // a blank line was found (= next source term), process previous entries:

                    ArrayList<Match> matches = new ArrayList<>(tempMatches);
                    Mapping mapping = new Mapping(SourceString, MappingTerm, OriginalMappingTerm, matches);
                    MappingTerm = "";
                    OriginalMappingTerm = "";

                    //System.out.println("Adding " + matches.size() + " matches to " + SourceString);
                    if (matches.size() > 0) {

                        int mapPos = -1;
                        for (int m = 0; m < matches.size(); m++) {
                            if (matches.get(m).getMap().equals("X")) {
                                mapPos = m;
                            }
                        }

                        int mapPosOrig = -1;
                        for (int m = 0; m < matches.size(); m++) {
                            if (matches.get(m).getOriginalMap().equals("X")) {
                                mapPosOrig = m;
                            }
                        }

                        if (colCount == 4) {
                            //if (mapPos == -1) {
                            mapping.setMappingStatus(0);
                            //}
                            if (mapPos == 0) {
                                mapping.setMappingStatus(1);
                            }
                            if (mapPos > 0) {
                                mapping.setMappingStatus(2);
                            }

                        }
                        if (colCount == 5) {
                            //if (mapPos == -1 && mapPosOrig == -1) {
                            mapping.setMappingStatus(0);
                            //}
                            if (mapPos == 0) {
                                mapping.setMappingStatus(1);
                            }
                            if (mapPos > 0 || (mapPos == 0 && mapPosOrig == -1)) {
                                mapping.setMappingStatus(2);
                            }
                        }

                        if (colCount == 6) {
                            //if (mapPos == -1 && mapPosOrig == -1) {
                            mapping.setMappingStatus(0);
                            //}
                            if (mapPos == 0) {
                                mapping.setMappingStatus(1);
                            }
                            if (mapPos > 0 || (mapPos == 0 && mapPosOrig == -1)) {
                                mapping.setMappingStatus(2);
                            }
                            mapping.setMappingStatus(Integer.parseInt(Status));
                        }

                    }

                    mappings.add(mapping);
                    tempMatches.clear();
                }
            }
        } else {
            System.out.println("Could not read the mapping file.");
        }

        return mappings;
    }

    void saveMappings(ArrayList<Mapping> mappings, String file) {

        System.out.println("Writing data to " + file);

        BufferedWriter bw = null;
        FileWriter fw = null;

        try {

            fw = new FileWriter(file);
            bw = new BufferedWriter(fw);

            bw.write("SourceString\tScore\tMap (original, do not edit)\tMap\tTargetString\tStatus\n");

            for (int a = 0; a < mappings.size(); a++) {

                bw.write("\n");

                ArrayList<Match> matches = mappings.get(a).getMatchings();

                for (int b = 0; b < matches.size(); b++) {

                    String mapEntry = "";

                    if (mappings.get(a).getMappingTerm().equals(matches.get(b).getTargetString())) {
                        mapEntry = "X";
                    }

                    bw.write(mappings.get(a).getSourceString() + "\t" + matches.get(b).getScore() + "\t" + matches.get(b).getOriginalMap() + "\t" + mapEntry + "\t" + matches.get(b).getTargetString() + "\t" + mappings.get(a).getMappingStatus() + "\n");
                }
            }

            System.out.println("Done");

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            try {

                if (bw != null) {
                    bw.close();
                }

                if (fw != null) {
                    fw.close();
                }

            } catch (Exception ex) {

                ex.printStackTrace();

            }

        }
    }
}
