package bricker.gameobjects;

import bricker.main.BrickerGameManger;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Heart extends Puck {
    private static final int POSSIBLE_LIVES = 4;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner     Position of the object, in window coordinates (pixels).
     *                          Note that (0,0) is the top-left corner of the window.
     * @param dimensions        Width and height in window coordinates.
     * @param renderable        The renderable representing the object. Can be null, in which case
     *                          the GameObject will not be rendered.
     * @param collisionSound
     * @param brickerGameManger
     */
    public Heart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound,
                 BrickerGameManger brickerGameManger) {
        super(topLeftCorner, dimensions, renderable, collisionSound, brickerGameManger);
    }

    @Override
    public boolean shouldCollideWith(GameObject other) {
        if(other.renderer().getRenderable() != null) {
            return other.renderer().getRenderable().equals(this.brickerGameManger.getPaddleImage()) && (
                    other.getCenter().y() == brickerGameManger.getMainPaddleHeight());
        }
        return false;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
//        brickerGameManger.removeObject(this);
//        if(this.brickerGameManger.getLivesCounter().value() < POSSIBLE_LIVES) {
//            this.brickerGameManger.getLivesCounter().increment();
//            brickerGameManger.incrementLivesCounter();
//        }
    }
}
