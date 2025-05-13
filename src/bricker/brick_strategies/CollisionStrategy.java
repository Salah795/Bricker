package bricker.brick_strategies;

import danogl.GameObject;

/**
 * Strategy interface for handling collisions between game objects in the Bricker game.
 * <p>
 * Implementations define the behavior when two GameObject instances collide.
 *
 * @author Salah Mahmied, Kais Sora.
 */
public interface CollisionStrategy{
    /**
     * Called when a collision occurs between two GameObject instances.
     *
     * @param first  the first GameObject involved in the collision
     * @param second the second GameObject involved in the collision
     */
    void onCollision(GameObject first, GameObject second);
}
