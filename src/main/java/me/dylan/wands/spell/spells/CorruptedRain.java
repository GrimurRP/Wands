package me.dylan.wands.spell.spells;

import me.dylan.wands.ListenerRegistry;
import me.dylan.wands.Main;
import me.dylan.wands.miscellaneous.utils.Common;
import me.dylan.wands.spell.SpellData;
import me.dylan.wands.spell.tools.SpellInfo;
import me.dylan.wands.spell.tools.sound.CompoundSound;
import me.dylan.wands.spell.types.Behavior;
import me.dylan.wands.spell.types.Spark;
import me.dylan.wands.spell.util.SpellEffectUtil;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CorruptedRain implements SpellData, Listener {
    private final Behavior behavior;
    private final PotionEffect wither = new PotionEffect(PotionEffectType.WITHER, 40, 2, true);
    private final PotionEffect blind = new PotionEffect(PotionEffectType.BLINDNESS, 160, 0, false);
    private final BlockData obsidian = Material.OBSIDIAN.createBlockData();
    private final String tagCorruptedRain = UUID.randomUUID().toString();
    private final Main plugin = Main.getPlugin();
    private final Set<Arrow> arrows = new HashSet<>();

    public CorruptedRain() {
        ListenerRegistry.addListener(this);
        this.behavior = Spark.newBuilder(Behavior.Target.MULTI)
                .setCastSound(CompoundSound.chain()
                        .add(Sound.ENTITY_ARROW_SHOOT)
                        .add(Sound.BLOCK_STONE_PLACE)
                        .addAll(Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1, 1)
                )
                .setEffectDistance(30)
                .extendedSetSpellRelativeEffects(this::spawnArrows)
                .build();

        plugin.addDisableLogic(() -> arrows.forEach(Entity::remove));
    }

    @Override
    public Behavior getBehavior() {
        return behavior;
    }

    private void spawnArrows(Location location, SpellInfo spellInfo) {
        location.add(0, 5, 0);
        World world = location.getWorld();
        Location particleLoc = location.clone().add(0, 2, 0);
        world.spawnParticle(Particle.SPELL_MOB, particleLoc, 50, 1, 1, 1, 1, null, true);
        world.spawnParticle(Particle.BLOCK_CRACK, particleLoc, 50, 0.8, 0.3, 0.8, 0, obsidian, true);
        world.spawnParticle(Particle.SMOKE_NORMAL, particleLoc, 70, 1, 1, 1, 0, null, true);
        new BukkitRunnable() {
            int count;

            @Override
            public void run() {
                if (++count >= 14) {
                    cancel();
                } else {
                    Location arrowLoc = SpellEffectUtil.randomizeLoc(location, 1.5, 0, 1.5);
                    Arrow arrow = (Arrow) world.spawnEntity(arrowLoc, EntityType.ARROW);
                    arrow.setDamage(6);
                    arrow.setPickupStatus(PickupStatus.ALLOWED);
                    arrow.setShooter(spellInfo.caster);
                    arrow.addCustomEffect(blind, true);
                    arrow.addCustomEffect(wither, true);
                    arrow.setVelocity(new Vector(SpellEffectUtil.randomize(0.4), -1, SpellEffectUtil.randomize(0.4)));
                    arrow.setMetadata(tagCorruptedRain, Common.METADATA_VALUE_TRUE);
                    arrows.add(arrow);
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    @EventHandler
    private void onArrowHit(ProjectileHitEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Arrow && entity.hasMetadata(tagCorruptedRain)) {
            entity.remove();
            arrows.remove(entity);
        }
    }

    @EventHandler
    private void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) event.getDamager();
            if (projectile.hasMetadata(tagCorruptedRain)) {
                Player source = (Player) projectile.getShooter();
                if (source != null && source.equals(event.getEntity()) && !Main.getPlugin().getConfigurableData().isSelfHarmAllowed()) {
                    event.setCancelled(true);
                    projectile.remove();
                }
            }
        }
    }
}