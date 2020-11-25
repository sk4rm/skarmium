package me.skarm.skarmium.events;

import de.tr7zw.nbtapi.NBTTileEntity;
import me.skarm.skarmium.commands.skarmiumCommands;
import me.skarm.skarmium.items.skarmiumItems;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Rotatable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;


public class skarmiumEvents implements Listener {

    // custom function for converting yaw to a BlockFace direction
    public static BlockFace yawToFace(float yawDegree) {
        BlockFace[] directions = {
                BlockFace.NORTH,
                BlockFace.NORTH_NORTH_EAST,
                BlockFace.NORTH_EAST,
                BlockFace.EAST_NORTH_EAST,
                BlockFace.EAST,
                BlockFace.EAST_SOUTH_EAST,
                BlockFace.SOUTH_EAST,
                BlockFace.SOUTH_SOUTH_EAST,
                BlockFace.SOUTH,
                BlockFace.SOUTH_SOUTH_WEST,
                BlockFace.SOUTH_WEST,
                BlockFace.WEST_SOUTH_WEST,
                BlockFace.WEST,
                BlockFace.WEST_NORTH_WEST,
                BlockFace.NORTH_WEST,
                BlockFace.NORTH_NORTH_WEST
        };

        int index = (int) (Math.round(yawDegree / 22.5f)) % 16;
        index = Math.abs(index);

        return directions[index];
    }

    // custom function to display win
    public static void displayWinTitle(String team) {
        List<Player> players = new ArrayList<>(Bukkit.getServer().getOnlinePlayers());

        // check which won
        if (team.equalsIgnoreCase("red")) {
            // red won
            for (int i = 0; i < players.size(); i++) {
                players.get(i).sendTitle("§c§lRed Team Won!", "GG", 1, 20 * 5, 1);
            }
        } else if (team.equalsIgnoreCase("blue")) {
            // blue won
            for (int i = 0; i < players.size(); i++) {
                players.get(i).sendTitle("§9§lBlue Team Won!", "GG", 1, 20 * 5, 1);
            }
        }
    }


    // flag spawnpoint coords and rotation
    public static int redx, redy, redz;
    public static int bluex, bluey, bluez;
    public static Rotatable redrot, bluerot;
    // neutral flags do not respawn

    // scores
    public static int redscore = 0;
    public static int bluescore = 0;
    public static int winning_score = 3;


    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.sendMessage(ChatColor.LIGHT_PURPLE + "Welcome to the server!");
    }

//    @EventHandler
//    public static void onPlayerWalkOnStone (PlayerMoveEvent event) {
//        Player player = event.getPlayer();
//        int x = player.getLocation().getBlockX();
//        int y = player.getLocation().getBlockY();
//        int z = player.getLocation().getBlockZ();
//
//        Material block = player.getWorld().getBlockAt(x, y-1, z).getType();
//        if (block == Material.STONE) {
//            player.sendMessage(ChatColor.BLUE + "You standin' on a piece of stone!");
//        }
//    }

    // auto zzz
    @EventHandler
    public static void onPlayerSleep(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();

        if (event.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK) {
            player.chat("zzz");
        }
    }

    // place flag with flag tool
    @EventHandler
    public static void onPlayerRightClick(PlayerInteractEvent event) {
        // check for right click action and make sure it only triggers once
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getHand() == EquipmentSlot.HAND) {
            // check if null
            if (event.getItem() != null) {
                // so the item exists
                // now check if held == tool

                if (event.getItem().getItemMeta().equals(skarmiumItems.flagtool.getItemMeta())) {
                    Player player = event.getPlayer();
                    Block block = event.getClickedBlock();
                    // block above clicked
                    Block block_above = player.getWorld().getBlockAt(block.getX(), block.getY() + 1, block.getZ());
                    // block above the block above clicked
                    Block block_above_above = player.getWorld().getBlockAt(block.getX(), block.getY() + 2, block.getZ());

                    // check if attempting to place flag on a gray banner
                    if (block.getType() == Material.GRAY_BANNER || block_above.getType() == Material.GRAY_BANNER ||
                            block.getType() == Material.BLUE_BANNER || block_above.getType() == Material.BLUE_BANNER ||
                            block.getType() == Material.RED_BANNER || block_above.getType() == Material.RED_BANNER) {
                        player.sendMessage("§e(!) You can't place a flag there anymore");

                        // check if two blocks above the clicked block is empty so to to place a banner
                    } else if (block_above.getType() == Material.AIR && block_above_above.getType() == Material.AIR) {
                        // block_above is also the block position where the flag stands
                        block_above.setType(Material.GRAY_BANNER);
                        // rotate the flag so that it faces the player
                        Rotatable flagRotation = (Rotatable) block_above.getBlockData();
                        // this is where the banner should be facing in terms of degrees
                        // starting from north where north = 0 degrees or 360 degrees
                        float bannerYaw = (player.getLocation().getYaw()) % 360.0f;
                        // convert yaw (degrees) to BlockFace (direction)
                        flagRotation.setRotation(yawToFace(bannerYaw));
                        block_above.setBlockData(flagRotation);

                        // set name to banner for future uses in code
                        NBTTileEntity banner_nbt = new NBTTileEntity(block_above.getState());
                        banner_nbt.setString("CustomName", "{\"text\":\"§7§lNeutral Flag§f\"}");

                        player.sendMessage("§9<♢> A flag has been placed at your specified location");

                    } else {
                        player.sendMessage("§e(!) There are obstacles in the way");
                    }

                }
            }
        }
    }

    // stop flag breaking
    @EventHandler
    public static void onFlagBreakAttempt(BlockBreakEvent event) {
        Player player = event.getPlayer();
        NBTTileEntity block_nbt = new NBTTileEntity(event.getBlock().getState());
        boolean isGrayFlag = event.getBlock().getType() == Material.GRAY_BANNER && block_nbt.getString("CustomName").equalsIgnoreCase("{\"text\":\"§7§lNeutral Flag§f\"}");
        boolean isRedFlag = event.getBlock().getType() == Material.RED_BANNER && block_nbt.getString("CustomName").equalsIgnoreCase("{\"text\":\"§c§lRed Flag§f\"}");
        boolean isBlueFlag = event.getBlock().getType() == Material.BLUE_BANNER && block_nbt.getString("CustomName").equalsIgnoreCase("{\"text\":\"§9§lBlue Flag§f\"}");
        if (isGrayFlag || isRedFlag || isBlueFlag) {
            // is valid flag
            event.setCancelled(true);
            player.sendMessage("§e(!) Remove the flag by standing on it and using '/flag remove'");
            player.sendMessage("§e(!) If you are in a team, capture by right clicking it");
        }
    }

    // flag capture woooooo!
    @EventHandler
    public static void onFlagCaptureAttempt(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getHand() == EquipmentSlot.HAND) {
            // is right click block
            // run whenever "is bare hand" or "is NOT holding flag tool"

            if (event.getItem() == null || !event.getItem().getItemMeta().equals(skarmiumItems.flagtool.getItemMeta())) {
                Player player = event.getPlayer();

                // check for flag
                NBTTileEntity flagToCapture = new NBTTileEntity(event.getClickedBlock().getState());
                boolean isGrayFlag = event.getClickedBlock().getType() == Material.GRAY_BANNER && flagToCapture.getString("CustomName").equalsIgnoreCase("{\"text\":\"§7§lNeutral Flag§f\"}");
                boolean isRedFlag = event.getClickedBlock().getType() == Material.RED_BANNER && flagToCapture.getString("CustomName").equalsIgnoreCase("{\"text\":\"§c§lRed Flag§f\"}");
                boolean isBlueFlag = event.getClickedBlock().getType() == Material.BLUE_BANNER && flagToCapture.getString("CustomName").equalsIgnoreCase("{\"text\":\"§9§lBlue Flag§f\"}");
                boolean isValidFlag = isGrayFlag || isRedFlag || isBlueFlag;
                if (isValidFlag) {
                    // is valid flag
                    String nameOfFlag;
                    ItemStack flagToWear;
                    if (isRedFlag) {
                        nameOfFlag = "§c§lRed Flag";
                        flagToWear = skarmiumItems.redFlag;
                    } else if (isBlueFlag) {
                        nameOfFlag = "§9§lBlue Flag";
                        flagToWear = skarmiumItems.blueFlag;
                    } else {
                        // isGrayFlag
                        nameOfFlag = "§7§lNeutral Flag";
                        flagToWear = skarmiumItems.grayFlag;
                    }

                    // check if player is in a team and the team has a color (color always true)
                    boolean isInTeam = player.getScoreboard().getEntryTeam(player.getName()) != null;
                    if (isInTeam) {
                        boolean teamIsRed = player.getScoreboard().getEntryTeam(player.getName()).getColor() == ChatColor.RED;
                        boolean teamIsBlue = player.getScoreboard().getEntryTeam(player.getName()).getColor() == ChatColor.BLUE;
                        // check if is either blue or red (and only these two colors)
                        if (teamIsRed) {
                            // check if flag is self or enemy
                            if (isRedFlag) {
                                // is self
                                player.sendMessage(skarmiumCommands.prefix_alert + "You can't capture your own flag");
                            } else {
                                // enemy and neutral flag
                                event.getClickedBlock().setType(Material.AIR);

                                // check if helmet slot has stuff
                                if (player.getInventory().getHelmet() != null) {
                                    // put somewhere else before switching with flag
                                    ItemStack helmetItem = player.getInventory().getHelmet();
                                    player.getInventory().addItem(helmetItem);
                                }

                                player.getInventory().setHelmet(flagToWear);
                                // glowing is given to flag bearer via a task scheduler
                                player.getServer().broadcastMessage(skarmiumCommands.prefix_alert + player.getName() + " from §c" + player.getScoreboard().getEntryTeam(player.getName()).getDisplayName() + "§e has captured the " + nameOfFlag + "§r§e!");
                            }

                        } else if (teamIsBlue) {
                            // check if flag is self or enemy
                            if (isBlueFlag) {
                                // is self
                                player.sendMessage(skarmiumCommands.prefix_alert + "You can't capture your own flag");
                            } else {
                                // enemy and neutral flag
                                event.getClickedBlock().setType(Material.AIR);

                                // check if helmet slot has stuff
                                if (player.getInventory().getHelmet() != null) {
                                    // put somewhere else before switching with flag
                                    ItemStack helmetItem = player.getInventory().getHelmet();
                                    player.getInventory().addItem(helmetItem);
                                }

                                player.getInventory().setHelmet(flagToWear);
                                // glowing is given to flag bearer via a task scheduler
                                player.getServer().broadcastMessage(skarmiumCommands.prefix_alert + player.getName() + " from §9" + player.getScoreboard().getEntryTeam(player.getName()).getDisplayName() + "§e has captured the " + nameOfFlag + "§r§e!");
                            }
                        } else {
                            player.sendMessage(skarmiumCommands.prefix_error + "You are not in a red team or a blue team");
                        }

                    } else {
                        player.sendMessage(skarmiumCommands.prefix_error + "You are not in a team");
                    }

                }
            }

        }
    }

    // player dies and drops flag
    @EventHandler
    public static void onPlayerDeathDropFlag(PlayerDeathEvent event) {
        Player player = event.getEntity();
        int x = player.getLocation().getBlockX();
        int y = player.getLocation().getBlockY();
        int z = player.getLocation().getBlockZ();

        // check if player helmet slot is empty
        if (player.getInventory().getHelmet() != null) {
            // now check if is flagbearer
            boolean isGrayFlag = player.getInventory().getHelmet().getItemMeta().equals(skarmiumItems.grayFlag.getItemMeta());
            boolean isRedFlag = player.getInventory().getHelmet().getItemMeta().equals(skarmiumItems.redFlag.getItemMeta());
            boolean isBlueFlag = player.getInventory().getHelmet().getItemMeta().equals(skarmiumItems.blueFlag.getItemMeta());

            if (isGrayFlag) {
                // neutral flag
                player.getWorld().getBlockAt(x, y, z).setType(Material.GRAY_BANNER);
                NBTTileEntity banner_nbt = new NBTTileEntity(player.getWorld().getBlockAt(x, y, z).getState());
                Rotatable flagRotation = (Rotatable) player.getWorld().getBlockAt(player.getLocation()).getBlockData();
                float bannerYaw = (player.getLocation().getYaw()) % 360.0f;
                flagRotation.setRotation(yawToFace(bannerYaw));
                player.getWorld().getBlockAt(player.getLocation()).setBlockData(flagRotation);
                banner_nbt.setString("CustomName", "{\"text\":\"§7§lNeutral Flag§f\"}");

                String nameOfGray = "§7§lNeutral Flag";

                // check team to broadcast message
                if (player.getScoreboard().getEntryTeam(player.getName()).getColor().equals(ChatColor.BLUE)) {
                    // broadcast blue team message
                    player.getServer().broadcastMessage(skarmiumCommands.prefix_alert + player.getName() + " from §9" + player.getScoreboard().getEntryTeam(player.getName()).getDisplayName() + "§e has dropped the " + nameOfGray + "§r§e!");
                } else if (player.getScoreboard().getEntryTeam(player.getName()).getColor().equals(ChatColor.RED)) {
                    // broadcast red team message
                    player.getServer().broadcastMessage(skarmiumCommands.prefix_alert + player.getName() + " from §c" + player.getScoreboard().getEntryTeam(player.getName()).getDisplayName() + "§e has dropped the " + nameOfGray + "§r§e!");
                } else {
                    // some random dude dropped the flag
                    player.getServer().broadcastMessage(skarmiumCommands.prefix_alert + player.getName() + " from §9" + player.getScoreboard().getEntryTeam(player.getName()).getDisplayName() + "§e has dropped the " + nameOfGray + "§r§e!");
                }

            } else if (isRedFlag) {
                // red flag
                player.getWorld().getBlockAt(x, y, z).setType(Material.RED_BANNER);
                NBTTileEntity banner_nbt = new NBTTileEntity(player.getWorld().getBlockAt(x, y, z).getState());
                Rotatable flagRotation = (Rotatable) player.getWorld().getBlockAt(player.getLocation()).getBlockData();
                float bannerYaw = (player.getLocation().getYaw()) % 360.0f;
                flagRotation.setRotation(yawToFace(bannerYaw));
                player.getWorld().getBlockAt(player.getLocation()).setBlockData(flagRotation);
                banner_nbt.setString("CustomName", "{\"text\":\"§c§lRed Flag§f\"}");

                String nameOfRed = "§c§lRed Flag";

                // check team to broadcast message
                if (player.getScoreboard().getEntryTeam(player.getName()).getColor().equals(ChatColor.BLUE)) {
                    // broadcast blue team message
                    player.getServer().broadcastMessage(skarmiumCommands.prefix_alert + player.getName() + " from §9" + player.getScoreboard().getEntryTeam(player.getName()).getDisplayName() + "§e has dropped the " + nameOfRed + "§r§e!");
                } else if (player.getScoreboard().getEntryTeam(player.getName()).getColor().equals(ChatColor.RED)) {
                    // broadcast red team message
                    player.getServer().broadcastMessage(skarmiumCommands.prefix_alert + player.getName() + " from §c" + player.getScoreboard().getEntryTeam(player.getName()).getDisplayName() + "§e has dropped the " + nameOfRed + "§r§e!");
                } else {
                    // some random dude dropped the flag
                    player.getServer().broadcastMessage(skarmiumCommands.prefix_alert + player.getName() + " from §9" + player.getScoreboard().getEntryTeam(player.getName()).getDisplayName() + "§e has dropped the " + nameOfRed + "§r§e!");
                }

            } else if (isBlueFlag) {
                // blue flag
                player.getWorld().getBlockAt(x, y, z).setType(Material.BLUE_BANNER);
                NBTTileEntity banner_nbt = new NBTTileEntity(player.getWorld().getBlockAt(x, y, z).getState());
                Rotatable flagRotation = (Rotatable) player.getWorld().getBlockAt(player.getLocation()).getBlockData();
                float bannerYaw = (player.getLocation().getYaw()) % 360.0f;
                flagRotation.setRotation(yawToFace(bannerYaw));
                player.getWorld().getBlockAt(player.getLocation()).setBlockData(flagRotation);
                banner_nbt.setString("CustomName", "{\"text\":\"§9§lBlue Flag§f\"}");

                String nameOfBlue = "§9§lBlue Flag";

                // check team to broadcast message
                if (player.getScoreboard().getEntryTeam(player.getName()).getColor().equals(ChatColor.BLUE)) {
                    // broadcast blue team message
                    player.getServer().broadcastMessage(skarmiumCommands.prefix_alert + player.getName() + " from §9" + player.getScoreboard().getEntryTeam(player.getName()).getDisplayName() + "§e has dropped the " + nameOfBlue + "§r§e!");
                } else if (player.getScoreboard().getEntryTeam(player.getName()).getColor().equals(ChatColor.RED)) {
                    // broadcast red team message
                    player.getServer().broadcastMessage(skarmiumCommands.prefix_alert + player.getName() + " from §c" + player.getScoreboard().getEntryTeam(player.getName()).getDisplayName() + "§e has dropped the " + nameOfBlue + "§r§e!");
                } else {
                    // some random dude dropped the flag
                    player.getServer().broadcastMessage(skarmiumCommands.prefix_alert + player.getName() + " from §9" + player.getScoreboard().getEntryTeam(player.getName()).getDisplayName() + "§e has dropped the " + nameOfBlue + "§r§e!");
                }

            } else {
                // what's going on here?    ',:(
            }

        }

    }


    // self team will tp the flag back to spawn when touched (not interact)
    @EventHandler
    public static void onSelfTeamReturnFlag(PlayerMoveEvent event) {
        // check if player has team
        if (event.getPlayer().getScoreboard().getEntryTeam(event.getPlayer().getName()) != null) {

            // check if is walking on the flag
            Player player = event.getPlayer();
            int x = player.getLocation().getBlockX();
            int y = player.getLocation().getBlockY();
            int z = player.getLocation().getBlockZ();
            Block block = player.getWorld().getBlockAt(x, y, z);
            NBTTileEntity block_nbt = new NBTTileEntity(block.getState());

            boolean isRedFlag = block.getType().equals(Material.RED_BANNER) && block_nbt.getString("CustomName").equalsIgnoreCase("{\"text\":\"§c§lRed Flag§f\"}");
            boolean isBlueFlag = block.getType().equals(Material.BLUE_BANNER) && block_nbt.getString("CustomName").equalsIgnoreCase("{\"text\":\"§9§lBlue Flag§f\"}");
            if (isRedFlag) {
                // red flag
                // check if the player is red team
                if (player.getScoreboard().getEntryTeam(player.getName()).getColor().equals(ChatColor.RED)) {
                    // check if already at spawn
                    boolean atRedx = player.getLocation().getBlockX() == redx;
                    boolean atRedy = player.getLocation().getBlockY() == redy;
                    boolean atRedz = player.getLocation().getBlockZ() == redz;
                    if (!(atRedx && atRedy && atRedz)) {
                        // then tp it back if not
                        block.setType(Material.AIR);
                        player.getWorld().getBlockAt(redx, redy, redz).setType(Material.RED_BANNER);
                        NBTTileEntity banner_nbt = new NBTTileEntity(player.getWorld().getBlockAt(redx, redy, redz).getState());
                        player.getWorld().getBlockAt(redx, redy, redz).setBlockData(redrot);
                        banner_nbt.setString("CustomName", "{\"text\":\"§c§lRed Flag§f\"}");
                        player.getServer().broadcastMessage(skarmiumCommands.prefix_alert + "§c§lRed Flag §r§ewas returned to its original location");
                    }

                }

            } else if (isBlueFlag) {
                // blue flag
                if (player.getScoreboard().getEntryTeam(player.getName()).getColor().equals(ChatColor.BLUE)) {
                    // check if already at spawn
                    boolean atBluex = player.getLocation().getBlockX() == bluex;
                    boolean atBluey = player.getLocation().getBlockY() == bluey;
                    boolean atBluez = player.getLocation().getBlockZ() == bluez;
                    if (!(atBluex && atBluey && atBluez)) {
                        // then tp it back if not
                        block.setType(Material.AIR);
                        player.getWorld().getBlockAt(bluex, bluey, bluez).setType(Material.BLUE_BANNER);
                        NBTTileEntity banner_nbt = new NBTTileEntity(player.getWorld().getBlockAt(bluex, bluey, bluez).getState());
                        player.getWorld().getBlockAt(bluex, bluey, bluez).setBlockData(bluerot);
                        banner_nbt.setString("CustomName", "{\"text\":\"§9§lBlue Flag§f\"}");
                        player.getServer().broadcastMessage(skarmiumCommands.prefix_alert + "§9§lBlue Flag §r§ewas returned to its original location");
                    }

                }

            } else {
                // huh??
            }

        }

    }


    // scoring system here we go
    @EventHandler
    public static void onTeamScore(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        int x = player.getLocation().getBlockX();
        int y = player.getLocation().getBlockY();
        int z = player.getLocation().getBlockZ();

        // check if player has team, if not error
        if (player.getScoreboard().getEntryTeam(player.getName()) != null) {

            String teamName = player.getScoreboard().getEntryTeam(player.getName()).getDisplayName();
            ChatColor teamColor = player.getScoreboard().getEntryTeam(player.getName()).getColor();

            // check player team color
            if (teamColor.equals(ChatColor.RED)) {
                // is red
                // now check if is at spawn
                boolean isAtFlagSpawn = (x == redx) && (y == redy) && (z == redz);
                if (isAtFlagSpawn) {
                    // check if ally flag is at spawn
                    NBTTileEntity block_nbt = new NBTTileEntity(player.getWorld().getBlockAt(redx,redy,redz).getState());
                    boolean isRedFlag = player.getWorld().getBlockAt(redx, redy, redz).getType() == Material.RED_BANNER && block_nbt.getString("CustomName").equalsIgnoreCase("{\"text\":\"§c§lRed Flag§f\"}");
                    if (isRedFlag) {

                        // check if player has anything in helmet slot, then check if player is bearing an enemy flag
                        if (player.getInventory().getHelmet() != null) {
                            if (player.getInventory().getHelmet().getItemMeta().equals(skarmiumItems.blueFlag.getItemMeta())) {
                                // check if scoreboard is set
                                if (player.getScoreboard().getObjective("skarmiumScores") != null) {
                                    // play ding!
                                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5, 1);

                                    // score +1
                                    redscore++;
                                    player.getScoreboard().getObjective("skarmiumScores").getScore("§c§lRed Team").setScore(redscore);
                                    player.getServer().broadcastMessage(skarmiumCommands.prefix_alert + "§c" + teamName + " §r§ehas scored a point!");

                                    // if reach limit = win!
                                    if (redscore == winning_score) {
                                        // win
                                        player.getServer().broadcastMessage(skarmiumCommands.prefix_alert + "§c" + teamName + " §r§ehas won the game!");
                                        displayWinTitle("red");
                                        // reset score
                                        skarmiumEvents.redscore = 0;
                                        skarmiumEvents.bluescore = 0;
                                        player.getScoreboard().getObjective("skarmiumScores").getScore("§c§lRed Team").setScore(skarmiumEvents.redscore);
                                        player.getScoreboard().getObjective("skarmiumScores").getScore("§9§lBlue Team").setScore(skarmiumEvents.bluescore);

                                        player.getServer().broadcastMessage(skarmiumCommands.prefix_alert + "Flag scores have been reset");
                                    }

                                } else {
                                    player.sendMessage(skarmiumCommands.prefix_error + "You can't score because a scoreboard is not yet setup");
                                }
                                // clear flag from self
                                player.getInventory().setHelmet(null);
                                // respawn enemy flag
                                player.getWorld().getBlockAt(bluex, bluey, bluez).setType(Material.BLUE_BANNER);
                                NBTTileEntity banner_nbt = new NBTTileEntity(player.getWorld().getBlockAt(bluex, bluey, bluez).getState());
                                banner_nbt.setString("CustomName", "{\"text\":\"§9§lBlue Flag§f\"}");
                                player.getWorld().getBlockAt(bluex, bluey, bluez).setBlockData(bluerot);
                            }
                        }
                    }
                }

            } else if (teamColor.equals(ChatColor.BLUE)) {
                // is blue
                boolean isAtFlagSpawn = (x == bluex) && (y == bluey) && (z == bluez);
                if (isAtFlagSpawn) {
                    // do same here
                    NBTTileEntity block_nbt = new NBTTileEntity(player.getWorld().getBlockAt(bluex,bluey,bluez).getState());
                    boolean isBlueFlag = player.getWorld().getBlockAt(bluex, bluey, bluez).getType() == Material.BLUE_BANNER && block_nbt.getString("CustomName").equalsIgnoreCase("{\"text\":\"§9§lBlue Flag§f\"}");
                    if (isBlueFlag) {

                        // check if player has anything in helmet slot, then check if player is bearing an enemy flag
                        if (player.getInventory().getHelmet() != null) {
                            if (player.getInventory().getHelmet().getItemMeta().equals(skarmiumItems.redFlag.getItemMeta())) {
                                // check if scoreboard is set
                                if (player.getScoreboard().getObjective("skarmiumScores") != null) {
                                    // play ding!
                                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5, 1);

                                    // score +1
                                    bluescore++;
                                    player.getScoreboard().getObjective("skarmiumScores").getScore("§9§lBlue Team").setScore(bluescore);
                                    player.getServer().broadcastMessage(skarmiumCommands.prefix_alert + "§9" + teamName + " §r§ehas scored a point!");

                                    // limit == win
                                    if (bluescore == winning_score) {
                                        // win
                                        player.getServer().broadcastMessage(skarmiumCommands.prefix_alert + "§9" + teamName + " §r§ehas won the game!");
                                        displayWinTitle("blue");
                                        // reset score
                                        skarmiumEvents.redscore = 0;
                                        skarmiumEvents.bluescore = 0;
                                        player.getScoreboard().getObjective("skarmiumScores").getScore("§c§lRed Team").setScore(skarmiumEvents.redscore);
                                        player.getScoreboard().getObjective("skarmiumScores").getScore("§9§lBlue Team").setScore(skarmiumEvents.bluescore);

                                        player.getServer().broadcastMessage(skarmiumCommands.prefix_alert + "Flag scores have been reset");
                                    }
                                } else {
                                    player.sendMessage(skarmiumCommands.prefix_error + "You can't score because a scoreboard is not yet set up");
                                }
                                // clear flag from self
                                player.getInventory().setHelmet(null);
                                // respawn enemy flag
                                player.getWorld().getBlockAt(redx, redy, redz).setType(Material.RED_BANNER);
                                NBTTileEntity banner_nbt = new NBTTileEntity(player.getWorld().getBlockAt(redx, redy, redz).getState());
                                banner_nbt.setString("CustomName", "{\"text\":\"§c§lRed Flag§f\"}");
                                player.getWorld().getBlockAt(redx, redy, redz).setBlockData(redrot);
                            }
                        }
                    }
                }
            } else {
                // this shouldn't have anything
            }

        } else {
            // means player has no team
            // player.sendMessage(skarmiumCommands.prefix_error + "You are currently not in a red or blue team");
            // meh. will spam.
        }
    }
}
