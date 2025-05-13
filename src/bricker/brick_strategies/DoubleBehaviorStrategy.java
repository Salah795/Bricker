package bricker.brick_strategies;

import danogl.GameObject;

import java.util.Random;

public class DoubleBehaviorStrategy implements CollisionStrategy {
    private CollisionStrategy[] collisionStrategies;
    private Random random;


    public DoubleBehaviorStrategy() {
        this.collisionStrategies = new CollisionStrategy[2];
        this.random = new Random();
    }

    private void ChooseStrategies() {

    }

    @Override
    public void onCollision(GameObject first, GameObject second) {
        for(CollisionStrategy collisionStrategy : this.collisionStrategies) {
            collisionStrategy.onCollision(first, second);
        }
    }
}
