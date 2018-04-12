package de.fau.med.imi.MDRMatcher;


public class MatchResult {

	private MappingTerm term1;
	private MappingTerm term2;
	private double matchScore = 0;
	private String matchLog = "";
	
	public MatchResult(MappingTerm term1, MappingTerm term2, double matchScore2, String matchLog) {
		this.setTerm1(term1);
		this.setTerm2(term2);
		this.setMatchScore(matchScore2);
		this.setMatchLog(matchLog);
	}

	double getMatchScore() {
		return matchScore;
	}

	void setMatchScore(double matchScore2) {
		this.matchScore = matchScore2;
	}

	MappingTerm getTerm2() {
		return term2;
	}

	void setTerm2(MappingTerm term) {
		this.term2 = term;
	}

	MappingTerm getTerm1() {
		return term1;
	}

	void setTerm1(MappingTerm term) {
		this.term1 = term;
	}

	public String toString() {
		//return matchScore + ": " + term1 + " -- " + term2;
		return matchScore + ": "+ term2;

	}
	
	public String getInfoCode() {
		//return matchScore + ": " + term1 + " -- " + term2;
		return matchScore + ": "+ term2;

	}

	public String getMatchLog() {
		return matchLog;
	}

	public void setMatchLog(String matchLog) {
		this.matchLog = matchLog;
	}

}
