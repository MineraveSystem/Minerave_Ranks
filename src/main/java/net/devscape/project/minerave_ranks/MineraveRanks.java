package net.devscape.project.minerave_ranks;

import lombok.Getter;
import net.devscape.project.minerave_ranks.Listeners.PlayerJoin;
import net.devscape.project.minerave_ranks.commands.RankCommand;
import net.devscape.project.minerave_ranks.managers.PlayerDataClass;
import net.devscape.project.minerave_ranks.managers.RankManager;
import net.devscape.project.minerave_ranks.storage.MariaDB;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

@Getter
public final class MineraveRanks extends JavaPlugin {

    private static MineraveRanks mineraveRanks;
    private MariaDB mariadb;
    private RankManager rankManager;
    private PlayerDataClass playerDataClass;

    @Override
    public void onEnable() {
        mineraveRanks = this;
        saveDefaultConfig();

        this.mariadb = new MariaDB(
                getConfig().getString("data.host"),
                getConfig().getInt("data.port"),
                getConfig().getString("data.database"),
                getConfig().getString("data.username"),
                getConfig().getString("data.password"));

        rankManager = new RankManager();
        playerDataClass = new PlayerDataClass();

        loadCommands();
        loadListeners();
        rankManager.load();
    }

    @Override
    public void onDisable() {
        try {
            if (!mariadb.getConnection().isClosed()) {
                mariadb.getConnection().close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadCommands() {
        getCommand("grant").setExecutor(new RankCommand());
    }

    public void loadListeners() {
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
    }


    public static MineraveRanks getMineraveRanks() {
        return mineraveRanks;
    }
}
