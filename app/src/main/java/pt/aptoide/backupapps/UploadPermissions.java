package pt.aptoide.backupapps;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 27-08-2013
 * Time: 10:59
 * To change this template use File | Settings | File Templates.
 */
public class UploadPermissions {
    private static SharedPreferences sPref;



    static public boolean isUploadPermited(Context context) {
        sPref = PreferenceManager.getDefaultSharedPreferences(context);

        ConnectivityManager connectivityState = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean connectionAvailable = false;

        if (isWiFi()) {
            try {

                connectionAvailable = connectivityState.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED;
                Log.d("ManagerDownloads", "isPermittedConnectionAvailable wifi: " + connectionAvailable);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (isWiMax()) {
            try {
                connectionAvailable = connectionAvailable || connectivityState.getNetworkInfo(6).getState() == NetworkInfo.State.CONNECTED;
                Log.d("ManagerDownloads", "isPermittedConnectionAvailable wimax: " + connectionAvailable);
            } catch (Exception e) {
            }
        }
        if (isMobile()) {
            try {
                connectionAvailable = connectionAvailable || connectivityState.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED;
                Log.d("ManagerDownloads", "isPermittedConnectionAvailable mobile: " + connectionAvailable);
            } catch (Exception e) {
            }
        }
        if (isEthernet()) {
            try {
                connectionAvailable = connectionAvailable || connectivityState.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET).getState() == NetworkInfo.State.CONNECTED;
                Log.d("ManagerDownloads", "isPermittedConnectionAvailable ethernet: " + connectionAvailable);
            } catch (Exception e) {
            }
        }

        Log.d("ManagerDownloads", "isPermittedConnectionAvailable: " + connectionAvailable);
        return connectionAvailable;


    }

    public static boolean isWiFi() {
        return sPref.getBoolean("prefer_wifi", true);
    }

    public static boolean isEthernet() {
        return sPref.getBoolean("prefer_ethernet", true);
    }

    public static boolean isMobile() {
        return sPref.getBoolean("prefer_mobile_data", true);
    }

    public static boolean isWiMax() {
        return sPref.getBoolean("prefer_wimax", true);
    }
}
