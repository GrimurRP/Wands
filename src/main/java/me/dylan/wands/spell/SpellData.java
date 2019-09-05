package me.dylan.wands.spell;

import me.dylan.wands.spell.types.Behavior;

@FunctionalInterface
public interface SpellData {
    default String getDisplayName() {
        return getClass().getSimpleName();
    }

    Behavior getBehavior();
}