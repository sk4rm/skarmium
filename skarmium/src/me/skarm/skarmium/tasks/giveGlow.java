package me.skarm.skarmium.tasks;

import me.skarm.skarmium.events.skarmiumEvents;
import me.skarm.skarmium.items.skarmiumItems;
import me.skarm.skarmium.skarmium;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class giveGlow extends BukkitRunnable {

    skarmium plugin;

    public giveGlow(skarmium plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {

        // list of all players
        List<Player> listOfPlayers = new ArrayList<>(Bukkit.getServer().getOnlinePlayers());

        // check every player give list of players
        for (int i = 0; i < listOfPlayers.toArray().length; i++) {
            Player player = listOfPlayers.get(i);
            // check if anything in his helmet slot
            if (player.getInventory().getHelmet() != null) {
                // check if is bearing a flag (no need for name check as no way to get banner up to helmet besides commands)
                boolean isBearingGrayFlag = player.getInventory().getHelmet().getItemMeta().equals(skarmiumItems.grayFlag.getItemMeta());
                boolean isBearingRedFlag = player.getInventory().getHelmet().getItemMeta().equals(skarmiumItems.redFlag.getItemMeta());
                boolean isBearingBlueFlag = player.getInventory().getHelmet().getItemMeta().equals(skarmiumItems.blueFlag.getItemMeta());

                // issue: timer on standby when checking inventory, glow only applies after exit inv after delay (if haven't alr)

                if (isBearingGrayFlag || isBearingRedFlag || isBearingBlueFlag) {
                    // give glow
                    PotionEffect glowing = new PotionEffect(PotionEffectType.GLOWING, (int) 20L * 3, 0, true, false);
                    player.addPotionEffect(glowing);
                }
            }
        }
    }
}
