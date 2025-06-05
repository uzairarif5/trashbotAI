package neuralNetwork;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class neuralNetwork extends nnFields{
	public neuralNetwork(int index) {
		int i = index + 1;
		try {
			this.link12 = readLinks("shooter".concat(Integer.toString(i)).concat("/link12"));
			this.link23 = readLinks("shooter".concat(Integer.toString(i)).concat("/link23"));
			this.biases2 = readBiases("shooter".concat(Integer.toString(i)).concat("/baises2"));
			this.biases3 = readBiases("shooter".concat(Integer.toString(i)).concat("/baises3"));
			this.plinks = readBiases("shooter".concat(Integer.toString(i)).concat("/plinks"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public float[][] readLinks(String name) throws IOException, ClassNotFoundException{
		FileInputStream in = new FileInputStream(name + ".dat");
		ObjectInputStream s = new ObjectInputStream(in);
		float[][] out = (float[][]) s.readObject();
		s.close();
		in.close();
		return out;
	}
	
	public float[] readBiases(String name) throws IOException, ClassNotFoundException{
		FileInputStream in = new FileInputStream(name + ".dat");
		ObjectInputStream s = new ObjectInputStream(in);
		float[] out = (float[]) s.readObject();
		s.close();
		in.close();
		return out;
	}
	
	public float[] Propagate(float[] inputs,double px, double py) throws Exception{
		//array1 (input) ---link12---> array2 (inter) ---link23---> array3 (output)
		if(inputs.length != this.link12.length) {
			throw new Exception("Input length (" + inputs.length + ") does not match number of nodes for input layer(" + this.link12.length +").");
		}
		float[] interLayer = new float[this.interSize];
		float[] outLayer = new float[this.finalOuts];
		float value;
		int j;
		int i;
		for(i = 0; i < this.interSize; i++) {
			value = 0;
			for (j = 0; j < this.link12.length; j++) {value += this.link12[j][i] * inputs[j];}
			interLayer[i] = (float) ((2.0/(Math.pow(Math.E, (value + this.biases2[i])*-1)+1))-1);
			for(j = 0; j < this.finalOuts; j++) {outLayer[j] += (this.link23[i][j] * interLayer[i]);}
		}
		outLayer[0] += (float) (this.plinks[0] * px);
		outLayer[1] += (float) (this.plinks[1] * py);
		for(i = 0; i < this.finalOuts; i++) {
			outLayer[i] = (float) Math.sin(Math.PI*(outLayer[i] + this.biases3[i])/((this.interSize+1)*2));
		}
		return outLayer;
	}
}
