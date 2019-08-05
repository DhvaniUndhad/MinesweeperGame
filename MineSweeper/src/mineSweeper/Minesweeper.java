/**
 * @author Dhvani Undhad  ID: 999 99 3003
 * Course: MCIS 5103 (Advance Programming Concepts)  Section: 029
 */

package mineSweeper;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JSlider;

public class Minesweeper {
	private static int row = 24;
	private static int coloum = 24;
	public int numberOfBombs = 91;

	private boolean finish;
	private boolean dead;

	private Random random;

	private Cell[][] cells;
	Cell cellObject = new Cell();
	private Image bomb = new ImageIcon("explod.gif").getImage().getScaledInstance(cellObject.getWidth(),
			cellObject.getHeight(), Image.SCALE_DEFAULT);
	private BufferedImage flag = ImageHandler.scale(ImageHandler.loadImage("images/flag.png"), cellObject.getWidth(),
			cellObject.getHeight());
	private BufferedImage pressed = ImageHandler.scale(ImageHandler.loadImage("images/pressed.png"),
			cellObject.getWidth(), cellObject.getHeight());
	private BufferedImage normal = ImageHandler.scale(ImageHandler.loadImage("images/normal.png"),
			cellObject.getWidth(), cellObject.getHeight());
	private BufferedImage crossImage = ImageHandler.scale(ImageHandler.loadImage("images/cross.png"),
			cellObject.getWidth(), cellObject.getHeight());

	public Minesweeper() {
		random = new Random();
		cells = new Cell[row][coloum];
		for (int x = 0; x < row; x++) {
			for (int y = 0; y < coloum; y++) {
				cells[x][y] = new Cell(x, y, normal, bomb, pressed, flag, crossImage);
			}
		}

		reset();
	}

	/**
	 * placing bombs randomly
	 */
	private void placeBombs() {
		for (int i = 0; i < numberOfBombs; i++) {
			placeBomb();
		}
	}

	private void placeBomb() {
		int x = random.nextInt(row);
		int y = random.nextInt(coloum);

		if (!cells[x][y].isBomb()) {
			cells[x][y].setBomb(true);
		} else {
			placeBomb();
		}
	}

	/**
	 * Setting number of nearby bombs to cells
	 */
	private void setNumbers() {
		for (int x = 0; x < row; x++) {
			for (int y = 0; y < coloum; y++) {
				int mx = x - 1;
				int gx = x + 1;
				int my = y - 1;
				int gy = y + 1;

				int amountOfBombs = 0;
				if (mx >= 0 && my >= 0 && cells[mx][my].isBomb())
					amountOfBombs++;
				if (mx >= 0 && cells[mx][y].isBomb())
					amountOfBombs++;
				if (mx >= 0 && gy < coloum && cells[mx][gy].isBomb())
					amountOfBombs++;

				if (my >= 0 && cells[x][my].isBomb())
					amountOfBombs++;
				if (gy < coloum && cells[x][gy].isBomb())
					amountOfBombs++;

				if (gx < row && my >= 0 && cells[gx][my].isBomb())
					amountOfBombs++;
				if (gx < row && cells[gx][y].isBomb())
					amountOfBombs++;
				if (gx < row && gy < coloum && cells[gx][gy].isBomb())
					amountOfBombs++;

				cells[x][y].setAmountOfNearBombs(amountOfBombs);
			}
		}
	}

	public void clickedLeft(int x, int y) {
		if (!dead && !finish) {
			int cellX = x / cellObject.getWidth();
			int cellY = y / cellObject.getHeight();
			if (!cells[cellX][cellY].isFlag()) {

				// Bonus feature implementation
				if (cells[cellX][cellY].isOpened()) {
					openAdjecentCell(cellX, cellY);
				} else {

					cells[cellX][cellY].setOpened(true);

					if (cells[cellX][cellY].isBomb()) {
						dead = true;
						try {
							playExplosionSound();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						for (int j = 0; j < row; j++) {
							for (int k = 0; k < coloum; k++) {
								if (cells[j][k].isBomb() && !cells[j][k].isFlag()) {
									cells[j][k].setOpened(true);
								}
								if (cells[j][k].isFlag() && !cells[j][k].isBomb()) {
									cells[j][k].setCross(true);
								}
							}
						}
					} else {
						if (cells[cellX][cellY].getAmountOfNearBombs() == 0) {
							open(cellX, cellY);
						}
					}

					checkFinish();
				}
			}
		}
	}

	/**
	 * *******BONUS FEATURE IMPLEMENTATION********* This method opens up
	 * adjecent cells on second left click on already opened cells
	 * 
	 * @param cellX
	 * @param cellY
	 */
	private void openAdjecentCell(int cellX, int cellY) {
		for (int i = cellX - 1; i < row && i <= cellX + 1; i++) {
			for (int j = cellY - 1; j < coloum && j <= cellY + 1; j++) {
				if (i >= 0 && j >= 0) {
					if (!cells[i][j].isOpened() && !cells[i][j].isBomb()) {
						if (!cells[i][j].isFlag()) {
							cells[i][j].setOpened(true);
						}
					}
				}
			}

		}

	}

	/**
	 * Blast all bombs when one bomb is opened
	 */
	public void blastAllBombs() {
		dead = true;
		try {
			playExplosionSound();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (int j = 0; j < row; j++) {
			for (int k = 0; k < coloum; k++) {
				if (cells[j][k].isBomb() && !cells[j][k].isFlag()) {
					cells[j][k].setOpened(true);
				}
				if (cells[j][k].isFlag() && !cells[j][k].isBomb()) {
					cells[j][k].setCross(true);
				}
			}
		}
	}

	/**
	 * One first click place flag and remove flag on second right click on same
	 * cell
	 * 
	 * @param x
	 * @param y
	 */
	public void clickedRight(int x, int y) {
		if (!dead && !finish) {
			int cellX = x / cellObject.getWidth();
			int cellY = y / cellObject.getHeight();
			cells[cellX][cellY].setCross(false);
			if (cells[cellX][cellY].isFlag()) {
				cells[cellX][cellY].setFlag(false);
				numberOfBombs++;
			} else if (!cells[cellX][cellY].isOpened()) {
				cells[cellX][cellY].placeFlag();
				numberOfBombs--;
				checkFinish();
			}

		}
	}

	/**
	 * This method opens up cells in form of left triangle, right triangle and
	 * vertical lines.
	 * 
	 * @param x
	 * @param y
	 */
	private void open(int x, int y) {
		if (!cells[x][y].isFlag()) {
			cells[x][y].setOpened(true);
			if (cells[x][y].getAmountOfNearBombs() == 0) {
				int mx = x - 1;
				int gx = x + 1;
				int my = y - 1;
				int gy = y + 1;

				// left triangle
				if (mx >= 0 && my >= 0 && cells[mx][my].canOpen())
					open(mx, my);
				if (mx >= 0 && cells[mx][y].canOpen())
					open(mx, y);
				if (mx >= 0 && gy < coloum && cells[mx][gy].canOpen())
					open(mx, gy);

				// vertical line
				if (my >= 0 && cells[x][my].canOpen())
					open(x, my);
				if (gy < coloum && cells[x][gy].canOpen())
					open(x, gy);

				// right triangle
				if (gx < row && my >= 0 && cells[gx][my].canOpen())
					open(gx, my);
				if (gx < row && cells[gx][y].canOpen())
					open(gx, y);
				if (gx < row && gy < coloum && cells[gx][gy].canOpen())
					open(gx, gy);

			}
		}
	}

	/**
	 * Check if game is Won/completed
	 */
	private void checkFinish() {
		finish = true;
		outer: for (int x = 0; x < row; x++) {
			for (int y = 0; y < coloum; y++) {
				if (!(cells[x][y].isOpened() || (cells[x][y].isBomb() && cells[x][y].isFlag()))) {
					finish = false;
					break outer;
				}
			}
		}
	}

	/**
	 * Reset all cells back to normal
	 */
	public void reset() {
		for (int x = 0; x < row; x++) {
			for (int y = 0; y < coloum; y++) {
				cells[x][y].reset();
			}
		}

		dead = false;
		finish = false;

		placeBombs();
		setNumbers();
	}

	public void draw(Graphics g) {
		for (int x = 0; x < row; x++) {
			for (int y = 0; y < coloum; y++) {
				cells[x][y].draw(g);
			}
		}

	}

	public static int getWidth() {
		return row;
	}

	public static int getHeight() {
		return coloum;
	}

	public int getMinesLeft() {
		return numberOfBombs;
	}

	public boolean isDead() {
		return dead;
	}

	public boolean isFinish() {
		return finish;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setColumn(int column) {
		this.coloum = column;
	}

	public void setMines(int mines) {
		this.numberOfBombs = mines;
	}

	/**
	 * This method plays bomb explosion sound when bombs are exploded
	 * 
	 * @throws InterruptedException
	 */
	public void playExplosionSound() throws InterruptedException {
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("bombexplotion.wav"));
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			JSlider slide = new JSlider();
			clip.start();
		} catch (UnsupportedAudioFileException uafe) {
			uafe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (LineUnavailableException lue) {
			lue.printStackTrace();
		}
	}

}
