package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Puck is a Ball that represents a fallen piece in the Bricker game.
 * <p>
 * It behaves like a puck: when it moves below the bottom of the window,
 * it removes itself from the game via the game manager.
 *
 * @author Salah Mahmied, Kais Sora.
 */
public class Puck extends Ball{

    /**
     * Reference to the BrickerGameManager used to remove this puck when it exits the window.
     */
    protected BrickerGameManager brickerGameManager;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner  Position of the object, in window coordinates (pixels).
     *                       Note that (0,0) is the top-left corner of the window.
     * @param dimensions     Width and height in window coordinates.
     * @param renderable     The renderable representing the object. Can be null, in which case
     *                       the GameObject will not be rendered.
     */
    public Puck(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound,
                BrickerGameManager brickerGameManager) {
        super(topLeftCorner, dimensions, renderable, collisionSound);
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * Updates the puck position each frame and checks if it fell below the window.
     * <p>
     * If the puck's center Y-coordinate exceeds the window height, it is removed.
     *
     * @param deltaTime time elapsed since the last update call, in seconds
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        double height = this.getCenter().y();
        float windowHeight = this.brickerGameManager.getWindowDimensions().y();
        if(height > windowHeight) {
            this.brickerGameManager.removeObject(this);
        }
    }
}
