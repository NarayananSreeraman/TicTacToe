import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import javax.swing.*;

public class NeonTicTacToe {
    int boardWidth = 600;
    int boardHeight = 700;

    JFrame frame = new JFrame("Neon Tic-Tac-Toe");
    JLabel titleLabel = new JLabel();
    JLabel scoreLabel = new JLabel();
    JPanel topPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JPanel bottomPanel = new JPanel();

    JButton resetButton = new JButton("Play Again");
    JButton resetScoreButton = new JButton("Reset Scores");

    // Use our custom class instead of standard JButton
    NeonTile board[][] = new NeonTile[3][3];

    String playerX = "X";
    String playerO = "O";
    String currentPlayer = playerX;
    boolean gameOver = false;
    int turns = 0;
    int scoreX = 0;
    int scoreO = 0;

    // --- NEON PALETTE ---
    Color darkBackground = new Color(20, 20, 20); // Almost black for neon contrast
    Color gridColor = new Color(40, 40, 40);
    Color neonBlue = new Color(0, 255, 255);      // Cyan/Electric Blue
    Color neonOrange = new Color(255, 140, 0);    // Hot Orange
    Color neonGreen = new Color(57, 255, 20);     // Neon Green (Win)
    Color defaultText = Color.white;

    public NeonTicTacToe() {
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(darkBackground);

        // --- TOP PANEL ---
        topPanel.setLayout(new GridLayout(2, 1));
        topPanel.setBackground(darkBackground);

        titleLabel.setBackground(darkBackground);
        titleLabel.setForeground(defaultText);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 40));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setText("Tic-Tac-Toe");
        titleLabel.setOpaque(true);
        topPanel.add(titleLabel);

        scoreLabel.setBackground(darkBackground);
        scoreLabel.setForeground(defaultText);
        scoreLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);
        scoreLabel.setText("X: 0   |   O: 0");
        scoreLabel.setOpaque(true);
        topPanel.add(scoreLabel);

        frame.add(topPanel, BorderLayout.NORTH);

        // --- GAME BOARD ---
        boardPanel.setLayout(new GridLayout(3, 3, 5, 5)); // 5px gap for grid effect
        boardPanel.setBackground(darkBackground);
        boardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(boardPanel, BorderLayout.CENTER);

        // --- BOTTOM PANEL ---
        bottomPanel.setLayout(new FlowLayout());
        bottomPanel.setBackground(darkBackground);

        setupButton(resetButton);
        resetButton.addActionListener(e -> resetGame());
        bottomPanel.add(resetButton);

        setupButton(resetScoreButton);
        resetScoreButton.addActionListener(e -> resetScores());
        bottomPanel.add(resetScoreButton);

        frame.add(bottomPanel, BorderLayout.SOUTH);

        // --- INITIALIZE NEON TILES ---
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                NeonTile tile = new NeonTile();
                board[r][c] = tile;
                boardPanel.add(tile);

                tile.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (gameOver) return;

                        NeonTile clickedTile = (NeonTile) e.getSource();
                        if (clickedTile.getSymbol().equals("")) {
                            
                            // Determine Color
                            Color glow = currentPlayer.equals(playerX) ? neonBlue : neonOrange;
                            
                            // Trigger Animation
                            clickedTile.animateSymbol(currentPlayer, glow);
                            
                            turns++;
                            checkWinner();

                            if (!gameOver) {
                                currentPlayer = currentPlayer.equals(playerX) ? playerO : playerX;
                                titleLabel.setText(currentPlayer + "'s turn");
                            }
                        }
                    }
                });
            }
        }
        frame.setVisible(true);
    }

    // --- GAME LOGIC ---
    void checkWinner() {
        // Horizontal
        for (int r = 0; r < 3; r++) {
            if (board[r][0].getSymbol().equals("")) continue;
            if (board[r][0].getSymbol().equals(board[r][1].getSymbol()) &&
                board[r][1].getSymbol().equals(board[r][2].getSymbol())) {
                setWinner(board[r][0], board[r][1], board[r][2]);
                return;
            }
        }

        // Vertical
        for (int c = 0; c < 3; c++) {
            if (board[0][c].getSymbol().equals("")) continue;
            if (board[0][c].getSymbol().equals(board[1][c].getSymbol()) &&
                board[1][c].getSymbol().equals(board[2][c].getSymbol())) {
                setWinner(board[0][c], board[1][c], board[2][c]);
                return;
            }
        }

        // Diagonals
        if (!board[0][0].getSymbol().equals("") &&
            board[0][0].getSymbol().equals(board[1][1].getSymbol()) &&
            board[1][1].getSymbol().equals(board[2][2].getSymbol())) {
            setWinner(board[0][0], board[1][1], board[2][2]);
            return;
        }

        if (!board[0][2].getSymbol().equals("") &&
            board[0][2].getSymbol().equals(board[1][1].getSymbol()) &&
            board[1][1].getSymbol().equals(board[2][0].getSymbol())) {
            setWinner(board[0][2], board[1][1], board[2][0]);
            return;
        }

        // Draw
        if (turns == 9) {
            titleLabel.setText("It's a Draw!");
            gameOver = true;
            resetButton.setVisible(true);
            resetScoreButton.setVisible(true);
        }
    }

    void setWinner(NeonTile t1, NeonTile t2, NeonTile t3) {
        gameOver = true;
        t1.setWinState(true);
        t2.setWinState(true);
        t3.setWinState(true);

        if (currentPlayer.equals(playerX)) {
            scoreX++;
            titleLabel.setText("X Wins!");
            scoreLabel.setForeground(neonBlue);
        } else {
            scoreO++;
            titleLabel.setText("O Wins!");
            scoreLabel.setForeground(neonOrange);
        }

        scoreLabel.setText("X: " + scoreX + "   |   O: " + scoreO);
        resetButton.setVisible(true);
        resetScoreButton.setVisible(true);
    }

    void resetGame() {
        currentPlayer = playerX;
        gameOver = false;
        turns = 0;
        titleLabel.setText("Tic-Tac-Toe");
        scoreLabel.setForeground(defaultText);
        resetButton.setVisible(false);
        resetScoreButton.setVisible(false);

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                board[r][c].reset();
            }
        }
    }

    void resetScores() {
        scoreX = 0;
        scoreO = 0;
        scoreLabel.setText("X: 0   |   O: 0");
        resetGame();
    }

    void setupButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setFocusable(false);
        btn.setVisible(false);
        btn.setBackground(Color.white);
        btn.setForeground(Color.black);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    public static void main(String[] args) {
        new NeonTicTacToe();
    }

    // ==========================================
    //    CUSTOM NEON TILE CLASS (THE MAGIC)
    // ==========================================
    class NeonTile extends JButton {
        private String symbol = "";
        private Color neonColor = Color.white;
        private float currentFontSize = 0f; // For animation
        private final float MAX_FONT_SIZE = 100f;
        private boolean isWinningTile = false;
        private Timer animationTimer;

        public NeonTile() {
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            
            // Hover effect
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    if (symbol.equals("") && !gameOver) {
                        setBackground(new Color(30, 30, 30));
                        repaint();
                    }
                }
                public void mouseExited(MouseEvent e) {
                    if (symbol.equals("")) {
                        setBackground(new Color(20, 20, 20)); // Return to dark
                        repaint();
                    }
                }
            });
        }

        public String getSymbol() { return symbol; }

        public void animateSymbol(String s, Color c) {
            this.symbol = s;
            this.neonColor = c;
            this.currentFontSize = 10f; // Start small

            // Timer to scale font size up smoothly
            animationTimer = new Timer(10, e -> {
                if (currentFontSize < MAX_FONT_SIZE) {
                    currentFontSize += 5; // Speed of animation
                    repaint();
                } else {
                    ((Timer)e.getSource()).stop();
                }
            });
            animationTimer.start();
        }

        public void setWinState(boolean state) {
            this.isWinningTile = state;
            repaint();
        }

        public void reset() {
            this.symbol = "";
            this.currentFontSize = 0;
            this.isWinningTile = false;
            this.setBackground(new Color(20, 20, 20));
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 1. Draw Background
            if (isWinningTile) {
                g2.setColor(new Color(57, 255, 20, 50)); // Transparent Green
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(57, 255, 20)); // Solid Green Border
                g2.setStroke(new BasicStroke(3));
                g2.drawRect(1, 1, getWidth()-2, getHeight()-2);
            } else {
                g2.setColor(getBackground() != null ? getBackground() : new Color(20, 20, 20));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }

            // 2. Draw Text with Neon Glow
            if (!symbol.equals("")) {
                Font f = new Font("Arial", Font.BOLD, (int)currentFontSize);
                g2.setFont(f);
                
                FontMetrics fm = g2.getFontMetrics();
                Rectangle2D r = fm.getStringBounds(symbol, g2);
                int x = (this.getWidth() - (int) r.getWidth()) / 2;
                int y = (this.getHeight() - (int) r.getHeight()) / 2 + fm.getAscent();

                // Draw Glow (Layers of transparent color offset slightly)
                g2.setColor(new Color(neonColor.getRed(), neonColor.getGreen(), neonColor.getBlue(), 50));
                g2.drawString(symbol, x-2, y-2);
                g2.drawString(symbol, x+2, y+2);
                g2.drawString(symbol, x-2, y+2);
                g2.drawString(symbol, x+2, y-2);

                // Draw Core Text
                g2.setColor(neonColor);
                g2.drawString(symbol, x, y);
            }
        }
    }
}