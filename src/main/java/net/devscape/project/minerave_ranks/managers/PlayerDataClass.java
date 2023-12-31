package net.devscape.project.minerave_ranks.managers;

import lombok.Getter;
import net.devscape.project.minerave_ranks.MineraveRanks;
import net.devscape.project.minerave_ranks.handlers.Rank;
import org.bukkit.entity.Player;

@Getter
public class PlayerDataClass {

    public PlayerDataClass() {}

    public Rank getRank(Player offlinePlayer) {
        return MineraveRanks.getMineraveRanks().getRankManager().getRank(MineraveRanks.getMineraveRanks().getMariadb().getRank(offlinePlayer.getUniqueId()));
    }

    public String getRankName(Player player) {
        return MineraveRanks.getMineraveRanks().getMariadb().getRank(player.getUniqueId());
    }

    public boolean isAdmin(Player player) {
        return getRank(player).isAdmin();
    }
}
