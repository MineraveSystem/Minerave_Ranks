package net.devscape.project.minerave_ranks.storage;

import lombok.Getter;
import net.devscape.project.minerave_ranks.MineraveRanks;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class RankDB {

    public Connection connection = MineraveRanks.getMineraveRanks().getMariadb().getConnection();

    public RankDB() {
        createTable();
        loadDefaultRanks();
    }

    public void createTable() {
        String rankTable = "CREATE TABLE IF NOT EXISTS `ranks` " +
                "(Name VARCHAR(255) NOT NULL, " +
                "Weight VARCHAR(255) NOT NULL, " +
                "Prefix VARCHAR(255) NOT NULL, " +
                "Suffix VARCHAR(255) NOT NULL, " +
                "isDonor VARCHAR(255) NOT NULL, " +
                "isStaff VARCHAR(255) NOT NULL, " +
                "isAdmin VARCHAR(255) NOT NULL, " +
                "Permissions VARCHAR(255) NOT NULL, " +
                "PRIMARY KEY (Name))";

        Statement stmt;
        try {
            stmt = connection.createStatement();
            stmt.execute(rankTable);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean rankExists(String rank) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM `ranks` WHERE (Name=?)");
            statement.setString(1, rank);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void createRank(String rank, int weight, String prefix, String suffix, boolean isDonor, boolean isStaff, boolean isAdmin) {
        if (rankExists(rank)) {
            return;
        }

        try (PreparedStatement statement = getConnection().prepareStatement(
                "INSERT INTO `ranks` (Name, Weight, Prefix, Suffix, isDonor, isStaff, isAdmin, Permissions) VALUES (?,?,?,?,?,?,?,?)")) {
            statement.setString(1, rank); // rank name
            statement.setInt(2, weight); // weight
            statement.setString(3, prefix); // prefix
            statement.setString(4,  suffix); // suffix
            statement.setBoolean(5, isDonor); // donor
            statement.setBoolean(6, isStaff); // staff
            statement.setBoolean(7, isAdmin); // admin
            statement.setString(8, ""); // permissions
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadAllRanks() {
        if (!MineraveRanks.getMineraveRanks().getRankManager().getRankMap().isEmpty()) {
            MineraveRanks.getMineraveRanks().getRankManager().getRankMap().clear();
        }

        for (String name : getAllRanks()) {
            try (PreparedStatement statement = getConnection().prepareStatement(
                    "SELECT * FROM ranks WHERE Name = ?")) {
                statement.setString(1, name);
                ResultSet result = statement.executeQuery();

                if (result.next()) {
                    int weight = result.getInt("Weight");
                    String displayname = result.getString("displayname");
                    String prefix = result.getString("Prefix");
                    String suffix = result.getString("Suffix");
                    boolean isdefault = result.getBoolean("isDefault");

                    String permissionsString = result.getString("Permissions");
                    List<String> permissions = deserializePermissions(permissionsString);

                    //Rank rank = new Rank(name, weight, displayname, prefix, suffix, isdefault);
                    //MineraveRanks.getMineraveRanks().getRankManager().getRankMap().add(rank);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void setPrefix(String rank, String value) {
        String sql = "UPDATE `ranks` SET Prefix=? WHERE (Name=?)";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setString(1, value);
            statement.setString(2, rank);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setSuffix(String rank, String value) {
        String sql = "UPDATE `ranks` SET Suffix=? WHERE (Name=?)";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setString(1, value);
            statement.setString(2, rank);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setWeight(String rank, int value) {
        String sql = "UPDATE `ranks` SET Weight=? WHERE (Name=?)";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, value);
            statement.setString(2, rank);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getWeight(String rank) {
        int value = 0;
        String query = "SELECT * FROM `ranks` WHERE (Name=?)";
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setString(1, rank);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    value = resultSet.getInt("Weight");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return value;
    }

    public String getPrefix(String rank) {
        String value = "";
        String query = "SELECT * FROM `ranks` WHERE (Name=?)";
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setString(1, rank);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    value = resultSet.getString("Prefix");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return value;
    }

    public boolean isDonor(String rank) {
        boolean value = false;
        String query = "SELECT * FROM `ranks` WHERE (Name=?)";
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setString(1, rank);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    value = resultSet.getBoolean("isDonor");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return value;
    }

    public boolean isStaff(String rank) {
        boolean value = false;
        String query = "SELECT * FROM `ranks` WHERE (Name=?)";
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setString(1, rank);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    value = resultSet.getBoolean("isStaff");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return value;
    }

    public boolean isAdmin(String rank) {
        boolean value = false;
        String query = "SELECT * FROM `ranks` WHERE (Name=?)";
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setString(1, rank);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    value = resultSet.getBoolean("isAdmin");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return value;
    }

    public String getSuffix(String rank) {
        String value = "";
        String query = "SELECT * FROM `ranks` WHERE (Name=?)";
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setString(1, rank);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    value = resultSet.getString("Suffix");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return value;
    }

    public ArrayList<String> getAllRanks() {
        ArrayList<String> ranks = new ArrayList<>();
        String query = "SELECT name FROM `ranks` WHERE 1";
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                ranks.add(set.getString("Name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ranks;
    }

    private String serializePermissions(List<String> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (String permission : permissions) {
            sb.append(permission).append(",");
        }
        return sb.toString();
    }

    private List<String> deserializePermissions(String permissionsString) {
        List<String> permissions = new ArrayList<>();

        if (permissionsString == null || permissionsString.isEmpty()) {
            return permissions;
        }

        String[] permissionNames = permissionsString.split(",");
        permissions.addAll(Arrays.asList(permissionNames));
        return permissions;
    }

    public void loadDefaultRanks() {
        createRank(
                "default",
                0,
                "&f兆 &7",
                "",
                false,
                false,
                false);

        createRank(
                "vip",
                30,
                "&f充 &f",
                "",
                true,
                false,
                false);

        createRank(
                "ultimate",
                35,
                "&f兄 &f",
                "",
                true,
                false,
                false);

        createRank(
                "creator",
                35,
                "&f先 &7",
                "",
                false,
                false,
                false);

        createRank(
                "chatmod",
                40,
                "&f元 &#4185ff",
                "",
                false,
                true,
                false);

        createRank(
                "mod",
                45,
                "&f優 &#4185ff",
                "",
                false,
                true,
                false);

        createRank(
                "team",
                50,
                "&f償 &#7053ff",
                "",
                false,
                true,
                false);

        createRank(
                "admin",
                60,
                "&f儒 &#ff2b2b",
                "",
                false,
                false,
                true);

        createRank(
                "owner",
                100,
                "&f倹 &#ff2b2b",
                "",
                false,
                false,
                true);
    }
}