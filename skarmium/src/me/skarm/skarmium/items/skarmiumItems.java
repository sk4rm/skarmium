package me.skarm.skarmium.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class skarmiumItems {
    // dunno what to use this for
    enum Rarity {
        COMMON,
        UNCOMMON,
        RARE,
        MYTHICAL,
        LEGENDARY
    }

    public static ItemStack wand, flagtool;

    public static void init () {
        createWand();
        createFlagTool();
    }

    // Silver Stake
    private static void createWand () {
        // declare item
        ItemStack item = new ItemStack(Material.END_ROD, 1);
        // declare item details
        ItemMeta meta = item.getItemMeta();
        // set name
        Objects.requireNonNull(meta).setDisplayName("§eSilver Stake");
        // set lore
        List<String> lore = new ArrayList<>();
        lore.add("§fCommon");
        lore.add("");
        lore.add("§7§oDracula may not like this");
        meta.setLore(lore);
        // set enchant
        meta.addEnchant(Enchantment.LUCK, 1, false);
        // hide stuff
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        // set the details set above to the actual item (it wasn't before)
        item.setItemMeta(meta);
        // set global item (the one outside this function) equal to local item (the one here)
        wand = item;
    }

    // Flag
    private static void createFlagTool () {
        ItemStack item = new ItemStack(Material.STICK, 1);
        ItemMeta meta = item.getItemMeta();
        Objects.requireNonNull(meta).setDisplayName("§7Flag Tool");
        List<String> lore = new ArrayList<>();
        lore.add("§7Unranked");
        lore.add("§fPlaces a flag down.");
        lore.add("§fUse '/flag set' to set it up.");
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        flagtool = item;
    }

}
