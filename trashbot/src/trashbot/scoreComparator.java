package trashbot;

import java.util.Comparator;

public class scoreComparator implements Comparator<shooter>{
	@Override
	public int compare(shooter o1, shooter o2) {
		if(o1.getScoreForComp() > o2.getScoreForComp()) {
			return 1;
		}
		else if(o1.getScoreForComp() == o2.getScoreForComp()){
			return 0;
		}
		else {
			return -1;
		}
	}
}
