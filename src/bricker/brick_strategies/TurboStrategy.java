package bricker.brick_strategies;

import bricker.gameobjects.Puck;
import bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * Collision strategy that activates turbo mode on the ball upon collision.
 * <p>
 * Extends BasicCollisionStrategy by transferring the ball into turbo mode
 * unless the collider is a puck and turbo is already active.
 *
 * @author Salah Mahmied, Kais Sora.
 */
public class TurboStrategy extends BasicCollisionStrategy{
    /**
     * Constructs a TurboStrategy with the specified game manager.
     *
     * @param brickerGameManager the game manager to handle turbo mode activation
     */
    public TurboStrategy(BrickerGameManager brickerGameManager) {
        super(brickerGameManager);
    }

    @Override
    public void onCollision(GameObject first, GameObject second) {
        super.onCollision(first, second);
        if(!(second instanceof Puck) && !brickerGameManager.getTurboMode()) {
            brickerGameManager.transferBallIntoTurboMode();
        }
    }
}
