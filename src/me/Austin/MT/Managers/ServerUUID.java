package me.Austin.MT.Managers;

import me.Austin.MT.Managers.Objects.Server;
import org.bukkit.Bukkit;

import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.UUID;

/**
 * Created by mcaus_000 on 1/29/2017.
 */
public class ServerUUID {


    public static UUID generateServerUUID(String rndWrd) {
        String byteString = rndWrd;

        byte[] byteUUID = byteString.getBytes(StandardCharsets.UTF_8);

        UUID uuid = UUID.nameUUIDFromBytes(byteUUID);

        return uuid;

    }


   public static String generateRandomWord(int wordLength) {
        Random r = new Random(); // Intialize a Random Number Generator with SysTime as the seed
        StringBuilder sb = new StringBuilder(wordLength);
        for (int i = 0; i < wordLength; i++) { // For each letter in the word
            char tmp = (char) ('a' + r.nextInt('z' - 'a')); // Generate a letter between a and z
            sb.append(tmp); // Add it to the String
        }
        return sb.toString();
    }




}
