package trashbot;

import java.util.Comparator;
import java.util.PriorityQueue;

public class testing {
	public static void main(String args[]) throws Exception {
        Comparator<trash> comparator = new trashComparator();
		PriorityQueue<trash> queue = new PriorityQueue<trash>(8,comparator);
		queue.add(new trash(34,new int[] {-9,0}));
		queue.add(new trash(25,new int[] {-5,1}));
		queue.add(new trash(25,new int[] {-5,5}));
		queue.add(new trash(33,new int[] {-5,0}));
		queue.add(new trash(27,new int[] {-5,0}));
		queue.add(new trash(30,new int[] {-5,0}));
		queue.add(new trash(31,new int[] {-14,0}));
		queue.add(new trash(32,new int[] {-5,0}));
		int s = queue.size();
		for(int i = 0;i<s;i++) {
			System.out.println(queue.poll().radius);
		}
	}
}
