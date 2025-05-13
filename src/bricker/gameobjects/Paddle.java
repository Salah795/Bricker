package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * Paddle represents the player's paddle in the Bricker game.
 * <p>
 * It handles user input to move left and right and constrains movement
 * within the game window boundaries.
 *
 * @author Salah Mahmied, Kais Sora.
 */
public class Paddle extends GameObject {

    //The movement speed of the paddle in pixels per second.
    private static final float MOVMENT_SPEED = 300;

    //Listener for user keyboard input to control paddle movement.
    private final UserInputListener inputListener;

    //Dimensions of the game window, used to clamp paddle position.
    private final Vector2 windowDimensions;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner    Position of the object, in window coordinates (pixels).
     *                         Note that (0,0) is the top-left corner of the window.
     * @param dimensions       Width and height in window coordinates.
     * @param renderable       The renderable representing the object. Can be null, in which case
     *                         the GameObject will not be rendered.
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                  UserInputListener inputListener, Vector2 windowDimensions) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 movmentDirection = Vector2.ZERO;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)){
            movmentDirection = movmentDirection.add(Vector2.LEFT);
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)){
            movmentDirection = movmentDirection.add(Vector2.RIGHT);
        }
        setVelocity(movmentDirection.mult(MOVMENT_SPEED));

        float rightEdge = windowDimensions.x() - BrickerGameManager.WALL_WIDTH - getDimensions().x();
        if (this.getTopLeftCorner().x() < BrickerGameManager.WALL_WIDTH){
            this.setTopLeftCorner(new Vector2(BrickerGameManager.WALL_WIDTH, this.getTopLeftCorner().y()));
        }
        if (this.getTopLeftCorner().x() > rightEdge){
            this.setTopLeftCorner(new Vector2(rightEdge, this.getTopLeftCorner().y()));
        }
    }
}

