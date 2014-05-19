package sokoban.gui;

import platform.geometry.Angle;
import platform.geometry.Angle.Direction;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import platform.core.Map;
import sokoban.io.ImageReader;
import platform.geometry.Angle;
import platform.geometry.Angle.Direction;

public class GamePanel extends JPanel implements KeyListener
{
	public Map currentMap;

	public GamePanel()
	{
		setBackground(Color.BLACK);
		addKeyListener(this);
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		if (currentMap != null) currentMap.draw(g);
	}
	
	public void changeMap(int level)
	{
		try
		{
			this.currentMap = new Map("maps/" + level+".map");
			Sokoban.levelTxt.setText("Level   " +level);
		}
		catch (Exception e)
		{
			System.err.println("There appears to be some configuration issue.");
			this.currentMap = null;
		}
		
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_UP) currentMap.move(new Angle(Direction.Up));
		else if (e.getKeyCode() == KeyEvent.VK_DOWN) currentMap.move(new Angle(Direction.Down));
		else if (e.getKeyCode() == KeyEvent.VK_LEFT) currentMap.move(new Angle(Direction.Left));
		else if (e.getKeyCode() == KeyEvent.VK_RIGHT) currentMap.move(new Angle(Direction.Right));
		
		if (currentMap.solved())
		{
			repaint();
			if (Sokoban.level == Sokoban.maxlevel)
				JOptionPane.showMessageDialog(this, "Congratulations, you passed the final level!");
			else
			{
				String msg = "Congratulations! You passed level " + Sokoban.level + "! \nContinue?";
				int type = JOptionPane.YES_NO_OPTION;
				String title = "Level Complete!";
				int choice = 0;
				choice = JOptionPane.showConfirmDialog(null, msg, title, type);
				if (choice == 1)
					System.exit(0);
				else if (choice == 0)
				{
					Sokoban.level++;
					changeMap(Sokoban.level);
				}
			}
		}
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent arg0) {}
	@Override
	public void keyTyped(KeyEvent arg0) {}	
}