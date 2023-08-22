package net.devscape.project.minerave_ranks.commands;

import net.devscape.project.minerave_ranks.MineraveRanks;
import net.devscape.project.minerave_ranks.handlers.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RankCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            if (cmd.getName().equalsIgnoreCase("grant")) {
                if (args.length == 0) {
                    sender.sendMessage(ChatColor.RED + "Usage: /grant <player> <rank>");
                } else if (args.length == 2) {
                    // /grant <player> <rank>
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                    String rank = args[1];

                    if (!MineraveRanks.getMineraveRanks().getMariadb().exists(target)) {
                        sender.sendMessage(ChatColor.RED + "This player has never played here!");
                        return true;
                    }

                    if (MineraveRanks.getMineraveRanks().getRankManager().getRank(rank) == null) {
                        sender.sendMessage(ChatColor.RED + "This rank does not exist!");
                        return true;
                    }

                    MineraveRanks.getMineraveRanks().getMariadb().setRank(target, rank);
                    sender.sendMessage(ChatColor.GREEN + "Successfully set the players rank to: " + rank);
                }
            }
        } else {

            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("grant")) {
                if (MineraveRanks.getMineraveRanks().getPlayerDataClass().isAdmin(player)) {
                    if (args.length == 0) {
                        player.sendMessage(ChatColor.RED + "Usage: /grant <player> <rank>");
                    } else if (args.length == 2) {
                        // /grant <player> <rank>
                        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                        String rank = args[1];

                        if (!MineraveRanks.getMineraveRanks().getMariadb().exists(target)) {
                            player.sendMessage(ChatColor.RED + "This player has never played here!");
                            return true;
                        }

                        if (MineraveRanks.getMineraveRanks().getRankManager().getRank(rank) == null) {
                            player.sendMessage(ChatColor.RED + "This rank does not exist!");
                            return true;
                        }

                        if (MineraveRanks.getMineraveRanks().getPlayerDataClass().getRankName(player).equalsIgnoreCase(rank)) {
                            player.sendMessage(ChatColor.RED + "This player already has this rank assigned.");
                            return true;
                        }

                        Rank sendersRank = MineraveRanks.getMineraveRanks().getPlayerDataClass().getRank(player);
                        Rank gettingRank = MineraveRanks.getMineraveRanks().getRankManager().getRank(rank);

                        if (sendersRank.getWeight() < gettingRank.getWeight()) {
                            player.sendMessage(ChatColor.RED + "You are unable to assign a rank that surpasses your own!");
                            return true;
                        }

                        MineraveRanks.getMineraveRanks().getMariadb().setRank(target, rank);
                        player.sendMessage(ChatColor.GREEN + "Successfully set the players rank to: " + rank);
                    }
                }
            }
        }
        return false;
    }
}
