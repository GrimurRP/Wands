package me.dylan.wands.spell.spells.witch;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.spellbuilders.Spark;
import me.dylan.wands.spell.spellbuilders.Spark.Target;
import me.dylan.wands.spell.spells.AffinityType;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Confuse implements Castable {
    @Override
    public AffinityType[] getAffinityTypes() {
        return new AffinityType[]{AffinityType.WITCH_MAGIC};
    }

    @Override
    public Behavior createBehaviour() {
        return Spark.newBuilder(Target.MULTI)
                .setSpellEffectRadius(3.0F)
                .setEntityDamage(8)
                .setPotionEffects(new PotionEffect(PotionEffectType.CONFUSION, 260, 4, false))
                .setSpellRelativeEffects((loc, spellInfo) -> {
                    World world = spellInfo.world();
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 50, 1.4, 1.2, 1.4, 0.08, null, true);
                    world.spawnParticle(Particle.SMOKE_NORMAL, loc, 50, 1.4, 1.2, 1.4, 0.08, null, true);
                    world.spawnParticle(Particle.SPELL_WITCH, loc, 50, 1.4, 1.2, 1.4, 0.15, null, true);
                    MagicSpark.SPARK_SOUND.play(loc);
                })
                .setCastSound(Sound.ENTITY_FIREWORK_ROCKET_BLAST)
                .setEffectDistance(30)
                .build();
    }
}
