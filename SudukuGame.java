package suduku;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class SudukuGame {

    public static void main(String[] args) {
        // Create frame
        JFrame frame = new JFrame("SUDOKU GAME");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        // === NORTH: Title + input + Generate + Instructions label ===
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        JLabel title = new JLabel("SUDOKU GAME", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        topBar.add(title);

        topBar.add(new JLabel("Enter Grid Size:", SwingConstants.RIGHT));
        JTextField sizeField = new JTextField(3);
        topBar.add(sizeField);

        JButton genBtn = new JButton("Generate");
        topBar.add(genBtn);

        JLabel instrHeader = new JLabel("INSTRUCTIONS");
        instrHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));
        topBar.add(instrHeader);

        frame.add(topBar, BorderLayout.NORTH);

        // === CENTER: Grid panel + side‐panel ===
        JPanel center = new JPanel(new BorderLayout(10,10));

        JPanel gridPanel = new JPanel();      // will become GridLayout later
        center.add(gridPanel, BorderLayout.CENTER);

        // Side info panel
        JPanel side = new JPanel();
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBorder(BorderFactory.createTitledBorder("Legend & Result"));

        // Color legend
        JPanel legend = new JPanel(new GridLayout(3,2,5,5));
        legend.add(colorBox(Color.LIGHT_GRAY));  legend.add(new JLabel("Fixed Numbers"));
        legend.add(colorBox(new Color(255,255,204))); legend.add(new JLabel("Your Input"));
        legend.add(colorBox(new Color(255,102,102))); legend.add(new JLabel("Invalid Input"));
        side.add(legend);

        side.add(Box.createVerticalStrut(10));
        side.add(new JLabel("(Input: 1 to grid size)", SwingConstants.CENTER));

        side.add(Box.createVerticalStrut(10));
        side.add(new JLabel("Result:", SwingConstants.LEFT));
        JTextField resultField = new JTextField(20);
        resultField.setEditable(false);
        side.add(resultField);

        center.add(side, BorderLayout.EAST);
        frame.add(center, BorderLayout.CENTER);

        // === SOUTH: Submit button + banner ===
        JPanel bottom = new JPanel(new BorderLayout(5,5));
        JButton submitBtn = new JButton("SUBMIT");
        bottom.add(submitBtn, BorderLayout.WEST);

        JLabel banner = new JLabel(
            "This Game is developed by Jaskirandeep kaur under Guidance of Fajge Sir. All Rights Reserved.",
            SwingConstants.CENTER
        );
        banner.setFont(new Font("Segoe UI Light", Font.ITALIC, 14));
        bottom.add(banner, BorderLayout.CENTER);

        frame.add(bottom, BorderLayout.SOUTH);

        // === State holders ===
        final int[] gridSize = {0};

        // Banner animation
        new Timer(100, e -> {
            String t = banner.getText();
            banner.setText(t.substring(1) + t.charAt(0));
        }).start();

        // Generate action
        genBtn.addActionListener(e -> {
            try {
                int n = Integer.parseInt(sizeField.getText().trim());
                if (n <= 0) throw new NumberFormatException();
                gridSize[0] = n;
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a positive integer grid size.");
                return;
            }

            gridPanel.removeAll();
            gridPanel.setLayout(new GridLayout(gridSize[0], gridSize[0], 2, 2));

            JTextField[][] cells = new JTextField[gridSize[0]][gridSize[0]];
            Random rand = new Random();
            int removeCnt = gridSize[0] * gridSize[0] / 3;

            // Fill grid
            for (int i = 0; i < gridSize[0]; i++) {
                for (int j = 0; j < gridSize[0]; j++) {
                    JTextField tf = new JTextField(2);
                    tf.setHorizontalAlignment(JTextField.CENTER);
                    tf.setFont(new Font("Serif", Font.PLAIN, 18));
                    tf.setText(String.valueOf((i + j) % gridSize[0] + 1));
                    tf.setEditable(false);
                    tf.setBackground(Color.LIGHT_GRAY);
                    cells[i][j] = tf;
                    gridPanel.add(tf);
                }
            }
            // Remove cells
            for (int k = 0; k < removeCnt; k++) {
                int i = rand.nextInt(gridSize[0]);
                int j = rand.nextInt(gridSize[0]);
                JTextField tf = cells[i][j];
                if (!tf.getText().isEmpty()) {
                    tf.setText("");
                    tf.setEditable(true);
                    tf.setBackground(new Color(255,255,204));
                    int row = i, col = j;
                    tf.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyReleased(KeyEvent ev) {
                            String in = tf.getText();
                            if (in.matches("[1-" + gridSize[0] + "]")) {
                                for (int c = 0; c < gridSize[0]; c++) {
                                    if (c != col && cells[row][c].getText().equals(in)) {
                                        tf.setBackground(new Color(255,102,102));
                                        return;
                                    }
                                }
                                for (int r = 0; r < gridSize[0]; r++) {
                                    if (r != row && cells[r][col].getText().equals(in)) {
                                        tf.setBackground(new Color(255,102,102));
                                        return;
                                    }
                                }
                                tf.setBackground(new Color(255,255,204));
                            } else {
                                tf.setBackground(new Color(255,102,102));
                            }
                        }
                    });
                }
            }

            frame.validate();
            frame.repaint();
        });

        // Submit action
        submitBtn.addActionListener(e -> {
            for (Component c : gridPanel.getComponents()) {
                if (c instanceof JTextField) {
                    JTextField tf = (JTextField)c;
                    if (tf.isEditable() && tf.getText().trim().isEmpty()) {
                        resultField.setText("Fill all the empty cells with valid answer...");
                        return;
                    }
                }
            }
            resultField.setText("Successfully Completed!!");
        });

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JPanel colorBox(Color c) {
        JPanel p = new JPanel();
        p.setBackground(c);
        p.setPreferredSize(new Dimension(20, 20));
        return p;
    }
}
