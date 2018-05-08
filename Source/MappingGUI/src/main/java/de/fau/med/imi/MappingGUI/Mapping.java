/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fau.med.imi.MappingGUI;

import java.util.ArrayList;

/**
 *
 * @author matesn
 */
public class Mapping {

    private String SourceString = "";
    private String MappingTerm = "";
    private ArrayList<Match> Matchings = new ArrayList<>();
    private int mappingStatus = 0;
    private String OriginalMappingTerm = "";

    Mapping(String SourceString, String MappingTerm, ArrayList<Match> Matches) {
        this.SourceString = SourceString;
        this.MappingTerm = MappingTerm;
        this.Matchings = Matches;
    }

    Mapping(String SourceString, String MappingTerm, ArrayList<Match> matches, int mappingStatus) {
        this.SourceString = SourceString;
        this.MappingTerm = MappingTerm;
        this.Matchings = matches;
        this.mappingStatus = mappingStatus;
    }

    Mapping(String SourceString, String MappingTerm, String OriginalMappingTerm, ArrayList<Match> matches) {
        this.SourceString = SourceString;
        this.MappingTerm = MappingTerm;
        this.OriginalMappingTerm = OriginalMappingTerm;
        this.Matchings = matches;
        this.mappingStatus = mappingStatus;
    }

    /**
     * @return the SourceString
     */
    public String getSourceString() {
        return SourceString;
    }

    /**
     * @param SourceString the SourceString to set
     */
    public void setSourceString(String SourceString) {
        this.SourceString = SourceString;
    }

    /**
     * @return the MappingTerm
     */
    public String getMappingTerm() {
        return MappingTerm;
    }

    /**
     * @param MappingTerm the MappingTerm to set
     */
    public void setMappingTerm(String MappingTerm) {
        this.MappingTerm = MappingTerm;
    }

    /**
     * @return the Matchings
     */
    public ArrayList<Match> getMatchings() {
        return Matchings;
    }

    /**
     * @param Matchings the Matchings to set
     */
    public void setMatchings(ArrayList<Match> Matchings) {
        this.Matchings = Matchings;
    }

    /**
     * @return the mappingStatus
     */
    public int getMappingStatus() {
        return mappingStatus;
    }

    /**
     * @param mappingStatus the mappingStatus to set
     */
    public void setMappingStatus(int mappingStatus) {
        this.mappingStatus = mappingStatus;
    }

    /**
     * @return the OriginalMappingTerm
     */
    public String getOriginalMappingTerm() {
        return OriginalMappingTerm;
    }

    /**
     * @param OriginalMappingTerm the OriginalMappingTerm to set
     */
    public void setOriginalMappingTerm(String OriginalMappingTerm) {
        this.OriginalMappingTerm = OriginalMappingTerm;
    }

}
