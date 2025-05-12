package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import bricker.main.BrickerGameManger;
import danogl.GameObject;

public class BasicCollisionStrategy implements CollisionStrategy {
    protected BrickerGameManger brickerGameManger;

    public BasicCollisionStrategy(BrickerGameManger brickerGameManger) {
        this.brickerGameManger = brickerGameManger;
    }

    public void onCollision(GameObject first, GameObject second) {
        this.brickerGameManger.removeBrick((Brick) first);
    }
}
