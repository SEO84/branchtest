package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class TetrisGame extends JPanel implements ActionListener {
    private final int BOARD_WIDTH = 10;
    private final int BOARD_HEIGHT = 20;
    private final int TILE_SIZE = 30;
    private final Color[] COLORS = {Color.cyan, Color.blue, Color.orange, Color.yellow, Color.green, Color.pink, Color.red};

    private Timer timer;
    private Tetromino currentPiece;
    private int[][] board;
    private boolean gameOver;

    public TetrisGame() {
        setPreferredSize(new Dimension(BOARD_WIDTH * TILE_SIZE, BOARD_HEIGHT * TILE_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!gameOver) {
                    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        moveCurrentPiece(-1, 0);
                    } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        moveCurrentPiece(1, 0);
                    } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        moveCurrentPiece(0, 1);
                    } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                        rotateCurrentPiece();
                    }
                }
            }
        });

        board = new int[BOARD_HEIGHT][BOARD_WIDTH];
        spawnNewPiece();
        timer = new Timer(500, this);
        timer.start();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Tetris Game");
        TetrisGame gamePanel = new TetrisGame();
        frame.add(gamePanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            if (!moveCurrentPiece(0, 1)) {
                placePiece();
                clearLines();
                spawnNewPiece();
                if (!canMove(currentPiece, 0, 0)) {
                    gameOver = true;
                }
            }
            repaint();
        }
    }

    private void spawnNewPiece() {
        Random rand = new Random();
        currentPiece = new Tetromino(rand.nextInt(COLORS.length), BOARD_WIDTH / 2 - 1, 0);
    }

    private void placePiece() {
        for (int[] block : currentPiece.getShape()) {
            int row = block[0] + currentPiece.y;
            int col = block[1] + currentPiece.x;
            if (row >= 0) {
                board[row][col] = currentPiece.colorIndex;
            }
        }
    }

    private boolean moveCurrentPiece(int dx, int dy) {
        if (canMove(currentPiece, dx, dy)) {
            currentPiece.x += dx;
            currentPiece.y += dy;
            return true;
        }
        return false;
    }

    private boolean canMove(Tetromino piece, int dx, int dy) {
        for (int[] block : piece.getShape()) {
            int row = block[0] + piece.y + dy;
            int col = block[1] + piece.x + dx;

            if (row < 0 || row >= BOARD_HEIGHT || col < 0 || col >= BOARD_WIDTH || board[row][col] != 0) {
                return false;
            }
        }
        return true;
    }

    private void rotateCurrentPiece() {
        Tetromino rotatedPiece = currentPiece.rotate();
        if (canMove(rotatedPiece, 0, 0)) {
            currentPiece = rotatedPiece;
        }
    }

    private void clearLines() {
        for (int row = BOARD_HEIGHT - 1; row >= 0; row--) {
            boolean fullLine = true;
            for (int col : board[row]) {
                if (col == 0) {
                    fullLine = false;
                    break;
                }
            }
            if (fullLine) {
                for (int r = row; r > 0; r--) {
                    System.arraycopy(board[r - 1], 0, board[r], 0, BOARD_WIDTH);
                }
                row++; // Check the same line again
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        drawCurrentPiece(g);
        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("Game Over", 100, 250);
        }
    }

    private void drawBoard(Graphics g) {
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                if (board[row][col] != 0) {
                    g.setColor(COLORS[board[row][col]]);
                    g.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    g.setColor(Color.BLACK);
                    g.drawRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
        }
    }

    private void drawCurrentPiece(Graphics g) {
        for (int[] block : currentPiece.getShape()) {
            int row = block[0] + currentPiece.y;
            int col = block[1] + currentPiece.x;
            g.setColor(COLORS[currentPiece.colorIndex]);
            g.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            g.setColor(Color.BLACK);
            g.drawRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
    }

    class Tetromino {
        int colorIndex;
        int x, y;

        private final int[][][] shapes = {
            {{0, 1}, {1, 1}, {2, 1}, {1, 0}}, // T
            {{0, 1}, {1, 1}, {2, 1}, {2, 0}}, // L
            {{0, 1}, {1, 1}, {1, 0}, {1, 2}}, // J
            {{0, 0}, {0, 1}, {1, 0}, {1, 1}}, // O
            {{0, 1}, {1, 1}, {1, 0}, {2, 0}}, // S
            {{0, 0}, {0, 1}, {1, 1}, {1, 2}}, // Z
            {{0, 0}, {0, 1}, {0, 2}, {0, 3}}  // I
        };

        public Tetromino(int colorIndex, int x, int y) {
            this.colorIndex = colorIndex;
            this.x = x;
            this.y = y;
        }

        public int[][] getShape() {
            return shapes[colorIndex];
        }

        public Tetromino rotate() {
            int[][] rotatedShape = new int[shapes[colorIndex].length][2];
            for (int i = 0; i < shapes[colorIndex].length; i++) {
                rotatedShape[i][0] = -shapes[colorIndex][i][1]; // Rotate
                rotatedShape[i][1] = shapes[colorIndex][i][0];
            }
            return new Tetromino(colorIndex, x, y) {
                @Override
                public int[][] getShape() {
                    return rotatedShape;
                }
            };
        }
    }
}
