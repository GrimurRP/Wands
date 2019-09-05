package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.SpellData;
import me.dylan.wands.spell.tools.sound.CompoundSound;
import me.dylan.wands.spell.types.Behavior;
import me.dylan.wands.spell.types.Wave;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class FlameWave implements SpellData {
    private final Behavior behavior;

    public FlameWave() {
        this.behavior = Wave.newBuilder()
                .setSpellEffectRadius(2.5F)
                .setEntityDamage(6)
                .setEffectDistance(20)
                .setCastSound(CompoundSound.chain()
                        .add(Sound.ENTITY_BLAZE_SHOOT)
                        .addAll(Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 0, 3, 3, 3, 3, 3)
                )
                .setEntityEffects(entity -> entity.setFireTicks(140))
                .setSpellRelativeEffects((loc, world) -> {
                    world.spawnParticle(Particle.FLAME, loc, 10, 0.8, 0.8, 0.8, 0.1, null, true);
                    world.spawnParticle(Particle.LAVA, loc, 1, 0.8, 0.8, 0.8, 0, null, true);
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 7, 0.6, 0.6, 0.6, 0.1, null, true);
                    world.spawnParticle(Particle.SMOKE_NORMAL, loc, 7, 1, 1, 1, 0.05, null, true);
                })
                .build();
    }

    @Override
    public Behavior getBehavior() {
        return behavior;
    }
}