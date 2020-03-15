package ai;

import util.Position;

import java.util.List;

/**
 * The AI class contains an agent behavior for the snake game
 */
public abstract class AI {
    public abstract void create(int rows, int columns);

    public abstract void update(char[][] board);

    public abstract char getAction(Position snakeHeadPosition, List<Character> allowedActions);
}
