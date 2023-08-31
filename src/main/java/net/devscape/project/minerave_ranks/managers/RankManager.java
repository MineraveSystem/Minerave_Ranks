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
        for (String str : MineraveRanks.getMineraveRanks().getRankDB().getAllRanks()) {
            int weight = MineraveRanks.getMineraveRanks().getRankDB().getWeight(str);
            String prefix = MineraveRanks.getMineraveRanks().getRankDB().getPrefix(str);
            String suffix = MineraveRanks.getMineraveRanks().getRankDB().getSuffix(str);
            boolean admin = MineraveRanks.getMineraveRanks().getRankDB().isAdmin(str);
            boolean staff = MineraveRanks.getMineraveRanks().getRankDB().isStaff(str);
            boolean donor = MineraveRanks.getMineraveRanks().getRankDB().isDonor(str);

            Rank rank = new Rank(
                    str,
                    weight,
                    prefix,
                    suffix,
                    admin,
                    staff,
                    donor);

            rankMap.add(rank);
        }
    }
}
