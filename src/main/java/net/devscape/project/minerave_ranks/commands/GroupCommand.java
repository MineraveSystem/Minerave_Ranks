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

import static net.devscape.project.minerave_ranks.utils.Utils.msgPlayer;

public class GroupCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        } else {

            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("group")) {
                if (MineraveRanks.getMineraveRanks().getPlayerDataClass().isAdmin(player)) {
                    if (args.length == 0) {
                        msgPlayer(player,
                                "&b&lGroup Commands:",
                                "",
                                "&3/group setprefix <rank> <...>",
                                "&3/group setsuffix <rank> <...>",
                                "&3/group setweight <rank> <...>");
                    } else if (args.length == 3) {
                        if (args[0].equalsIgnoreCase("setprefix")) {
                            String rank = args[1];
                            String value = args[2];

                            if (MineraveRanks.getMineraveRanks().getRankManager().getRank(rank) == null) {
                                msgPlayer(player,"&cThis rank does not exist!");
                                return true;
                            }

                            MineraveRanks.getMineraveRanks().getRankDB().setPrefix(rank, value);
                            msgPlayer(player,"&aSuccessfully set this rank's prefix to: &f" + value);
                        } else if (args[0].equalsIgnoreCase("setsuffix")) {
                            String rank = args[1];
                            String value = args[2];

                            if (MineraveRanks.getMineraveRanks().getRankManager().getRank(rank) == null) {
                                msgPlayer(player,"&cThis rank does not exist!");
                                return true;
                            }

                            MineraveRanks.getMineraveRanks().getRankDB().setSuffix(rank, value);
                            msgPlayer(player,"&aSuccessfully set this rank's suffix to: &f" + value);
                        } if (args[0].equalsIgnoreCase("setweight")) {
                            String rank = args[1];
                            int value = Integer.parseInt(args[2]);

                            if (MineraveRanks.getMineraveRanks().getRankManager().getRank(rank) == null) {
                                msgPlayer(player,"&cThis rank does not exist!");
                                return true;
                            }

                            MineraveRanks.getMineraveRanks().getRankDB().setWeight(rank, value);
                            msgPlayer(player,"&aSuccessfully set this rank's prefix to: &f" + value);
                        } else {
                            msgPlayer(player, "&cInvalid arguments!");
                        }
                    } else {
                        msgPlayer(player, "&cInvalid arguments!");
                    }
                } else {
                    msgPlayer(player, "&cNo permission!");
                }
            }
        }
        return false;
    }
}
