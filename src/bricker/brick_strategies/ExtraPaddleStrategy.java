package bricker.brick_strategies;

import bricker.main.BrickerGameManger;
import danogl.GameObject;
import danogl.util.Counter;
import danogl.util.Vector2;

public class ExtraPaddleStrategy extends BasicCollisionStrategy{
    private static Counter extraPaddleCounter = new Counter(0);

    public ExtraPaddleStrategy(BrickerGameManger brickerGameManger) {
        super(brickerGameManger);
    }

    @Override
    public void onCollision(GameObject first, GameObject second) {
        super.onCollision(first, second);
        Vector2 extraPaddleLocation = new Vector2(brickerGameManger.getWindowDimensions().mult(0.5f));
        if(extraPaddleCounter.value() == 0) {
            extraPaddleCounter.increment();
            brickerGameManger.createExtraPaddle(extraPaddleLocation, extraPaddleCounter);
        }
    }
}
