package me.Austin.MT;

import me.Austin.MT.Managers.LogToFile;
import me.Austin.MT.Managers.MySQL;
import me.Austin.MT.Managers.PMessage;
import me.Austin.MT.Managers.PwdGen;
import org.bukkit.entity.Player;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by MrMcaustin1 on 12/31/2016 at 1:20 PM.
 * <p>
 * Check if players account is in the table
 * Generate password, hash it and send de-hashed password to them. Store un-hashed pwd with UUID in flat file.
 *
 * @author MrMcaustin1
 * @since 1.0
 * @deprecated Needs to be redone
 */
/*
TODO: Redo all of the sign up let the user pick password. BCrypt php doesn't use a salt or generates another one hashed passwords will be different.
 */
public class SignupHandler {

    /**
     * Create the login of a player
     *
     * @param p
     *         A player
     *
     * @since 1.0
     */
    public static void createLogin(Player p) {


        if (hasLogin(p)) {
            PMessage.Message(p, "You already have a login! If you do not remember it please contact staff!", "High");

        } else {
            String pass = createPassword();
            String hashedPass = hashPwd(pass);
            PMessage.Message(p, "Your username is " + p.getName() + " and your password is " + pass, "Normal");
            PMessage.Message(p, "Please write your password down somewhere safe so you remember it! This password cannot be retrieved!", "Normal");
            storePassword(pass, hashedPass, p.getUniqueId().toString());
        }
    }

    /**
     * Checks to see if the player has login
     *
     * @param p
     *         A layer
     *
     * @return Either true or false
     *
     * @since 1.0
     */
    private static boolean hasLogin(Player p) {
        String sNull = "null";
        try {
            Statement statement = MySQL.getConnection().createStatement();

            ResultSet rs = statement.executeQuery("SELECT * FROM users WHERE UUID='" + p.getUniqueId().toString() + "';");
            rs.next();
            if (rs.getString("DWP").equals(sNull)) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            PMessage.stackTrace();
        }
        return false;
    }

    /**
     * Hash the password using BCrypt
     *
     * @param pass
     *         The generated password
     *
     * @return The hashed password as a string
     *
     * @since 1.0
     */
    private static String hashPwd(String pass) {
        String hashedPass = "";

        return hashedPass;
    }

    /**
     * Create a password
     *
     * @return The password
     *
     * @since 1.0
     */
    private static String createPassword() {
        String pass;

        pass = new String(PwdGen.generatePswd(6, 6, 3, 2, 1));

        return pass;
    }

    /**
     * Executes the storing of the password
     *
     * @param pass
     *         The de-hashed password
     * @param hashedPass
     *         The hashed password
     * @param uid
     *         The UUID of the player
     *
     * @since 1.0
     */
    private static void storePassword(String pass, String hashedPass, String uid) {
        writePassword(hashedPass, uid);
        try {
            //UPDATE `users` SET `DWP` = 'test' WHERE `users`.`userID` = 1;
            Statement statement = MySQL.getConnection().createStatement();
            statement.executeUpdate("UPDATE users SET DWP='" + hashedPass + "' WHERE UUID='" + uid + "';");
        } catch (SQLException e) {
            e.printStackTrace();
            PMessage.stackTrace();
        }
    }

    /**
     * Create a text file called Passwords.txt
     *
     * @since 1.0
     */
    private static void createPassTxt() {
        try {
            File file = new File("Passwords.txt");

            boolean fvar = file.createNewFile();
            if (fvar) {
                LogToFile.log("Config", "Passwords.txt was created!");
            }
        } catch (IOException e) {
            PMessage.stackTrace();
            e.printStackTrace();
        }
    }

    /**
     * Write the password to a text file
     *
     * @param pass
     *         The de-hashed password
     * @param uuid
     *         The UUID of the player
     *
     * @since 1.0
     */
    private static void writePassword(String pass, String uuid) {
        createPassTxt();
        PrintWriter outS = null;
        try {
            outS = new PrintWriter(new FileOutputStream(("Passwords.txt"), true));
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            PMessage.stackTrace();
        }

        outS.print(uuid + ":" + pass + "\n");
        outS.close();
    }

}
