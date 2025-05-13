package bricker.brick_strategies;

import bricker.main.BrickerGameManger;

/**
 * Factory to create CollisionStrategy instances based on a strategy index.
 * <p>
 * Maps integer indices to different collision behaviors for bricks in the Bricker game.
 *
 * @author Salah Mahmied, Kais Sora.
 */
public class CollisionStrategyFactory {

    //Index for the basic collision strategy that simply removes the brick.
    private static final int BASIC_COLLISION_STRATEGY_INDEX = 5;

    //Index for the strategy that spawns pucks when a brick is hit.
    private static final int PUCKS_COLLISION_STRATEGY_INDEX = 6;

    //Index for the strategy that spawns an extra paddle on collision.
    private static final int EXTRA_PADDLE_COLLISION_STRATEGY_INDEX = 7;

    //Index for the turbo mode strategy that boosts ball speed.
    private static final int TURBO_STRATEGY_INDEX = 8;

    //Index for the strategy that awards extra lives (hearts) on collision.
    private static final int HEARTS_STRATEGY_INDEX = 9;

    /**
     * Builds a CollisionStrategy implementation corresponding to the given index.
     *
     * @param strategyIndex the index indicating which collision behavior to use
     * @param brickerGameManger the game manager used to apply the collision effects
     * @return a CollisionStrategy instance matching the strategyIndex;
     *         defaults to DoubleBehaviorStrategy if index is unrecognized
     */
    public CollisionStrategy buildCollisionStrategy(int strategyIndex, BrickerGameManger brickerGameManger) {
        if(strategyIndex <= BASIC_COLLISION_STRATEGY_INDEX) {
            return new BasicCollisionStrategy(brickerGameManger);
        }
        if(strategyIndex == PUCKS_COLLISION_STRATEGY_INDEX) {
            return new PucksStrategy(brickerGameManger);
        }
        if(strategyIndex == EXTRA_PADDLE_COLLISION_STRATEGY_INDEX) {
            return new ExtraPaddleStrategy(brickerGameManger);
        }
        if(strategyIndex == TURBO_STRATEGY_INDEX) {
            return new TurboStrategy(brickerGameManger);
        }
        if(strategyIndex == HEARTS_STRATEGY_INDEX) {
            return new HeartsStrategy(brickerGameManger);
        }
        return new DoubleBehaviorStrategy(brickerGameManger);
    }
}
