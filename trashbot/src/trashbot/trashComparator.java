package trashbot;

import java.util.Comparator;

public class trashComparator implements Comparator<trash>{

	@Override
	public int compare(trash o1, trash o2) {
		if(o1.distance > o2.distance) {
			return 1;
		}
		else if(o1.distance == o2.distance){
			return 0;
		}
		else {
			return -1;
		}
	}

}
