package ai;

import util.Position;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * An implementation of a value based dynamic programming planning algorithm using the Bellman equation for Markov Decision Process
 */
public class DynamicProgrammingPlanning extends AI {
    private final HashMap<Character, Double> rewardPerElement = new HashMap<>();
    private final double DISCOUNT_FACTOR = 0.95;
    private final double DELTA_THRESHOLD = 0.0001;
    private double[][] rewards;
    private double[][] values;

    public DynamicProgrammingPlanning() {
        super();

        this.rewardPerElement.put('W', -10E12d);
        this.rewardPerElement.put('B', -10E12d);
        this.rewardPerElement.put('T', -1d);
        this.rewardPerElement.put('H', -1d);
        this.rewardPerElement.put('A', 10E9d);
        this.rewardPerElement.put(' ', -1d);
    }

    /**
     * Create rewards and values matrix of the AI
     *
     * @param rows    number of rows
     * @param columns number of columns
     */
    @Override
    public void create(int rows, int columns) {
        this.rewards = new double[rows][columns];
        this.values = new double[rows][columns];
    }

    /**
     * Update the reward matrix of the AI depending on the game board
     */
    @Override
    public void update(char[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                char boardElement = board[i][j];
                this.rewards[i][j] = this.rewardPerElement.get(boardElement);
            }
        }
    }

    /**
     * Get the action of the AI
     *
     * @param snakeHeadPosition The position of the snake head
     * @param allowedActions    The actions allowed by the game
     * @return The chosen action of the AI
     */
    @Override
    public char getAction(Position snakeHeadPosition, List<Character> allowedActions) {
        char action = ' ';
        double northValue = -Double.MAX_VALUE, southValue = -Double.MAX_VALUE, westValue = -Double.MAX_VALUE, eastValue = -Double.MAX_VALUE;
        this.updateValues();

        if (allowedActions.contains('N')) {
            northValue = this.values[snakeHeadPosition.row - 1][snakeHeadPosition.column];
        }
        if (allowedActions.contains('S')) {
            southValue = this.values[snakeHeadPosition.row + 1][snakeHeadPosition.column];
        }
        if (allowedActions.contains('W')) {
            westValue = this.values[snakeHeadPosition.row][snakeHeadPosition.column - 1];
        }
        if (allowedActions.contains('E')) {
            eastValue = this.values[snakeHeadPosition.row][snakeHeadPosition.column + 1];
        }

        double[] valuesAround = {northValue, southValue, westValue, eastValue};
        Arrays.sort(valuesAround);
        double biggestValue = valuesAround[valuesAround.length - 1];

        if (northValue == biggestValue) {
            action = 'N';
        } else if (southValue == biggestValue) {
            action = 'S';
        } else if (westValue == biggestValue) {
            action = 'W';
        } else if (eastValue == biggestValue) {
            action = 'E';
        }

        return action;
    }

    /**
     * Update the value matrix of the AI
     */
    public void updateValues() {
        double delta = Double.MAX_VALUE;

        while (delta > this.DELTA_THRESHOLD) {
            for (int i = 0; i < this.values.length; i++) {
                for (int j = 0; j < this.values[i].length; j++) {
                    double oldValue = this.values[i][j];
                    this.values[i][j] = this.rewards[i][j] + this.DISCOUNT_FACTOR * this.getMaxValueAround(i, j);
                    delta = Math.abs(this.values[i][j] - oldValue);
                }
            }
        }
    }

    /**
     * Get the maximum value around a certain position (North, South, West, East)
     *
     * @param row    The position row
     * @param column The position column
     * @return The maximum value
     */
    public double getMaxValueAround(int row, int column) {
        double northValue = -Double.MAX_VALUE, southValue = -Double.MAX_VALUE, westValue = -Double.MAX_VALUE, eastValue = -Double.MAX_VALUE;

        if (row - 1 >= 0) {
            northValue = this.values[row - 1][column];
        }
        if (row + 1 < this.values.length) {
            southValue = this.values[row + 1][column];
        }
        if (column - 1 >= 0) {
            westValue = this.values[row][column - 1];
        }
        if (column + 1 < this.values[0].length) {
            eastValue = this.values[row][column + 1];
        }

        double[] valuesAround = {northValue, southValue, westValue, eastValue};
        Arrays.sort(valuesAround);
        return valuesAround[valuesAround.length - 1];
    }
}
