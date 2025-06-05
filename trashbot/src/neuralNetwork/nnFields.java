package neuralNetwork;

public class nnFields {
	public static int numOfNearestTrash = 6;
	public static int properties = 5;	//properties for each trash [size] [relative x] [relative y] [moveX] [moveY]
	public int interSize = 30;	//interLayer size
	public int interSize2 = 20;	//interLayer2 size
	public int finalOuts = 5;	//outcome for shooter horizontalD, verticalD, shoot, shootX, shootY
	public float[][] link12;
	public float[][] link23;
	public float[] biases2;
	public float[] biases3;
	public float[] plinks;		//for position
	public static int numOfShooters = 100; //always more than 20
	public static int totalIteration = 3;
	
	//mating
	public static int reproductionMode = 1; //1 = reproduce best two; 2 = clone successful ones and mutate asexually, then randomly select from winners and reproduce asexually; 3 = RouletteWheel;
	public float chanceOfPickingFirst = 0.5f;
	public float mutationRate = 0.1f;
}
