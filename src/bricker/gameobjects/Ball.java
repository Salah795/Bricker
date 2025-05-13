package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;


/**
 * Ball represents a moving ball in the Bricker game.
 * <p>
 * Handles collision response by reflecting velocity, playing a collision sound,
 * and counting the number of collisions.
 *
 * @author Salah Mahmied, Kais Sora.
 */
public class Ball extends GameObject {

    //Sound played whenever the ball collides with another object.
    private final Sound collisionSound;

    //Tracks the total number of collisions the ball has experienced.
    private int collisionCounter;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param collisionSound the Sound to play when the ball collides
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
        collisionCounter = 0;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        this.collisionCounter++;
        super.onCollisionEnter(other, collision);
        Vector2 newVal = getVelocity().flipped(collision.getNormal());
        setVelocity(newVal);
        this.collisionSound.play();
    }

    /**
     * Returns the number of collisions this ball has had since creation.
     *
     * @return the collision count
     */
    public int getCollisionCounter() {return collisionCounter;}
}



