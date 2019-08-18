package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.SpellData;
import me.dylan.wands.spell.SpellEffectUtil;
import me.dylan.wands.spell.types.Behaviour;
import me.dylan.wands.spell.types.Spark;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;

public class FireSpark implements SpellData {
    private final Behaviour behaviour;

    public FireSpark() {
        this.behaviour = Spark.newBuilder(Behaviour.Target.MULTI)
                .setSpellEffectRadius(2.8F)
                .setEntityDamage(9)
                .setEntityEffects(entity -> entity.setFireTicks(100))
                .setSpellRelativeEffects((loc, world) -> {
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 10, 0.3, 0.3, 0.3, 0.1, null, true);
                    world.spawnParticle(Particle.SMOKE_NORMAL, loc, 10, 0.2, 0.2, 0.2, 0.1, null, true);
                    world.spawnParticle(Particle.FLAME, loc, 10, 0.8, 0.8, 0.8, 0.1, null, true);
                    world.spawnParticle(Particle.LAVA, loc, 10, 0.8, 0.8, 0.8, 0, null, true);
                    SpellEffectUtil.runTaskLater(() ->
                            world.playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, SoundCategory.MASTER, 4.0F, 1.0F), 10);
                })
                .setCastSound(Sound.ENTITY_FIREWORK_ROCKET_BLAST)

                .setEffectDistance(30)
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}