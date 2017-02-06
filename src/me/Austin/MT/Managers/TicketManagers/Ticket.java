package me.Austin.MT.Managers.TicketManagers;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by mcaus_000 on 2/4/2017.
 */
public class Ticket implements Serializable {

    static public int ticketID;
    static public String tUUID;
    public static String priority;
    static public Date date;
    static public String msg;
    static public String completed;
    static public String assigned;


}
