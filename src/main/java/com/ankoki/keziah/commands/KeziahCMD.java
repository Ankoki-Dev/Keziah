package com.ankoki.keziah.commands;

import com.ankoki.keziah.utils.SpellManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KeziahCMD implements CommandExecutor {

    // This is just for testing, improve later on.
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        ItemStack item = player.getInventory().getItemInMainHand();
        item = SpellManager.addSpell(item, 61);
        player.getInventory().setItemInMainHand(item);
        player.sendMessage("§fKez§aiah §f| Successfully enchanted your tool.");
        return true;
    }
}
