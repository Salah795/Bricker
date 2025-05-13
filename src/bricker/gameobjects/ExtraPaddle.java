package bricker.gameobjects;

import bricker.main.BrickerGameManger;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class ExtraPaddle extends Paddle{
    private static final int MAX_COLLISIONS = 4;

    private int collisionsCounter;
    private BrickerGameManger brickerGameManger;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner     Position of the object, in window coordinates (pixels).
     *                          Note that (0,0) is the top-left corner of the window.
     * @param dimensions        Width and height in window coordinates.
     * @param renderable        The renderable representing the object. Can be null, in which case
     *                          the GameObject will not be rendered.
     * @param inputListener
     * @param brickerGameManger
     */
    public ExtraPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                       UserInputListener inputListener, BrickerGameManger brickerGameManger) {
        super(topLeftCorner, dimensions, renderable, inputListener, brickerGameManger.getWindowDimensions());
        this.collisionsCounter = 0;
        this.brickerGameManger = brickerGameManger;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        this.collisionsCounter++;
        if(this.collisionsCounter == MAX_COLLISIONS) {
            this.brickerGameManger.removeObject(this);
        }

    }
}
