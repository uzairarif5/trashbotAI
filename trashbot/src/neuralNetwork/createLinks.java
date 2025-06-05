package neuralNetwork;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Random;

public class createLinks extends nnFields {
	public static boolean createAllNew = true;
	
	public void generateNew(){
		int firstSize = (nnFields.numOfNearestTrash * nnFields.properties);
		this.link12 = new float[firstSize][this.interSize];
		this.link23 = new float[this.interSize][this.finalOuts];
		this.biases2 = new float[this.interSize];
		this.biases3 = new float[this.finalOuts];
		this.plinks = new float[2];
		Random rand = new Random();
		int j;
		for(int i = 0; i < this.interSize; i++) {
			for(j = 0; j < firstSize; j++) {
				this.link12[j][i] = (rand.nextFloat() * 2) - 1;
			}
			for(j = 0; j < this.finalOuts; j++) {
				this.link23[i][j] = (rand.nextFloat() * 2) - 1;
			}
			this.biases2[i] = (rand.nextFloat() * 2) - 1;
		}
		for(j = 0; j < this.finalOuts; j++) {
			this.biases3[j] = (rand.nextFloat() * 2) - 1;
		}
		this.plinks[0] = (rand.nextFloat() * 2) - 1;
		this.plinks[1] = (rand.nextFloat() * 2) - 1;
	}
	
	public void createAndWrite(String name, Object data, int i) {
		File outputFile;
		FileOutputStream out;
		ObjectOutput s;
	    try {
	    	String folderS = "shooterData/shooter".concat(Integer.toString(i+1));
	    	File Folder = new File(folderS);
	    	Folder.mkdir();
				outputFile = new File(folderS.concat("/").concat(name).concat(".dat"));
				if (outputFile.createNewFile()) {
					System.out.println("File created: " + outputFile.getName());
				}
		    out = new FileOutputStream(outputFile);
		    s = new ObjectOutputStream(out);
		    s.writeObject(data);
		    s.close();
		    out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void randomizeZeros(String name,int index) {
		try {
			FileInputStream inFile = new FileInputStream("shooterData/shooter" + index + "/" + name + ".dat");
			ObjectInputStream ins = new ObjectInputStream(inFile);
			float[][] out = (float[][]) ins.readObject();
			Random rand = new Random();
			ins.close();
			inFile.close();
			for(int i = 0;i<out.length;i++) {
				for(int j= 0;j<out[0].length;j++) {
					if (out[i][j] == 0) {
						out[i][j] = (rand.nextFloat() * 2) - 1;
					}
				}
			}
	    	FileOutputStream outFile = new FileOutputStream("shooterData/shooter" + index + "/" + name + ".dat");
	    	ObjectOutputStream outs = new ObjectOutputStream(outFile);
		    outs.writeObject(out);
		    outs.close();
		    outFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		createLinks cl = new createLinks();
		if(createLinks.createAllNew) {
			for(int i = 0;i<nnFields.numOfShooters;i++) {
				cl.generateNew();
				cl.createAndWrite("link12",cl.link12,i);
				cl.createAndWrite("link23",cl.link23,i);
		    cl.createAndWrite("biases2", cl.biases2,i);
		    cl.createAndWrite("biases3", cl.biases3,i);
		    cl.createAndWrite("plinks", cl.plinks,i);
		    System.out.println("Shooter " + (i + 1) + " weights and biases set.");
			}
		}
		else {
			for(int i = 0;i<nnFields.numOfShooters;i++) {
				cl.randomizeZeros("link23", i+1);
			    System.out.println("Shooter " + (i + 1) + " links set.");
			}
		}
	}
}
