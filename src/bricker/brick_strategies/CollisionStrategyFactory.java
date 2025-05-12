package bricker.brick_strategies;

import bricker.main.BrickerGameManger;

public class CollisionStrategyFactory {
    private static final int BASIC_COLLISION_STRATEGY_INDEX = 5;
    private static final int PUCKS_COLLISION_STRATEGY_INDEX = 6;
    private static final int EXTRA_PADDLE_COLLISION_STRATEGY_INDEX = 7;
    private static final int TURBO_STRATEGY_INDEX = 8;
    private static final int HEARTS_STRATEGY_INDEX = 9;

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
    }
}
