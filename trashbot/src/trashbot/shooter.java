package trashbot;

import java.awt.Color;
import java.awt.Graphics;
import java.time.Instant;
import java.util.LinkedList;
import java.util.PriorityQueue;

import neuralNetwork.neuralNetwork;
import neuralNetwork.nnFields;

public class shooter {
	public Color colour;
	public float posx;
	public float posy;
	public int shooterR;
	public int hitPoint;
	public int shootPoint;
	public int maxCombo;
	public boolean dead;
	public float[] inputForBrain;
	public long lastShoot;
	public neuralNetwork shooterBrain;
	public PriorityQueue<trash> trashQueue;
	public float currentScore;
	public float score;
	public int dodges;
	public int surviveTicks;
	public LinkedList<float[]> prevPath;
	
	public shooter(Color c,int[] iniPos) {
		this.colour = c;
		this.posx = iniPos[0];
		this.posy = iniPos[1];
		this.shooterR = 10;
		this.lastShoot = 0;
		this.hitPoint = 0;
		this.shootPoint = 0;
		this.maxCombo = 0;
		this.score = 0;
		this.dead = false;
		this.dodges = 0;
		int brainInputSize = nnFields.numOfNearestTrash*nnFields.properties;
		this.inputForBrain = new float[brainInputSize];
		this.trashQueue = new PriorityQueue<>(8, new trashComparator());
		this.prevPath = new LinkedList<>();
		this.surviveTicks = 0;
	}
	
	public void setNN(int i){
		this.shooterBrain = new neuralNetwork(i);
	}
	
	public float[] getPos() {
		return new float[] {this.posx,this.posy};
	}
	
	public void dodged() {
		this.dodges += 1;
	}
	
	public float getX() {
		return this.posx;
	}
	public float getY() {
		return this.posy;
	}
	
	public Color getColor() {
		return this.colour;
	}
	
	public void goUp() {
		if(this.posy > this.shooterR + 20) {
			this.posy -= 5;
		}
	}
	public void goDown() {
		if(this.posy < gameScreen.gameDim.getHeight() - this.shooterR - 20) {
			this.posy += 5;
		}
	}
	public void goLeft() {
		if(this.posx > this.shooterR + 20) {
			this.posx -= 5;
		}
	}
	public void goRight() {
		if(this.posx < gameScreen.gameDim.getWidth() - this.shooterR - 20) {
			this.posx += 5;
		}
	}
	
	public void move(int index) throws Exception {
		float relX;
		float relY;
		for(int i=0;i<(nnFields.numOfNearestTrash*nnFields.properties)-1;i += nnFields.properties) {
			if(!this.trashQueue.isEmpty()) {
				trash t = this.trashQueue.poll();
				this.inputForBrain[i] = ((float) t.radius-trash.limits[0])/(trash.limits[1]-trash.limits[0]);
				relX = t.getPos()[0]-this.getX();
				if(relX >= 0) {
					this.inputForBrain[i+1] = ((float) 20/(relX+20));
				}
				else {
					this.inputForBrain[i+1] = ((float) 20/(relX-20));
				}
				relY = t.getPos()[1]-this.getY();
				if(relY >=0) {
					this.inputForBrain[i+2] = ((float) 20/(relY+20));
				}
				else {
					this.inputForBrain[i+2] = ((float) 20/(relY-20));
				}
				this.inputForBrain[i+3] = ((float) t.movementDirectionX)/3;
				this.inputForBrain[i+4] = ((float) t.movementDirectionY)/3;
			}
			else {
				this.inputForBrain[i] = 0;
				this.inputForBrain[i+1] = 0;
				this.inputForBrain[i+2] = 0;
				this.inputForBrain[i+3] = 0;
			}
		}
		double inputPx = (2*(this.posx-20)/(gameScreen.gameDim.getWidth()-40))-1;
		double inputPy = (2*(this.posy-20)/(gameScreen.gameDim.getHeight()-40))-1;
		float[] out = this.shooterBrain.Propagate(this.inputForBrain, inputPx, inputPy);
		if(this.posx > this.shooterR + 10 && this.posx < gameScreen.gameDim.getWidth() - this.shooterR - 10) {
			this.posx += out[0]*4;
		}
		else {
			this.surviveTicks -= 1;
		}
		if(this.posy > this.shooterR + 10 && this.posy < gameScreen.gameDim.getHeight() - this.shooterR - 10) {
			this.posy += out[1]*4;
		}
		else {
			this.surviveTicks -= 1;
		}
		this.prevPath.add(new float[] {this.posx,this.posy});
		if(this.prevPath.size() > 200) {
			this.prevPath.removeFirst();
		}
		if(out[2] > 0 && !((out[3] == 0)&&(out[4]==0))) {
			this.shoot(index,new float[] {out[3]+this.posx,out[4]+this.posy});			
		}
		this.trashQueue.clear();
		this.surviveTicks += 2;
	}
	
	public void shoot(int i,float[] p) {
		if(Instant.now().toEpochMilli() - this.lastShoot > (long) gameCode.tickInterval*30) {
			double hypLength = Math.pow(Math.pow((double) this.posx-p[0], 2) + Math.pow((double) this.posy-p[1], 2), 0.5);
			double[] bulletPos = new double[]{this.posx, this.posy};
			double[] bulletDir = new double[] {(p[0]-this.posx)*5/hypLength,(p[1]-this.posy)*5/hypLength};
			gameScreen.bulletCollection.get(i).add(new bullet(this, bulletPos, bulletDir));
			this.shootPoint += 1;
			this.lastShoot = Instant.now().toEpochMilli();
		}
	}
	
	public int getRadius() {
		return this.shooterR;
	}
	
	public void calculateScore() {
		float tickScore = ((float) this.surviveTicks)/4;
		float potentialScore = this.hitPoint + (this.maxCombo*2) - (this.shootPoint*1.5f) + tickScore + ((this.dodges-1)*2);
		this.currentScore = Math.max(potentialScore, 0);
	}
	
	public float getCurScore() {
		return this.currentScore;
	}
	
	public void setScoreForComp(float f) {
		this.score = f;
	}
	
	public float getScoreForComp() {
		return this.score;
	}
	
    public void draw(Graphics g) {
    	int diameter = this.shooterR*2;
        g.setColor(this.colour);
        g.fillOval((int) this.posx-this.shooterR, (int) this.posy-this.shooterR, diameter, diameter);
    }

}
	
