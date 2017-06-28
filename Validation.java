package com.il.tikkun.utils;

/**
 * Created by David vardi on 7/31/2016.
 */
public class Validation {

    public static boolean validID(String mId) {

        while (mId.length() < 9)

            mId = addZero(mId);

        int[] originalId = new int[mId.length()];

        int[] oneTwo = {1, 2, 1, 2, 1, 2, 1, 2, 1};

        int[] multiply = new int[9];

        int[] oneDigit = new int[9];

        int sum = 0;

        for (int i = 0; i < mId.length(); i++) {

            originalId[i] = mId.charAt(i) - '0';
        }

        for (int i = 0; i < 9; i++)

            multiply[i] = originalId[i] * oneTwo[i];

        for (int i = 0; i < 9; i++)

            oneDigit[i] = (multiply[i] / 10) + multiply[i] % 10;

        for (int i = 0; i < 9; i++)

            sum += oneDigit[i];

        return sum % 10 == 0;


    }

    private static String addZero(String mId) {

        return "0" + mId;
    }

}
