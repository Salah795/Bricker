package bricker.brick_strategies;

import bricker.gameobjects.Puck;
import bricker.main.BrickerGameManger;
import danogl.GameObject;

public class TurboStrategy extends BasicCollisionStrategy{
    public TurboStrategy(BrickerGameManger brickerGameManger) {
        super(brickerGameManger);
    }

    @Override
    public void onCollision(GameObject first, GameObject second) {
        super.onCollision(first, second);
        if(!(second instanceof Puck) && !brickerGameManger.getTurboMode()) {
            brickerGameManger.transferBallIntoTurboMode();
        }
    }
}
