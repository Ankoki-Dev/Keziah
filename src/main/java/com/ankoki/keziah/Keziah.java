package com.ankoki.keziah;

import com.ankoki.keziah.utils.Console;
import com.ankoki.keziah.utils.DefaultSpells;
import com.ankoki.keziah.utils.SpellManager;
import com.ankoki.keziah.utils.annotations.Spell;
import com.ankoki.keziah.commands.KeziahCMD;
import de.tr7zw.nbtapi.NBTItem;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// Basically a rewrite of Elementals but with annotation spell
// registering and potential code cleanup.
public class Keziah extends JavaPlugin implements Listener {

    private static final List<Class<?>> SPELL_CLASSES = new ArrayList<>();

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        new NBTItem(new ItemStack(Material.LEVER));
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getServer().getPluginCommand("keziah").setExecutor(new KeziahCMD());
        Keziah.registerFrom(DefaultSpells.class);
        Console.info("Keziah has been successfully enabled in " + (System.currentTimeMillis() - start) + "ms");
    }

    @Override
    public void onDisable() {
        SPELL_CLASSES.clear();
    }

    /**
     * Register spells from the given classes.
     *
     * @param clazzes the classes to register from.
     */
    public static void registerFrom(Class<?>... clazzes) {
        Collections.addAll(SPELL_CLASSES, clazzes);
    }

    // Listener so that registered classes are not accessible elsewhere.
    @EventHandler
    private void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Player player = event.getPlayer();
        ItemStack hand = player.getInventory().getItemInMainHand();
        if (hand.getType() == Material.AIR) return;
        for (Class<?> clazz : Keziah.SPELL_CLASSES) {
            Object instance;
            try {
                Constructor<?> constructor = clazz.getDeclaredConstructor();
                instance = constructor.newInstance();
            } catch (Exception ex) {
                continue;
            }
            for (Method method : clazz.getDeclaredMethods()) {
                Spell annotation = method.getAnnotation(Spell.class);
                if (annotation == null || method.getReturnType() != void.class) continue;
                if (!SpellManager.hasSpell(hand, annotation.id())) continue;
                if (!Arrays.equals(method.getParameterTypes(), new Class[]{Player.class})) continue;
                try {
                    method.setAccessible(true);
                    method.invoke(instance, player);
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                            TextComponent.fromLegacyText("§fYou have casted §a" + annotation.name()));
                    event.setCancelled(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return;
            }
        }
    }
}