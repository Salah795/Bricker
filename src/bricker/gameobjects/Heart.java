package bricker.gameobjects;

import bricker.main.BrickerGameManger;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;


/**
 * Heart represents a collectible life token in the Bricker game.
 * <p>
 * Extends Puck to fall until caught by the paddle, granting an extra life.
 *
 * @author Salah Mahmied, Kais Sora.
 */
public class Heart extends Puck {

    //Maximum number of lives the player can have.
    private static final int POSSIBLE_LIVES = 4;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner     Position of the object, in window coordinates (pixels).
     *                          Note that (0,0) is the top-left corner of the window.
     * @param dimensions        Width and height in window coordinates.
     * @param renderable        The renderable representing the object. Can be null, in which case
     *                          the GameObject will not be rendered.
     * @param collisionSound    the Sound to play when the heart collides with the paddle
     * @param brickerGameManger the game manager used to remove this heart and update lives
     */
    public Heart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound,
                 BrickerGameManger brickerGameManger) {
        super(topLeftCorner, dimensions, renderable, collisionSound, brickerGameManger);
    }

    /**
     * Determines if this heart should collide with another object.
     * <p>
     * Only collides with the paddle when at the paddle's vertical position.
     *
     * @param other the GameObject to test collision with
     * @return true if the other object is the paddle at the catching height
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        if(other.renderer().getRenderable() != null) {
            return other.renderer().getRenderable().equals(this.brickerGameManger.getPaddleImage()) && (
                    other.getCenter().y() == brickerGameManger.getMainPaddleHeight());
        }
        return false;
    }

    /**
     * Called when the heart collides with another object.
     * <p>
     * Removes the heart and, if below max lives, increments the player's life count.
     *
     * @param other     the GameObject that collided (expected to be the paddle)
     * @param collision collision details (unused for hearts)
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        brickerGameManger.removeObject(this);
        if(this.brickerGameManger.getLivesCounter().value() < POSSIBLE_LIVES) {
            this.brickerGameManger.getLivesCounter().increment();
            brickerGameManger.incrementLivesCounter();
        }
    }
}
