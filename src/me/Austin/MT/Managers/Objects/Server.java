package me.Austin.MT.Managers.Objects;

import me.Austin.MT.Managers.LogToFile;
import me.Austin.MT.Managers.MySQL;
import me.Austin.MT.Managers.ServerUUID;

import java.io.*;
import java.net.InetAddress;
import java.sql.*;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mcaus_000 on 1/29/2017.
 */
public class Server implements Serializable {


    public static UUID sUUID;
    public static String randomWord;
    public static String ipAdd;
    public static String plan;

    public static void setInfo() {

        writePlan();

        createServerInfo();


        randomWord = ServerUUID.generateRandomWord(5);
        sUUID = ServerUUID.generateServerUUID(randomWord);
        plan = getPlan();

        if (!containsUUID()) {
            createServerInfo();
            writeSUUID(String.valueOf(sUUID));
            writeSUUID(randomWord);
            MySQL.createServerTable(String.valueOf(getSUUID()));
            setServerInfoTable(sUUID, randomWord, plan);

        }
        updatePlan(plan, getSUUID());

    }

    private static void updatePlan(String plan, String uuid) {
        try {
            String sql = "UPDATE `Servers` SET `Plan` = \"" + plan + "\" WHERE `UUID` = \"" + uuid + "\";";
            PreparedStatement ps = MySQL.getConnection().prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static void setServerInfoTable(UUID uuid, String rndWrd, String plan) {
        try {
            String ipAdd = InetAddress.getLocalHost().getHostAddress();

            String sql = "REPLACE INTO `Servers`(`UUID`, `IP`, `RndWrd`, `Plan`) VALUES (\"" + uuid.toString() + "\", inet_aton(\"" + ipAdd + "\"), \"" + rndWrd + "\", \"" + plan + "\");";
            PreparedStatement ps = MySQL.getConnection().prepareStatement(sql);
            ps.executeUpdate();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the file ServerInfo.txt
     */
    private static void createServerInfo() {
        try {
            File file = new File("ServerInfo.txt");
         /*If file gets created then the createNewFile()
          * method would return true or if the file is
	      * already present it would return false
	      */

            boolean fvar = file.createNewFile();
            if (fvar) {
                LogToFile.log("Config", "ServerInfo.txt was created");
            }
        } catch (IOException e) {
            System.out.println("Exception Occurred:");
            e.printStackTrace();
        }
    }

    /**
     * Write the number to ServerInfo.txt
     *
     * @param num
     *         The number you want to write(Could be a string)
     */
    public static void writeSUUID(String num) {
        createServerInfo();//Create the file
        PrintWriter outS = null;//Set the printwriter ot null
        try {
            outS = new PrintWriter(new FileOutputStream(("ServerInfo.txt"), true));//Try to get the opportunity to write
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();//File cant be found, this should never occur
        }
        outS.print(num + "\n");//Write the number and then go to the next line
        outS.close();
    }

    /**
     * Write the plan to ServerInfo.txt
     */
    public static void writePlan() {
        if (!containsPlan()) {
            createServerInfo();//Create the file
            PrintWriter outS = null;//Set the printwriter ot null
            try {
                outS = new PrintWriter(new FileOutputStream(("ServerInfo.txt"), true));//Try to get the opportunity to write
            } catch (FileNotFoundException fnfe) {
                fnfe.printStackTrace();//File cant be found, this should never occur
            }
            outS.print("Plan: Free" + "\n");//Write the number and then go to the next line
            outS.close();
        }
    }

    public static boolean containsUUID() {

        try {
            InputStream is = new FileInputStream("ServerInfo.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line = br.readLine();
            StringBuilder sb = new StringBuilder();

            while (line != null) {
                sb.append(line).append("\n");
                line = br.readLine();
            }

            String fileAsString = sb.toString();

            Matcher m = Pattern.compile("[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}").matcher(fileAsString);

            return m.find();

        } catch (Exception e) {
            e.printStackTrace();
        }


        return false;
    }

    public static boolean containsPlan() {
        try {
            InputStream is = new FileInputStream("ServerInfo.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line = br.readLine();
            StringBuilder sb = new StringBuilder();

            while (line != null) {
                sb.append(line).append("\n");
                line = br.readLine();
            }

            String fileAsString = sb.toString();

            Matcher m = Pattern.compile(".*Plan: (.*)").matcher(fileAsString);

            return m.find();

        } catch (Exception e) {
            e.printStackTrace();
        }


        return false;
    }

    public static String getSUUID() {
        createServerInfo();

        try {
            InputStream is = new FileInputStream("ServerInfo.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line = br.readLine();
            StringBuilder sb = new StringBuilder();

            while (line != null) {
                sb.append(line).append("\n");
                line = br.readLine();
            }

            String fileAsString = sb.toString();

            Matcher m = Pattern.compile("[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}").matcher(fileAsString);

            while (m.find()) {
                return m.group();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getRndWrd() {

        try {
            InputStream is = new FileInputStream("ServerInfo.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line = br.readLine();
            StringBuilder sb = new StringBuilder();

            while (line != null) {
                sb.append(line).append("\n");
                line = br.readLine();
            }

            String fileAsString = sb.toString();

            Matcher m = Pattern.compile("[a-z]{5}").matcher(fileAsString);


            return m.group(1);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void createTable() {
        MySQL.createServerTable(String.valueOf(getSUUID()));
    }

    public static boolean tablesExists() {
        try {


            Connection conn = MySQL.getConnection();
            DatabaseMetaData md = conn.getMetaData();
            ResultSet rs;

            rs = md.getTables(null, null, getSUUID(), null);
            System.out.println(rs.next());

            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            //  PMessage.stackTrace();
        }
        return true;
    }

    public static String getPlan() {
        try {
            InputStream is = new FileInputStream("ServerInfo.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line = br.readLine();
            StringBuilder sb = new StringBuilder();

            while (line != null) {
                sb.append(line).append("\n");
                line = br.readLine();
            }

            String fileAsString = sb.toString();

            Matcher m = Pattern.compile(".*Plan: (.*)").matcher(fileAsString);
            m.find();
            return m.group(1);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
