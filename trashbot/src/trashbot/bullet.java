package trashbot;

import java.awt.Color;
import java.awt.Graphics;

public class bullet {
	public int bRadius;
	public double[] posxy;
	public double[] movementDirection;
	public shooter owner;
	public int combo;
	
	public bullet(shooter o, double[] p, double[] md) {
		this.owner = o;
		this.bRadius = 4;
		this.posxy = p;
		this.movementDirection = md;
		this.combo = 0;
	}
	
	public void increaseOwnerHitPoint() {
		this.combo += 1;
		this.owner.hitPoint += 1;
		if(this.combo > this.owner.maxCombo) {
			this.owner.maxCombo = this.combo;
		}
	}
	
	public void move() {
		this.posxy[0] += this.movementDirection[0];
		this.posxy[1] += this.movementDirection[1];
	}
	
	public double[] getPos() {
		return this.posxy;
	}
	
	public int getRadius() {
		return bRadius;
	}
	
    public void draw(Graphics g) {
    	int diameter = this.bRadius*2;
        g.setColor(Color.yellow);
        g.fillOval((int)this.posxy[0]-this.bRadius,(int) this.posxy[1]-this.bRadius, diameter, diameter);
    }
}
