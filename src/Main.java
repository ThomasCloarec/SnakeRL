import ai.AI;
import ai.DynamicProgrammingPlanning;
import model.Game;
import view.View;

/**
 * An implementation of several machine learning (or maybe some non-ML) algorithms for the game of snake.
 *
 * @author Thomas Cloarec - 2020
 */

public class Main {
    public static void main(String[] args) {
        Game game = new Game(21, 21, 100, 50);
        View view = new View();
        game.addViewObserver(view);
        AI ai = new DynamicProgrammingPlanning();
        game.addIAObserver(ai);
        game.launch();
    }
}
