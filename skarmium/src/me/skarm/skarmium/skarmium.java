package me.skarm.skarmium;

import me.skarm.skarmium.commands.skarmiumAutocomplete;
import me.skarm.skarmium.commands.skarmiumCommands;
import me.skarm.skarmium.events.skarmiumEvents;
import me.skarm.skarmium.items.skarmiumItems;
import me.skarm.skarmium.tasks.giveGlow;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Objects;

public class skarmium extends JavaPlugin {

    @Override
    public void onEnable () {
        // set all event listener and handlers
        getServer().getPluginManager().registerEvents(new skarmiumEvents(), this);

        // declare commands
        skarmiumCommands cmds = new skarmiumCommands();
        // list of all plugin commands
        String[] listofcmds = {"heal", "feed", "bigsummon", "flag"};
        // set command handlers (executors) for each and every command
        for (String cmd : listofcmds) {
            Objects.requireNonNull(getCommand(cmd)).setExecutor(cmds);
            Objects.requireNonNull(getCommand("flag")).setTabCompleter(new skarmiumAutocomplete());
        }

        // declare all items
        skarmiumItems.init();

        // setup tasks
        // task: give glow to flag bearers
        BukkitTask giveGlowToFlagbearer = (BukkitTask) new giveGlow(this).runTaskTimer(this, 20L, 20L * 2);

        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[skarmium] Plugin is enabled! :)");
    }

    @Override
    public void onDisable () {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "[skarmium] Plugin is disabled! :(");
    }

}
