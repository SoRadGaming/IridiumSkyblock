package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.MissionRestart;
import com.iridium.iridiumskyblock.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class onPlayerFish implements Listener {

    @EventHandler
    public void onPlayerFish(PlayerFishEvent e) {
        try {
            if (e.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
                User u = User.getUser(e.getPlayer());
                if (u.getIsland() != null) {
                    if (u.getIsland().fisherman > -1) {
                        u.getIsland().fisherman++;
                        if (u.getIsland().fisherman >= IridiumSkyblock.getMissions().fisherman.amount) {
                            u.getIsland().fisherman = IridiumSkyblock.getConfiguration().missionRestart == MissionRestart.Instantly ? 0 : -1;
                            u.getIsland().completeMission("Fisherman", IridiumSkyblock.getMissions().fisherman.crystalReward, IridiumSkyblock.getMissions().fisherman.vaultReward);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            IridiumSkyblock.getInstance().sendErrorMessage(ex);
        }
    }
}
