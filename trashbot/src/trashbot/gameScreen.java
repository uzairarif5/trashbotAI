package trashbot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.concurrent.CyclicBarrier;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import neuralNetwork.nnFields;
import neuralNetwork.reproduce;

public class gameScreen extends JPanel{
	private static final long serialVersionUID = 1L;
	public static gameScreen gsIns;
	public static ArrayList<shooter> allShooters;
	public static ArrayList<LinkedList<trash>> trashCollection;
	public static ArrayList<LinkedList<bullet>> bulletCollection;
	public static Dimension gameDim = new Dimension(400,460);
	public static float DiagonalLength;
	public static int focusIndex;
	public static int[] gameMid;
	public static Thread gameScreenKeyThread;
	public static JLabel indexLabel;
	public static boolean allShootersDead;
	public static int compareMode = 0; //0 -> sum; 1 -> max; 2 -> min;
	public int startSeedLimit = 64;
	public int seedForAllTrashes;
	public int totalDead;
	public float[] scores;
	public ArrayList<Color> colors;
	public boolean selfMode;
	public static boolean start;
	public boolean quit;
	public long lastClick;
	public boolean release;
	public int iteration;
	public int generation;
	public Set<Integer> pressedKeys = new HashSet<>();
	
	public gameScreen() {
		this.setBackground(new Color(0,0,150));
		this.iteration = 1;
		this.generation = 1;	    
		this.colors = new ArrayList<>();
		gameScreen.focusIndex = 0;
	    Random r = new Random();
		for(int i =0;i<nnFields.numOfShooters;i++) {
			this.colors.add(new Color(r.nextInt(156)+100,r.nextInt(206)+50,r.nextInt(206)+50));
		}
	}
	
	public void newGame() {
		this.iteration = 1;
		this.generation = 1;
		if(!this.selfMode) {
			this.remove(gameScreen.indexLabel);
			this.scores = null;
			
		}
		this.startGame(this.selfMode? "Self":"Bot");	
	}
	
	public void newGen() {
		if(!this.quit) {
			this.remove(gameScreen.indexLabel);
			this.iteration = 1;
			this.generation++;
			this.scores = null;
			this.startGame("Bot");
		}
	}
	
	public void newIteration(){
		if(!this.quit) {
			this.iteration++;
			this.remove(gameScreen.indexLabel);
			this.startGame("Bot");
		}
	}
	
	public static gameScreen getIns() {
		if(gameScreen.gsIns == null) {
			gameScreen.gsIns = new gameScreen();
			gameScreen.gsIns.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if(e.getKeyCode() == 81) {
						try {gameScreen.getIns().gameQuit();} catch (InterruptedException e1) {e1.printStackTrace();}
					}
					else if(e.getKeyChar() == 'k') {
						gameScreen.getIns().newGame();
					}
					else if (!gameScreen.getIns().selfMode && (e.getKeyCode() == 39 || e.getKeyCode() == 37)) {
						if(e.getKeyCode() == 39) {
							//right
							if(gameScreen.focusIndex == nnFields.numOfShooters-1) {
								gameScreen.focusIndex = 0;
							}
							else {
								gameScreen.focusIndex += 1;
							}
							
						}
						else {
							//left
							if(gameScreen.focusIndex == 0) {
								gameScreen.focusIndex = nnFields.numOfShooters-1;
							}
							else {
								gameScreen.focusIndex -= 1;
							}
						}
						gameScreen.indexLabel.setText("Generation " + gameScreen.getIns().generation + "; Iteration " + gameScreen.getIns().iteration + "; Shooter ".concat(Integer.toString(gameScreen.focusIndex+1)));
						gameScreen.getIns().repaint();
					}
					else if(gameScreen.start && !gameScreen.getIns().pressedKeys.contains(e.getKeyCode())) {
						gameScreen.getIns().pressedKeys.add(e.getKeyCode());
						gameScreen.gameScreenKeyThread = new Thread(() -> {
								try {
									shooter focus = gameScreen.allShooters.get(gameScreen.focusIndex);
									while(!gameScreen.getIns().release && !gameScreen.getIns().pressedKeys.isEmpty() && !focus.dead) {
										for (Integer pressedKey : gameScreen.getIns().pressedKeys) {
											switch (pressedKey) {
												case 87://w
													focus.goUp();
													break;
												case 83://s
													focus.goDown();
													break;
												case 65://a
													focus.goLeft();
													break;
												case 68://d
													focus.goRight();
											}
											try {
												synchronized (gameScreen.gameScreenKeyThread) {
													gameScreen.gameScreenKeyThread.wait(50);
												}
											} catch (InterruptedException e2) {
												e2.printStackTrace();
											}
										}
									}
								}
								catch(ConcurrentModificationException e2) {
									System.out.println(e2.getMessage());
								}
						});  
						if(!gameScreen.gameScreenKeyThread.isAlive()) {gameScreen.gameScreenKeyThread.start();}
						else {gameScreen.gameScreenKeyThread.notify();}
					}
				}
			    @Override
			    public void keyReleased(KeyEvent e) {
			    	gameScreen.getIns().release = true;
			    	gameScreen.getIns().pressedKeys.remove(e.getKeyCode());
			    	gameScreen.getIns().release = false;
			    }
			});
			gameScreen.gsIns.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1 && !gameScreen.allShooters.get(gameScreen.focusIndex).dead) {
					gameScreen.allShooters.get(gameScreen.focusIndex).shoot(gameScreen.focusIndex,new float[] {e.getX(),e.getY()});
				}
				}
			});
			
		}
		return gameScreen.gsIns;
	}
	
	public synchronized void killShooter(shooter s) {
		s.dead = true;
		if(this.selfMode) {
			s.calculateScore();
			JOptionPane.showMessageDialog(homeScreen.homeFrame,String.format("Your Score: %7.2f",s.getCurScore()));
		}
		else {
			this.totalDead +=1;
			if(this.totalDead == nnFields.numOfShooters) {
				gameScreen.allShootersDead = true;
				this.afterAllDead();
			}
		}
	}
	
	public void afterAllDead() {
		int i;
		scoreComparator scoreC = new scoreComparator();
		PriorityQueue<shooter> orderedShooters = new PriorityQueue<>(scoreC);
		int successSize = nnFields.numOfShooters/3;
		int[] succesIndices = new int[successSize];
		int[] failIndices = new int[nnFields.numOfShooters-successSize];
		int[] orderedIndices = new int[nnFields.numOfShooters];
		i = 0;
		for(shooter shooter:gameScreen.allShooters) {
			shooter.calculateScore();
			if(gameScreen.compareMode == 0) {
				this.scores[i] = Math.max(0, shooter.getCurScore() + this.scores[i]);
			}
			else if(gameScreen.compareMode == 1){
				this.scores[i] = Math.max(this.scores[i], shooter.getCurScore());
			}
			else {
				if(this.scores[i] != 0) {
					this.scores[i] = Math.min(this.scores[i], Math.max(shooter.getCurScore(),0));
				}
				else {
					this.scores[i] = Math.max(this.scores[i], shooter.getCurScore());
				}
			}
			shooter.setScoreForComp(this.scores[i]);
			i++;
			orderedShooters.add(shooter);
		}
		System.out.print("Shooters:");
		for(i = 0;  i< failIndices.length;i++) {
			failIndices[i] = gameScreen.allShooters.indexOf(orderedShooters.poll());
			orderedIndices[i] = failIndices[i];
			System.out.printf("%8d|", failIndices[i]+1);
		}
		System.out.print("\nfScores: ");
		for(i = 0;  i< failIndices.length;i++) {
			System.out.printf(" %7.1f|", this.scores[failIndices[i]]);
		}
		System.out.print("\nShooters:");
		for(i = 0;  i< succesIndices.length;i++) {
			succesIndices[i] = gameScreen.allShooters.indexOf(orderedShooters.poll());
			orderedIndices[i+failIndices.length] = succesIndices[i];
			System.out.printf("%8d|", succesIndices[i]+1);
		}
		System.out.print("\npScores: ");
		for(i = 0;  i< succesIndices.length;i++) {
			System.out.printf(" %7.1f|",this.scores[succesIndices[i]]);
		}
		System.out.println('\n');
		if(this.iteration == nnFields.totalIteration) {
			if(nnFields.reproductionMode == 2) {
				new Timer().schedule((new java.util.TimerTask() {
					int[] var1;
					int[] var2;
				    @Override
				    public void run() {
						try {
							reproduce.cloneAndMutate(this.var1,this.var2);
					    	gameScreen.getIns().newGen();
						} catch (Exception e) {
							e.printStackTrace();
						}
				    }
				    public TimerTask para(int[] x1, int[] x2) {
				    	this.var1= x1;
				    	this.var2 = x2;
				    	return this;
				    }
				}).para(succesIndices, failIndices),1000);
			}
			else if(nnFields.reproductionMode == 1){
				new Timer().schedule((new java.util.TimerTask() {
					int var1;
					int var2;
				    @Override
				    public void run() {
						reproduce.reproduceTwoShooters(this.var1,this.var2);
				    	gameScreen.getIns().newGen();
				    }
				    public TimerTask para(int x1, int x2) {
				    	this.var1= x1;
				    	this.var2 = x2;
				    	return this;
				    }
				}).para(succesIndices[succesIndices.length-1], succesIndices[succesIndices.length-2]),1000);
			}
			else if(nnFields.reproductionMode == 3) {
				new Timer().schedule((new java.util.TimerTask() {
					int[] var1;
					float[] var2;
					int var3;
				    @Override
				    public void run() {
						try {
							reproduce.RouletteWheel(this.var1,this.var2, this.var3);
					    	gameScreen.getIns().newGen();
						} catch (Exception e) {
							e.printStackTrace();
						}
				    }
				    public TimerTask para(int[] x1, float[] x2, int x3) {
				    	this.var1= x1;
				    	this.var2 = x2;
				    	this.var3 = x3;
				    	return this;
				    }
				}).para(orderedIndices, this.scores, successSize),1000);	
			}
		}
		else {
			new Timer().schedule(new java.util.TimerTask() {
			    @Override
				public void run() {gameScreen.getIns().newIteration();}
			},1000);
		}
	}
	
	public void gameQuit() throws InterruptedException {
		gameScreen.focusIndex = 0;
		this.quit = true;
		gameScreen.start = false;
		this.iteration = 1;
		this.generation = 1;
		if(!this.selfMode) {gameScreen.getIns().remove(gameScreen.indexLabel);}
		homeScreen.cl.show(homeScreen.homeFrame.getContentPane(), "home");
		homeScreen.homeFrame.setSize(homeScreen.homeDim);
	}

	public void startGame(String mode) {
		gameScreen.gameMid = new int[]{(int) (gameScreen.gameDim.getWidth()/2), (int) gameScreen.gameDim.getHeight()/2};
		this.quit = false;
		this.release = false;
		this.lastClick = 0;
		gameScreen.allShootersDead = false;
		gameScreen.allShooters = new ArrayList<>();
		gameScreen.trashCollection = new ArrayList<>();
		gameScreen.bulletCollection = new ArrayList<>();
		gameScreen.DiagonalLength = (float) (Math.pow(Math.pow(gameScreen.gameDim.getHeight(), 2)+ Math.pow(gameScreen.gameDim.getWidth(),2),0.5));
		if(this.scores == null) {
			this.scores = new float[nnFields.numOfShooters];
		}
		if(Objects.equals(mode, "Self")) {
			this.selfMode = true;
			gameCode.sameTrashes = false;
			gameScreen.allShooters.add(new shooter(Color.red, gameScreen.gameMid));
			gameScreen.trashCollection.add(new LinkedList<>());
			gameScreen.bulletCollection.add(new LinkedList<>());
		}
		else {
			this.selfMode = false;
			if(gameCode.sameTrashes) {
				this.seedForAllTrashes = (new Random()).nextInt(this.startSeedLimit);
				System.out.println("Seed: " + this.seedForAllTrashes);
			}
		    gameScreen.indexLabel = new JLabel("Generation " + this.generation + "; Iteration " + this.iteration + "; Shooter " + (gameScreen.focusIndex+1));
		    gameScreen.indexLabel.setPreferredSize(new Dimension(250,30));
		    gameScreen.indexLabel.setHorizontalAlignment(SwingConstants.CENTER);
		    gameScreen.indexLabel.setBackground(new Color(0.9f,0.9f,0.9f,0.7f));
		    gameScreen.indexLabel.setOpaque(true);
		    this.add(gameScreen.indexLabel);
		    this.totalDead = 0;
		    homeScreen.homeFrame.pack();
		    for(int i =0;i<nnFields.numOfShooters;i++) {
				gameScreen.allShooters.add(new shooter(this.colors.get(i), gameScreen.gameMid));
				gameScreen.trashCollection.add(new LinkedList<>());
				gameScreen.bulletCollection.add(new LinkedList<>());
		    }
		    gameCode.globalTrash = new Stack<>();
		}
		this.repaint();
		new Timer().schedule( 
      new java.util.TimerTask() {
          @Override
          public void run() {
        gameScreen.start = true;
        if(!gameScreen.getIns().quit) {
          if(!gameScreen.getIns().selfMode) {
            CyclicBarrier gate;
              if(gameCode.sameTrashes) {
                gate = new CyclicBarrier(nnFields.numOfShooters+1);
                new Thread(() -> {
	                try {
		                staticVariableSet.r = new Random(gameScreen.getIns().seedForAllTrashes);
		                gate.await();
		                while(!gameScreen.allShootersDead) {
			                try {
				                staticVariableSet.setVariables();
				                gameCode.globalTrash.add(new trash(staticVariableSet.globalRadius, staticVariableSet.gobalStartingPos, staticVariableSet.globalMovementD[0],staticVariableSet.globalMovementD[1]));
				                Thread.sleep(gameCode.tickInterval);
			                }
			                catch (Exception e) {
				                e.printStackTrace();
			                }
		                }
	                }
	                catch (Exception e1) {
		                e1.printStackTrace();
	                }
                }).start();
              }
              else {
                gate = new CyclicBarrier(nnFields.numOfShooters);
              }
              for(int i =0;i<nnFields.numOfShooters;i++) {
                gameScreen.allShooters.get(i).setNN(i);
                new Thread((new Runnable() {
                  public int arg;
									public void run() {
                    try {
                      gameCode gc = new gameCode();
											gate.await();
											gc.runGame(this.arg);
										} catch (Exception e) {
											e.printStackTrace();
										}
	                }
									public Runnable pass(int x) {
										this.arg = x;
										return this;
									}
								}).pass(i)).start();
            }
          }
          else {
            (new gameCode()).runGame(0);
          }
        }
          }
      },
      500
		);
	}

  @Override
  protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      try {
          gameScreen.allShooters.get(gameScreen.focusIndex).draw(g);
        for(trash t:gameScreen.trashCollection.get(gameScreen.focusIndex)) {
          t.draw(g);
        }
        for(bullet b:gameScreen.bulletCollection.get(gameScreen.focusIndex)) {
          b.draw(g);
        }
      }
      catch(Exception e) {}
  }

}
