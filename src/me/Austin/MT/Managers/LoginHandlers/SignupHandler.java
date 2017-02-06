package me.Austin.MT.Managers.LoginHandlers;

import me.Austin.MT.Managers.MySQL;
import me.Austin.MT.Managers.Objects.User;
import me.Austin.MT.Managers.Objects.UserInfo;
import me.Austin.MT.Managers.PMessage;
import org.bukkit.entity.Player;
import org.mindrot.jbcrypt.BCrypt;

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
        String hashed = BCrypt.hashpw(s, BCrypt.gensalt());
        User user = UserInfo.getUser(p.getUniqueId().toString());

        try {
            if (checkSignUp(p)) {
                //UPDATE `Servers`.`users` SET `Password`='test' WHERE `userID`='5';

                String sql = "UPDATE `Servers`.`users` SET `Password`='" + hashed + "' WHERE `userID`='" + user.userID + "';\n";
                PreparedStatement ps = MySQL.getConnection().prepareStatement(sql);
                ps.executeUpdate();

                PMessage.Message(p, "Success! You have officially signed up! Now please login with the command /ticket login <Password", "Normal");
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
                return rs.getInt("userID") != id;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}