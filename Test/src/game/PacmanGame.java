package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class PacmanGame extends JPanel implements ActionListener, KeyListener {

    private Timer timer;
    private Pacman pacman;
    private Ghost ghost;
    private boolean gameOver;

    public PacmanGame() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        pacman = new Pacman(100, 100);
        ghost = new Ghost(300, 300);
        gameOver = false;

        timer = new Timer(20, this);
        timer.start();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Pac-Man Game");
        PacmanGame gamePanel = new PacmanGame(); // 27번째 줄
        frame.add(gamePanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            pacman.move();
            ghost.move();

            // Check collision
            if (pacman.getBounds().intersects(ghost.getBounds())) {
                gameOver = true;
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("Game Over", 250, 300);
        } else {
            pacman.draw(g);
            ghost.draw(g);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameOver) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                pacman.setDx(-5);
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                pacman.setDx(5);
            } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                pacman.setDy(-5);
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                pacman.setDy(5);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
            pacman.setDx(0);
        }
        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
            pacman.setDy(0);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    class Pacman {
        private int x, y, dx, dy;
        private final int SIZE = 30;

        public Pacman(int x, int y) {
            this.x = x;
            this.y = y;
            this.dx = 0;
            this.dy = 0;
        }

        public void move() {
            x += dx;
            y += dy;

            // Keep Pacman within the bounds
            if (x < 0) x = 0;
            if (x > getWidth() - SIZE) x = getWidth() - SIZE;
            if (y < 0) y = 0;
            if (y > getHeight() - SIZE) y = getHeight() - SIZE;
        }

        public void draw(Graphics g) {
            g.setColor(Color.YELLOW);
            g.fillArc(x, y, SIZE, SIZE, 45, 270); // Draw Pacman
        }

        public void setDx(int dx) {
            this.dx = dx;
        }

        public void setDy(int dy) {
            this.dy = dy;
        }

        public Rectangle getBounds() {
            return new Rectangle(x, y, SIZE, SIZE);
        }
    }

    class Ghost {
        private int x, y;
        private final int SIZE = 30;

        public Ghost(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void move() {
            // Random movement for the ghost
            Random rand = new Random();
            int direction = rand.nextInt(4);
            switch (direction) {
                case 0: // Up
                    if (y > 0) y -= 5;
                    break;
                case 1: // Down
                    if (y < 570) y += 5;
                    break;
                case 2: // Left
                    if (x > 0) x -= 5;
                    break;
                case 3: // Right
                    if (x < 770) x += 5;
                    break;
            }
        }

        public void draw(Graphics g) {
            g.setColor(Color.RED);
            g.fillOval(x, y, SIZE, SIZE); // Draw Ghost
        }

        public Rectangle getBounds() {
            return new Rectangle(x, y, SIZE, SIZE);
        }
    }
}
