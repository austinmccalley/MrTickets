package me.Austin.MT.Managers.LoginHandlers;

import me.Austin.MT.Managers.LogToFile;
import me.Austin.MT.Managers.PMessage;
import org.bukkit.entity.Player;
import org.mindrot.jbcrypt.BCrypt;

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
        /*
        DOES NOT WORK! ALWAYS LOGS THEM IN!!!
         */
        String hashed = BCrypt.hashpw(s, BCrypt.gensalt());
        if (!SignupHandler.checkSignUp(p)) {
            if (BCrypt.checkpw(s, hashed)) {
                //Correct login
                loggedIn.add(p);
                PMessage.Message(p, "Success! You have logged in!", "Normal");
                LogToFile.log("Config", p.getName() + " has logged in!");
            } else {
                //Incorrect login
                PMessage.Message(p, "Incorrect password!", "High");
            }
        }

    }

    public static boolean isLoggedIn(Player p) {
        return loggedIn.contains(p);
    }

}
