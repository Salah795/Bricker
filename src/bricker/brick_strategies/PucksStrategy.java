package bricker.brick_strategies;

import bricker.main.BrickerGameManger;
import danogl.GameObject;

public class PucksStrategy extends BasicCollisionStrategy{
    public PucksStrategy(BrickerGameManger brickerGameManger) {
        super(brickerGameManger);
    }

    @Override
    public void onCollision(GameObject first, GameObject second) {
        super.onCollision(first, second);
        this.brickerGameManger.createPucks(first.getCenter());
    }
}
