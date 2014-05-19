/*
 * Sokoban Implementation
 * Software Design
 * Assignment 5
 * 
 * Alejandro Zúñiga Peñaranda
 * Ke Li Huang
 * David Millan Novell
 */


package sokoban.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import sokoban.io.ImageReader;

public class Sokoban implements ActionListener
{
	// Define the interface elements which can produce actions.
	private JFrame gui;
	private JButton start,back, last, next, choose, exit, about;
	private Container pane;
	
	// The important elements of the game
	GamePanel gamePanel;
	public static JLabel levelTxt = new JLabel();
	public static int level = 1;
	public static int maxlevel = 29;
	public static void main(String[] args)
	{
		// REMOVE: new GameFrame();
		
		// Creates a new sokoban game.
		Sokoban sokoban = new Sokoban();
		JFrame guiFrame = new JFrame();
		sokoban.buildGUI(guiFrame);
		
		// Add the game panel (JPanel).
		sokoban.gui.setVisible(true);
		sokoban.gamePanel = new GamePanel();
		sokoban.pane.setVisible(true);
		sokoban.gamePanel.setVisible(true);
		sokoban.pane.add(sokoban.gamePanel,0);
		sokoban.gamePanel.setOpaque(false);
		//sokoban.gamePanel.setOpaque(false);
		sokoban.gamePanel.requestFocus();
		sokoban.gui.validate();
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == start)
		{
			gamePanel.changeMap(level);
			gamePanel.requestFocus();
			gamePanel.repaint();
		}
		else if (e.getSource() == last)
		{
			level--;
			if (level < 1)
			{
				level++;
				JOptionPane.showMessageDialog(gui, "This is the first level");
				gamePanel.requestFocus();
			}
			else
			{
				gamePanel.changeMap(level);
				gamePanel.requestFocus();
			}
		}
		else if (e.getSource() == next)
		{
			level++;
			if (level > maxlevel)
			{
				level--;
				JOptionPane.showMessageDialog(gui, "This is the final level");
				gamePanel.requestFocus();
			}
			else
			{
				gamePanel.changeMap(level);
				gamePanel.requestFocus();
			}
		}
		else if (e.getSource() == exit) System.exit(0);
		else if (e.getSource() == about) {JOptionPane.showMessageDialog(gui, "This is the final work of Group5 for software design class");}
		else if (e.getSource() == choose) {
			String lvl=JOptionPane.showInputDialog(this,"Please enter the level to jump to(1~50)");
			level=Integer.parseInt(lvl);
		if(level>maxlevel||level<1)
		{JOptionPane.showMessageDialog(gui, "Level not exist");gamePanel.requestFocus();}
		else
			{
			gamePanel.changeMap(level);
			gamePanel.requestFocus();
			}}
		else if (e.getSource() == back) { gamePanel.currentMap.undoMovement(); gamePanel.repaint(); gamePanel.requestFocus(); }
	}
	
	private void buildGUI(JFrame GUI)
	{
		this.gui = GUI;



		// Create a new window and populate the window with the options and the GameFrame to host the game.
		gui.setTitle("Sokoban Group 5");
		gui.setSize(700, 700);
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setVisible(true);
		
		// Create the window menu (the top bar).
		//MenuBar menuBar = new MenuBar();
		//JToolBar bar = new JToolBar();
		// Game Menu
		
			JToolBar bar = new JToolBar();
			start = new JButton("Start");
			back = new JButton("Undo");
			last = new JButton("Previous Level");
			next = new JButton("Next Level");
			choose = new JButton("Select Level");
			exit = new JButton("Exit");
			about = new JButton("About");
			start.addActionListener(this);
			last.addActionListener(this);
			next.addActionListener(this);
			choose.addActionListener(this);
			exit.addActionListener(this);
			back.addActionListener(this);
			about.addActionListener(this);
			bar.add(start);
			bar.add(last);
			bar.add(next);
			bar.add(choose);
			bar.add(back);
			bar.add(exit);
			bar.add(about);
			bar.setFloatable(false);
		
		// Set the content pane properties.
		pane = gui.getContentPane();
		pane.setLayout(new BorderLayout());
		pane.setBackground(Color.WHITE);
		
		// Display the window menu.
		gui.add("North",bar);
		levelTxt.setFont(new Font("����_2312",Font.BOLD,30));
		gui.add("South",levelTxt);
	}
}