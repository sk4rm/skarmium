package me.skarm.skarmium.events;

import de.tr7zw.nbtapi.NBTTileEntity;
import me.skarm.skarmium.commands.skarmiumCommands;
import me.skarm.skarmium.items.skarmiumItems;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Rotatable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class skarmiumEvents implements Listener {

    // custom function for converting yaw to a BlockFace direction
    public static BlockFace yawToFace (float yawDegree) {
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



    @EventHandler
    public static void onPlayerJoin (PlayerJoinEvent event) {
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
    public static void onPlayerSleep (PlayerBedEnterEvent event) {
        Player player = event.getPlayer();

        if (event.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK){
            player.chat("zzz");
        }
    }

    // place flag with flag tool
    @EventHandler
    public static void onPlayerRightClick (PlayerInteractEvent event) {
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
                        banner_nbt.setString("CustomName","{\"text\":\"§7§lNeutral Flag§f\"}");

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
    public static void onFlagBreakAttempt (BlockBreakEvent event) {
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
    public static void onFlagCaptureAttempt (PlayerInteractEvent event) {
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
                                player.getInventory().setHelmet(flagToWear);
                                // give glowing
//                                PotionEffect glowing = new PotionEffect(PotionEffectType.GLOWING, 99999, 0, true, false);
//                                player.addPotionEffect(glowing);
                                player.getServer().broadcastMessage(skarmiumCommands.prefix_alert + player.getName() + " from §c" + player.getScoreboard().getEntryTeam(player.getName()).getDisplayName() + "§e has captured the " + nameOfFlag +"§r§e!");
                            }

                        } else if (teamIsBlue) {
                            player.sendMessage("blue");
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

}
