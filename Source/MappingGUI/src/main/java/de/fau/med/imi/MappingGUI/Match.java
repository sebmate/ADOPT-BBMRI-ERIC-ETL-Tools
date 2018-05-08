/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fau.med.imi.MappingGUI;

/**
 *
 * @author matesn
 */
public class Match {

    private String Score = "";
    private String Map = "";
    private String OriginalMap = "";
    private String TargetString = "";

    Match(String Score, String Map, String OriginalMap, String TargetString) {
        this.Score = Score;
        this.Map = Map;
        this.OriginalMap = OriginalMap;
        this.TargetString = TargetString;
    }

    /**
     * @return the Score
     */
    public String getScore() {
        return Score;
    }

    /**
     * @param Score the Score to set
     */
    public void setScore(String Score) {
        this.Score = Score;
    }

    /**
     * @return the Map
     */
    public String getMap() {
        return Map;
    }

    /**
     * @param Map the Map to set
     */
    public void setMap(String Map) {
        this.Map = Map;
    }

    /**
     * @return the TargetString
     */
    public String getTargetString() {
        return TargetString;
    }

    /**
     * @param TargetString the TargetString to set
     */
    public void setTargetString(String TargetString) {
        this.TargetString = TargetString;
    }

    /**
     * @return the OriginalMap
     */
    public String getOriginalMap() {
        return OriginalMap;
    }

    /**
     * @param OriginalMap the OriginalMap to set
     */
    public void setOriginalMap(String OriginalMap) {
        this.OriginalMap = OriginalMap;
    }

}
