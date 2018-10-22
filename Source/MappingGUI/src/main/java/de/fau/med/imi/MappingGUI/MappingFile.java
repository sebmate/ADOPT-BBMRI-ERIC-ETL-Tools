/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fau.med.imi.MappingGUI;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

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

        switch (c.length) {
            case 6:
                if (c[0].equals("SourceString") && c[1].equals("Score") && c[2].equals("Map (original, do not edit)")
                        && c[3].equals("Map") && c[4].equals("TargetString") && c[5].equals("Status")) {

                    System.out.println("File is a six-column mapping file.");
                    colCount = 6;
                }
                break;
            case 5:
                if (c[0].equals("SourceString") && c[1].equals("Score") && c[2].equals("Map (original, do not edit)")
                        && c[3].equals("Map") && c[4].equals("TargetString")) {

                    System.out.println("File is a five-column mapping file.");
                    colCount = 5;
                }
                break;
            case 4:
                if (c[0].equals("SourceString") && c[1].equals("Score") && c[2].equals("Map")
                        && c[3].equals("TargetString")) {
                    System.out.println("File is a four-column mapping file.");
                    colCount = 4;
                }
                break;
            default:
                System.out.println("Don't know how to handle a mapping file with " + c.length + " columns, exiting.");
                System.exit(0);
        }

        if (colCount > 0) {

            String SourceString = "", lastSourceString = "";
            String Score = "";
            String Map = "", OriginalMap = "";
            String TargetString = "";
            String OriginalMappingTerm = "";
            String Status = "0";

            ArrayList<Match> tempMatches = new ArrayList<>();
            ArrayList<String> maps = new ArrayList();

            for (int a = 1; a < lines.length; a++) {
                String[] columns = lines[a].split("\t");

                switch (columns.length) {
                    case 4:
                        SourceString = columns[0];
                        Score = columns[1];
                        Map = columns[2];
                        OriginalMap = columns[2];
                        TargetString = columns[3];
                        break;
                    case 5:
                        SourceString = columns[0];
                        Score = columns[1];
                        OriginalMap = columns[2];
                        Map = columns[3];
                        TargetString = columns[4];
                        break;
                    case 6:
                        SourceString = columns[0];
                        Score = columns[1];
                        OriginalMap = columns[2];
                        Map = columns[3];
                        TargetString = columns[4];
                        Status = columns[5];
                        break;
                    default:
                        break;
                }

                if (a > 0 && lastSourceString.equals("") && columns.length == colCount) {
                    lastSourceString = SourceString;
                }

                if (SourceString.equals(lastSourceString) && columns.length == colCount) { // Still at the same source data element
                }

                if ((!SourceString.equals(lastSourceString) && columns.length == colCount) || a == lines.length - 1) { // New source data element
                    
                      // Puts the data onto the "to process" stack (tempMatches):

                    if (tempMatches.size() > 0) {
                        
                        ArrayList<String> maps2 = new ArrayList<String>(maps.size());
                        for (String s : maps) {
                            maps2.add(new String(s));
                        }
                        ArrayList<Match> matches = new ArrayList<>(tempMatches);
                        Mapping mapping = new Mapping(lastSourceString, maps2, OriginalMappingTerm, matches, Integer.parseInt(Status));

                        OriginalMappingTerm = "";
                        maps.clear();

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

                // --------------------------------
                // Puts the data onto the "to process" stack (tempMatches):
                if (columns.length == colCount) {
                    if (Map.equals("X")) {
                        maps.add(TargetString);
                    }
                    if (OriginalMap.equals("X")) {
                        OriginalMappingTerm = TargetString;
                    }
                    Match ma = new Match(Score, Map, OriginalMap, TargetString);
                    tempMatches.add(ma);
                    //currScore = Score;
                    //System.out.println(SourceString + " - " + TargetString);
                }
                // --------------------------------
                if (columns.length == colCount) {
                    lastSourceString = SourceString;
                }
            }
        } else {
            System.out.println("Could not read the mapping file.");
        }

        return mappings;
    }

    void saveMappings(ArrayList<Mapping> mappings, String file, String site) {

        // Write normal mapping file:
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

                    if (mappings.get(a).getMappingTerms().contains(matches.get(b).getTargetString())) {
                        //if (mappings.get(a).getMappingTerm().equals(matches.get(b).getTargetString())) {
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

        // Write normal mapping file:
        // TODO: Handling when not using the CCDC file structure.
        file = "knownMappings\\" + site + ".known";

        System.out.println("Writing validated mappings to " + file);

        try {
            fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            bw.write("SourceString\tScore\tMap (original, do not edit)\tMap\tTargetString\tStatus\n");
            for (int a = 0; a < mappings.size(); a++) {
                bw.write("\n");
                ArrayList<Match> matches = mappings.get(a).getMatchings();
                for (int b = 0; b < matches.size(); b++) {
                    String mapEntry = "";
                    if (mappings.get(a).getMappingTerms().contains(matches.get(b).getTargetString()) && mappings.get(a).getMappingStatus() == 2) {
                        //if (mappings.get(a).getMappingTerm().equals(matches.get(b).getTargetString()) && mappings.get(a).getMappingStatus() == 2) {
                        mapEntry = "X";
                        bw.write(mappings.get(a).getSourceString() + "\t" + matches.get(b).getScore() + "\t" + matches.get(b).getOriginalMap() + "\t" + mapEntry + "\t" + matches.get(b).getTargetString() + "\t" + mappings.get(a).getMappingStatus() + "\n");
                    }
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
