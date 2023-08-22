package net.devscape.project.minerave_ranks.Listeners;

import net.devscape.project.minerave_ranks.MineraveRanks;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        MineraveRanks.getMineraveRanks().getMariadb().createPlayer(player);
    }
}