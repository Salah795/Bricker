package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * Collision strategy that awards an extra heart when a brick is hit.
 * <p>
 * Extends BasicCollisionStrategy to remove the brick and additionally
 * spawn a fallen heart at the collision location.
 *
 * @author Salah Mahmied, Kais Sora.
 */
public class HeartsStrategy extends BasicCollisionStrategy{
    /**
     * Constructs a HeartsStrategy with the given game manager.
     *
     * @param brickerGameManager the game manager to handle brick removal and heart creation
     */
    public HeartsStrategy(BrickerGameManager brickerGameManager) {
        super(brickerGameManager);
    }

    @Override
    public void onCollision(GameObject first, GameObject second) {
        super.onCollision(first, second);
        this.brickerGameManager.createFallenHearts(first.getCenter());
    }
}
