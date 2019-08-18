package me.dylan.wands.spell.spells;

import me.dylan.wands.Main;
import me.dylan.wands.spell.SpellData;
import me.dylan.wands.spell.SpellEffectUtil;
import me.dylan.wands.spell.types.Behaviour;
import me.dylan.wands.spell.types.Spark;
import me.dylan.wands.util.Common;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Wolf;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class CorruptedWolves implements SpellData {

    private final Behaviour behaviour;
    private final Main plugin = Main.getPlugin();
    private final Set<Wolf> wolves = new HashSet<>();
    private final PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 160, 4, true);

    public CorruptedWolves() {
        plugin.addDisableLogic(() -> wolves.forEach(Entity::remove));

        this.behaviour = Spark.newBuilder(Behaviour.Target.SINGLE)
                .setSpellEffectRadius(2.5F)
                .setCastSound(Sound.ENTITY_EVOKER_PREPARE_SUMMON)
                .setEffectDistance(30)
                .setEntityEffects(this::accept)
                .setSpellRelativeEffects((loc, world) -> {
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 20, 0.4, 0.4, 0.4, 0.1, null, true);
                    world.spawnParticle(Particle.SMOKE_NORMAL, loc, 20, 0.4, 0.4, 0.4, 0.1, null, true);
                })
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }

    private void accept(LivingEntity target) {
        Location loc = target.getLocation();
        World world = loc.getWorld();
        for (int i = 0; i < 10; i++) {
            Wolf wolf = (Wolf) world.spawnEntity(SpellEffectUtil.getFirstPassableBlockAbove(SpellEffectUtil.randomizeLoc(loc, 2, 0, 2)), EntityType.WOLF);
            wolves.add(wolf);
            wolf.setMetadata(SpellEffectUtil.CAN_DAMAGE_WITH_WANDS, Common.METADATA_VALUE_TRUE);
            Location location = wolf.getLocation();
            world.playSound(location, Sound.BLOCK_CHORUS_FLOWER_GROW, SoundCategory.MASTER, 4, 1);
            world.spawnParticle(Particle.SMOKE_LARGE, location, 2, 0.1, 0.1, 0.05, 0.1, null, true);
            wolf.addPotionEffect(speed, true);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (wolf.isValid()) {
                        world.spawnParticle(Particle.SMOKE_NORMAL, wolf.getLocation(), 1, 0.1, 0.1, 0.1, 0.05, null, true);
                    } else {
                        cancel();
                        wolves.remove(wolf);
                        world.spawnParticle(Particle.SMOKE_LARGE, wolf.getLocation(), 3, 0, 0, 0, 0.1, null, true);
                    }
                }
            }.runTaskTimer(plugin, 1, 1);
            Bukkit.getScheduler().runTaskLater(plugin, () -> wolf.damage(1, target), 10);
            Bukkit.getScheduler().runTaskLater(plugin, wolf::remove, 160);
        }
    }
}
