package view;

import javax.swing.*;
import java.awt.*;

/**
 * This View class contains the view of the snake game
 */
public class View {
    private JFrame jFrame = new JFrame();

    /**
     * Create the JFrame view of the game with the right number of rows and columns
     *
     * @param rows    number of rows of the game
     * @param columns number of columns of the game
     */
    public void create(int rows, int columns) {
        this.jFrame.setTitle("Snake game");
        this.jFrame.setSize(500, 500);
        this.jFrame.setLocationRelativeTo(null);
        this.jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(rows, columns));

        for (int i = 0; i < rows * columns; i++) {
            jPanel.add(new JPanel());
        }

        this.jFrame.setContentPane(jPanel);
        this.jFrame.setVisible(true);
    }

    /**
     * Update the view depending on the game board
     *
     * @param board The representation matrix of the game
     */
    public void update(char[][] board) {
        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[row].length; column++) {
                char element = board[row][column];
                Component jPanelComponent = this.jFrame.getContentPane().getComponent(row * board[row].length + column);

                if (element == 'W') { // wall
                    jPanelComponent.setBackground(Color.black);
                } else if (element == 'B' || element == 'T') { // snake body OR snake tail
                    jPanelComponent.setBackground(Color.lightGray);
                } else if (element == 'H') { // snake head
                    jPanelComponent.setBackground(Color.gray);
                } else if (element == 'A') { // apple
                    jPanelComponent.setBackground(Color.red);
                } else { // empty space
                    jPanelComponent.setBackground(Color.white);
                }
            }
        }

        jFrame.repaint();
    }
}
