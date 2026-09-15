package com.zerohitsplats;

import org.junit.Test;
import static org.junit.Assert.*;

public class AttackBatchTest
{
    @Test public void missProducesExactlyOneDrop()
    {
        AttackBatch batch = new AttackBatch();
        batch.animation(); batch.animation();
        assertTrue(batch.finish());
        assertFalse(batch.finish());
    }

    @Test public void damageXpSuppressesRegardlessOfEventOrder()
    {
        AttackBatch batch = new AttackBatch();
        batch.damageXp(); batch.animation(); assertFalse(batch.finish());
        batch.animation(); batch.damageXp(); assertFalse(batch.finish());
    }

    @Test public void projectileAndAnimationAreOneAttack()
    {
        AttackBatch batch = new AttackBatch();
        batch.animation(); batch.projectile(); assertTrue(batch.finish());
        assertFalse(batch.finish());
    }

    @Test public void delayedProjectileDoesNotDuplicateMiss()
    {
        AttackBatch batch = new AttackBatch();
        batch.animation(); assertTrue(batch.finish());
        batch.projectile(); assertFalse(batch.finish());
    }

    @Test public void delayedProjectileDoesNotTurnHitIntoMiss()
    {
        AttackBatch batch = new AttackBatch();
        batch.animation(); batch.damageXp(); assertFalse(batch.finish());
        batch.projectile(); assertFalse(batch.finish());
    }

    @Test public void repeatedTwoTickShotsWithoutNewAnimationWork()
    {
        AttackBatch batch = new AttackBatch();
        batch.animation(); batch.projectile(); assertTrue(batch.finish());
        assertFalse(batch.finish());
        batch.projectile(); assertTrue(batch.finish());
        assertFalse(batch.finish());
        batch.projectile(); batch.damageXp(); assertFalse(batch.finish());
    }

    @Test public void xpWithoutAttackDoesNotProduceDrop()
    {
        AttackBatch batch = new AttackBatch();
        batch.damageXp(); assertFalse(batch.finish());
        assertFalse(batch.finish());
    }

    @Test public void logoutDiscardsPendingAttack()
    {
        AttackBatch batch = new AttackBatch();
        batch.animation(); batch.clear(); assertFalse(batch.finish());
        batch.animation(); assertTrue(batch.finish());
    }
}
