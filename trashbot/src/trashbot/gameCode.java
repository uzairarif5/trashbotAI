package trashbot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Stack;

public class gameCode {
	public static int spawnOffset = trash.limits[1]+10;
	public static boolean cornerSpawn = true;
	public static int tickInterval = 5;
	public static boolean sameTrashes = true;
	public static Stack<trash> globalTrash;
	
	public int[] setStartingPosition(int[] startingPos,shooter thisShooter) {
		Random r = new Random();
		if(!gameCode.cornerSpawn || Math.pow(thisShooter.getX()-gameScreen.gameMid[0],2)+Math.pow(thisShooter.getY()-gameScreen.gameMid[1],2)<1000) {
			switch(r.nextInt(4)) {
				case 0:
					startingPos = new int[] {r.nextInt((int) gameScreen.gameDim.getWidth()), -1*gameCode.spawnOffset};
					break;
				case 1:
					startingPos = new int[] {-1*gameCode.spawnOffset, r.nextInt((int) gameScreen.gameDim.getHeight())};
					break;
				case 2:
					startingPos = new int[] {r.nextInt((int) gameScreen.gameDim.getWidth()), (int) gameScreen.gameDim.getHeight() + gameCode.spawnOffset};
					break;
				case 3:
					startingPos = new int[] {(int) gameScreen.gameDim.getWidth() + gameCode.spawnOffset, r.nextInt((int) gameScreen.gameDim.getHeight())};
			}
		}
		else if(thisShooter.getX()<gameScreen.gameDim.getWidth()/2) {
			if(thisShooter.getY()<gameScreen.gameDim.getHeight()/2) {
				if(Math.random()>0.5) {
					startingPos = new int[] {r.nextInt((int) gameScreen.gameDim.getWidth()/2), -1*gameCode.spawnOffset};
				}
				else {
					startingPos = new int[] {-1*gameCode.spawnOffset, r.nextInt((int) gameScreen.gameDim.getHeight()/2)};
				}
			}
			else {
				if(Math.random()>0.5) {
					startingPos = new int[] {r.nextInt((int) gameScreen.gameDim.getWidth()/2), (int) gameScreen.gameDim.getHeight() + gameCode.spawnOffset};
			
				}
				else {
					startingPos = new int[] {-1*gameCode.spawnOffset, (r.nextInt((int) gameScreen.gameDim.getHeight()/2)) + (int) gameScreen.gameDim.getHeight()/2};
				}
			}
			
		}
		else {
			if(thisShooter.getY()<gameScreen.gameDim.getHeight()/2) {
				if(Math.random()>0.5) {
					startingPos = new int[] {r.nextInt((int) gameScreen.gameDim.getWidth()/2) + (int) gameScreen.gameDim.getWidth()/2, -1*gameCode.spawnOffset};
				}
				else {
					startingPos = new int[] {(int) gameScreen.gameDim.getWidth() + gameCode.spawnOffset, r.nextInt((int) gameScreen.gameDim.getHeight()/2)};
				}
			}
			else {
				if(Math.random()>0.5) {
					startingPos = new int[] {r.nextInt((int) gameScreen.gameDim.getWidth()/2) + (int) gameScreen.gameDim.getWidth()/2, (int) gameScreen.gameDim.getHeight() + gameCode.spawnOffset};
				}
				else {
					startingPos = new int[] {(int) gameScreen.gameDim.getWidth() + gameCode.spawnOffset, (r.nextInt((int) gameScreen.gameDim.getHeight()/2)) + (int) gameScreen.gameDim.getHeight()/2};					
				}
			}
		}
		return startingPos;
	}
	
	public void runGame(int index) {
		try {
			//Initializing variables for new trashes 
			double nextTrash = Math.random() * 10;
			int[] startingPos = new int[] {0,0};
			int trashX, trashY;
			double bX, bY;
			int counter = 0;
			int globalTrashIndex = 0;
			shooter thisShooter = gameScreen.allShooters.get(index);
			ArrayList<trash> removeTrash;
			HashSet<bullet> removeBullet;
			float distance;
			gameScreen gsIns = gameScreen.getIns();
			
			while(!(gsIns.quit || thisShooter.dead)) {
				gsIns.repaint();
				removeTrash = new ArrayList<trash>();
				removeBullet = new HashSet<bullet>();
				Thread.sleep(gameCode.tickInterval);
				counter += 1;
				if(counter > nextTrash) {
					if(!gameCode.sameTrashes) {
						nextTrash = counter + (Math.random() * 5) + 25;
						startingPos = this.setStartingPosition(startingPos, thisShooter);
						gameScreen.trashCollection.get(index).add(new trash((int) (Math.random()*(trash.limits[1]-trash.limits[0])) + trash.limits[0], startingPos));
					}
					else{
						nextTrash = counter + 55;
						if(gameCode.globalTrash.size()>0) {
							gameScreen.trashCollection.get(index).add((trash) gameCode.globalTrash.get(globalTrashIndex).clone());
							globalTrashIndex++;
						}
			    	}
				}
				for(bullet b: gameScreen.bulletCollection.get(index)) {
					b.move();
					bX = b.getPos()[0];
					bY = b.getPos()[1];
					if (!(bX > -10 && bX < (int) gameScreen.gameDim.getWidth() + 10 && bY > -10 && bY < (int) gameScreen.gameDim.getHeight() + 10)) {
						removeBullet.add(b);
					}
				}
				for(trash t: gameScreen.trashCollection.get(index)) {
					t.move();
					shooter s = gameScreen.allShooters.get(index);
					distance = Math.max(0, (float) Math.pow(Math.pow((s.getPos()[0]-t.getPos()[0]),2) + Math.pow(s.getPos()[1]-t.getPos()[1], 2), 0.5)  - t.radius - s.getRadius()); 
					t.distance = distance;
					s.trashQueue.add(t);
					if(distance < 1) {
						gsIns.killShooter(s);
						break;
					}
					if(t.flaggedForDodge) {
						boolean stillFlag = false;
						for(float[] prevPos:s.prevPath) {
							distance = Math.max(0, (float) Math.pow(Math.pow((prevPos[0]-t.getPos()[0]),2) + Math.pow(prevPos[1]-t.getPos()[1], 2), 0.5)  - t.radius - s.getRadius()); 		
							if(distance < 1) {
								stillFlag = true;
							}
						}
						if(!stillFlag) {
							t.flaggedForDodge = false;
							s.dodged();
						}
					}
					else {
						//trash doesn't need to be flagged forever, unflag when trash goes away
						for(float[] prevPos:s.prevPath) {
							distance = Math.max(0, (float) Math.pow(Math.pow((prevPos[0]-t.getPos()[0]),2) + Math.pow(prevPos[1]-t.getPos()[1], 2), 0.5)  - t.radius - s.getRadius()); 		
							if(distance < 1) {
								t.flaggedForDodge = true;
								break;
							}
						}
					}
					for(bullet b: gameScreen.bulletCollection.get(index)) {
						distance = (float) Math.pow(Math.pow((b.getPos()[0]-t.getPos()[0]),2) + Math.pow(b.getPos()[1]-t.getPos()[1], 2), 0.5); 
						if(distance < b.getRadius() + t.radius + 1) {
							removeTrash.add(t);
							//removeBullet.add(b);//comment this to disable combo
							b.increaseOwnerHitPoint();
						}
					}
					trashX = t.getPos()[0];
					trashY = t.getPos()[1];
					if (!(trashX > -1*gameCode.spawnOffset && trashX < (int) gameScreen.gameDim.getWidth() + gameCode.spawnOffset && trashY > -1*gameCode.spawnOffset && trashY < (int) gameScreen.gameDim.getHeight() + gameCode.spawnOffset)) {
						removeTrash.add(t);
					}
				}
				for(trash t: removeTrash) {
					gameScreen.trashCollection.get(index).remove(t);
				}
				for(bullet b: removeBullet) {
					gameScreen.bulletCollection.get(index).remove(b);
				}
				if(!gsIns.selfMode) {
					gameScreen.allShooters.get(index).move(index);
				}
			}
		} catch (Exception e) {
			System.out.println("An error was at index " +Integer.toString(index)+ ":\n");
			e.printStackTrace();
			System.exit(1);
		}
	}
}
