package com.iam.artists.utils;

/**
 * Util class for correct simple strings cases
 */

public class Corrector {

    public static String songsCountInGenetive(int count) {
        int i = count % 10;
        if (i == 1) {
            return "песня";
        } else if (i > 1 && i < 5) {
            return "песни";
        } else {
            return "песен";
        }
    }

    public static String albumsCountInGenetive(int count) {
        int i = count % 10;
        if (i == 1) {
            return "альбом";
        } else if (i > 1 && i < 5) {
            return "альбома";
        } else {
            return "альбомов";
        }
    }
}
