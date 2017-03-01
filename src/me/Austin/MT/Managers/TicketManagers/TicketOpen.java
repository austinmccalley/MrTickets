package me.Austin.MT.Managers.TicketManagers;

import me.Austin.MT.Managers.LogToFile;
import me.Austin.MT.Managers.MySQL;
import me.Austin.MT.Managers.Objects.Server;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

/**
 * This class submits a ticket using the implemented MySQL.
 *
 * @author MrMcaustin1
 * @since 0.0.1
 */
public class TicketOpen {
    //Tables
    private static String tickets = Server.getSUUID() + "-tickets";

    /**
     * Open Ticket Method - Submits the open ticket method to MySQL and logs it
     *
     * @param p
     *         The player opening the ticket
     * @param i
     *         The error number the user was assigned
     * @param msg
     *         The message for the ticket
     *
     * @throws SQLException
     *         Just in case there is some shit MySQL wants to do
     * @since 1.0
     */

    public static void openTicket(Player p, Integer i, String msg) throws SQLException {
        UUID uuid = p.getUniqueId();
        int errorNum = i;
        UUID rnd = UUID.randomUUID();

        System.out.println(rnd);

        PreparedStatement ps = MySQL.getConnection().prepareStatement(
                "INSERT INTO `" + tickets + "`(`UUID`, `Error`, `Message`, `Completed`, `Assigned`, `Date`, `milliseconds`, `Priority`, `tUUID`) VALUES (?,?,?,?,?,?,?,?, ?)");
        ps.setString(1, uuid.toString());
        ps.setInt(2, errorNum);
        ps.setString(3, msg);
        ps.setString(4, "Open");
        ps.setString(5, AssignTickets.assignTicket(p, false));
        ps.setDate(6, new java.sql.Date(new java.util.Date().getTime()));
        ps.setLong(7, System.currentTimeMillis());
        ps.setString(8, "Normal");
        ps.setString(9, rnd.toString());
        ps.executeUpdate();
        LogToFile.log("info", "Added " + uuid.toString() + "'s ticket to the table!");
    }
}
