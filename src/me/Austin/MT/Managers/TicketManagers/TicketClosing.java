package me.Austin.MT.Managers.TicketManagers;

import me.Austin.MT.Managers.MySQL;
import me.Austin.MT.Managers.Objects.Server;
import me.Austin.MT.Managers.Objects.Staff;
import me.Austin.MT.Managers.Objects.StaffInfo;
import me.Austin.MT.Managers.PMessage;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by mcaus_000 on 2/4/2017.
 */
public class TicketClosing {
    //Tables
    private static String tickets = "`" + Server.getSUUID() + "-tickets`";
    private static String staffT = "`" + Server.getSUUID() + "-staff`";

    public static void closeTicket(Ticket ticket, Player p) {
        try {
            Staff staff = StaffInfo.getStaff(p.getUniqueId().toString());

            Connection conn = MySQL.getConnection();

            String toggle = "SELECT * FROM " + tickets + " WHERE ticketID='" + Ticket.ticketID + "'";
            ResultSet rs = conn.createStatement().executeQuery(toggle);

            rs.next();

            if (rs.getString("Completed").equalsIgnoreCase("Open")) {

                String sql = "UPDATE " + tickets + " SET Completed='Closed' WHERE ticketID='" + Ticket.ticketID + "'";
                String tClosedSQL = "UPDATE " + staffT + " SET tClosed= tClosed +1 WHERE staffID='" + staff.staffID + "'";

                PreparedStatement ps = conn.prepareStatement(sql);
                PreparedStatement tClosedPS = conn.prepareStatement(tClosedSQL);

                ps.executeUpdate();
                tClosedPS.executeUpdate();

                System.out.println(tClosedSQL);

                PMessage.Message(p, "Closed ticket #" + Ticket.ticketID, "Normal");

            } else {

                String sql = "UPDATE " + tickets + " SET Completed='Open' WHERE ticketID='" + Ticket.ticketID + "'";
                String tClosedSQL = "UPDATE " + staffT + " SET tClosed= tClosed -1 WHERE staffID='" + staff.staffID + "'";

                PreparedStatement ps = conn.prepareStatement(sql);
                PreparedStatement tClosedPS = conn.prepareStatement(tClosedSQL);

                ps.executeUpdate();
                tClosedPS.executeUpdate();
                PMessage.Message(p, "Opened ticket #" + Ticket.ticketID, "Normal");

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
