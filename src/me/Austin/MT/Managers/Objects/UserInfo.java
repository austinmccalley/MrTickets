package me.Austin.MT.Managers.Objects;

import me.Austin.MT.Managers.MySQL;

import java.net.InetAddress;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;

/**
 * Created by mcaus_000 on 2/5/2017.
 */
public class UserInfo {

    //Tables
    private static String users = "`users`";

    public static User getUser(String uuidS) {
        User user = new User();

        try {
            Statement statement = MySQL.getConnection().createStatement();

            String sql = "SELECT * FROM " + users + " WHERE UUID='" + uuidS + "';";
            ResultSet rs = statement.executeQuery(sql);
            rs.next();

            float mIP = rs.getFloat("IP");
            String ip = "SELECT INET_NTOA(" + mIP + ");";


            user.userID = rs.getInt("userID");
            user.name = rs.getString("UserName");
            user.uuid = UUID.fromString(rs.getString("UUID"));

            ResultSet r1 = statement.executeQuery(ip);
            r1.next();
            user.ip = InetAddress.getByName(r1.getString(1));


        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }


}
