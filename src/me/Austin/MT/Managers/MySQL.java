package me.Austin.MT.Managers;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.sql.*;

/**
 * This class contains all MySQL functions.
 *
 * @author Unkown someone from Stackoverflow/Spigot
 * @since 1.0
 */
public class MySQL {

    //TODO: Get these values from a config BEFORE BETA release
    private static String host = "mrtickets.clzj8rdectfm.us-west-2.rds.amazonaws.com";
    private static String port = "3306";
    private static String database = "Servers";
    private static String username = "mrmcaustin1";
    private static String password = "Cl!fford123";
    private static Connection con;

    private static ConsoleCommandSender console = Bukkit.getConsoleSender();

    /**
     * Connect to the database
     */
    public static void connect() {
        if (!isConnected()) {
            try {
                con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username,
                        password);
                console.sendMessage("[MrTickets] Connected to MySQL!!");
                LogToFile.log("info", "Connected to MySQL");
            } catch (SQLException e) {
                e.printStackTrace();

                PMessage.stackTrace();
            }
        }
    }

    /**
     * Disconnect from the database
     */
    public static void disconnect() {
        if (isConnected()) {
            try {
                con.close();
                console.sendMessage("[MrTickets] Disconnected from MySQL!!");
                LogToFile.log("info", "Disconnected from MySQL");
            } catch (SQLException e) {
                e.printStackTrace();

                PMessage.stackTrace();
            }
        }
    }

    /**
     * Checks if the db is connected
     *
     * @return If the DB is connected
     */
    public static boolean isConnected() {
        return (con != null);
    }

    /**
     * Get the current connection
     *
     * @return The connection
     */
    public static Connection getConnection() {
        return con;
    }

    /**
     * This method is used to check if a player is in a table.
     *
     * @param player
     *         the player to look up
     * @param table
     *         the table
     *
     * @return if the player is in the table
     *
     * @author ItsMattHogan - Spigot
     */
    public static boolean tableContainsPlayer(Player player, String table) {

        try {
            Statement statement = getConnection().createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT * FROM `" + table + "` WHERE `UUID` = '" + player.getUniqueId().toString() + "';");

            return result.next();
        } catch (SQLException exception) {
            // exception.printStackTrace();

            PMessage.stackTrace();
        }

        return false;
    }


    //
    public static void createServerTable(String sUUID) {
        try {
            Statement statement = getConnection().createStatement();


            //Ticket Table
            String sql = "CREATE TABLE `" + sUUID + "-tickets` (\n" +
                    " `TicketID` int(11) NOT NULL AUTO_INCREMENT,\n" +
                    " `UUID` varchar(36) NOT NULL,\n" +
                    " `Error` int(6) NOT NULL,\n" +
                    " `Message` text NOT NULL,\n" +
                    " `Completed` tinytext NOT NULL,\n" +
                    " `Assigned` varchar(36) NOT NULL,\n" +
                    " `Date` date NOT NULL,\n" +
                    " `milliseconds` bigint(255) NOT NULL,\n" +
                    " `Priority` text NOT NULL,\n" +
                    " PRIMARY KEY (`TicketID`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=latin1";
            statement.executeUpdate(sql);

            //Staff Table
            String staffTable = "CREATE TABLE `" + sUUID + "-staff` (\n" +
                    " `staffID` int(11) NOT NULL AUTO_INCREMENT,\n" +
                    " `UUID` varchar(36) NOT NULL,\n" +
                    " `tAssigned` int(11) NOT NULL,\n" +
                    " PRIMARY KEY (`staffID`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=latin1\n";
            statement.executeUpdate(staffTable);

        } catch (SQLException e) {
            PMessage.stackTrace();
        }
    }

}
