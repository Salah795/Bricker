package bricker.gameobjects;

import bricker.main.BrickerGameManger;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Puck extends Ball{
    protected BrickerGameManger brickerGameManger;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner  Position of the object, in window coordinates (pixels).
     *                       Note that (0,0) is the top-left corner of the window.
     * @param dimensions     Width and height in window coordinates.
     * @param renderable     The renderable representing the object. Can be null, in which case
     *                       the GameObject will not be rendered.
     * @param collisionSound
     */
    public Puck(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound,
                BrickerGameManger brickerGameManger) {
        super(topLeftCorner, dimensions, renderable, collisionSound);
        this.brickerGameManger = brickerGameManger;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        double height = this.getCenter().y();
        float windowHeight = this.brickerGameManger.getWindowDimensions().y();
        if(height > windowHeight) {
            this.brickerGameManger.removeObject(this);
        }
    }
}
