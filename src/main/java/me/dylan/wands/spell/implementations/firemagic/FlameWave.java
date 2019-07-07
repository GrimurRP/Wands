package me.dylan.wands.spell.implementations.firemagic;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.SpellEffectUtil;
import me.dylan.wands.spell.handler.Behaviour;
import me.dylan.wands.spell.handler.WaveSpell;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;

public enum FlameWave implements Castable {
    INSTANCE;
    private final Behaviour behaviour;

    FlameWave() {
        this.behaviour = WaveSpell.newBuilder()
                .setSpellEffectRadius(2.5F)
                .setAffectedEntityDamage(2)
                .setEffectDistance(20)
                .setCastSound(Sound.ENTITY_FIREWORK_ROCKET_BLAST)
                .setAffectedEntityEffects(entity -> entity.setFireTicks(140))
                .setSpellRelativeEffects(loc -> {
                    World world = loc.getWorld();
                    world.spawnParticle(Particle.FLAME, loc, 10, 0.8, 0.8, 0.8, 0.1, null, true);
                    world.spawnParticle(Particle.LAVA, loc, 1, 0.8, 0.8, 0.8, 0, null, true);
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 7, 0.6, 0.6, 0.6, 0.1, null, true);
                    world.spawnParticle(Particle.SMOKE_NORMAL, loc, 7, 1, 1, 1, 0.05, null, true);
                })
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}