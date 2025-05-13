package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * A basic collision strategy for the Bricker game.
 * <p>
 * When a collision is detected between the ball and a brick, this strategy
 * instructs the game manager to remove the brick.
 *
 * @author Salah Mahmied, Kais Sora.
 */
public class BasicCollisionStrategy implements CollisionStrategy {

    //Reference to the game manager used to remove bricks and manage game state.
    protected BrickerGameManager brickerGameManager;

    /**
     * Creates a BasicCollisionStrategy with the given BrickerGameManager.
     *
     * @param brickerGameManager the game manager responsible for handling brick removal
     */
    public BasicCollisionStrategy(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * Called when a collision occurs between two game objects.
     * <p>
     * Assumes the first object is a Brick and removes it via the game manager.
     *
     * @param first  the first GameObject involved in the collision (expected to be a Brick)
     * @param second the second GameObject involved in the collision
     */
    @Override
    public void onCollision(GameObject first, GameObject second) {
        this.brickerGameManager.removeBrick((Brick) first);
    }
}
