package utils;

import DAO.UserDB;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class handles the tracking of login attempts.
 * @author Jacob Boyd
 */
public class Log {
    private static String path = "/login_activity.txt";

    /**
     * trackLogIn appends the login data to the file login_activity.txt in the root folder.
     * It appends the date/time of attempted log in and whether the login was successful or not.
     * @param tracking The current date/ time
     * @param isSuccessful Whether or not the login was successful
     * @throws IOException if the login activity file cannot be accessed/ created.
     */
    public static void trackLogIn(String tracking, Boolean isSuccessful) throws IOException {

        if(isSuccessful){
            tracking += "\tUserID:\t" + UserDB.getCurrentUser().getUserID();
            FileWriter fileWriter = new FileWriter("login_activity.txt", true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(tracking + "\t SUCCESSFUL");
            bufferedWriter.newLine();
            bufferedWriter.close();
            fileWriter.close();
        } else {
            FileWriter fileWriter = new FileWriter("login_activity.txt", true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(tracking + "\t NOT SUCCESSFUL");
            bufferedWriter.newLine();
            bufferedWriter.close();
            fileWriter.close();
        }



    }
}
