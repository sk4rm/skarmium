package me.skarm.skarmium.commands;

import de.tr7zw.nbtapi.NBTTileEntity;
import me.skarm.skarmium.items.skarmiumItems;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Objects;

public class skarmiumCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // check if is console (might remove or edit)
        if (!(sender instanceof Player)) {
            sender.sendMessage("§9<♢> Only players can execute that command");
            return true;
        }
        Player player = (Player) sender;
        // prefixes for responses
        String prefix_diamond = "§9<♢> ";
        String prefix_alert = "§e(!) ";
        String prefix_error = "§c/△\\ ";


        // heal
        if (cmd.getName().equalsIgnoreCase("heal")) {
            double maxHp = Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getDefaultValue();
            player.setHealth(maxHp);
            player.sendMessage(prefix_diamond +"Health bar replenished");
        }

        // feed
        else if (cmd.getName().equalsIgnoreCase("feed")) {
            player.setFoodLevel(20);
            player.sendMessage(prefix_diamond + "Hunger bar filled");
        }

        // bigsummon
        else if (cmd.getName().equalsIgnoreCase("bigsummon")) {
            if (args.length == 2) {
                // check first argument type
                try {
                    EntityType entity = EntityType.valueOf(args[0].toUpperCase());

                    // now check second argument type
                    try {
                        int amount = Integer.parseInt(args[1]);
                        if (amount <= 0) {
                            throw new IllegalArgumentException();
                        }

                        for (int i = 0; i < amount; i++){
                            player.getWorld().spawnEntity(player.getLocation(), entity);
                        }
                    } catch (IllegalArgumentException exception) {
                        player.sendMessage(prefix_error + args[1] + " is not a valid integer");
                    }

                } catch (IllegalArgumentException exception) {
                    player.sendMessage(prefix_error + args[0] + " is not a valid entityType");
                }

            } else {
                player.sendMessage(prefix_error + "Correct usage: /bigsummon <entityType> <amount>");
            }
        }

        // flag
        else if (cmd.getName().equalsIgnoreCase("flag")) {
            if (args.length == 0) {
                // just /flag without any arguments
                player.sendMessage(prefix_error + "Correct usage: /flag <subcommand> <parameters>");
                player.sendMessage(prefix_alert + "Type '/flag help' for a list of subcommands");

            } else {
                switch (args[0]) {
                    // flag help
                    case "help":
                        player.sendMessage(prefix_diamond + "List of subcommands include:");
                        player.sendMessage("help - show this help dialogue");
                        player.sendMessage("tool - gives a player a flag tool");
                        player.sendMessage("set - settings for a flag");
                        player.sendMessage("remove - for removing any unbreakable gray, blue and red flag you are standing on");
                        break;

                    // flag tool
                    case "tool": case "wand":
                        player.getInventory().addItem(skarmiumItems.flagtool);
                        player.sendMessage(prefix_diamond + "Right click to place a flag down");
                        break;

                    // flag set
                    case "set":
                        player.sendMessage(prefix_diamond + "Flag settings: ");
                        break;

                    // flag remove
                    case "remove":
                        NBTTileEntity flagToRemove = new NBTTileEntity(player.getWorld().getBlockAt(player.getLocation()).getState());
                        String nameOfFlag = flagToRemove.getString("CustomName");
                        if (nameOfFlag.equalsIgnoreCase("{\"text\":\"§7§lNeutral Flag§f\"}") ||
                            nameOfFlag.equalsIgnoreCase("{\"text\":\"§c§lRed Flag§f\"}") ||
                            nameOfFlag.equalsIgnoreCase("{\"text\":\"§9§lBlue Flag§f\"}")) {
                            player.sendMessage(prefix_diamond + "Flag removed");
                            player.getWorld().getBlockAt(player.getLocation()).setType(Material.AIR);
                        } else {
                            player.sendMessage(prefix_error + "There aren't any gray, blue or red flags at your current location");
                        }
                        break;

                    // flag fwehvawvieeg
                    default:
                        player.sendMessage(prefix_error + "No such subcommand " + args[0]);
                        break;
                }

            }

        }

        return true;
    }
}
