package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * Collision strategy that spawns pucks when a brick is hit.
 * <p>
 * Extends BasicCollisionStrategy by removing the brick and creating pucks at the brick's center.
 *
 * @author Salah Mahmied, Kais Sora.
 */
public class PucksStrategy extends BasicCollisionStrategy{
    /**
     * Constructs a PucksStrategy with the given game manager.
     *
     * @param brickerGameManager the game manager to handle brick removal and puck creation
     */
    public PucksStrategy(BrickerGameManager brickerGameManager) {
        super(brickerGameManager);
    }

    /**
     * Called when a collision occurs: removes the brick and spawns pucks at the collision point.
     *
     * @param first  the first GameObject involved in the collision (the brick)
     * @param second the second GameObject involved in the collision (usually the ball)
     */
    @Override
    public void onCollision(GameObject first, GameObject second) {
        super.onCollision(first, second);
        this.brickerGameManager.createPucks(first.getCenter());
    }
}
