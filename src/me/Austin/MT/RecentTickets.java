package me.Austin.MT;

import me.Austin.MT.Managers.MySQL;
import me.Austin.MT.Managers.Objects.Server;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 * This class is to gather the most recent tickets
 *
 * @author MrMcaustin1
 * @since 1.0
 */
public class RecentTickets {


    public static HashMap<Integer, String> rTickets = new HashMap<>();
    //Tables
    private static String tickets = "`" + Server.getSUUID() + "-tickets`";

    /**
     * RecentTickers Class - Returns the recent tickets
     *
     * @param p
     *         Player that runs the command
     *
     * @return The 5 most recent tickets
     *
     * @throws SQLException
     *         Just in case MySQL wants to be a dick
     */
    public static HashMap<Integer, String> recentTickets(Player p) throws SQLException {
        rTickets.clear();
        int t = 0;
        Statement statement = MySQL.getConnection().createStatement();
        ResultSet r2 = statement
                .executeQuery("SELECT COUNT(*) FROM " + tickets + " ORDER BY Date DESC;");
        while (r2.next()) {
            t = r2.getInt(1);
        }
        if (t > 5) {
            t = 5;
        }

        ResultSet result = statement.executeQuery(
                "SELECT * FROM " + tickets + " ORDER BY Date DESC LIMIT " + t + ";");

        for (int i = 0; i < t; i++) {
            result.next();
            rTickets.put(result.getInt("TicketID"), result.getString("UUID"));

        }
        return rTickets;
    }

}
