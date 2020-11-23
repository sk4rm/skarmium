package me.skarm.skarmium.events;

import me.skarm.skarmium.items.skarmiumItems;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Rotation;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Rotatable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class skarmiumEvents implements Listener {

    // custom function for converting yaw to a BlockFace direction
    private static BlockFace yawToFace (float yawDegree) {
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

    @EventHandler
    public static void onPlayerSleep (PlayerBedEnterEvent event) {
        Player player = event.getPlayer();

        if (event.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK){
            player.chat("zzz");
        }
    }

//    @EventHandler
//    public static void onPlayerThrowEgg (PlayerEggThrowEvent event) {
//        event.setHatching(true);
//        event.setNumHatches((byte) 5);
//        event.setHatchingType(EntityType.RABBIT);
//    }

    @EventHandler
    public static void onPlayerRightClick (PlayerInteractEvent event) {
        // check for right click action
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            // check if null
            if (event.getItem() != null) {
                // so the item exists
                // now check if held == tool
                if (event.getItem().getItemMeta().equals(skarmiumItems.flagtool.getItemMeta())) {
                    Player player = event.getPlayer();
                    Block block = event.getClickedBlock();
                    // block above clicked
                    Block block_above = player.getWorld().getBlockAt(block.getX(), block.getY()+1, block.getZ());
                    // block above the block above clicked
                    Block block_above_above = player.getWorld().getBlockAt(block.getX(), block.getY()+2, block.getZ());
                    // check if two blocks above the clicked block is empty so to to place a banner
                    if (block_above.getType() == Material.AIR && block_above_above.getType() == Material.AIR) {
                        // block_above is also the block position where the flag stands
                        block_above.setType(Material.GRAY_BANNER);
                        // rotate the flag so that it faces the player
                        Rotatable flagRotation = (Rotatable) block_above.getBlockData();
                        // this is where the banner should be facing in terms of degrees
                        // starting from north where north = 0 degrees or 360 degrees
                        float bannerYaw = (player.getLocation().getYaw()) % 360.0f;
                        System.out.println(bannerYaw);
                        // convert yaw (degrees) to blockface (direction)
                        flagRotation.setRotation(yawToFace(bannerYaw));
                        block_above.setBlockData(flagRotation);
                        player.sendMessage("§9<♢> A flag has been placed at your specified location");
                    } else {
                        player.sendMessage("§e(!) There are obstacles in the way");
                    }

                }
            }
        }
    }

    @EventHandler
    public static void onFlagBreakAttempt (BlockDamageEvent event) {
        //
    }

}
