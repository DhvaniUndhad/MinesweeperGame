/**
 * @author Dhvani Undhad  ID: 999 99 3003
 * Course: MCIS 5103 (Advance Programming Concepts)  Section: 029
 */

package mineSweeper;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Cell extends JPanel {
	private static final long serialVersionUID = -483784036199063613L;
	private BufferedImage normal;
	private BufferedImage openedImage;
	private BufferedImage flagImage;
	private Image bombImage;
	private BufferedImage crossImage;
	private int x;
	private int y;
	private boolean bomb;
	private boolean opened;
	private boolean cross;
	private boolean flag;
	private int amountOfNearBombs;
	private static int width = GameGUI.getScreenWidth() / Minesweeper.getWidth();
	private static int height = GameGUI.getScreenHeight() / Minesweeper.getHeight();

	public Cell(int x, int y, BufferedImage normal, Image bomb, BufferedImage openedImage, BufferedImage flag,
			BufferedImage crossImage) {
		this.x = x;
		this.y = y;
		this.normal = normal;
		this.bombImage = bomb;
		this.openedImage = openedImage;
		this.flagImage = flag;
		this.crossImage = crossImage;
	}

	public Cell() {

	}

	public void placeFlag() {
		if (flag)
			flag = false;
		else {
			if (!opened)
				flag = true;
		}
	}

	public void reset() {
		flag = false;
		bomb = false;
		opened = false;
	}

	public boolean canOpen() {
		return !opened && !bomb && amountOfNearBombs >= 0;
	}

	/**
	 * Method to draw graphics for each Cell
	 * 
	 * @param g
	 */
	public void draw(Graphics g) {
		if (!opened) {
			if (!flag)
				g.drawImage(normal, x * width, y * height, null);
			else {
				if (!cross) {
					g.drawImage(flagImage, x * width, y * height, null);
				} else {
					g.drawImage(crossImage, x * width, y * height, null);
				}
			}
		} else {
			if (bomb)
				g.drawImage(bombImage, x * width, y * height, this);
			else {
				g.drawImage(openedImage, x * width, y * height, null);
				if (amountOfNearBombs > 0) {
					g.setColor(Color.BLACK);
					g.setFont(new Font("TimesRoman", Font.BOLD, 20));

					g.drawString("" + amountOfNearBombs, x * width + 7, y * height + height - 4);
				}
			}
		}
	}

	/**
	 * Getters and setters
	 */

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flaged) {
		flag = flaged;
	}

	public void setOpenedImage(BufferedImage openedImage) {
		this.openedImage = openedImage;
	}

	public boolean isCross() {
		return cross;
	}

	public void setCross(boolean cross) {
		this.cross = cross;
	}

	public boolean isOpened() {
		return opened;
	}

	public void setOpened(boolean opened) {
		this.opened = opened;
	}

	public boolean isBomb() {
		return bomb;
	}

	public void setBomb(boolean bomb) {
		this.bomb = bomb;
	}

	public int getAmountOfNearBombs() {
		return amountOfNearBombs;
	}

	public void setAmountOfNearBombs(int amountOfNearBombs) {
		this.amountOfNearBombs = amountOfNearBombs;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
