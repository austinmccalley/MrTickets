package me.Austin.MT;

import me.Austin.MT.Managers.MySQL;
import me.Austin.MT.Managers.Objects.Server;
import me.Austin.MT.Managers.PMessage;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Managing all functions that have to do with active tickets
 *
 * @author MrMcaustin1
 * @since 1.0
 */
public class ActiveTickets {

    //The my active tickets class
    public static HashMap<Integer, Long> mAT = new HashMap<>();
    //Tables
    private static String tickets = "`" + Server.getSUUID() + "-tickets`";

    /**
     * Returns all active tickets from a user
     *
     * @param p
     *         For UUID reference
     *
     * @return A hashmap containing the TicketID and the Millisecond it was submitted on
     */
    @SuppressWarnings("rawtypes")
    public static HashMap myActiveTickets(Player p) {

        mAT.clear();//Make sure the HashMap is clear

        try {

            int t = 0;//Total number of tickets

            Statement statement = MySQL.getConnection().createStatement();//Create a MySQL statement
            ResultSet r2 = statement
                    .executeQuery("SELECT COUNT(*) FROM " + tickets + " ORDER BY Date DESC;");//Query that is executed

            while (r2.next()) {//If you can get a result
                t = r2.getInt(1);//Get the total numer

            }

            //If t is bigger than 5 then set it to as a limit
            if (t > 5) {
                t = 5;
            }

            ResultSet result = statement.executeQuery(
                    "SELECT * FROM " + tickets + " WHERE UUID='" + p.getUniqueId().toString() + "' AND Completed='Open' ORDER BY Date DESC LIMIT " + t + ";");//The query that is executed

            for (int i = 0; i < t; i++) {//As long as i is less than t continue through this until i is equal to t
                result.next();//Next result
                mAT.put(result.getInt("TicketID"), result.getLong("milliseconds"));//Get the two results and put them into the hashmap

            }

            return mAT;

        } catch (SQLException e) {
            PMessage.stackTrace();
            e.printStackTrace();
        }
        return mAT;
    }

    /**
     * Convert a millisecond duration to a string format
     *
     * @param millis
     *         A duration to convert to a string form
     *
     * @return A string of the form "X Days Y Hours Z Minutes A Seconds".
     *
     * @throws IllegalArgumentException
     *         In case milliseconds are less than 0
     */
    public static String getDurationBreakdown(long millis) {

        if (millis < 0) {//If for some reason the long is less than 0
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }

        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        StringBuilder sb = new StringBuilder(64);
        sb.append(days);
        sb.append(" Days ");
        sb.append(hours);
        sb.append(" Hours ");
        sb.append(minutes);
        sb.append(" Minutes ");
        sb.append(seconds);
        sb.append(" Seconds");

        return sb.toString();
    }

    /**
     * Get number of tickets submitted by a player
     *
     * @param p
     *         The player in which the query is done for
     *
     * @return Number of tickets submitted by x player
     *
     * @deprecated Not used and probably be removed in Alpha versions
     */
    public static Integer submittedTickets(Player p) {
        int subT = 0;//Set the total ticket# to 0

        try {
            Statement statement = MySQL.getConnection().createStatement();//Create a statement
            ResultSet r2 = statement.executeQuery("SELECT COUNT(*) FROM " + tickets + " WHERE UUID= '" + p.getUniqueId() + "';");//resultset

            while (r2.next()) {//If there is a result
                subT = r2.getInt(1);//Get the value and set subT to it
            }

            r2.close();//Close the result set

            return subT;//return the int
        } catch (SQLException e) {
            PMessage.stackTrace();
            e.printStackTrace();
        }
        return null;
    }
}
