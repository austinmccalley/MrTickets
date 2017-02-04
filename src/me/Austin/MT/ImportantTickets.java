package me.Austin.MT;

import me.Austin.MT.Managers.ErrorNumGen;
import me.Austin.MT.Managers.MySQL;
import me.Austin.MT.Managers.Objects.Server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrMcaustin1 on 12/30/2016 at 5:53 PM.
 * Collection of important tickets
 * This feature will probably be changing soon
 *
 * @author MrMcaustin1
 * @since 1.0
 * @deprecated
 */
   /*
   Notes
    Priorities:
        Normal(Default), High(Escalated by staff), Extreme(Notification on join)

    Normal -> 1
    High -> 2
    Extreme -> 3

MultiMap:
    Key: TickedID
    Values: UUID, Priority, Date
     */

public class ImportantTickets {

    /**
     * Get the highest oldest ticket that is not closed
     *
     * @return The ticket as a string
     */
    public static String getHighTicket() {
        try {

            Statement statement = MySQL.getConnection().createStatement();//Create a statement
            ResultSet rs = statement.executeQuery("SELECT * FROM " + Server.sUUID + " WHERE Priority='High' AND Completed='Open' ORDER BY Date ASC");//Select all tickets where the priority is high and isn't closed
            if (rs != null) {//If this is possible
                rs.next();

                TicketInfo ti = new TicketInfo();//Create a ticket info
                //Add all the info
                TicketInfo.tUUID = rs.getString("UUID");
                TicketInfo.ticketID = rs.getInt("TicketID");
                TicketInfo.priority = rs.getString("Priority");
                TicketInfo.date = rs.getDate("Date");


                String tm1 = TicketInfo.ticketStringInfo();//Return all the ticket info as a string

                return tm1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Getting a ticket with high priority failed! Error #" + Integer.toString(ErrorNumGen.newNum());//This will output to the player as a success
    }

    /**
     * Get the oldest ticket that isn't closed with priority as extreme
     *
     * @return A string with all the ticket info
     */
    public static String getExtremeTicket() {
        try {
            Statement statement = MySQL.getConnection().createStatement();//Create a statement
            ResultSet rs = statement.executeQuery("SELECT * FROM " + Server.sUUID +" WHERE Priority='Extreme' AND Completed='Open' ORDER BY Date ASC");//Get the ticket

            if (rs != null) {//If the result set is not null
                rs.next();

                //Create the ticket and add the info
                TicketInfo ti = new TicketInfo();
                TicketInfo.tUUID = rs.getString("UUID");
                TicketInfo.ticketID = rs.getInt("TicketID");
                TicketInfo.priority = rs.getString("Priority");
                TicketInfo.date = rs.getDate("Date");

                String tm1 = TicketInfo.ticketStringInfo();//Set the ticket info as a string

                return tm1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Getting a ticket with extreme priority failed! Maybe a good thing? Error #" + Integer.toString(ErrorNumGen.newNum());//This will output to the player as a success
    }

    /**
     * Get the top 2 important tickets
     *
     * @return The top 2 tickets as a list
     */
    public static List<String> getImportantTickets() {
        List<String> iTickets = new ArrayList<String>(); //Create the important tickets list

        iTickets.add(getHighTicket());//Add the highest ticket info
        iTickets.add(getExtremeTicket());//Add the extreme ticket info

        return iTickets;//Return the list
    }
}
