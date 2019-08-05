/**
 * @author Dhvani Undhad  ID: 999 99 3003
 * Course: MCIS 5103 (Advance Programming Concepts)  Section: 029
 */

package mineSweeper;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageHandler {

	/**
	 * Method to load image based on path
	 * 
	 * @param path
	 * @return
	 */
	public static BufferedImage loadImage(String path) {
		try {
			return ImageIO.read(ImageHandler.class.getClassLoader().getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Method to scale image by width & height
	 * 
	 * @param source
	 * @param width
	 * @param height
	 * @return
	 */
	public static BufferedImage scale(BufferedImage source, int width, int height) {
		BufferedImage scaled = new BufferedImage(width, height, source.getType());
		Graphics g = scaled.getGraphics();
		g.drawImage(source, 0, 0, width, height, null);
		g.dispose();
		return scaled;
	}
}
