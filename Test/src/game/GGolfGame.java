package game;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GGolfGame extends JPanel implements ActionListener {

    private static final int PANEL_WIDTH = 800;
    private static final int PANEL_HEIGHT = 400;
    private static final int GOAL_X = 750;  // 목표 홀의 X 좌표
    private static final int GOAL_Y = 300;  // 목표 홀의 Y 좌표

    private int ballX = 50;  // 공의 현재 X 좌표
    private int ballY = 300; // 공의 현재 Y 좌표
    private int strokeCount = 0;  // 타수

    private Timer timer;
    private boolean isMoving = false;
    private int targetX;
    private int targetY;
    private int dx;
    private int dy;

    public GGolfGame() {
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setBackground(Color.GREEN);
        timer = new Timer(10, this);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("그래픽 골프 게임");
        GolfGameWithGraphics gamePanel = new GolfGameWithGraphics();

        // 사용자 입력을 위한 패널
        JPanel inputPanel = new JPanel();
        JLabel angleLabel = new JLabel("각도 (1-90): ");
        JTextField angleInput = new JTextField(5);
        JLabel powerLabel = new JLabel("힘 (1-100): ");
        JTextField powerInput = new JTextField(5);
        JButton hitButton = new JButton("공 치기");

        inputPanel.add(angleLabel);
        inputPanel.add(angleInput);
        inputPanel.add(powerLabel);
        inputPanel.add(powerInput);
        inputPanel.add(hitButton);

        hitButton.addActionListener(e -> {
            try {
                int angle = Integer.parseInt(angleInput.getText());
                int power = Integer.parseInt(powerInput.getText());

                if (angle < 1 || angle > 90 || power < 1 || power > 100) {
                    JOptionPane.showMessageDialog(frame, "각도와 힘을 올바르게 입력하세요!");
                } else {
                    gamePanel.hitBall(angle, power);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "숫자를 입력하세요!");
            }
        });

        frame.setLayout(new BorderLayout());
        frame.add(gamePanel, BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    // 공을 치는 동작 구현
    public void hitBall(int angle, int power) {
        if (isMoving) return;  // 공이 이미 이동 중이면 무시

        strokeCount++;
        System.out.println("타수: " + strokeCount);

        // 목표 좌표 계산
        double radians = Math.toRadians(angle);
        targetX = ballX + (int) (power * Math.cos(radians) * 5);
        targetY = ballY - (int) (power * Math.sin(radians) * 5);

        // 속도 계산 (단순한 이동량)
        dx = (targetX - ballX) / 50;
        dy = (targetY - ballY) / 50;

        isMoving = true;
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isMoving) {
            ballX += dx;
            ballY += dy;

            // 목표 지점에 도달했을 때
            if (Math.abs(ballX - targetX) < 2 && Math.abs(ballY - targetY) < 2) {
                isMoving = false;
                timer.stop();
                checkIfGoal();
            }
            repaint();
        }
    }

    // 목표 도달 여부 체크
    private void checkIfGoal() {
        if (Math.abs(ballX - GOAL_X) < 10 && Math.abs(ballY - GOAL_Y) < 10) {
            JOptionPane.showMessageDialog(this, "축하합니다! " + strokeCount + "번의 타수로 골에 도달했습니다.");
            resetGame();
        }
    }

    // 게임 재시작
    private void resetGame() {
        ballX = 50;
        ballY = 300;
        strokeCount = 0;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 공 그리기
        g.setColor(Color.RED);
        g.fillOval(ballX, ballY, 20, 20);

        // 목표 홀 그리기
        g.setColor(Color.BLACK);
        g.fillOval(GOAL_X, GOAL_Y, 20, 20);

        // 안내 메시지
        g.setColor(Color.WHITE);
        g.drawString("타수: " + strokeCount, 10, 20);
        g.drawString("목표 홀", GOAL_X - 20, GOAL_Y - 10);
    }
}
