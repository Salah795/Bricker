package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.util.Vector2;

/**
 * Collision strategy that spawns an extra paddle when a brick is hit.
 * <p>
 * Extends BasicCollisionStrategy to remove the brick and create an extra paddle
 * at the center of the window.
 *
 * @author Salah Mahmied, Kais Sora.
 */
public class ExtraPaddleStrategy extends BasicCollisionStrategy{
    /**
     * Constructs an ExtraPaddleStrategy with the given game manager.
     *
     * @param brickerGameManager the game manager to handle brick removal and paddle creation
     */
    public ExtraPaddleStrategy(BrickerGameManager brickerGameManager) {
        super(brickerGameManager);
    }

    @Override
    public void onCollision(GameObject first, GameObject second) {
        super.onCollision(first, second);
        Vector2 extraPaddleLocation = new Vector2(brickerGameManager.getWindowDimensions().mult(0.5f));
        brickerGameManager.createExtraPaddle(extraPaddleLocation);
    }
}
