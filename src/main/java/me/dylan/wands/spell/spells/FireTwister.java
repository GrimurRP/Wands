package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.SpellData;
import me.dylan.wands.spell.tools.KnockBack;
import me.dylan.wands.spell.types.Behavior;
import me.dylan.wands.spell.types.Circle;
import me.dylan.wands.spell.types.Circle.CirclePlacement;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class FireTwister implements SpellData {
    private final Behavior behavior;

    public FireTwister() {
        this.behavior = Circle.newBuilder(CirclePlacement.TARGET)
                .setCircleRadius(3)
                .setEntityDamage(7)
                .setSpellEffectRadius(3.0F)
                .setEffectDistance(30)
                .setEntityEffects(entity -> {
                    entity.getLocation().createExplosion(0.0f);
                    entity.setFireTicks(60);
                })
                .setMetersPerTick(2)
                .setCastSound(Sound.ENTITY_BLAZE_SHOOT)
                .setSpellRelativeEffects((loc, world) -> {
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 5, 0.3, 0.3, 0.3, 0.1, null, true);
                    world.spawnParticle(Particle.FLAME, loc, 2, 0.3, 0.3, 0.3, 0.1, null, true);
                    world.spawnParticle(Particle.LAVA, loc, 2, 0.3, 0.3, 0.3, 0, null, true);
                })
                .setKnockBack(KnockBack.SIDEWAYS)

                .setCircleHeight(1)
                .build();
    }

    @Override
    public Behavior getBehavior() {
        return behavior;
    }
}