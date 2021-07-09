package com.ankoki.keziah.utils;

import com.ankoki.keziah.utils.annotations.Spell;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DefaultSpells {

    @Spell(name = "test spell", id = 61)
    private void myTestSpell(Player player) {
        Bukkit.broadcastMessage("§cWoah! A spell has been cast.");
    }
}
