package bricker.gameobjects;

import bricker.main.BrickerGameManger;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * ExtraPaddle is a special paddle that disappears after a set number of collisions.
 * <p>
 * Extends Paddle to allow a limited number of hits before removal from the game.
 *
 * @author Salah Mahmied, Kais Sora.
 */
public class ExtraPaddle extends Paddle{

    //Maximum number of collisions this extra paddle can sustain before being removed.
    private static final int MAX_COLLISIONS = 4;

    //Reference to the game manager for handling object removal and accessing window dimensions.
    private final BrickerGameManger brickerGameManger;

    //Tracks the number of collisions this extra paddle has experienced.
    private int collisionsCounter;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner     Position of the object, in window coordinates (pixels).
     *                          Note that (0,0) is the top-left corner of the window.
     * @param dimensions        Width and height in window coordinates.
     * @param renderable        The renderable representing the object. Can be null, in which case
     *                          the GameObject will not be rendered.
     * @param inputListener    listener for user input to move the paddle
     * @param brickerGameManger the game manager used to remove this paddle upon max collisions
     */
    public ExtraPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                       UserInputListener inputListener, BrickerGameManger brickerGameManger) {
        super(topLeftCorner, dimensions, renderable, inputListener, brickerGameManger.getWindowDimensions());
        this.collisionsCounter = 0;
        this.brickerGameManger = brickerGameManger;
    }

    /**
     * Handles collision events: increments the collision counter and removes
     * the paddle once it reaches the maximum collision count.
     *
     * @param other     the GameObject that collided with this paddle
     * @param collision collision details including the impact information
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        this.collisionsCounter++;
        if(this.collisionsCounter == MAX_COLLISIONS) {
            this.brickerGameManger.removeObject(this);
        }

    }
}

