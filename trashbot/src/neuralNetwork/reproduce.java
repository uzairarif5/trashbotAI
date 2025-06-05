package neuralNetwork;

import java.util.Random;

import trashbot.gameScreen;

public class reproduce {
	
	public static void reproduceTwoShooters(int num,int num2) {
		neuralNetwork s1b = gameScreen.allShooters.get(num).shooterBrain;
		neuralNetwork s2b = gameScreen.allShooters.get(num2).shooterBrain;
		neuralNetwork sc; //shooterToChange
		int j;
		int k;
		createLinks cl = new createLinks();
		for(int i =0;i<nnFields.numOfShooters;i++) {
			//dont change best one or second best one
			if(i == num||i == num2) {continue;}
			sc = gameScreen.allShooters.get(i).shooterBrain;
			//link23 baises2 and link12
			for(j=0;j<s1b.interSize;j++) {
				for(k = 0;k<s1b.link23[0].length;k++) {
					if(Math.random()<cl.mutationRate) {
						sc.link23[j][k] = (float) ((Math.random()*2)-1);
					}
					else {
						if(Math.random()<cl.chanceOfPickingFirst) {
							sc.link23[j][k] = s1b.link23[j][k];
						}
						else {
							sc.link23[j][k] = s2b.link23[j][k];
						}
					}
				}
				for(k=0;k<s1b.link12.length;k++) {
					if(Math.random()<cl.mutationRate) {
						sc.link12[k][j] = (float) ((Math.random()*2)-1);
					}
					else {
						if(Math.random()<cl.chanceOfPickingFirst) {
							sc.link12[k][j] = s1b.link12[k][j];
						}
						else {
							sc.link12[k][j] = s2b.link12[k][j];
						}
					}
				}
				if(Math.random()<cl.mutationRate) {
					sc.biases2[j] = (float) ((Math.random()*2)-1);
				}
				else {
					if(Math.random()<cl.chanceOfPickingFirst) {
						sc.biases2[j] = s1b.biases2[j];
					}
					else {
						sc.biases2[j] = s2b.biases2[j];
					}
				}
			}
			/*
			//[last two of link23], link34 and baises3
			for(k = 0;k<s1b.interSize2;k++) {
				if(Math.random()<cl.mutationRate) {
					sc.link23[s1b.interSize][j]= (float) ((Math.random()*2)-1);
				}
				else {
					if(Math.random()<cl.chanceOfPickingFirst) {
						sc.link23[s1b.interSize][j] = s1b.link23[s1b.interSize][j];
					}
					else {
						sc.link23[s2b.interSize][j] = s2b.link23[s2b.interSize][j];
					}
				}
				if(Math.random()<cl.mutationRate) {
					sc.link23[s1b.interSize+1][j]= (float) ((Math.random()*2)-1);
				}
				else {
					if(Math.random()<cl.chanceOfPickingFirst) {
						sc.link23[s1b.interSize+1][j] = s1b.link23[s1b.interSize+1][j];
					}
					else {
						sc.link23[s2b.interSize+1][j] = s2b.link23[s2b.interSize+1][j];
					}
				}
				for(j=0;j<s1b.finalOuts;j++) {
					if(Math.random()<cl.mutationRate) {
						sc.link34[k][j] = (float) ((Math.random()*2)-1);
					}
					else {
						if(Math.random()<cl.chanceOfPickingFirst) {
							sc.link34[k][j] = s1b.link34[k][j];
						}
						else {
							sc.link34[k][j] = s2b.link34[k][j];
						}
					}
				}
				if(Math.random()<cl.mutationRate) {
					sc.baises3[k] = (float) ((Math.random()*2)-1);
				}
				else {
					if(Math.random()<cl.chanceOfPickingFirst) {
						sc.baises3[k] = s1b.baises3[k];
					}
					else {
						sc.baises3[k] = s2b.baises3[k];
					}
				}
			}
			//baises4
			for(k=0;k<s1b.finalOuts;k++) {
				if(Math.random()<cl.mutationRate) {
					sc.baises4[k] = (float) ((Math.random()*2)-1);
				}
				else {
					if(Math.random()<cl.chanceOfPickingFirst) {
						sc.baises4[k] = s1b.baises4[k];
					}
					else {
						sc.baises4[k] = s2b.baises4[k];
					}
				}
			}
			*/
			//baises3
			for(k=0;k<s1b.finalOuts;k++) {
				if(Math.random()<cl.mutationRate) {
					sc.biases3[k] = (float) ((Math.random()*2)-1);
				}
				else {
					if(Math.random()<cl.chanceOfPickingFirst) {
						sc.biases3[k] = s1b.biases3[k];
					}
					else {
						sc.biases3[k] = s2b.biases3[k];
					}
				}
			}
			
			//plinks
			if(Math.random()<cl.mutationRate) {
				sc.plinks[0] = (float) ((Math.random()*2)-1);
			}
			else {
				if(Math.random()<cl.chanceOfPickingFirst) {
					sc.plinks[0] = s1b.plinks[0];
				}
				else{
					sc.plinks[0] = s2b.plinks[0];
				}
			}
			if(Math.random()<cl.mutationRate) {
				sc.plinks[1] = (float) ((Math.random()*2)-1);
			}
			else {
				if(Math.random()<cl.chanceOfPickingFirst) {
					sc.plinks[1] = s1b.plinks[1];
				}
				else{
					sc.plinks[1] = s2b.plinks[1];
				}
			}
			cl.createAndWrite("link12",sc.link12,i);
		    cl.createAndWrite("link23", sc.link23,i);
		    //cl.createAndWrite("link34", sc.link34,i);
		    cl.createAndWrite("baises2", sc.biases2,i);
		    cl.createAndWrite("baises3", sc.biases3,i);
		    //cl.createAndWrite("baises4", sc.baises4,i);
		    cl.createAndWrite("plinks", sc.plinks,i);
		    System.out.println("Shooter " + Integer.toString(i+1) + " weights and baises set.");
		}
	}
	
	public static void cloneAndMutate(int[] si, int[] fi) throws Exception {
		int i = 0;
		int k;
		int j;
		neuralNetwork sb;
		neuralNetwork sc;
		createLinks cl = new createLinks();
		if(si.length > fi.length) {
			System.out.println("Success size: " + Integer.toString(si.length));
			System.out.println("fail size: " + Integer.toString(fi.length));
			throw new Exception("success list is bigger than fail list");
		}
		//Asexually update a loser from each winner
		while(i<si.length) {
			sb = gameScreen.allShooters.get(si[i]).shooterBrain;
			sc = gameScreen.allShooters.get(fi[i]).shooterBrain;
			//link23, baises2 and link12
			for(j=0;j<sb.interSize;j++) {
				for(k = 0;k<sb.link23[0].length;k++) {
					if(Math.random()<cl.mutationRate) {
						sc.link23[j][k] = (float) ((Math.random()*2)-1);
					}
					else {
						sc.link23[j][k] = sb.link23[j][k];
					}
				}
				if(Math.random()<cl.mutationRate) {
					sc.biases2[j] = (float) ((Math.random()*2)-1);
				}
				else {
					sc.biases2[j] = sb.biases2[j];
				}
				for(k = 0;k<sb.link12.length;k++) {
					if(Math.random()<cl.mutationRate) {
						sc.link12[k][j] = (float) ((Math.random()*2)-1);
					}
					else {
						sc.link12[k][j] = sb.link12[k][j];
					}
				}
			}
			/*
			//link34 and baises3
			for(j=0;j<sb.interSize2;j++) {
				for(k = 0;k<sb.finalOuts;k++) {
					if(Math.random()<cl.mutationRate) {
						sc.link34[j][k] = (float) ((Math.random()*2)-1);
					}
					else {
						sc.link34[j][k] = sb.link34[j][k];
					}
				}
				if(Math.random()<cl.mutationRate) {
					sc.baises3[j] = (float) ((Math.random()*2)-1);
				}
				else {
					sc.baises3[j] = sb.baises3[j];
				}
			}
			//baises4
			for(k = 0;k<sb.finalOuts;k++) {
				if(Math.random()<cl.mutationRate) {
					sc.baises4[k] = (float) ((Math.random()*2)-1);
				}
				else {
					sc.baises4[k] = sb.baises4[k];
				}
			}
			*/

			//baises3
			for(k = 0;k<sb.finalOuts;k++) {
				if(Math.random()<cl.mutationRate) {
					sc.biases3[k] = (float) ((Math.random()*2)-1);
				}
				else {
					sc.biases3[k] = sb.biases3[k];
				}
			}
			
			//plinks
			if(Math.random()<cl.mutationRate) {
				sc.plinks[0] = (float) ((Math.random()*2)-1);
			}
			else {
				sc.plinks[0] = sb.plinks[0];
			}
			if(Math.random()<cl.mutationRate) {
				sc.plinks[1] = (float) ((Math.random()*2)-1);
			}
			else {
				sc.plinks[1] = sb.plinks[1];
			}
			
			cl.createAndWrite("link12",sc.link12,fi[i]);
		    cl.createAndWrite("link23", sc.link23,fi[i]);
		    //cl.createAndWrite("link34", sc.link34,fi[i]);
		    cl.createAndWrite("baises2", sc.biases2,fi[i]);
		    cl.createAndWrite("baises3", sc.biases3,fi[i]);
		    //cl.createAndWrite("baises4", sc.baises4,fi[i]);
		    cl.createAndWrite("plinks", sc.plinks,fi[i]);
		    System.out.println("Shooter " + Integer.toString(fi[i]+1) + " weights and baises set.");
			i++;
		}
		//As for the rest: randomly select 2 from the winners (can be asexual) or mutate
		Random r = new Random();
		int selectedWinner;
		while(i<fi.length) {
			selectedWinner = r.nextInt(si.length);
			sb = gameScreen.allShooters.get(si[selectedWinner]).shooterBrain;
		    System.out.println("Selected winner: " + Integer.toString(si[selectedWinner]+1));
			
			sc = gameScreen.allShooters.get(fi[i]).shooterBrain;
			//link23, baises2 and link12
			for(j=0;j<sb.interSize;j++) {
				for(k=0;k<sb.link12.length;k++) {
					if(Math.random()<cl.mutationRate) {
						sc.link12[k][j] = (float) ((Math.random()*2)-1);
					}
					else {
						sc.link12[k][j] = sb.link12[k][j];
					}
				}
				if(Math.random()<cl.mutationRate) {
					sc.biases2[j] = (float) ((Math.random()*2)-1);
				}
				else {
					sc.biases2[j] = sb.biases2[j];
				}
				for(k = 0;k<sb.link23[0].length;k++) {
					if(Math.random()<cl.mutationRate) {
						sc.link23[j][k] = (float) ((Math.random()*2)-1);
					}
					else {
						sc.link23[j][k] = sb.link23[j][k];
					}
				}
			}
			/*
			//link34 and baises3
			for(j=0;j<s1b.interSize2;j++) {
				for(k = 0;k<s1b.finalOuts;k++) {
					if(Math.random()<cl.mutationRate) {
						sc.link34[j][k] = (float) ((Math.random()*2)-1);
					}
					else {
						if(Math.random()<cl.chanceOfPickingFirst) {
							sc.link34[j][k] = s1b.link34[j][k];
						}
						else {
							sc.link34[j][k] = s2b.link34[j][k];
						}
					}
				}
				if(Math.random()<cl.mutationRate) {
					sc.baises3[j] = (float) ((Math.random()*2)-1);
				}
				else {
					if(Math.random()<cl.chanceOfPickingFirst) {
						sc.baises3[j] = s1b.baises3[j];
					}
					else{
						sc.baises3[j] = s2b.baises3[j];
					}
				}
			}
			//baises4
			for(k = 0;k<s1b.finalOuts;k++) {
				if(Math.random()<cl.mutationRate) {
					sc.baises4[k] = (float) ((Math.random()*2)-1);
				}
				else {
					if(Math.random()<cl.chanceOfPickingFirst) {
						sc.baises4[k] = s1b.baises4[k];
					}
					else{
						sc.baises4[k] = s2b.baises4[k];
					}
				}
			}
			*/
			//baises3
			for(k = 0;k<sb.finalOuts;k++) {
				if(Math.random()<cl.mutationRate) {
					sc.biases3[k] = (float) ((Math.random()*2)-1);
				}
				else {
					sc.biases3[k] = sb.biases3[k];
				}
			}
			//plinks
			if(Math.random()<cl.mutationRate) {
				sc.plinks[0] = (float) ((Math.random()*2)-1);
			}
			else {
				sc.plinks[0] = sb.plinks[0];
			}
			if(Math.random()<cl.mutationRate) {
				sc.plinks[1] = (float) ((Math.random()*2)-1);
			}
			else {
				sc.plinks[1] = sb.plinks[1];
			}
			cl.createAndWrite("link12",sc.link12,fi[i]);
		    cl.createAndWrite("link23", sc.link23,fi[i]);
		    //cl.createAndWrite("link34", sc.link34,fi[i]);
		    cl.createAndWrite("baises2", sc.biases2,fi[i]);
		    cl.createAndWrite("baises3", sc.biases3,fi[i]);
		    //cl.createAndWrite("baises4", sc.baises4,fi[i]);
		    cl.createAndWrite("plinks", sc.plinks,fi[i]);
		    System.out.println("Shooter " + Integer.toString(fi[i]+1) + " weights and baises set.");
			i++;
		}
	}
	
	public static void RouletteWheel(int[] shooterIndexByScore, float[] scores, int successSize) throws Exception {
		float sum = 0;
		for(int i =0; i<scores.length;i++) {
			sum += scores[i];
		}
		neuralNetwork s1b = null;
		neuralNetwork s2b = null;
		int i;
		int j;
		int k;
		float sum2;
		float firstP = (new Random()).nextFloat()*sum;
		sum2 = 0;
		for(i = 0; i<gameScreen.allShooters.size();i++) {
			sum2 += scores[shooterIndexByScore[i]];
			if(firstP < sum2) {
				s1b = gameScreen.allShooters.get(shooterIndexByScore[i]).shooterBrain;
				System.out.println("RW first Selection: " + Integer.toString(shooterIndexByScore[i]+1));
				break;
			}
		}
		float secondP = (new Random()).nextFloat()*sum;
		sum2 = 0;
		for(i = 0; i<gameScreen.allShooters.size();i++) {
			sum2 += scores[shooterIndexByScore[i]];
			if(secondP < sum2) {
				s2b = gameScreen.allShooters.get(shooterIndexByScore[i]).shooterBrain;
				System.out.println("RW second Selection: " + Integer.toString(shooterIndexByScore[i]+1));
				break;
			}
		}

		neuralNetwork sc; //shooterToChange
		createLinks cl = new createLinks();
		boolean skipped;
		for(i =0;i<nnFields.numOfShooters;i++) {
			skipped = false;
			for(j =1;j<=successSize;j++) {
				if(i == shooterIndexByScore[shooterIndexByScore.length-j]) {
					skipped = true;
					break;
				}
			}
			if(skipped) {
				System.out.println("Skipped: " + Integer.toString(i+1));
				continue;
			}
			sc = gameScreen.allShooters.get(i).shooterBrain;

			//link23, baises2 and link12
			for(j=0;j<s1b.interSize;j++) {
				for(k=0;k<s1b.link12.length;k++) {
					if(Math.random()<cl.mutationRate) {
						sc.link12[k][j] = (float) ((Math.random()*2)-1);
					}
					else {
						if(Math.random()<cl.chanceOfPickingFirst) {
							sc.link12[k][j] = s1b.link12[k][j];
						}
						else {
							sc.link12[k][j] = s2b.link12[k][j];
						}
					}
				}
				for(k = 0;k<s1b.link23[0].length;k++) {
					if(Math.random()<cl.mutationRate) {
						sc.link23[j][k] = (float) ((Math.random()*2)-1);
					}
					else {
						if(Math.random()<cl.chanceOfPickingFirst) {
							sc.link23[j][k] = s1b.link23[j][k];
						}
						else {
							sc.link23[j][k] = s2b.link23[j][k];
						}
					}
				}
				if(Math.random()<cl.mutationRate) {
					sc.biases2[j] = (float) ((Math.random()*2)-1);
				}
				else {
					if(Math.random()<cl.chanceOfPickingFirst) {
						sc.biases2[j] = s1b.biases2[j];
					}
					else{
						sc.biases2[j] = s2b.biases2[j];
					}
				}
			}
			//baises3
			for(k = 0;k<s1b.finalOuts;k++) {
				if(Math.random()<cl.mutationRate) {
					sc.biases3[k] = (float) ((Math.random()*2)-1);
				}
				else {
					if(Math.random()<cl.chanceOfPickingFirst) {
						sc.biases3[k] = s1b.biases3[k];
					}
					else{
						sc.biases3[k] = s2b.biases3[k];
					}
				}
			}
			//plinks
			if(Math.random()<cl.mutationRate) {
				sc.plinks[0] = (float) ((Math.random()*2)-1);
			}
			else {
				if(Math.random()<cl.chanceOfPickingFirst) {
					sc.plinks[0] = s1b.plinks[0];
				}
				else{
					sc.plinks[0] = s2b.plinks[0];
				}
			}
			if(Math.random()<cl.mutationRate) {
				sc.plinks[1] = (float) ((Math.random()*2)-1);
			}
			else {
				if(Math.random()<cl.chanceOfPickingFirst) {
					sc.plinks[1] = s1b.plinks[1];
				}
				else{
					sc.plinks[1] = s2b.plinks[1];
				}
			}
			cl.createAndWrite("link12",sc.link12,i);
		    cl.createAndWrite("link23", sc.link23,i);
		    cl.createAndWrite("baises2", sc.biases2,i);
		    cl.createAndWrite("baises3", sc.biases3,i);
		    cl.createAndWrite("plinks", sc.plinks,i);
		    System.out.println("Shooter " + Integer.toString(i+1) + " weights and baises set.");
		}
	}
}
