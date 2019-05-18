package me.dylan.wands.spellbehaviour;

import me.dylan.wands.Wands;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class SpellBehaviour implements Listener {
    final static Wands plugin = Wands.getPlugin();
    private static final Map<Player, Long> lastUsed = new HashMap<>();
    final int entityDamage;
    final float effectAreaRange;
    final Consumer<Location> castEffects;
    final Consumer<Location> visualEffects;
    final Consumer<Entity> entityEffects;

    SpellBehaviour(Builder.BuilderWrapper builderWrapper) {
        this.entityDamage = builderWrapper.entityDamage;
        this.effectAreaRange = builderWrapper.effectAreaRange;
        this.castEffects = builderWrapper.castEffects;
        this.visualEffects = builderWrapper.visualEffects;
        this.entityEffects = builderWrapper.entityEffects;
    }

    @EventHandler
    public static void onQuit(PlayerQuitEvent event) {
        lastUsed.remove(event.getPlayer());
    }

    abstract void cast(Player player);

    public final void executeSpellFrom(Player player) {
        int remainingTime = getRemainingTime(player);
        if (remainingTime <= 0) {
            cast(player);
            lastUsed.put(player, System.currentTimeMillis());
        } else {
            notifyOfCooldown(player, remainingTime);
        }
    }

    private int getRemainingTime(Player player) {
        int cooldown = Wands.getPlugin().getPluginData().getMagicCooldownTime() * 1000;
        long now = System.currentTimeMillis();
        Long previous = lastUsed.get(player);
        if (previous == null) {
            return 0;
        }
        long elapsed = now - previous;
        return (int) Math.ceil(cooldown - elapsed) / 1000;
    }

    private void notifyOfCooldown(Player player, int remaining) {
        player.sendActionBar("§6Wait §7" + remaining + " §6second" + ((remaining != 1) ? "s" : ""));
        player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_AMBIENT, 0.3F, 1);
    }

    public abstract static class Builder<T extends Builder<T>> {

        final BuilderWrapper builderWrapper = new BuilderWrapper();

        abstract T self();

        /**
         * Sets the damage that is applied to the Damageable effected by the spell.
         *
         * @param damage The amount of damage
         * @return this
         */

        public T setEntityDamage(int damage) {
            builderWrapper.entityDamage = damage;
            return self();
        }

        /**
         * Sets the radius of the affected Damageables after the spell concludes.
         *
         * @param radius The radius
         * @return this
         */

        public T setEffectRadius(float radius) {
            builderWrapper.effectAreaRange = radius;
            return self();
        }

        /**
         * Sets the effect that will be executed relative to the player.
         *
         * @param castEffects Effects relative to the player
         * @return this
         */

        public T setCastEffects(Consumer<Location> castEffects) {
            builderWrapper.castEffects = castEffects;
            return self();
        }

        /**
         * Sets the visual effects that the spell shows, whether it is a trail of particles
         * or is executed relative to where you look is up to the spell type BasePropperties is used in.
         *
         * @param effects Effects relative to the spell
         * @return this
         */

        public T setVisualEffects(Consumer<Location> effects) {
            builderWrapper.visualEffects = effects;
            return self();
        }

        /**
         * Sets the effects which will effect the Damageables in the spell's effect range.
         *
         * @param effects Effects applied to the affected Damageables
         * @return this
         */

        public T setEntityEffects(Consumer<Entity> effects) {
            builderWrapper.entityEffects = effects;
            return self();
        }

        BuilderWrapper createBuilderWrapper() {
            return builderWrapper;
        }

        static class BuilderWrapper {
            private final static Consumer<?> EMPTY_CONSUMER = e -> {
            };

            private int entityDamage = 3;
            private float effectAreaRange = 2;
            private Consumer<Location> castEffects = emptyConsumer();
            private Consumer<Location> visualEffects = emptyConsumer();
            private Consumer<Entity> entityEffects = emptyConsumer();

            @SuppressWarnings("unchecked")
            private <T> Consumer<T> emptyConsumer() {
                return (Consumer<T>) EMPTY_CONSUMER;
            }
        }
    }
}
