package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

import java.util.Random;

/**
 * A decorator that applies two randomly chosen collision strategies on each collision.
 * <p>
 * It selects a pair of strategies from available collision behaviors and
 * executes both when a collision occurs, enabling combined effects.
 *
 * @author Salah Mahmied, Kais Sora.
 */
public class DoubleBehaviorStrategy implements CollisionDecorator {

    /**
     * Minimum index (inclusive) for selecting a collision strategy at random.
     */
    private static final int FIRST_POSSIBLE_RANDOM_INDEX = 6;

    /**
     * Maximum index (inclusive) for selecting a collision strategy at random.
     */
    private static final int LAST_POSSIBLE_RANDOM_INDEX = 10;

    /**
     * Total number of strategies to select and execute on collision.
     */
    private static final int POSSIBLE_STRATEGIES = 3;

    /**
     * Reference to the Bricker game manager for applying collision effects.
     */
    private final BrickerGameManager brickerGameManager;

    /**
     * Factory used to create collision strategy instances based on an index.
     */
    private final CollisionStrategyFactory collisionStrategyFactory;

    /**
     * Random number generator for selecting strategy indices.
     */
    private final Random random;

    /**
     * Array holding the selected collision strategies to execute.
     */
    private final CollisionStrategy[] collisionStrategies;

    /**
     * Number of strategies currently populated in the strategies array.
     */
    private int filledStrategies;

    /**
     * Upper bound for random index selection during strategy choice.
     */
    private int currentPossibleIndex;

    /**
     * Constructs the decorator with the given game manager and initializes strategy selection.
     *
     * @param brickerGameManager the game manager used to remove bricks and handle collisions
     */
    public DoubleBehaviorStrategy(BrickerGameManager brickerGameManager) {
        this.collisionStrategies = new CollisionStrategy[POSSIBLE_STRATEGIES];
        this.currentPossibleIndex = LAST_POSSIBLE_RANDOM_INDEX;
        this.filledStrategies = POSSIBLE_STRATEGIES - 1;
        this.brickerGameManager = brickerGameManager;
        this.collisionStrategyFactory = new CollisionStrategyFactory();
        this.random = new Random();
        chooseStrategies();
    }

    /**
     * Randomly selects two collision strategies from the available range
     * and populates the internal array for execution on collisions.
     */
    public void chooseStrategies() {
        for(int index = 0; index < POSSIBLE_STRATEGIES - 1; index++) {
            int chosenIndex = this.random.nextInt(FIRST_POSSIBLE_RANDOM_INDEX,
                    currentPossibleIndex + 1);
            if(chosenIndex == LAST_POSSIBLE_RANDOM_INDEX) {
                this.currentPossibleIndex--;
                filledStrategies++;
                chosenIndex = this.random.nextInt(FIRST_POSSIBLE_RANDOM_INDEX,
                        LAST_POSSIBLE_RANDOM_INDEX);
                this.collisionStrategies[index] = this.collisionStrategyFactory.
                        buildCollisionStrategy(chosenIndex, brickerGameManager);
                chosenIndex = this.random.nextInt(FIRST_POSSIBLE_RANDOM_INDEX,
                        LAST_POSSIBLE_RANDOM_INDEX);
                this.collisionStrategies[POSSIBLE_STRATEGIES - 1] = this.collisionStrategyFactory.
                        buildCollisionStrategy(chosenIndex, brickerGameManager);
            } else {
                this.collisionStrategies[index] = this.collisionStrategyFactory.
                        buildCollisionStrategy(chosenIndex, brickerGameManager);
            }
        }
    }

    /**
     * Executes each selected collision strategy when a collision occurs.
     *
     * @param first  the first GameObject involved in the collision
     * @param second the second GameObject involved in the collision
     */
    @Override
    public void onCollision(GameObject first, GameObject second) {
        for(int index = 0; index < filledStrategies; index++) {
            this.collisionStrategies[index].onCollision(first, second);
        }
    }
}
