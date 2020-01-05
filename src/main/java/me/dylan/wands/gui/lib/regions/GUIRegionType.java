package me.dylan.wands.gui.lib.regions;

import org.bukkit.event.inventory.InventoryType;

import java.util.*;

public enum GUIRegionType {
    // @formatter:off
    CHEST_CONTENTS(InventoryType.CHEST,         0,      17,     3), // 9x3

    PLAYER_ARMOR(InventoryType.PLAYER,          36,     39,     4),
    PLAYER_HOTBAR(InventoryType.PLAYER,         0,      8,      9),
    PLAYER_INVENTORY(InventoryType.PLAYER,      9,      35,     9),
    PLAYER_OFFHAND(InventoryType.PLAYER,        40,     40,     1),

    WORKBENCH_RESULT(InventoryType.WORKBENCH,   0,      0,      1),
    WORKBENCH_CRAFTING(InventoryType.WORKBENCH, 1,      9,      3),

    FURNACE_INPUT(InventoryType.FURNACE,        0,      0,      1),
    FURNACE_FUEL(InventoryType.FURNACE,         1,      1,      1),
    FURNACE_RESULT(InventoryType.FURNACE,       2,      2,      1),

    ANVIL_FIRST_ITEM(InventoryType.ANVIL,       0,      0,      1),
    ANVIL_SECOND_ITEM(InventoryType.ANVIL,      1,      1,      1),
    ANVIL_RESULT(InventoryType.ANVIL,           2,      2,      1),

    DROPPER_CONTENTS(InventoryType.DROPPER,     0,      8,      3),
    DISPENSER_CONTENTS(InventoryType.DISPENSER, 0,      8,      3),

    HOPPER_CONTENTS(InventoryType.HOPPER,       0,      4,      5);
    // @formatter:on

    private final InventoryType inventoryType;
    private final int firstSlot, lastSlot;
    private final int regionWidth, regionHeight;

    GUIRegionType(InventoryType type, int first, int last, int regionWidth) {
        this.inventoryType = type;
        this.firstSlot = first;
        this.lastSlot = last;
        this.regionWidth = regionWidth;
        this.regionHeight = getSize() / regionWidth;

        if (regionWidth * regionHeight != getSize())
            throw new IllegalStateException("Developer screw-up in GUIRegionType");
    }

    public int getRegionWidth() {
        return regionWidth;
    }

    public int getRegionHeight() {
        return regionHeight;
    }

    public int getSize() {
        return lastSlot - firstSlot + 1;
    }

    public int getFirstSlot() {
        return firstSlot;
    }

    public int getLastSlot() {
        return lastSlot;
    }

    public boolean containsSlot(int slot) {
        return firstSlot >= slot && lastSlot <= slot;
    }

    public InventoryType getInventoryType() {
        return this.inventoryType;
    }

    private static final Map<InventoryType, Set<GUIRegionType>> INVENTORY_TYPE_SET_MAP;

    static {
        Map<InventoryType, Set<GUIRegionType>> map = new EnumMap<>(InventoryType.class);
        for (GUIRegionType type : values()) {
            map.computeIfAbsent(type.getInventoryType(), k -> Collections.newSetFromMap(new IdentityHashMap<>()))
                    .add(type);
        }

        for (Map.Entry<InventoryType, Set<GUIRegionType>> entry : map.entrySet()) {
            entry.setValue(Collections.unmodifiableSet(entry.getValue()));
        }

        INVENTORY_TYPE_SET_MAP = Collections.unmodifiableMap(map);
    }

    public static Set<GUIRegionType> getPossibleRegionTypes(InventoryType type) {
        return INVENTORY_TYPE_SET_MAP.get(type);
    }

}
