package trashbot;

import java.awt.Color;
import java.awt.Graphics;

public class trash implements Cloneable{
	public int radius;
	public static int[] limits = {10,25};
	public int posx;
	public int posy;
	public int movementDirectionX;
	public int movementDirectionY;
	public float distance;
	public Color c;
	public boolean flaggedForDodge;
	
	public trash(int r,int[] sp) throws Exception {
		this.c = Color.DARK_GRAY;
		this.movementDirectionX = 0;
		this.flaggedForDodge = true;
		this.movementDirectionY = 0;
		if(r >= trash.limits[0] && r <= trash.limits[1]) {
			this.radius = r;
		}
		else {
			throw new IllegalArgumentException(String.format("Radius should be between %d and %d",trash.limits[0],trash.limits[1]));
		}
		if((sp[0] < 0 || sp[0] > gameScreen.gameDim.getWidth()) || (sp[1] < 0 || sp[1] > gameScreen.gameDim.getHeight())) {
			this.posx = sp[0];
			this.posy = sp[1];
			if(sp[0]<gameScreen.gameDim.getWidth()/2) {
				this.movementDirectionX = (int) Math.ceil(Math.random() * 3);
			}
			else {
				this.movementDirectionX = (int) Math.ceil(Math.random() * -3);
			}
			if(sp[1]<gameScreen.gameDim.getHeight()/2) {
				this.movementDirectionY = (int) Math.ceil(Math.random() * 3);
			}
			else {
				this.movementDirectionY = (int) Math.ceil(Math.random() * -3);
			}
		}
		else {
			throw new IllegalArgumentException(String.format("Starting position should outside game view; not [%d, %d]",sp[0],sp[1]));
		}
	}
	
	public trash(int r,int[] sp, int mdx, int mdy) throws Exception {
		this.c = Color.DARK_GRAY;
		this.movementDirectionX = mdx;
		this.movementDirectionY = mdy;
		if(r >= trash.limits[0] && r <= trash.limits[1]) {
			this.radius = r;
		}
		else {
			throw new IllegalArgumentException(String.format("Radius should be between %d and %d",trash.limits[0],trash.limits[1]));
		}
		if((sp[0] < 0 || sp[0] > gameScreen.gameDim.getWidth()) || (sp[1] < 0 || sp[1] > gameScreen.gameDim.getHeight())) {
			this.posx = sp[0];
			this.posy = sp[1];
		}
	}
	
	public void move() {
		this.posx += this.movementDirectionX;
		this.posy += this.movementDirectionY;
	}
	
	public int[] getPos() {
		return new int[] {this.posx,this.posy};
	}
	
	public int getDirX() {
		return this.movementDirectionX;
	}
	
	public int getDirY() {
		return this.movementDirectionY;
	}
	
	public void setColor(Color c) {
		this.c = c;
	}

    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }
	
    public void draw(Graphics g) {
    	int diameter = this.radius * 2;
        g.setColor(this.c);
        g.fillOval(this.posx-this.radius, this.posy-this.radius, diameter, diameter);
    }

}

