package me.Austin.MT.Managers.TicketManagers;

import me.Austin.MT.Managers.MySQL;
import me.Austin.MT.Managers.Objects.Server;
import me.Austin.MT.Managers.PMessage;
import org.bukkit.entity.Player;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MrMcaustin1 on 12/30/2016 at 10:31 PM.
 * Deals with sorting and collecting of closed tickets
 *
 * @author MrMcaustin1
 * @since 0.0.1
 */
public class ClosedTicketHandler {

    //Tables
    private static String tickets = "`" + Server.getSUUID() + "-tickets`";

    /**
     * Get a list of closed tickets of a normal player
     *
     * @param p
     *         Player
     *
     * @return List of all closed tickets
     */
    //TODO: Update to include who closed it
    public static List<Integer> defClosedTickets(Player p) {

        List<Integer> cTickets = new ArrayList<>();//Make the list
        cTickets.clear();//Clear it to make sure its totally clear

        try {

            int t = 0;//Initialize the count int
            Statement statement = MySQL.getConnection().createStatement();//Create the statement

            ResultSet r2 = statement
                    .executeQuery("SELECT COUNT(*) FROM " + tickets + " WHERE Completed='Closed' AND UUID='" + p.getUniqueId() + "';");//Execute the query

            while (r2.next()) {
                t = r2.getInt(1);//get the count
            }

            if (t > 5) {
                t = 5;//If t is greater than 5 set it back to 5
            }

            ResultSet result = statement.executeQuery(
                    "SELECT *  FROM " + tickets + " WHERE Completed='Closed' AND UUID='" + p.getUniqueId() + "' LIMIT " + t + ";");//Get all the closed tickets who were submitted by a player

            for (int i = 0; i < t; i++) {
                result.next();
                cTickets.add(result.getInt("TicketID"));//Add the ticket number to the list
            }

            return cTickets;

        } catch (SQLException e) {
            PMessage.stackTrace();
            e.printStackTrace();
        }

        cTickets.add(0);//Return a ticket ID of 0
        return cTickets;
    }

    /**
     * Get all the tickets who are closed by the staff member
     *
     * @param p
     *         The staff member is question
     *
     * @return A HashMap of the ticket id and the date
     */
    public static HashMap<Integer, Date> adminClosedTickets(Player p) {
        HashMap<Integer, Date> acTickets = new HashMap<>();//Create the map

        int t = 0;//Set the t count to 0

        try {

            Statement statement = MySQL.getConnection().createStatement();//Create the statement
            ResultSet r2 = statement
                    .executeQuery("SELECT COUNT(*) FROM " + tickets + " WHERE Completed='Closed' AND Assigned='" + p.getUniqueId().toString() + "';");//Execute the query

            while (r2.next()) {
                t = r2.getInt(1);//Get the total count
            }
            if (t > 5) {
                t = 5;//If t is greater than 5 set it to 5
            }

            ResultSet result = statement.executeQuery(
                    "SELECT * FROM " + tickets + " WHERE Completed='Closed' AND Assigned='" + p.getUniqueId().toString() + "' LIMIT " + t + ";");//Get 5 tickets that are closed and assigned to user x

            for (int i = 0; i < t; i++) {
                result.next();
                acTickets.put(result.getInt("TicketID"), result.getDate("Date"));//Add the ticket id and date to the map

            }

            return acTickets;

        } catch (SQLException e) {
            PMessage.stackTrace();
            e.printStackTrace();
        }

        acTickets.put(0,
                new java.sql.Date(new java.util.Date().getTime()));//If there is an error set the ticket id to 0 and get the current date
        return acTickets;
    }
}
