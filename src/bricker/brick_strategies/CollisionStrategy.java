package bricker.brick_strategies;

import danogl.GameObject;

public interface CollisionStrategy{
    void onCollision(GameObject first, GameObject second);
}
