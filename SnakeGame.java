import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame extends JFrame {
    private static final int BOARD_WIDTH = 600;
    private static final int BOARD_HEIGHT = 600;
    private static final int DOT_SIZE = 20;
    private static final int ALL_DOTS = 900;
    private static final int RAND_POS = 29;
    private static final int DELAY = 140;

    private final LinkedList<Point> snake = new LinkedList<>();
    private Point food;
    private char direction = 'R'; // Initial direction (Right)
    private boolean gameOver = false;

    public SnakeGame() {
        setTitle("Snake Game");
        setSize(BOARD_WIDTH, BOARD_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        setResizable(false);

        // Initialize the game
        initGame();

        // Add KeyListener to handle snake's movement
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!gameOver) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_LEFT:
                            if (direction != 'R') direction = 'L';
                            break;
                        case KeyEvent.VK_RIGHT:
                            if (direction != 'L') direction = 'R';
                            break;
                        case KeyEvent.VK_UP:
                            if (direction != 'D') direction = 'U';
                            break;
                        case KeyEvent.VK_DOWN:
                            if (direction != 'U') direction = 'D';
                            break;
                    }
                } else {
                    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                        gameOver = false;
                        initGame();
                        repaint();
                    }
                }
            }
        });

        // Start the game loop
        Timer timer = new Timer(DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameOver) {
                    moveSnake();
                    checkCollision();
                    checkFood();
                    repaint();
                }
            }
        });
        timer.start();
    }

    private void initGame() {
        // Initial snake with one segment
        snake.clear();
        snake.add(new Point(100, 100));
        direction = 'R';
        spawnFood();
    }

    private void moveSnake() {
        Point head = snake.getFirst();
        Point newHead = null;

        // Create a new head based on the current direction
        switch (direction) {
            case 'L': newHead = new Point(head.x - DOT_SIZE, head.y); break;
            case 'R': newHead = new Point(head.x + DOT_SIZE, head.y); break;
            case 'U': newHead = new Point(head.x, head.y - DOT_SIZE); break;
            case 'D': newHead = new Point(head.x, head.y + DOT_SIZE); break;
        }

        // Add the new head to the front of the snake
        snake.addFirst(newHead);

        // If the snake eats food, don't remove the last segment
        if (newHead.equals(food)) {
            spawnFood();
        } else {
            // Remove the last segment of the snake to keep the length the same
            snake.removeLast();
        }
    }

    private void checkCollision() {
        Point head = snake.getFirst();

        // Check if the snake hits the wall
        if (head.x < 0 || head.x >= BOARD_WIDTH || head.y < 0 || head.y >= BOARD_HEIGHT) {
            gameOver = true;
        }

        // Check if the snake collides with itself
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                gameOver = true;
                break;
            }
        }
    }

    private void checkFood() {
        // If the snake eats food, spawn new food
        if (food == null) {
            spawnFood();
        }
    }

    private void spawnFood() {
        Random rand = new Random();
        food = new Point(rand.nextInt(RAND_POS) * DOT_SIZE, rand.nextInt(RAND_POS) * DOT_SIZE);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (gameOver) {
            String message = "Game Over! Press SPACE to Restart";
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.setColor(Color.RED);
            g.drawString(message, (BOARD_WIDTH - metrics.stringWidth(message)) / 2, BOARD_HEIGHT / 2);
            return;
        }

        // Draw the snake
        g.setColor(Color.GREEN);
        for (Point point : snake) {
            g.fillRect(point.x, point.y, DOT_SIZE, DOT_SIZE);
        }

        // Draw the food
        g.setColor(Color.RED);
        g.fillRect(food.x, food.y, DOT_SIZE, DOT_SIZE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SnakeGame game = new SnakeGame();
            game.setVisible(true);
        });
    }
}
