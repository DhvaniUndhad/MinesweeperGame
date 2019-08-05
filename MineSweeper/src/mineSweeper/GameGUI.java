/**
 * @author Dhvani Undhad  ID: 999 99 3003
 * Course: MCIS 5103 (Advance Programming Concepts)  Section: 029
 */

package mineSweeper;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.WindowConstants;

public class GameGUI implements MouseListener, KeyListener {
	public int rows = 24, cols = 24, mines = 91;
	private static int width = 600;
	private static int height = 680;
	private Screen screen;
	private Minesweeper world;
	private Font font;
	private Container cp;
	private JPanel scorePanel;
	private Timer time;
	private JTextField clock;
	private JTextField flagsLeft;
	private JButton reset;
	private int insetLeft;
	private int insetTop;
	public JFrame frame;
	public String level = "hard";

	public GameGUI() {

		world = new Minesweeper();
		frame = new JFrame();

		// Main frame for the game
		frame.setResizable(false);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		frame.addMouseListener(this);
		frame.addKeyListener(this);
		cp = frame.getContentPane();

		// cells screen
		screen = new Screen();
		frame.add(screen);
		JPanel north = new JPanel();

		// score panel responsible for display of Timer and Mines left.
		scorePanel = new JPanel();
		scorePanel.setPreferredSize(new Dimension(20, 50));
		scorePanel.setLayout(new GridLayout(2, 3));
		JLabel timer = new JLabel(" Timer:");
		JLabel minesLeft = new JLabel(" Mines:");
		reset = new JButton();
		reset.setText("Reset");
		clock = new JTextField("" + 0);
		flagsLeft = new JTextField("" + mines);
		flagsLeft.setEditable(false);
		clock.setEditable(false);
		reset.addActionListener(new ResetListener());
		scorePanel.add(timer);
		scorePanel.add(clock);
		scorePanel.add(reset);
		scorePanel.add(minesLeft);
		scorePanel.add(flagsLeft);

		// Game menu
		JMenuBar menu = new JMenuBar();
		menu.setLayout(new FlowLayout(FlowLayout.LEFT));
		JMenu game = new JMenu("Game");
		JMenuItem options = new JMenuItem("Levels");
		options.addActionListener(new OptionsListener());
		game.add(options);
		menu.add(game);
		north.setLayout(new BoxLayout(north, 1));
		north.add(menu);
		north.add(scorePanel);
		cp.add(north, BorderLayout.NORTH);
		time = new Timer(1000, new TimerListener());
		frame.pack();
		insetLeft = frame.getInsets().left;
		insetTop = frame.getInsets().top;
		frame.setSize(width + insetLeft + frame.getInsets().right, height + frame.getInsets().bottom + insetTop);
		frame.setLocationRelativeTo(null);
		font = new Font("SansSerif", 0, 10);
		frame.setVisible(true);
	}

	/**
	 * This method contains logic for time bounds for all 3 levels and actions
	 * to be performed after timer expires
	 */
	private class TimerListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			if (clock.getText().compareTo("999") < 0) {
				clock.setText((Integer.parseInt(clock.getText()) + 1) + "");
			}
			if (level.equalsIgnoreCase("easy") && clock.getText().compareTo("60") == 0) {
				time.stop();
				world.blastAllBombs();
				screen.repaint();
				JOptionPane.showMessageDialog(frame, "Times up for Begineer!");
			} else if (level.equalsIgnoreCase("medium") && clock.getText().compareTo("180") == 0) {
				time.stop();
				world.blastAllBombs();
				screen.repaint();
				JOptionPane.showMessageDialog(frame, "Times up for Intermediate!");
			} else if (level.equalsIgnoreCase("hard") && clock.getText().compareTo("600") == 0) {
				time.stop();
				world.blastAllBombs();
				screen.repaint();
				JOptionPane.showMessageDialog(frame, "Times up for Expert!");
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (!time.isRunning() && e.getY() > 102) {
			time.start();
		}

		// Actions on left click
		if (e.getButton() == 1 && e.getY() > 102) {
			world.clickedLeft(e.getX() - insetLeft, e.getY() - insetTop - 80);
			if (world.isDead() || world.isFinish()) {
				time.stop();
			}
		}

		// Actions on right click
		if (e.getButton() == 3 && e.getY() > 102) {
			world.clickedRight(e.getX() - insetLeft, e.getY() - insetTop - 80);
			if (world.isDead() || world.isFinish()) {
				time.stop();
			}
		}
		flagsLeft.setText("" + world.getMinesLeft());
		screen.repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	public class Screen extends JPanel {

		private static final long serialVersionUID = -1842542901094564576L;

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setFont(font);
			world.draw(g);
			if (world.isDead()) {
				Image gameOverImage = new ImageIcon("gameover.gif").getImage();
				g.drawImage(gameOverImage, 0, 50, this);
			} else if (world.isFinish()) {
				Image wonImage = new ImageIcon("youwon.png").getImage();
				g.drawImage(wonImage, 0, 50, this);
			}
		}
	}

	public static int getScreenWidth() {
		return width;
	}

	public static int getScreenHeight() {
		return width;
	}

	private class OptionsListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			@SuppressWarnings("unused")
			OptionsFrame opts = new OptionsFrame();
		}

		private class OptionsFrame extends JFrame {

			private static final long serialVersionUID = -2209006387279353918L;

			private JTextField r;
			private JTextField c;
			private JTextField m;

			private int w;
			private int h;

			JButton easy, medium, hard;

			public OptionsFrame() {
				setLocation(100, 100);
				setResizable(false);
				setSize(330, 160);
				setTitle("Options");
				JPanel txt = new JPanel();
				txt.setLayout(new GridLayout(3, 2));
				JTextField rs = new JTextField("Rows: ");
				rs.setEditable(false);
				txt.add(rs);
				r = new JTextField("" + rows);
				r.setEditable(false);
				txt.add(r);

				JTextField cs = new JTextField("Columns: ");
				cs.setEditable(false);
				txt.add(cs);
				c = new JTextField("" + cols);
				c.setEditable(false);
				txt.add(c);

				JTextField ms = new JTextField("Mines: ");
				ms.setEditable(false);
				txt.add(ms);
				m = new JTextField("" + mines);
				m.setEditable(false);
				txt.add(m);

				getContentPane().add(txt, BorderLayout.NORTH);

				// preset games options
				easy = new JButton("Beginner");
				medium = new JButton("Intermediate");
				hard = new JButton("Expert");
				easy.addActionListener(new ModeListener());
				medium.addActionListener(new ModeListener());
				hard.addActionListener(new ModeListener());
				JPanel modes = new JPanel();
				modes.setLayout(new GridLayout(1, 3));
				modes.add(easy);
				modes.add(medium);
				modes.add(hard);

				getContentPane().add(modes, BorderLayout.CENTER);
				JButton okay = new JButton("OK");
				okay.addActionListener(new OkayListener(this));
				getContentPane().add(okay, BorderLayout.SOUTH);

				setVisible(true);
			}

			private class ModeListener implements ActionListener {

				public void actionPerformed(ActionEvent ae) {
					if (ae.getSource().equals(easy)) {
						level = "easy";
						r.setText("" + 8);
						c.setText("" + 8);
						m.setText("" + 10);
						flagsLeft.setText("" + 10);
						mines = 10;
						w = 200;
						h = 304;
					} else if (ae.getSource().equals(medium)) {
						level = "medium";
						r.setText("" + 16);
						c.setText("" + 16);
						m.setText("" + 36);
						flagsLeft.setText("" + 36);
						mines = 36;
						w = 400;
						h = 504;
					} else if (ae.getSource().equals(hard)) {
						level = "hard";
						r.setText("" + 24);
						c.setText("" + 24);
						m.setText("" + 91);
						flagsLeft.setText("" + 91);
						mines = 91;
						w = 600;
						h = 704;
					}
				}
			}

			private class OkayListener implements ActionListener {

				private JFrame window;

				public OkayListener(JFrame window) {
					this.window = window;
				}

				public void actionPerformed(ActionEvent arg0) {
					try {
						int tmp = Integer.parseInt(r.getText());
						int tmp2 = Integer.parseInt(c.getText());
						int tmp3 = Integer.parseInt(m.getText());

						if (w == 0 || h == 0) {
							if (tmp3 == 91) {
								w = 600;
								h = 704;
							} else if (tmp3 == 36) {
								w = 400;
								h = 504;
							} else if (tmp3 == 10) {
								w = 200;
								h = 304;
							}
						}

						int width = w;
						int height = h;

						if (tmp > 0 && tmp2 > 0 && tmp3 < tmp * tmp2) {
							rows = tmp;
							world.setRow(rows);
							cols = tmp2;
							world.setColumn(cols);
							mines = tmp3;
							world.setMines(mines);
							clock.setText(0 + "");
							time.stop();
							world.reset();
							screen.repaint();
							frame.setPreferredSize(new Dimension(width, height));
							frame.pack();

						} else
							throw new NumberFormatException();
						window.dispose();
					} catch (NumberFormatException nfe) {
					}
				}
			}
		}
	}

	private class ResetListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			clock.setText(0 + "");
			flagsLeft.setText("" + mines);
			world.setMines(mines);
			time.stop();
			world.reset();
			screen.repaint();
		}
	}
}
