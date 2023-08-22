package net.devscape.project.minerave_ranks.storage;

import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.*;
import java.util.UUID;

@Getter
public class MariaDB {

    private Connection connection;

    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;

    private boolean isConnected;

    public MariaDB(String host, int port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;

        this.connect();
        createTable();
    }

    private void connect() {
        try {
            MariaDbDataSource dataSource = new MariaDbDataSource();
            dataSource.setUser(username);
            dataSource.setPassword(password);
            dataSource.setUrl("jdbc:mariadb://" + host + ":" + port + "/" + database);

            synchronized (this) {
                if (connection != null && !connection.isClosed()) {
                    System.err.println("Error: Connection to MariaDB is already open.");
                    return;
                }
                this.connection = dataSource.getConnection();
                isConnected = true;
                createTable();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("MariaDB: Error connecting to the database. Check the MariaDB data details before contacting the developer.");
        }
    }

    public void createTable() {
        String userTable = "CREATE TABLE IF NOT EXISTS `users` " +
                "(Name VARCHAR(255) NOT NULL, " +
                "UUID VARCHAR(255) NOT NULL, " +
                "Rank VARCHAR(255) NOT NULL, " +
                "PRIMARY KEY (UUID))";

        Statement stmt;
        try {
            stmt = connection.createStatement();
            stmt.execute(userTable);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean exists(Player player) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM `users` WHERE (UUID=?)");
            statement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean exists(OfflinePlayer player) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM `users` WHERE (UUID=?)");
            statement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void createPlayer(Player player) {
        if (exists(player)) {
            return;
        }

        try (PreparedStatement statement = getConnection().prepareStatement(
                "INSERT INTO `users` (Name, UUID, Rank) VALUES (?,?,?)")) {
            statement.setString(1, player.getName());
            statement.setString(2, player.getUniqueId().toString());
            statement.setString(3, "Default");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setRank(OfflinePlayer player, String identifier) {
        String sql = "UPDATE `users` SET Rank=? WHERE (UUID=?)";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setString(1, identifier);
            statement.setString(2, player.getUniqueId().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getRank(UUID uuid) {
        String value = "";
        String query = "SELECT * FROM `users` WHERE (UUID=?)";
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setString(1, uuid.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    value = resultSet.getString("Rank");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return value;
    }
}