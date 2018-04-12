package de.fau.med.imi.MDRMatcher;
import java.util.Comparator;

public class MatchResultsComparator implements Comparator<MatchResult> {

	@Override
	public int compare(MatchResult arg0, MatchResult arg1) {

		if (arg0.getMatchScore() < arg1.getMatchScore())
			return 1;
		if (arg0.getMatchScore() > arg1.getMatchScore())
			return -1;
		return 0;
	}
}
