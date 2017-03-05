package me.Austin.MT;

import me.Austin.MT.Managers.ErrorNumGen;
import me.Austin.MT.Managers.LogToFile;
import me.Austin.MT.Managers.MySQL;
import me.Austin.MT.Managers.Objects.Server;
import me.Austin.MT.Managers.Objects.User;
import me.Austin.MT.Managers.Objects.UserInfo;
import me.Austin.MT.Managers.PMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.Permission;

import java.net.InetAddress;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

/**
 * This class can handel all functions relating to when a user joins.
 *
 * @author MrMcaustin1
 * @since 0.0.1
 */
public class Join implements Listener {

    //Tables
    private static String staff = Server.getSUUID() + "-staff";
    private static String users = "`users`";

    private static void addUser(Player p) {
        try {
            String local = InetAddress.getLocalHost().getHostAddress();
            if (!MySQL.tableContainsPlayer(p, "users")) {
                int admin = 0;
                if (p.hasPermission(new Permission("MrTickets.Admin"))) {
                    admin = 1;
                }

                String sql = "REPLACE INTO " + users + "(`UserName`, `UUID`, `IP`, `Admin`) VALUES (\"" + p.getName() + "\",\"" + p.getUniqueId() + "\",inet_aton(\"" + local + "\"), " + admin + ");";
                PreparedStatement ps = MySQL.getConnection().prepareStatement(sql);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
            addStaffTable(p.getUniqueId());//Call the addPlayer function
            addUser(p);
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
    private void addStaffTable(UUID uuid) throws SQLException {
        Player p = Bukkit.getPlayer(uuid);

        if (p.hasPermission(new Permission("MrTickets.Admin"))) {//Checks the player if they have the permission for ticket admin
            if (!MySQL.tableContainsPlayer(p, staff)) {//Checks if the staff table doesn't contains the player

                PreparedStatement ps = MySQL.getConnection()
                        .prepareStatement("INSERT INTO `" + staff + "` (`UUID`, `tAssigned`, `tClosed`) VALUES ('" + uuid.toString() + "', 0, 0)");//Prepared statement
                ps.executeUpdate();//Execute the update

                //    int staffID = getStaffID(uuid.toString());

                LogToFile.log("info", "Added " + uuid.toString() + " to the table! ");//Logs the staff addition

                addStaff(p);
            } else {

                addStaff(p);
            }
        }


    }

    private int getStaffID(String uuid) {
        try {
            Statement statement = MySQL.getConnection().createStatement();//Create a statement
            ResultSet result = statement
                    .executeQuery("SELECT `staffID` FROM " + staff + " WHERE UUID = '" + uuid.toString() + "'");//Get the players staffID
            result.next();
            int staffID = result.getInt("staffID");//Set the staffID to the int staffId
            return staffID;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void addStaff(Player p) {
        try {

            User user = UserInfo.getUser(p.getUniqueId().toString());
            int uid = user.userID;

            Server server = Server.getServer();
            int sid = Server.id;

            String sql = "INSERT IGNORE INTO `UserAdmin` (`User`,`Server`) VALUES (" + uid + ", " + sid + ")";


            PreparedStatement ps = MySQL.getConnection().prepareStatement(sql);
            ps.executeUpdate();
//TODO: Get correct server id

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
