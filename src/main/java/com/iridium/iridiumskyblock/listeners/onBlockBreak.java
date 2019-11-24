package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.*;
import org.bukkit.CropState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.material.Crops;

public class onBlockBreak implements Listener {

    @EventHandler
    public void onBreakBlock(BlockBreakEvent e) {
        try {
            if (e.isCancelled()) return;
            User u = User.getUser(e.getPlayer());
            Island island = IridiumSkyblock.getIslandManager().getIslandViaLocation(e.getBlock().getLocation());
            if (island != null) {
                if (u.islandID == island.getId()) {
                    if (e.getBlock().getType().name().endsWith("ORE")) {
                        if (u.getIsland().miner > -1) {
                            u.getIsland().miner++;
                            if (u.getIsland().miner >= IridiumSkyblock.getMissions().miner.amount) {
                                island.miner = IridiumSkyblock.getConfiguration().missionRestart == MissionRestart.Instantly ? 0 : -1;
                                island.completeMission("Miner", IridiumSkyblock.getMissions().miner.crystalReward, IridiumSkyblock.getMissions().miner.vaultReward);
                            }
                        }
                    }
                    if (e.getBlock().getState().getData() instanceof Crops) {
                        CropState state = ((Crops) e.getBlock().getState().getData()).getState();
                        if (u.getIsland().farmer > -1 && state == CropState.RIPE) {
                            u.getIsland().farmer++;
                            if (u.getIsland().farmer >= IridiumSkyblock.getMissions().farmer.amount) {
                                island.farmer = IridiumSkyblock.getConfiguration().missionRestart == MissionRestart.Instantly ? 0 : -1;
                                island.completeMission("Farmer", IridiumSkyblock.getMissions().farmer.crystalReward, IridiumSkyblock.getMissions().farmer.vaultReward);
                            }
                        }
                    }
                }
                if ((!island.getPermissions((u.islandID == island.getId() || island.isCoop(u.getIsland())) ? (island.isCoop(u.getIsland()) ? Role.Member : u.getRole()) : Role.Visitor).breakBlocks) && !u.bypassing)
                    e.setCancelled(true);
            } else {
                if (e.getBlock().getLocation().getWorld().equals(IridiumSkyblock.getIslandManager().getWorld()) || e.getBlock().getLocation().getWorld().equals(IridiumSkyblock.getIslandManager().getNetherWorld())) {
                    if (!u.bypassing) {
                        e.setCancelled(true);
                    }
                }
            }
        } catch (Exception ex) {
            IridiumSkyblock.getInstance().sendErrorMessage(ex);
        }
    }
}
