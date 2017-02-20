package me.Austin.MT.Managers;

import org.bukkit.Bukkit;

import java.io.*;
import java.util.Random;

/**
 * Created by MrMcaustin1 on 12/30/2016 at 8:34 PM.
 * Handles all error
 *
 * @author MrMcaustin1
 * @since 0.0.1
 */
public class ErrorNumGen {

    /**
     * Generates a new number from 0 to 999,999
     *
     * @return The new number
     */
    public static Integer newNum() {
        try {
            int num = 0;//Set the int to 0

            for (int i = 1; i > 0; i++) {//Loop through this indefinitely until a new number is generated that works

                createErrorNums();
                Random r = new Random();//Call random
                int rNumb = r.nextInt(999999) + 1;//Get a random number
                String lNum = Integer.toString(rNumb);//Get the string version

                if (!(searchBR("ErrorNums.txt", lNum))) {//Check to see if the number has already been used
                    num = rNumb;//Set num to the new number
                    writeNum(lNum);//Write it to the ErrorNums file
                    return num;//Return the number

                } else {
                    //TODO: Some better alert for this
                    Bukkit.broadcastMessage("New error number could not be created!");//Alerts entire server no more error numbers can be made(Ticket count has to be > 999,999)
                    return 0;//Return the number 0
                }
            }

            return num;//Return the number
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return 0;//Return the number 0
    }

    /**
     * Create the file ErrorNums.txt
     */
    private static void createErrorNums() {
        try {
            File file = new File("ErrorNums.txt");
         /*If file gets created then the createNewFile()
          * method would return true or if the file is
	      * already present it would return false
	      */
            boolean fvar = file.createNewFile();
            if (fvar) {
                LogToFile.log("Config", "ErrorNums.txt was created");
            }
        } catch (IOException e) {
            System.out.println("Exception Occurred:");
            e.printStackTrace();
        }
    }

    /**
     * Write the number to ErrorNums.txt
     *
     * @param num
     *         The number you want to write(Could be a string)
     */
    public static void writeNum(String num) {
        createErrorNums();//Create the file
        PrintWriter outS = null;//Set the printwriter ot null
        try {
            outS = new PrintWriter(new FileOutputStream(("ErrorNums.txt"), true));//Try to get the opportunity to write
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();//File cant be found, this should never occur
        }
        outS.print(num + "\n");//Write the number and then go to the next line
        outS.close();
    }

    /**
     * Check if the string is already in the file
     *
     * @param filePath
     *         The file in question
     * @param searchQuery
     *         The string you are searching for
     *
     * @return A boolean whether its in the File or not
     *
     * @throws IOException
     *         In case there is an exception
     */

    public static boolean searchBR(String filePath, String searchQuery) throws IOException {
        searchQuery = searchQuery.trim();//Trim the search
        BufferedReader br = null;//Get the bufferedreader

        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));//Initialize the Buffered reader with the file.
            String line;//Initialize the line string
            while ((line = br.readLine()) != null) {//If the line is not null that the buffered reader is reading
                if (line.contains(searchQuery)) {//If it contains the search return true
                    return true;
                }
            }
        } finally {
            try {
                if (br != null)
                    br.close();//Try to close the buffered reader
            } catch (Exception e) {
                //TODO: Add better error handling
                System.err.println("Exception while closing buffered reader " + e.toString());//In case there is an error
            }
        }

        return false;
    }
}
