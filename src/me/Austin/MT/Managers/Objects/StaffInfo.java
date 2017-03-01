package me.Austin.MT.Managers.Objects;

import me.Austin.MT.Managers.MySQL;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by mcaus_000 on 2/27/2017.
 */
public class StaffInfo {

    //tables

    private static String staffT = "`" + Server.getSUUID().toString() + "-staff`";

    public static Staff getStaff(String sUUID) {
        Staff staff = new Staff();

        try {

            Statement statement = MySQL.getConnection().createStatement();

            String sql = "SELECT * FROM " + staffT + " WHERE UUID='" + sUUID + "';";
            ResultSet rs = statement.executeQuery(sql);
            rs.next();

            staff.staffID = rs.getInt("staffID");
            staff.UUID = rs.getString("UUID");
            staff.tAssigned = rs.getInt("tAssigned");
            staff.tClosed = rs.getInt("tClosed");

        } catch (Exception e) {
            e.printStackTrace();
        }


        return staff;
    }

}
