package me.Austin.MT.Managers.LoginHandlers;

import me.Austin.MT.Managers.LogToFile;
import me.Austin.MT.Managers.MySQL;
import me.Austin.MT.Managers.Objects.UserInfo;
import me.Austin.MT.Managers.PMessage;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by mcaus_000 on 2/5/2017.
 */
public class LoginHandler {
    /*
    Notes;

    Check if the server has been registered by console.
    Force staff to create a password on the website before giving them permission.
    /ticket signup <pass>
    /tickets login <pass>


     */

    public static ArrayList<Player> loggedIn = new ArrayList<>();

    public static void login(Player p, String s) {

        try {
            String sql = "SELECT * FROM `users` WHERE userID='" + UserInfo.getUser(p.getUniqueId().toString()).userID + "';";

            Statement statement = MySQL.getConnection().createStatement();

            ResultSet rs = statement.executeQuery(sql);

            String hashed = "";

            while (rs.next()) {
                hashed = rs.getString("Password");
            }

            if (SignupHandler.checkSignUp(p)) {
                if (hashed.equalsIgnoreCase(SignupHandler.generateHash(s))) {
                    //Correct login
                    loggedIn.add(p);
                    PMessage.Message(p, "Success! You have logged in!", "Normal");
                    LogToFile.log("Config", p.getName() + " has logged in!");
                } else {
                    //Incorrect login
                    PMessage.Message(p, "Incorrect password!", "High");
                }
            } else {
                PMessage.Message(p, "Please sign up before trying to login!", "High");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static boolean isLoggedIn(Player p) {
        return loggedIn.contains(p);
    }


}
