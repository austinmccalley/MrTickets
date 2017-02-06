package me.Austin.MT.Managers.Objects;

import me.Austin.MT.Managers.MySQL;
import me.Austin.MT.Managers.TicketManagers.Ticket;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by MrMcaustin1 on 12/30/2016 at 5:58 PM.
 * <p>
 * Used to get all the ticket info and store it as a string. <i>This feature is still a WIP</i>
 *
 * @author MrMcaustin1
 * @since 1.0
 */

public class TicketInfo {

    //Tables
    private static String tickets = "`" + Server.getSUUID() + "-tickets`";


    public static Ticket getTicket(int id) {
        Ticket ticket = new Ticket();

        try {
            Statement statement = MySQL.getConnection().createStatement();

            String sql = "SELECT * FROM " + tickets + " WHERE TicketID=" + id + ";";

            ResultSet rs = statement.executeQuery(sql);
            rs.next();

            Ticket.ticketID = id;
            Ticket.tUUID = rs.getString("UUID");
            Ticket.priority = rs.getString("Priority");
            Ticket.date = rs.getDate("Date");
            Ticket.msg = rs.getString("Message");
            Ticket.completed = rs.getString("Completed");
            Ticket.assigned = rs.getString("Assigned");

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return ticket;
    }


}
