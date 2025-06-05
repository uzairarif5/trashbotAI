package neuralNetwork;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class checkLinks {
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		FileInputStream in;
		ObjectInputStream s;
		float[][] out;
		try {
			in = new FileInputStream("shooter91/link23.dat");
			s = new ObjectInputStream(in);
			out = (float[][]) s.readObject();
			s.close();
			in.close();
			for(int i = 0;i<out.length;i++) {
				for(int j = 0;j<out[0].length;j++) {
					System.out.print(String.format("%5.2f",out[i][j]) + " ");
				}
				System.out.println();
			}
			/*
			for(int i = 0;i<out.length;i++) {
				System.out.println(String.format("%5.2f",out[i]) + " ");
			}*/
			System.out.println(out.length);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}

}
