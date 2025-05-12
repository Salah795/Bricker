package bricker.brick_strategies;

import bricker.main.BrickerGameManger;
import danogl.GameObject;

public class HeartsStrategy extends BasicCollisionStrategy{
    public HeartsStrategy(BrickerGameManger brickerGameManger) {
        super(brickerGameManger);
    }

    @Override
    public void onCollision(GameObject first, GameObject second) {
        super.onCollision(first, second);
        this.brickerGameManger.createFallenHearts(first.getCenter());
    }
}
