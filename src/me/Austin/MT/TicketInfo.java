package me.Austin.MT;

import org.bukkit.Bukkit;

import java.io.Serializable;
import java.sql.Date;
import java.util.UUID;

/**
 * Created by MrMcaustin1 on 12/30/2016 at 5:58 PM.
 *
 * Used to get all the ticket info and store it as a string. <i>This feature is still a WIP</i>
 * @since 1.0
 * @author MrMcaustin1
 */

public class TicketInfo implements Serializable {

    static public int ticketID;
    static public String tUUID;
    static public String priority;
    static protected Date date;

    /**
     * Sets all the ticket info into a string
     *
     * @return The string containing all the info all pretty like
     * @since 1.0
     */
    public static String ticketStringInfo() {
        String tm = "Ticked ID #" + Integer.toString(ticketID) + " with a priority of " + priority + " by " + Bukkit.getPlayer(UUID.fromString(tUUID)).getName() + " on " + date;
        return tm;
    }

}
