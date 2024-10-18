package game;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class GalaxyGame extends JPanel implements ActionListener, KeyListener {

    private Timer timer;
    private SpaceShip player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Bullet> bullets;
    private boolean gameOver;
    private int score;

    public GalaxyGame() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        player = new SpaceShip(400, 500);
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();
        gameOver = false;
        score = 0;

        timer = new Timer(20, this);
        timer.start();

        // 적 생성 타이머
        new Timer(1000, e -> spawnEnemy()).start();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Galaxy Shooter");
        GalaxyGame gamePanel = new GalaxyGame(); // 46번째 줄
        frame.add(gamePanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            player.move();
            moveEnemies();
            moveBullets();
            checkCollisions();
        }
        repaint();
    }

    // 적 생성
    public void spawnEnemy() {
        if (!gameOver) {
            Random rand = new Random();
            int x = rand.nextInt(750); // 랜덤 x 위치
            enemies.add(new Enemy(x, 0));
        }
    }

    // 적 이동
    public void moveEnemies() {
        Iterator<Enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            enemy.move();
            if (enemy.getY() > 600) { // 화면 아래로 사라지면 제거
                iterator.remove();
            }
        }
    }

    // 총알 이동
    public void moveBullets() {
        Iterator<Bullet> iterator = bullets.iterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();
            bullet.move();
            if (bullet.getY() < 0) { // 화면 위로 사라지면 제거
                iterator.remove();
            }
        }
    }

    // 충돌 체크
    public void checkCollisions() {
        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();

            // 적과 플레이어 충돌 확인
            if (player.getBounds().intersects(enemy.getBounds())) {
                gameOver = true;
                return;
            }

            // 적과 총알 충돌 확인
            Iterator<Bullet> bulletIterator = bullets.iterator();
            while (bulletIterator.hasNext()) {
                Bullet bullet = bulletIterator.next();
                if (bullet.getBounds().intersects(enemy.getBounds())) {
                    enemyIterator.remove();
                    bulletIterator.remove();
                    score += 10;
                    break;
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("Game Over", 250, 300);
        } else {
            player.draw(g);
            for (Enemy enemy : enemies) {
                enemy.draw(g);
            }
            for (Bullet bullet : bullets) {
                bullet.draw(g);
            }

            // 점수 표시
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Score: " + score, 10, 20);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameOver) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                player.setDx(-5);
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                player.setDx(5);
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                bullets.add(new Bullet(player.getX() + 20, player.getY()));
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
            player.setDx(0);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    class SpaceShip {
        private int x, y, dx;
        private final int WIDTH = 50, HEIGHT = 50;

        public SpaceShip(int x, int y) {
            this.x = x;
            this.y = y;
            this.dx = 0;
        }

        public void move() {
            x += dx;
            if (x < 0) x = 0;
            if (x > 750) x = 750;
        }

        public void draw(Graphics g) {
            g.setColor(Color.BLUE);
            g.fillRect(x, y, WIDTH, HEIGHT);
        }

        public void setDx(int dx) {
            this.dx = dx;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public Rectangle getBounds() {
            return new Rectangle(x, y, WIDTH, HEIGHT);
        }
    }

    class Enemy {
        private int x, y;
        private final int WIDTH = 40, HEIGHT = 40;

        public Enemy(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void move() {
            y += 2; // 적의 이동 속도
        }

        public void draw(Graphics g) {
            g.setColor(Color.RED);
            g.fillRect(x, y, WIDTH, HEIGHT);
        }

        public int getY() {
            return y;
        }

        public Rectangle getBounds() {
            return new Rectangle(x, y, WIDTH, HEIGHT);
        }
    }

    class Bullet {
        private int x, y;
        private final int WIDTH = 5, HEIGHT = 10;

        public Bullet(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void move() {
            y -= 5; // 총알의 속도
        }

        public void draw(Graphics g) {
            g.setColor(Color.YELLOW);
            g.fillRect(x, y, WIDTH, HEIGHT);
        }

        public int getY() {
            return y;
        }

        public Rectangle getBounds() {
            return new Rectangle(x, y, WIDTH, HEIGHT);
        }
    }
}
