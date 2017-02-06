package me.Austin.MT.Managers.TicketManagers;

import me.Austin.MT.Managers.MySQL;
import me.Austin.MT.Managers.Objects.Server;
import me.Austin.MT.Managers.PMessage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by mcaus_000 on 2/4/2017.
 */
public class EscalateTicket {
    //Tables
    private static String tickets = "`" + Server.getSUUID() + "-tickets`";

    public static void escalateTicket(Ticket ticket) {
        try {
            Connection conn = MySQL.getConnection();

            String sql;

            if (Ticket.priority.equalsIgnoreCase("Normal")) {

                sql = "UPDATE " + tickets + " SET Priority='High' WHERE ticketID='" + Ticket.ticketID + "'";
            } else if (Ticket.priority.equalsIgnoreCase("High")) {

                sql = "UPDATE " + tickets + " SET Priority='Extreme' WHERE ticketID='" + Ticket.ticketID + "'";
                PMessage.sendAdminMessage("Note ticket #" + Ticket.ticketID + " has a priority of EXTREME");
            } else {
                sql = "";
                PMessage.sendAdminMessage("Note ticket #" + Ticket.ticketID + " has a priority of EXTREME");
            }

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
