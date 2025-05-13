package bricker.brick_strategies;


/**
 * A decorator interface for CollisionStrategy implementations.
 * <p>
 * Implementations wrap another CollisionStrategy to augment or modify
 * its behavior when a collision occurs.
 *
 * @author Salah Mahmied, Kais Sora.
 */
public interface CollisionDecorator extends CollisionStrategy{ }
