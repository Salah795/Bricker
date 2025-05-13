
package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Wall represents a static boundary in the Bricker game.
 * <p>
 * When the ball or other game object collides with the wall, it bounces off
 * by flipping its velocity vector based on the collision normal.
 *
 * @author Salah Mahmied, Kias Sora.
 */
public class Wall extends GameObject {
    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Wall(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
    }

    /**
     * Called when another GameObject enters collision with this wall.
     * <p>
     * Computes the new velocity by reflecting the current velocity across
     * the collision normal and applies it to the wall's GameObject.
     *
     * @param other     the GameObject that collided with this wall
     * @param collision the Collision object containing contact information, including normal
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        Vector2 newVelocity = getVelocity().flipped(collision.getNormal());
        setVelocity(newVelocity);
    }
}
