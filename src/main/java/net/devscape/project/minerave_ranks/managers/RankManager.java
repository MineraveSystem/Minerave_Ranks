package net.devscape.project.minerave_ranks.managers;

import lombok.Getter;
import net.devscape.project.minerave_ranks.MineraveRanks;
import net.devscape.project.minerave_ranks.handlers.Rank;

import java.util.ArrayList;
import java.util.List;

@Getter
public class RankManager {
    
    private final List<Rank> rankMap = new ArrayList<>();

    public Rank getRank(String name) {
        for (Rank rank : rankMap) {
            if (rank.getName().equalsIgnoreCase(name)) {
                return rank;
            }
        }

        return null;
    }

    public void load() {
        for (String str : MineraveRanks.getMineraveRanks().getConfig().getConfigurationSection("ranks").getKeys(false)) {

            int weight = MineraveRanks.getMineraveRanks().getConfig().getInt("ranks." + str + ".weight");
            String prefix = MineraveRanks.getMineraveRanks().getConfig().getString("ranks." + str + ".prefix");
            //String suffix = MineraveRanks.getMineraveRanks().getConfig().getString("ranks." + str + ".suffix");
            //String displayname = MineraveRanks.getMineraveRanks().getConfig().getString("ranks." + str + ".displayname");
            boolean admin = MineraveRanks.getMineraveRanks().getConfig().getBoolean("ranks." + str + ".admin");

            Rank rank = new Rank(
                    str,
                    weight,
                    prefix,
                    "",
                    "",
                    admin);

            rankMap.add(rank);
        }
    }

    public void save() {

    }
}
