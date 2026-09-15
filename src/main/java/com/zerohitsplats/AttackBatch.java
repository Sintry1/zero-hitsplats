package com.zerohitsplats;

/** Reconciles events at the packet-batch boundary, regardless of XP/animation order. */
final class AttackBatch
{
    private boolean animation;
    private boolean projectile;
    private boolean damageXp;
    private int batch;
    private int lastAnimation = -100;
    private int lastProjectile = -100;
    private int lastDamageXp = -100;

    void animation() { animation = true; }
    void projectile() { projectile = true; }
    void damageXp() { damageXp = true; }

    boolean finish()
    {
        // A projectile may first appear one packet batch after its launch animation.
        // Conversely, an animation notification can follow the projectile evidence.
        boolean attack = (animation && batch - lastProjectile > 1)
            || (projectile && batch - lastAnimation > 1);
        boolean zero = attack && !damageXp
            && !(projectile && !animation && batch - lastDamageXp <= 1);
        if (animation) { lastAnimation = batch; }
        if (projectile) { lastProjectile = batch; }
        if (damageXp) { lastDamageXp = batch; }
        animation = projectile = damageXp = false;
        batch++;
        return zero;
    }

    void clear()
    {
        animation = projectile = damageXp = false;
        batch = 0;
        lastAnimation = lastProjectile = lastDamageXp = -100;
    }
}
