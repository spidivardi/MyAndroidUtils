package com.il.tikkun.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Patterns;
import android.widget.Toast;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * class to get information about the user like email or device details.
 * <p/>
 * Created by David vardi on 5/3/2016.
 */
public class UserInformation {

    public Context mContext;

    private TelephonyManager tm;

    public UserInformation(Context context) {

        mContext = context;
        tm = (TelephonyManager) ((ContextWrapper) context).getBaseContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
    }


    /**
     * is Support TelephonyManager in phone
     */
    public boolean isSupport() {

        return tm != null;
    }


    public String getMyPhoneNumber() {

        return tm.getLine1Number();

    }


    public String getMyEmail() {

        AccountManager manager = (AccountManager) mContext
                .getSystemService(Activity.ACCOUNT_SERVICE);
        Account[] list = manager.getAccounts();
        String gmail = null;

        for (Account account : list) {
            //Log.e("gmail", account.toString());
            if (account.type.equalsIgnoreCase("com.google")) {
                gmail = account.name;
                //Log.e("gmail", gmail);
                break;
            }
        }
        return gmail;
    }


    /**
     * Note that this requires the GET_ACCOUNTS permission:
     *
     * @return PrimaryAccount
     */
    public String getPrimaryAccount() {

        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+

        Account[] accounts = AccountManager.get(mContext).getAccountsByType("com.google");

        for (Account account : accounts) {

            if (emailPattern.matcher(account.name).matches()) {

                return account.name;
            }
        }
        return null;
    }

    public String getUniqueDeviceID() {

        String androidId = "" + getDeviceId();
        androidId += "-" + getSimSerialNumber();
        androidId += "-"
                + android.provider.Settings.Secure.getString(
                mContext.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);

        //Log.e("androidId", "ppp" + androidId);
        return androidId;
    }

    /**
     * @return Sim Serial Number device
     */
    private String getSimSerialNumber() {

        return tm.getSimSerialNumber();

    }


    /**
     * @return Device Id device
     */
    public String getDeviceId() {
        try {

            return tm.getDeviceId();

        } catch (SecurityException e) {

            UserManager.setIsFirstTime(mContext, true);

            Toast.makeText(mContext, "SecurityException:  android.permission.READ_PHONE_STATE", Toast.LENGTH_SHORT).show();

            return tm.getDeviceId();

        }


    }


    /**
     * Returns the consumer  device name
     */
    public static String getDeviceManufacturer() {

        return Build.MANUFACTURER;
    }

    /**
     * Returns the consumer  device Propertis
     */
    public static String getDevicePropertis() {

        return "MANUFACTURER = " + Build.MANUFACTURER + " MODEL = " + Build.MODEL + " PRODUCT = " + Build.PRODUCT + " SERIAL = " + Build.SERIAL;
    }


    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }
}

