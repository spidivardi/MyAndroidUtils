package com.il.tikkun.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Locale;


public class ActivityHelper {

    /**
     * method for start "Activity" with the language the app and not the local language.
     *
     * @param activity The mActivity that opens a the next mActivity.
     * @param language the code the language for example, "en" for English.
     * @param intent   the intent with the mActivity Who want to run it.
     */
    public static void startActivityByLanguage(Activity activity, String language, Intent intent) {

        Locale myLocale = new Locale(language);

        Locale.setDefault(myLocale);

        Resources res = activity.getResources();

        DisplayMetrics dm = res.getDisplayMetrics();

        Configuration conf = res.getConfiguration();

        conf.locale = myLocale;

        res.updateConfiguration(conf, dm);

        activity.startActivity(intent);

        activity.finish();
    }

    public static void setLocalLanguage(Activity activity, String language) {

        Locale myLocale = new Locale(language);

        Locale.setDefault(myLocale);

        Resources res = activity.getResources();

        DisplayMetrics dm = res.getDisplayMetrics();

        Configuration conf = res.getConfiguration();

        conf.locale = myLocale;

        res.updateConfiguration(conf, dm);

    }


    /**
     * method for start "Activity" with the language the app and not the local language.
     *
     * @param activity The mActivity that opens a the next mActivity.
     * @param language the code the language for example, "en" for English.
     * @param intent   the intent with the mActivity Who want to run it.
     */
    public static void startActivityByLanguageAndNotFinish(Activity activity, String language, Intent intent) {

        Locale myLocale = new Locale(language);

        Locale.setDefault(myLocale);

        Resources res = activity.getResources();

        DisplayMetrics dm = res.getDisplayMetrics();

        Configuration conf = res.getConfiguration();

        conf.locale = myLocale;

        res.updateConfiguration(conf, dm);

        activity.startActivity(intent);

    }
}
