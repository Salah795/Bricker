package bricker.brick_strategies;

import bricker.main.BrickerGameManger;
import danogl.GameObject;
import danogl.util.Counter;
import danogl.util.Vector2;

public class ExtraPaddleStrategy extends BasicCollisionStrategy{
    public ExtraPaddleStrategy(BrickerGameManger brickerGameManger) {
        super(brickerGameManger);
    }

    @Override
    public void onCollision(GameObject first, GameObject second) {
        super.onCollision(first, second);
        Vector2 extraPaddleLocation = new Vector2(brickerGameManger.getWindowDimensions().mult(0.5f));
            brickerGameManger.createExtraPaddle(extraPaddleLocation);
    }
}
