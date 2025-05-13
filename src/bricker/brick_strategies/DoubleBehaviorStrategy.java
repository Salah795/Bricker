package bricker.brick_strategies;

import bricker.main.BrickerGameManger;
import danogl.GameObject;

import java.util.Random;

public class DoubleBehaviorStrategy implements CollisionStrategy {
    private static final int FIRST_POSSIBLE_RANDOM_INDEX = 6;
    private static final int LAST_POSSIBLE_RANDOM_INDEX = 10;
    private static final int POSSIBLE_STRATEGIES = 3;

    private CollisionStrategy[] collisionStrategies;
    private BrickerGameManger brickerGameManger;
    private CollisionStrategyFactory collisionStrategyFactory;
    private Random random;
    private int filledStrategies;

    public DoubleBehaviorStrategy(BrickerGameManger brickerGameManger) {
        this.collisionStrategies = new CollisionStrategy[POSSIBLE_STRATEGIES];
        this.filledStrategies = POSSIBLE_STRATEGIES - 1;
        this.brickerGameManger = brickerGameManger;
        this.collisionStrategyFactory = new CollisionStrategyFactory();
        this.random = new Random();
        chooseStrategies();
    }

    public void chooseStrategies() {
        for(int index = 0; index < POSSIBLE_STRATEGIES - 1; index++) {
            int chosenIndex = this.random.nextInt(FIRST_POSSIBLE_RANDOM_INDEX,
                    LAST_POSSIBLE_RANDOM_INDEX + 1);
            this.collisionStrategies[index] = this.collisionStrategyFactory.buildCollisionStrategy(chosenIndex,
                    brickerGameManger);
            if(chosenIndex == LAST_POSSIBLE_RANDOM_INDEX) {
                filledStrategies++;
                chosenIndex = this.random.nextInt(FIRST_POSSIBLE_RANDOM_INDEX,
                        LAST_POSSIBLE_RANDOM_INDEX);
                this.collisionStrategies[index] = this.collisionStrategyFactory.buildCollisionStrategy(chosenIndex,
                        brickerGameManger);
                chosenIndex = this.random.nextInt(FIRST_POSSIBLE_RANDOM_INDEX,
                        LAST_POSSIBLE_RANDOM_INDEX);
                this.collisionStrategies[POSSIBLE_STRATEGIES - 1] = this.collisionStrategyFactory.
                        buildCollisionStrategy(chosenIndex, brickerGameManger);
            }
        }
    }

    @Override
    public void onCollision(GameObject first, GameObject second) {
        for(int index = 0; index < filledStrategies - 1; index++) {
            this.collisionStrategies[index].onCollision(first, second);
        }
    }
}
