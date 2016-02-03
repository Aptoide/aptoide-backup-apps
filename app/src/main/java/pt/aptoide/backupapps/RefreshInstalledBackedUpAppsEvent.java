package pt.aptoide.backupapps;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: rmateus
 * Date: 20-08-2013
 * Time: 15:51
 * To change this template use File | Settings | File Templates.
 */
public class RefreshInstalledBackedUpAppsEvent {
    private ArrayList<Integer> installedBackedUpApps;

    public RefreshInstalledBackedUpAppsEvent(ArrayList<Integer> installedBackedUpApps) {
        this.installedBackedUpApps = installedBackedUpApps;
    }

    public ArrayList<Integer> getInstalledBackedUpApps() {

        Log.d("TAG", installedBackedUpApps+"");

        return installedBackedUpApps;
    }

}
