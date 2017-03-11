package me.Austin.MT.Managers.LoginHandlers;

import me.Austin.MT.Managers.MySQL;
import me.Austin.MT.Managers.Objects.User;
import me.Austin.MT.Managers.Objects.UserInfo;
import me.Austin.MT.Managers.PMessage;
import org.bukkit.entity.Player;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by mcaus_000 on 2/5/2017.
 */
public class SignupHandler {
        /*
    Notes;

    Force staff to create a password on the website before giving them permission.
    /ticket signup <pass>
    /tickets login <pass>


     */

    public static void signUp(Player p, String s) {
        String hashed = generateHash(s);
        User user = UserInfo.getUser(p.getUniqueId().toString());

        try {
            if (!checkSignUp(p)) {
                //UPDATE `Servers`.`users` SET `Password`='test' WHERE `userID`='5';

                String sql = "UPDATE `Servers`.`users` SET `Password`='" + hashed + "' WHERE `userID`='" + user.userID + "';\n";
                PreparedStatement ps = MySQL.getConnection().prepareStatement(sql);
                ps.executeUpdate();

                PMessage.Message(p, "Success! You have officially signed up! Now please login with the command /ticket login <Password>", "Normal");
                System.out.println("[MrT] User " + p.getName() + " has officially signed up!");
            } else {
                PMessage.Message(p, "You already have signed up!", "High");
            }
        } catch (
                SQLException e)

        {
            e.printStackTrace();
        }

    }


    public static boolean checkSignUp(Player p) {
        try {
            int id = UserInfo.getUser(p.getUniqueId().toString()).userID;

            String sql = "SELECT * FROM `users` WHERE Password IS NULL OR Password = '';";

            Statement statement = MySQL.getConnection().createStatement();

            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                System.out.println(rs.getInt("userID") != id);
                return rs.getInt("userID") != id;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    protected static String generateHash(String toHash) {
        MessageDigest md = null;
        byte[] hash = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
            hash = md.digest(toHash.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return convertToHex(hash);
    }

    /**
     * Converts the given byte[] to a hex string.
     *
     * @param raw
     *         the byte[] to convert
     *
     * @return the string the given byte[] represents
     */
    private static String convertToHex(byte[] raw) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < raw.length; i++) {
            sb.append(Integer.toString((raw[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

}
