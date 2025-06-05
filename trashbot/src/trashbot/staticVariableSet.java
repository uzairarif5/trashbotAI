package trashbot;

import java.util.Random;

public class staticVariableSet {
	public static Random r;
	public static int[] globalMovementD;
	public static int globalRadius = 10;
	public static int[] gobalStartingPos = new int[] {-gameCode.spawnOffset,-gameCode.spawnOffset};
	
	public static void setVariables() {
		switch(staticVariableSet.r.nextInt(4)) {
			case 0:
				staticVariableSet.gobalStartingPos = new int[] {staticVariableSet.r.nextInt((int) gameScreen.gameDim.getWidth()), -1*gameCode.spawnOffset};
				break;
			case 1:
				staticVariableSet.gobalStartingPos = new int[] {-1*gameCode.spawnOffset, staticVariableSet.r.nextInt((int) gameScreen.gameDim.getHeight())};
				break;
			case 2:
				staticVariableSet.gobalStartingPos = new int[] {staticVariableSet.r.nextInt((int) gameScreen.gameDim.getWidth()), (int) gameScreen.gameDim.getHeight() + gameCode.spawnOffset};
				break;
			case 3:
				staticVariableSet.gobalStartingPos = new int[] {(int) gameScreen.gameDim.getWidth() + gameCode.spawnOffset, staticVariableSet.r.nextInt((int) gameScreen.gameDim.getHeight())};
		}
		staticVariableSet.globalMovementD = new int[] {0, 0};
		while(staticVariableSet.globalMovementD[0] == 0 && staticVariableSet.globalMovementD[1] == 0) {
			if(staticVariableSet.gobalStartingPos[0]<gameScreen.gameDim.getWidth()/2) {
				staticVariableSet.globalMovementD[0] = staticVariableSet.r.nextInt(4);
			}
			else {
				staticVariableSet.globalMovementD[0] = staticVariableSet.r.nextInt(4) * -1;
			}
			if(staticVariableSet.gobalStartingPos[1]<gameScreen.gameDim.getHeight()/2) {
				staticVariableSet.globalMovementD[1] = staticVariableSet.r.nextInt(4);
			}
			else {
				staticVariableSet.globalMovementD [1] = staticVariableSet.r.nextInt(4) * -1;
			}
		}
		staticVariableSet.globalRadius = staticVariableSet.r.nextInt(trash.limits[1]-trash.limits[0]) + trash.limits[0];
	}
}
