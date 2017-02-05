package me.Austin.MT.TicketManagers;

import me.Austin.MT.Managers.MySQL;
import me.Austin.MT.Managers.Objects.Server;
import me.Austin.MT.Managers.PMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.UUID;

/**
 * Managing all functions that have to do with assigning tickets ** Being reworked **
 *
 * @author MrMcaustin1
 * @since 1.0
 * <p>
 * <B>
 * TODO: REDO THIS CLASS MAJOR!!!!!
 * </B>
 * </p>
 */
public class AssignTickets {

    //HashMap of tickets assigned to a player
    public static HashMap<Integer, String> aTickets = new HashMap<>();


    //Tables
    private static String tickets = Server.getSUUID() + "-tickets";
    private static String staff = Server.getSUUID() + "-staff";

    /**
     * Sort through all the staff members and find the one with the least amount currently active and assign them this ticket
     *
     * @param p
     *         The player owning the ticket
     *
     * @return The UUID of the person handling the ticket
     */

    public static String assignTicket(Player p, boolean reassign) {
        String UUIDs;//Initialize the string
        try {
            Statement statement = MySQL.getConnection().createStatement();//Create the statement

            ResultSet result = statement.executeQuery("SELECT * FROM `" + staff + "` ORDER BY `tAssigned` ASC;");//The resultset
            result.next();//Go to the first result

            UUIDs = result.getString("UUID");//Set UUIDs to the UUID
            UUID UUIDu = UUID.fromString(UUIDs);//Get the java UUID from the string


            ResultSet result2 = statement.executeQuery("SELECT COUNT(*) FROM `" + tickets + "` WHERE Assigned='" + UUIDs + "' AND Completed='Open'");//Count all the tickets in the table tickets that are assigned to the handler
            result2.next();//Get the first result
            int tAssigned = result2.getInt(1);//Set the int to the number of tickets

            PreparedStatement ps = MySQL.getConnection().prepareStatement(
                    "UPDATE `" + staff + "` SET tAssigned = '" + tAssigned + "' WHERE UUID = '" + UUIDs + "'");//Update the staff

            ps.executeUpdate();//Execute the update
            if (!reassign) {
                PMessage.Message(p, "Your ticket is being handled by " + Bukkit.getPlayer(UUIDu).getName(), "Normal");//Message the player who is handling their ticket
            } else {
                PMessage.Message(p, "The ticket is now being handeled by " + Bukkit.getPlayer(UUIDu).getName(), "Normal");
            }

            return UUIDs;//Return the UUID in a string
        } catch (SQLException e) {
            PMessage.stackTrace();
            e.printStackTrace();
        }
        return "069a79f4-44e9-4726-a5be-fca90e38aaf5";//In case MySQL fails it will return the UUID of Notch
    }

    /**
     * Get a HashMap of all the tickets a staff member is assigned
     *
     * @param p
     *         The staff member
     *
     * @return A HashMAp containing the ticket ID and the UUID
     */
    public static HashMap<Integer, String> assignedTickets(Player p) {
        try {
            aTickets.clear();//Clear the map to remove the chance of duplicates
            int t = 0;//Set the count to 0

            Statement statement = MySQL.getConnection().createStatement();//Create a statement
            ResultSet r2 = statement
                    .executeQuery("SELECT COUNT(*) FROM `" + tickets + "` WHERE UUID='" + p.getUniqueId().toString() + "'ORDER BY Date DESC;");//Write and execute the query

            while (r2.next()) {
                t = r2.getInt(1);//Get the count of tickets
            }

            if (t > 5) {
                t = 5;//If t is bigger than 5 set it back to five
            }

            ResultSet result = statement.executeQuery(
                    "SELECT * FROM `" + tickets + "` WHERE Assigned='" + p.getUniqueId().toString() + "' ORDER BY Date DESC LIMIT " + t + ";");//Query the DB for all the tickets that someone is assigned


            while (result.next()) {
                aTickets.put(result.getInt("TicketID"), result.getString("UUID"));//Iterate through all the results and put them into aTickets
            }

            return aTickets;

        } catch (SQLException e) {
            PMessage.stackTrace();
            e.printStackTrace();
        }
        aTickets.put(0, "069a79f4-44e9-4726-a5be-fca90e38aaf5");//In case there is an error give them ticket #0 and Notch's UUID
        return aTickets;
    }

    /**
     * Get the total number of tickets
     *
     * @return The total number of tickets
     */
    public static Integer totalTickets() {
        int totalT = 0;//Initialize the int

        try {
            Statement statement = MySQL.getConnection().createStatement();//Create the statement
            ResultSet r2 = statement.executeQuery("SELECT COUNT(*) FROM `" + tickets + "`;"); //Execute the querry

            while (r2.next()) {
                totalT = r2.getInt(1);//Get the total number
            }

            return totalT;

        } catch (SQLException e) {
            PMessage.stackTrace();
            e.printStackTrace();
        }
        return null;//In case of MySQL error return null(0)
    }
}
