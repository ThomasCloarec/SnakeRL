package model;

import ai.AI;
import util.Position;
import view.View;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This Game class contains the model of the snake game.
 * It uses the observer pattern to communicate with its viewers and AIs.
 */
public class Game {
    private static final int MIN_ROWS = 13;
    private static final int MIN_COLUMNS = 13;
    private final int EPISODES;
    private final int DELAY_BETWEEN_ROUND_MILLI;

    private final char[][] board;
    private final List<View> viewObservers = new ArrayList<>();
    private List<Position> snake;
    private Position apple;
    private int score;
    private AI AIObserver;

    /**
     * Game constructor to create the model of the snake game.
     *
     * @param rows    number of rows of the game (rows >= {@value Game#MIN_ROWS})
     * @param columns number of columns of the game (columns >= {@value Game#MIN_COLUMNS})
     */
    public Game(int rows, int columns, int episodes, int delayBetweenRoundMilli) {
        if (rows < Game.MIN_ROWS) {
            throw new IllegalArgumentException("Parameter 'row' of Game class constructor must be bigger than " + Game.MIN_ROWS + ".");
        } else if (columns < Game.MIN_COLUMNS) {
            throw new IllegalArgumentException("Parameter 'column' of Game class constructor must be bigger than " + Game.MIN_COLUMNS + ".");
        } else {
            this.board = new char[rows][columns];
            this.EPISODES = episodes;
            this.DELAY_BETWEEN_ROUND_MILLI = delayBetweenRoundMilli;
        }
    }

    /**
     * Launch the game (it will run all the episodes for the training)
     */
    public void launch() {
        new Thread(() -> {
            int episode = 0;
            while (episode < this.EPISODES) {
                this.newEpisode();

                boolean episodeFinished = false;
                char oldAction = ' ';
                while (!episodeFinished) {
                    this.updateViews();
                    this.updateAI();

                    try {
                        Thread.sleep(this.DELAY_BETWEEN_ROUND_MILLI);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    List<Character> allowedActions = new ArrayList<>();
                    Position nextPossiblePosition;

                    nextPossiblePosition = this.getNextSnakeHeadPosition('N');
                    if (this.snake.get(1).row != nextPossiblePosition.row || this.snake.get(1).column != nextPossiblePosition.column) {
                        allowedActions.add('N');
                    }
                    nextPossiblePosition = this.getNextSnakeHeadPosition('S');
                    if (this.snake.get(1).row != nextPossiblePosition.row || this.snake.get(1).column != nextPossiblePosition.column) {
                        allowedActions.add('S');
                    }
                    nextPossiblePosition = this.getNextSnakeHeadPosition('W');
                    if (this.snake.get(1).row != nextPossiblePosition.row || this.snake.get(1).column != nextPossiblePosition.column) {
                        allowedActions.add('W');
                    }
                    nextPossiblePosition = this.getNextSnakeHeadPosition('E');
                    if (this.snake.get(1).row != nextPossiblePosition.row || this.snake.get(1).column != nextPossiblePosition.column) {
                        allowedActions.add('E');
                    }

                    char action = this.AIObserver.getAction(this.snake.get(0), allowedActions);
                    Position nextSnakeHeadPosition;
                    if (action == ' ') {
                        action = oldAction;
                    }
                    nextSnakeHeadPosition = this.getNextSnakeHeadPosition(action);
                    if (nextSnakeHeadPosition.row == this.snake.get(1).row && nextSnakeHeadPosition.column == this.snake.get(1).column) {
                        action = oldAction;
                        nextSnakeHeadPosition = this.getNextSnakeHeadPosition(action);
                    }

                    char nextBoardSnakeHeadElement = this.board[nextSnakeHeadPosition.row][nextSnakeHeadPosition.column];
                    if (nextBoardSnakeHeadElement == 'W') {
                        episodeFinished = true;
                    } else if (nextBoardSnakeHeadElement == 'B') {
                        episodeFinished = true;
                    } else if (nextBoardSnakeHeadElement == 'A') {
                        score++;
                        this.board[this.snake.get(0).row][this.snake.get(0).column] = 'B';
                        this.snake.add(0, nextSnakeHeadPosition);
                        this.board[this.snake.get(0).row][this.snake.get(0).column] = 'H';

                        boolean appleOnFloor = false;
                        while (!appleOnFloor) {
                            int randomRow = (int) (Math.random() * this.board.length);
                            int randomColumn = (int) (Math.random() * this.board[0].length);
                            if (this.board[randomRow][randomColumn] == ' ') {
                                this.board[randomRow][randomColumn] = 'A';
                                appleOnFloor = true;
                            }
                        }
                    } else {
                        this.board[this.snake.get(this.snake.size() - 1).row][this.snake.get(this.snake.size() - 1).column] = ' ';
                        this.snake.remove(this.snake.size() - 1);
                        this.board[this.snake.get(this.snake.size() - 1).row][this.snake.get(this.snake.size() - 1).column] = 'T';

                        this.board[this.snake.get(0).row][this.snake.get(0).column] = 'B';
                        this.snake.add(0, nextSnakeHeadPosition);
                        this.board[this.snake.get(0).row][this.snake.get(0).column] = 'H';
                    }

                    oldAction = action;
                }

                episode++;
            }
        }).start();
    }

    public Position getNextSnakeHeadPosition(char action) {
        Position nextSnakeHeadPosition = null;

        if (action == 'N') {
            nextSnakeHeadPosition = new Position(this.snake.get(0).row - 1, this.snake.get(0).column);
        } else if (action == 'S') {
            nextSnakeHeadPosition = new Position(this.snake.get(0).row + 1, this.snake.get(0).column);
        } else if (action == 'W') {
            nextSnakeHeadPosition = new Position(this.snake.get(0).row, this.snake.get(0).column - 1);
        } else if (action == 'E') {
            nextSnakeHeadPosition = new Position(this.snake.get(0).row, this.snake.get(0).column + 1);
        }

        return nextSnakeHeadPosition;
    }

    /**
     * Start a new episode of the training (one game iteration)
     */
    public void newEpisode() {
        this.score = 0;

        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[0].length; j++) {
                if (i == 0 || i == this.board.length - 1 || j == 0 || j == this.board[0].length - 1) {
                    this.board[i][j] = 'W';
                } else {
                    this.board[i][j] = ' ';
                }
            }
        }

        Position snakeHead = new Position(this.board.length / 2, this.board[0].length / 2 - 1);
        this.snake = new ArrayList<>();
        this.snake.add(snakeHead);
        this.snake.add(new Position(snakeHead.row, snakeHead.column - 1));
        this.snake.add(new Position(snakeHead.row, snakeHead.column - 2));
        this.snake.add(new Position(snakeHead.row, snakeHead.column - 3));

        for (int i = 0; i < this.snake.size(); i++) {
            Position snakeElement = this.snake.get(i);
            if (i == 0) {
                this.board[snakeElement.row][snakeElement.column] = 'H';
            } else if (i == this.snake.size() - 1) {
                this.board[snakeElement.row][snakeElement.column] = 'T';
            } else {
                this.board[snakeElement.row][snakeElement.column] = 'B';
            }
        }

        this.apple = new Position(snakeHead.row, snakeHead.column + 4);
        this.board[this.apple.row][this.apple.column] = 'A';
    }

    /**
     * Add a view observer to the game instance
     *
     * @param view A view which will be called when the game update
     */
    public void addViewObserver(View view) {
        SwingUtilities.invokeLater(() -> view.create(this.board.length, this.board[0].length));
        this.viewObservers.add(view);
    }

    /**
     * Call the view observers to update them
     */
    public void updateViews() {
        for (View view : viewObservers) {
            SwingUtilities.invokeLater(() -> view.update(this.board));
        }
    }

    /**
     * Add an ai observer to the game instance
     *
     * @param ai An ai which will be called when the game needs an action
     */
    public void addIAObserver(AI ai) {
        ai.create(this.board.length, this.board[0].length);
        this.AIObserver = ai;
    }

    public void updateAI() {
        this.AIObserver.update(this.board);
    }
}
