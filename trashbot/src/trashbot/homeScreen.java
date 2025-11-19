package trashbot;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.*;    

public class homeScreen{
	public static JFrame homeFrame;
	public static CardLayout cl;
	public static gameScreen gs;
	public static Dimension homeDim = new Dimension(600,300);
	
	public void homePage()  {
		JPanel panelObj = new JPanel();
		panelObj.setLayout(new GridBagLayout());
		panelObj.setBackground(Color.GRAY);
		GridBagConstraints gbc = new GridBagConstraints();
		
		//title JLabel
		JLabel title = new JLabel("TrashBot",SwingConstants.CENTER);
		title.setFont(new Font("Calibri", Font.BOLD, 20));
		title.setForeground(Color.lightGray);
		title.setOpaque(true);
		title.setBackground(Color.blue);
		title.setPreferredSize(new Dimension(300,150));
		title.setBorder(BorderFactory.createLineBorder(Color.black, 2));  
		gbc.gridx = 0;  
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.weightx = 1;
    gbc.weighty = 0.5;
    gbc.insets = new Insets(0,0,0,0);
		panelObj.add(title,gbc);
		
		//Self Play
		JButton btn1 = new JButton("Self Play");  
		btn1.setFocusPainted(false);
		btn1.setBackground(Color.lightGray);
		btn1.setPreferredSize(new Dimension(100,30));
		btn1.setBorder(BorderFactory.createLineBorder(Color.black,2));
		gbc.gridx = 0;  
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		gbc.insets = new Insets(0,0,0,0);
		panelObj.add(btn1,gbc);
		btn1.addActionListener(e -> {
			homeScreen.cl.show(homeScreen.homeFrame.getContentPane(),"gs");
			homeScreen.gs.startGame("Self");
			homeScreen.homeFrame.getContentPane().setPreferredSize(gameScreen.gameDim);
			homeScreen.homeFrame.pack();
		});
		
		//Bot Play
		JButton btn2 = new JButton("Bot Play");
		btn2.setFocusPainted(false);
		btn2.setBackground(Color.lightGray);
		btn2.setPreferredSize(new Dimension(100,30));
		btn2.setBorder(BorderFactory.createLineBorder(Color.black,2));
		gbc.gridx = 1;  
		gbc.gridy = 1;
		gbc.gridwidth = 1;
    gbc.weightx = 0.5;
    gbc.weighty = 0.5;
    gbc.insets = new Insets(0,0,0,0);
		panelObj.add(btn2,gbc);
		btn2.addActionListener(e -> {
			homeScreen.cl.show(homeScreen.homeFrame.getContentPane(),"gs");
			homeScreen.homeFrame.getContentPane().setPreferredSize(gameScreen.gameDim);
			homeScreen.homeFrame.pack();
			homeScreen.gs.startGame("Bot");
		});
		
		//add panel to homeFrame
		homeScreen.homeFrame.add(panelObj,"home");
		homeScreen.cl.next(homeScreen.homeFrame.getContentPane());
	}   
	public static void main(String[] args) throws Exception {
		homeScreen thisIns = new homeScreen();
		homeScreen.cl = new CardLayout();
		homeScreen.homeFrame = new JFrame("TrashBot"); 
		homeScreen.homeFrame.setLayout(homeScreen.cl);
		homeScreen.homeFrame.setSize(homeScreen.homeDim);   
		homeScreen.homeFrame.setResizable(false);
		homeScreen.homeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		homeScreen.homeFrame.setVisible(true);
		Thread.sleep(50);
		homeScreen.gs = gameScreen.getIns();
		homeScreen.gs.setFocusable(true);
		homeScreen.gs.addComponentListener( new ComponentAdapter() {
	    @Override
	    public void componentShown( ComponentEvent e ) {
	      gameScreen.gsIns.requestFocusInWindow();
	    }
		});
		homeScreen.homeFrame.add(homeScreen.gs,"gs");
		thisIns.homePage();
	}
	
}