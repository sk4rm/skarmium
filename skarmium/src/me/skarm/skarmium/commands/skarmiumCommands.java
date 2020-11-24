package me.skarm.skarmium.commands;

import de.tr7zw.nbtapi.NBTTileEntity;
import me.skarm.skarmium.events.skarmiumEvents;
import me.skarm.skarmium.items.skarmiumItems;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.data.Rotatable;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Objects;

public class skarmiumCommands implements CommandExecutor {

    // chat prefixes
    public static String prefix_diamond = "§9<♢> ";
    public static String prefix_alert = "§e(!) ";
    public static String prefix_error = "§c/△\\ ";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // check if is console (might remove or edit)
        if (!(sender instanceof Player)) {
            sender.sendMessage("§9<♢> Only players can execute that command");
            return true;
        }
        Player player = (Player) sender;


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
                        player.sendMessage("remove - for removing any unbreakable gray, blue, and red flag you are standing on");
                        break;

                    // flag tool
                    case "tool": case "wand":
                        player.getInventory().addItem(skarmiumItems.flagtool);
                        player.sendMessage(prefix_diamond + "Right click to place a flag down");
                        break;

                    // flag team
                    case "team":
                        if (args.length == 1) {
                            // flag team
                            player.sendMessage(prefix_error + "Correct usage: /flag team <red/blue/gray>");
                        } else if (args.length == 2) {

                            // set up checks for flags
                            NBTTileEntity flag = new NBTTileEntity(player.getWorld().getBlockAt(player.getLocation()).getState());
                            boolean isGrayFlag = player.getWorld().getBlockAt(player.getLocation()).getType() == Material.GRAY_BANNER && flag.getString("CustomName").equalsIgnoreCase("{\"text\":\"§7§lNeutral Flag§f\"}");
                            boolean isRedFlag = player.getWorld().getBlockAt(player.getLocation()).getType() == Material.RED_BANNER && flag.getString("CustomName").equalsIgnoreCase("{\"text\":\"§c§lRed Flag§f\"}");
                            boolean isBlueFlag = player.getWorld().getBlockAt(player.getLocation()).getType() == Material.BLUE_BANNER && flag.getString("CustomName").equalsIgnoreCase("{\"text\":\"§9§lBlue Flag§f\"}");
//                            System.out.println(isGrayFlag + "" + isRedFlag + "" + isBlueFlag);

                            // color choices
                            if (args[1].equalsIgnoreCase("red")) {
                                // flag team red
                                if (isBlueFlag || isGrayFlag) {
                                    player.getWorld().getBlockAt(player.getLocation()).setType(Material.RED_BANNER);
                                    flag.setString("CustomName", "{\"text\":\"§c§lRed Flag§f\"}");
                                    Rotatable flagRotation = (Rotatable) player.getWorld().getBlockAt(player.getLocation()).getBlockData();
                                    float bannerYaw = (player.getLocation().getYaw()) % 360.0f;
                                    flagRotation.setRotation(skarmiumEvents.yawToFace(bannerYaw));
                                    player.getWorld().getBlockAt(player.getLocation()).setBlockData(flagRotation);
                                    player.sendMessage(prefix_diamond + "Red team flag set");

                                    // save coords to global variable in events
                                    skarmiumEvents.redx = player.getLocation().getBlockX();
                                    skarmiumEvents.redy = player.getLocation().getBlockY();
                                    skarmiumEvents.redz = player.getLocation().getBlockZ();
                                    skarmiumEvents.redrot = flagRotation;

                                } else {
                                    player.sendMessage(prefix_error + "Make sure you're standing on a flag of a different team");
                                }

                            } else if (args[1].equalsIgnoreCase("blue")) {
                                // flag team blue
                                if (isRedFlag || isGrayFlag) {
                                    player.getWorld().getBlockAt(player.getLocation()).setType(Material.BLUE_BANNER);
                                    flag.setString("CustomName", "{\"text\":\"§9§lBlue Flag§f\"}");
                                    Rotatable flagRotation = (Rotatable) player.getWorld().getBlockAt(player.getLocation()).getBlockData();
                                    float bannerYaw = (player.getLocation().getYaw()) % 360.0f;
                                    flagRotation.setRotation(skarmiumEvents.yawToFace(bannerYaw));
                                    player.getWorld().getBlockAt(player.getLocation()).setBlockData(flagRotation);
                                    player.sendMessage(prefix_diamond + "Blue team flag set");

                                    // save coords to global variable in events
                                    skarmiumEvents.bluex = player.getLocation().getBlockX();
                                    skarmiumEvents.bluey = player.getLocation().getBlockY();
                                    skarmiumEvents.bluez = player.getLocation().getBlockZ();
                                    skarmiumEvents.bluerot = flagRotation;

                                } else {
                                    player.sendMessage(prefix_error + "Make sure you're standing on a flag of a different team");
                                }

                            } else if (args[1].equalsIgnoreCase("gray")) {
                                // flag team gray (neutral)
                                if (isRedFlag || isBlueFlag) {
                                    player.getWorld().getBlockAt(player.getLocation()).setType(Material.GRAY_BANNER);
                                    flag.setString("CustomName", "{\"text\":\"§7§lNeutral Flag§f\"}");
                                    Rotatable flagRotation = (Rotatable) player.getWorld().getBlockAt(player.getLocation()).getBlockData();
                                    float bannerYaw = (player.getLocation().getYaw()) % 360.0f;
                                    flagRotation.setRotation(skarmiumEvents.yawToFace(bannerYaw));
                                    player.getWorld().getBlockAt(player.getLocation()).setBlockData(flagRotation);
                                    player.sendMessage(prefix_diamond + "Flag set to neutral");
                                } else {
                                    player.sendMessage(prefix_error + "Make sure you're standing on a valid flag of a different team");
                                }

                            } else {
                                // flag team shiny diamond
                                player.sendMessage(prefix_error + "No such team " + args[1]);
                            }

                        } else {
                            player.sendMessage(prefix_error + "Correct usage: /flag team <red/blue/gray>");
                        }

                        break;

                    // flag remove
                    case "remove":
                        // check nbt tag for custom name
                        NBTTileEntity flagToRemove = new NBTTileEntity(player.getWorld().getBlockAt(player.getLocation()).getState());
                        // somehow the below boolean checks work and no null errors :)
                        boolean isGrayFlag = player.getWorld().getBlockAt(player.getLocation()).getType() == Material.GRAY_BANNER && flagToRemove.getString("CustomName").equalsIgnoreCase("{\"text\":\"§7§lNeutral Flag§f\"}");
                        boolean isRedFlag = player.getWorld().getBlockAt(player.getLocation()).getType() == Material.RED_BANNER && flagToRemove.getString("CustomName").equalsIgnoreCase("{\"text\":\"§c§lRed Flag§f\"}");
                        boolean isBlueFlag = player.getWorld().getBlockAt(player.getLocation()).getType() == Material.BLUE_BANNER && flagToRemove.getString("CustomName").equalsIgnoreCase("{\"text\":\"§9§lBlue Flag§f\"}");
                        if (isGrayFlag || isRedFlag || isBlueFlag) {
                            player.getWorld().getBlockAt(player.getLocation()).setType(Material.AIR);
                            player.sendMessage(prefix_diamond + "Flag removed");

                            if (isBlueFlag) {
                                // reset coords of blue
                                skarmiumEvents.bluex = 0;
                                skarmiumEvents.bluey = 0;
                                skarmiumEvents.bluez = 0;

                            } else if (isRedFlag) {
                                // reset coords of red
                                skarmiumEvents.redx = 0;
                                skarmiumEvents.redy = 0;
                                skarmiumEvents.redz = 0;

                            } else {
                                // reset nothing
                            }

                        } else {
                            player.sendMessage(prefix_error + "There aren't any valid gray, blue, or red flags at your current location");
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
