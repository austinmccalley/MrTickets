package me.Austin.MT;

import me.Austin.MT.Managers.ErrorNumGen;
import me.Austin.MT.Managers.LogToFile;
import me.Austin.MT.Managers.MySQL;
import me.Austin.MT.Managers.Objects.Server;
import me.Austin.MT.Managers.PMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.Permission;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

/**
 * This class can handel all functions relating to when a user joins.
 *
 * @author MrMcaustin1
 * @since 1.0
 */
public class Join implements Listener {

    //Tables
    private static String tickets = Server.getSUUID() + "-tickets";
    private static String staff = Server.getSUUID() + "-staff";

    /**
     * The player joining event, this gets called automatically
     *
     * @param e
     *         The event
     */
    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();//Get the player who joined
        try {
            addStaff(p.getUniqueId());//Call the addPlayer function
            PMessage.Message(p, "Welcome to the server " + p.getName() + "!", "Normal");//Welcome them to the server
        } catch (SQLException e1) {
            PMessage.stackTrace();
            e1.printStackTrace();
            int rNumb = ErrorNumGen.newNum();
            p.kickPlayer(ChatColor.WHITE + "Please try re-joining. There was a MySQL error. Please provide the number "
                    + ChatColor.RED + rNumb + "" + ChatColor.WHITE + " to the staff.");
            String rN = String.valueOf(rNumb);
            LogToFile.log("severe", "Could not add " + p.getUniqueId().toString() + " to the table! #" + rN);
        }
    }

    /**
     * This method is called upon when a player joins. It checks if the user is a staff member and if so it adds them to the table and messages the Player their staff ID.
     * <p>
     * <i>StaffID is not used currently anywhere in the plugin server side</i>
     *
     * @param uuid
     *         UUID of the player joining
     *
     * @throws SQLException
     *         Just in case MySQL decides to shit itself
     */
    private void addStaff(UUID uuid) throws SQLException {
        Player p = Bukkit.getPlayer(uuid);

        if (p.hasPermission(new Permission("MrTickets.Admin"))) {//Checks the player if they have the permission for ticket admin
            if (!MySQL.tableContainsPlayer(p, staff)) {//Checks if the staff table doesn't contains the player

                System.out.println("INSERT INTO `" + staff + "` (`UUID`, `tAssigned`) VALUES (`" + uuid.toString() + "`, 0)");
                PreparedStatement ps = MySQL.getConnection()
                        .prepareStatement("INSERT INTO `" + staff + "`(`UUID`, `tAssigned`) VALUES ('" + uuid.toString() + "',0)");//Prepared statement
                ps.executeUpdate();//Execute the update

                Statement statement = MySQL.getConnection().createStatement();//Create a statement
                ResultSet result = statement
                        .executeQuery("SELECT `staffID` FROM " + staff + " WHERE UUID = '" + uuid.toString() + "'");//Get the players staffID
                result.next();
                int staffID = result.getInt("staffID");//Set the staffID to the int staffId

                LogToFile.log("info", "Added " + uuid.toString() + " to the table! There staff number is #" + staffID);//Logs the staff addition
                PMessage.Message(p, "You have been added to the staff list. Please memorize your staff number, it is #"
                        + Integer.toString(staffID), "Normal");//Messages them what there staff number is(Staff number still has no use)
            } else {

                Statement statement = MySQL.getConnection().createStatement();//Create a statement
                ResultSet result = statement
                        .executeQuery("SELECT `staffID` FROM " + staff + " WHERE UUID = '" + uuid.toString() + "'");//Get the players staffID
                result.next();
                int staffID = result.getInt("staffID");
                PMessage.Message(p, "Just as a reminder your staff number is #" + staffID, "Normal");//message them their staff id
            }
        }


    }


}
