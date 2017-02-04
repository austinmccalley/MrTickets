package me.Austin.MT.Managers;

import java.util.Random;

/**
 * Created by MrMcaustin1 on 12/31/2016 at 1:55 PM.
 * <p>
 * This PwdGen class was found online and changed to meet my needs
 *
 * @author Unknown
 * @since 1.0
 */
public class PwdGen {

    private static final String ALPHA_CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUM = "0123456789";
    private static final String SPL_CHARS = "!@#$%^&*_=+-/";

    /**
     * Generate a password
     *
     * @param minLen        Minimum length
     * @param maxLen        Maximum length
     * @param noOfCAPSAlpha Number of capital letters
     * @param noOfDigits    Number of digits
     * @param noOfSplChars  Number of special charcters
     * @return The generated password
     */
    public static char[] generatePswd(int minLen, int maxLen, int noOfCAPSAlpha,
                                      int noOfDigits, int noOfSplChars) {
        if (minLen > maxLen)
            throw new IllegalArgumentException("Min. Length > Max. Length!");
        if ((noOfCAPSAlpha + noOfDigits + noOfSplChars) > minLen)
            throw new IllegalArgumentException
                    ("Min. Length should be atleast sum of (CAPS, DIGITS, SPL CHARS) Length!");
        Random rnd = new Random();
        int len = rnd.nextInt(maxLen - minLen + 1) + minLen;
        char[] pswd = new char[len];
        int index = 0;
        for (int i = 0; i < noOfCAPSAlpha; i++) {
            index = getNextIndex(rnd, len, pswd);
            pswd[index] = ALPHA_CAPS.charAt(rnd.nextInt(ALPHA_CAPS.length()));
        }
        for (int i = 0; i < noOfDigits; i++) {
            index = getNextIndex(rnd, len, pswd);
            pswd[index] = NUM.charAt(rnd.nextInt(NUM.length()));
        }
        for (int i = 0; i < noOfSplChars; i++) {
            index = getNextIndex(rnd, len, pswd);
            pswd[index] = SPL_CHARS.charAt(rnd.nextInt(SPL_CHARS.length()));
        }
        for (int i = 0; i < len; i++) {
            if (pswd[i] == 0) {
                pswd[i] = ALPHA.charAt(rnd.nextInt(ALPHA.length()));
            }
        }
        return pswd;
    }

    private static int getNextIndex(Random rnd, int len, char[] pswd) {
        int index = rnd.nextInt(len);
        while (pswd[index = rnd.nextInt(len)] != 0) ;
        return index;
    }

}
