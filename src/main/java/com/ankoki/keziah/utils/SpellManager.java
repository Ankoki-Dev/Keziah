package com.ankoki.keziah.utils;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class SpellManager {

    public static ItemStack addSpell(ItemStack item, long id) {
        NBTItem castable = new NBTItem(item);
        castable.setLong("keziah-spell", id);
        castable.setUUID("keziah-unstackable", UUID.randomUUID());
        return castable.getItem();
    }

    public static ItemStack removeSpell(ItemStack item) {
        NBTItem castable = new NBTItem(item);
        if (castable.hasKey("keziah-spell")) {
            castable.removeKey("keziah-spell");
            castable.removeKey("keziah-unstackable");
        }
        return castable.getItem();
    }

    public static boolean hasAnySpell(ItemStack item) {
        return new NBTItem((item)).hasKey("keziah-spell");
    }

    public static boolean hasSpell(ItemStack item, long id) {
        NBTItem castable = new NBTItem(item);
        if (!castable.hasKey("keziah-spell")) return false;
        return castable.getLong("keziah-spell") == id;
    }
}
